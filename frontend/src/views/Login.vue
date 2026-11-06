<template>
  <div class="login-page">
    <div class="login-box">
      <div class="login-title">社区智慧物业报修与工单处理系统</div>
      <div class="login-sub">Community Property Repair and Work Order System</div>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入账号" size="large" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            size="large"
            :prefix-icon="Lock"
            show-password
            @keyup.enter="submit"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" style="width: 100%" :loading="loading" @click="submit">
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-tip">住户、维修工与物业管理员使用各自账号登录，进入后显示对应的功能菜单</div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { login } from '../api'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const submit = () => {
  formRef.value.validate(async valid => {
    if (!valid) {
      return
    }
    loading.value = true
    try {
      const res = await login(form)
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('user', JSON.stringify(res.data))
      ElMessage.success('登录成功')
      router.push('/dashboard')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1d3557 0%, #2a6f97 55%, #4a90a4 100%);
}

.login-box {
  width: 440px;
  padding: 42px 38px 28px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 8px 26px rgba(0, 0, 0, 0.2);
}

.login-title {
  font-size: 20px;
  font-weight: bold;
  text-align: center;
  color: #1d3557;
  letter-spacing: 1px;
  line-height: 1.5;
}

.login-sub {
  margin-top: 8px;
  margin-bottom: 26px;
  font-size: 11px;
  text-align: center;
  color: #909399;
}

.login-tip {
  margin-top: 4px;
  font-size: 12px;
  color: #a8abb2;
  text-align: center;
  line-height: 1.7;
}
</style>
