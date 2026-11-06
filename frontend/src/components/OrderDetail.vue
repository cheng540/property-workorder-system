<template>
  <el-drawer v-model="visible" title="工单详情" size="540px">
    <div v-if="detail.order">
      <div class="order-no">{{ detail.order.orderNo }}</div>

      <el-descriptions :column="1" border>
        <el-descriptions-item label="报修事项">{{ detail.order.title }}</el-descriptions-item>
        <el-descriptions-item label="报修类型">{{ detail.order.typeName }}</el-descriptions-item>
        <el-descriptions-item label="报修地址">{{ detail.order.address }}</el-descriptions-item>
        <el-descriptions-item label="紧急程度">
          <el-tag :type="detail.order.urgency === 2 ? 'danger' : 'info'" effect="plain" size="small">
            {{ urgencyText(detail.order.urgency) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="住户">{{ detail.order.residentName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.order.residentPhone }}</el-descriptions-item>
        <el-descriptions-item label="维修工">{{ detail.order.workerName || '尚未派单' }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="statusType(detail.order.status)" effect="plain" size="small">
            {{ statusText(detail.order.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ detail.order.createTime }}</el-descriptions-item>
        <el-descriptions-item label="派单时间">{{ detail.order.assignTime || '—' }}</el-descriptions-item>
        <el-descriptions-item label="完工时间">{{ detail.order.finishTime || '—' }}</el-descriptions-item>
        <el-descriptions-item label="故障描述">{{ detail.order.content }}</el-descriptions-item>
        <el-descriptions-item label="处理说明">{{ detail.order.handleRemark || '—' }}</el-descriptions-item>
        <el-descriptions-item label="住户评价">
          <span v-if="detail.order.rating">{{ detail.order.rating }} 星 · {{ detail.order.evaluate }}</span>
          <span v-else>暂未评价</span>
        </el-descriptions-item>
      </el-descriptions>

      <div class="section-title">工单流转记录</div>
      <el-timeline>
        <el-timeline-item
          v-for="log in detail.logs"
          :key="log.id"
          :timestamp="log.createTime"
          placement="top"
          color="#2a6f97"
        >
          <div class="log-action">{{ log.action }}</div>
          <div class="log-remark">{{ log.remark }}</div>
          <div class="log-user">操作人：{{ log.operatorName }}</div>
        </el-timeline-item>
      </el-timeline>
    </div>
  </el-drawer>
</template>

<script setup>
import { ref, watch } from 'vue'
import { getOrderDetail } from '../api'
import { statusText, statusType, urgencyText } from '../utils/status'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  orderId: { type: [Number, String], default: null }
})

const emit = defineEmits(['update:modelValue'])

const visible = ref(false)
const detail = ref({ order: null, logs: [] })

watch(
  () => props.modelValue,
  val => {
    visible.value = val
    if (val && props.orderId) {
      load()
    }
  }
)

watch(visible, val => emit('update:modelValue', val))

const load = async () => {
  const res = await getOrderDetail(props.orderId)
  detail.value = res.data
}
</script>

<style scoped>
.order-no {
  font-size: 15px;
  font-weight: bold;
  color: #1d3557;
  margin-bottom: 12px;
}

.section-title {
  margin: 20px 0 14px;
  font-size: 14px;
  font-weight: bold;
  color: #303133;
  border-left: 3px solid #2a6f97;
  padding-left: 8px;
}

.log-action {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
}

.log-remark {
  margin-top: 4px;
  font-size: 13px;
  color: #606266;
  line-height: 1.6;
}

.log-user {
  margin-top: 4px;
  font-size: 12px;
  color: #909399;
}
</style>
