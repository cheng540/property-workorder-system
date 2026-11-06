<template>
  <el-card shadow="never" class="form-card">
    <template #header>
      <span class="card-title">填写报修信息</span>
    </template>

    <el-form :model="form" :rules="rules" ref="formRef" label-width="92px">
      <el-form-item label="报修类型" prop="typeId">
        <el-select v-model="form.typeId" placeholder="请选择报修类型" style="width: 280px">
          <el-option v-for="item in types" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>

      <el-form-item label="故障标题" prop="title">
        <el-input v-model="form.title" placeholder="用一句话说明问题" style="width: 420px" maxlength="40" show-word-limit />
      </el-form-item>

      <el-form-item label="报修地址" prop="address">
        <el-input v-model="form.address" placeholder="楼栋房号或公共区域位置" style="width: 280px" />
      </el-form-item>

      <el-form-item label="紧急程度">
        <el-radio-group v-model="form.urgency">
          <el-radio :value="1">一般，可按顺序安排</el-radio>
          <el-radio :value="2">紧急，影响正常生活</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="故障描述" prop="content">
        <el-input
          v-model="form.content"
          type="textarea"
          :rows="5"
          placeholder="请说明故障现象、发生时间，方便维修师傅提前准备工具"
          style="width: 520px"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" :loading="submitting" @click="submit">提交报修</el-button>
        <el-button @click="reset">重新填写</el-button>
      </el-form-item>
    </el-form>

    <el-alert
      title="提交后物业会在二十四小时内派单，紧急事项两小时内响应，处理进度可在「我的报修」中查看。"
      type="info"
      :closable="false"
      show-icon
    />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getTypes, createOrder } from '../api'

const router = useRouter()
const formRef = ref(null)
const types = ref([])
const submitting = ref(false)
const user = JSON.parse(localStorage.getItem('user') || '{}')

const form = reactive({
  typeId: null,
  title: '',
  address: user.address || '',
  urgency: 1,
  content: ''
})

const rules = {
  typeId: [{ required: true, message: '请选择报修类型', trigger: 'change' }],
  title: [{ required: true, message: '请填写故障标题', trigger: 'blur' }],
  address: [{ required: true, message: '请填写报修地址', trigger: 'blur' }],
  content: [{ required: true, message: '请描述故障情况', trigger: 'blur' }]
}

const submit = () => {
  formRef.value.validate(async valid => {
    if (!valid) {
      return
    }
    submitting.value = true
    try {
      await createOrder(form)
      ElMessage.success('报修已提交，等待物业派单')
      router.push('/my-orders')
    } finally {
      submitting.value = false
    }
  })
}

const reset = () => {
  formRef.value.resetFields()
  form.address = user.address || ''
  form.urgency = 1
}

onMounted(async () => {
  const res = await getTypes()
  types.value = res.data
})
</script>

<style scoped>
.form-card {
  max-width: 860px;
}

.card-title {
  font-size: 15px;
  font-weight: bold;
  color: #303133;
}
</style>
