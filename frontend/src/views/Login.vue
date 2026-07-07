<template>
  <div class="auth-page">
    <div class="auth-panel">
      <h1 class="auth-title">短链接访问分析系统</h1>
      <el-form :model="form" label-position="top">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-button type="primary" style="width:100%" @click="submit">登录</el-button>
      </el-form>
      <p style="text-align:center;margin-top:16px;color:#64748b">没有账号？<router-link to="/register">注册</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '../api/auth'

const router = useRouter()
const form = reactive({ username: 'admin', password: '123456' })

async function submit() {
  const res = await login(form)
  localStorage.setItem('token', res.token)
  localStorage.setItem('userInfo', JSON.stringify(res))
  ElMessage.success('登录成功')
  router.push('/dashboard')
}
</script>
