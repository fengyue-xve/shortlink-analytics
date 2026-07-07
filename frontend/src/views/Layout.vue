<template>
  <div class="layout">
    <aside class="sidebar">
      <div class="brand">短链接分析</div>
      <nav class="nav">
        <router-link to="/dashboard">数据概览</router-link>
        <router-link to="/campaign">投放活动</router-link>
        <router-link to="/links">链接管理</router-link>
        <router-link to="/ai-campaign">AI投放助手</router-link>
      </nav>
    </aside>
    <main class="main">
      <div class="header">
        <div>你好，{{ user.nickname || user.username }}</div>
        <el-button @click="logout">退出</el-button>
      </div>
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const user = computed(() => JSON.parse(localStorage.getItem('userInfo') || '{}'))
function logout() {
  localStorage.clear()
  router.push('/login')
}
</script>
