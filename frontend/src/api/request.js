import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({ baseURL: '/api', timeout: 10000 })

request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

request.interceptors.response.use(response => {
  const res = response.data
  if (res.code !== 200) {
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message))
  }
  return res.data
}, error => {
  if (error.response && error.response.status === 401) {
    localStorage.clear()
    location.href = '/login'
  } else {
    ElMessage.error('网络异常或服务未启动')
  }
  return Promise.reject(error)
})

export default request
