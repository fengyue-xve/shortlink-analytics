<template>
  <div class="auth-page">
    <div class="auth-panel">
      <h1 class="auth-title">创建账号</h1>
      <el-form :model="form" label-position="top">
        <el-form-item label="用户名"><el-input v-model="form.username" /></el-form-item>
        <el-form-item label="昵称"><el-input v-model="form.nickname" /></el-form-item>
        <el-form-item label="密码"><el-input v-model="form.password" type="password" show-password /></el-form-item>
        <el-button type="primary" style="width:100%" @click="submit">注册</el-button>
      </el-form>
      <p style="text-align:center;margin-top:16px;color:#64748b">已有账号？<router-link to="/login">登录</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '../api/auth'

const router = useRouter()
const form = reactive({ username: '', nickname: '', password: '' })

async function submit() {
  await register(form)
  ElMessage.success('注册成功，请登录')
  router.push('/login')
}
</script>
