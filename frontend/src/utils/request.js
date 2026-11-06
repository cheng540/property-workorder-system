import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截：统一带上 token
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = 'Bearer ' + token
  }
  return config
})

// 响应拦截：code 不是 200 统一提示，401 回登录页
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res
    }
    if (res.code === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      ElMessage.error(res.msg || '登录已失效，请重新登录')
      window.location.hash = '#/login'
      return Promise.reject(res)
    }
    ElMessage.error(res.msg || '操作失败')
    return Promise.reject(res)
  },
  error => {
    ElMessage.error('请求失败，请确认服务已启动')
    return Promise.reject(error)
  }
)

export default request
