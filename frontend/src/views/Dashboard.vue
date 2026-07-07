<template>
  <section>
    <div class="cards">
      <div class="stat"><div class="stat-label">短链接总数</div><div class="stat-value">{{ stats.totalLinks || 0 }}</div></div>
      <div class="stat"><div class="stat-label">总访问量</div><div class="stat-value">{{ stats.totalVisits || 0 }}</div></div>
      <div class="stat"><div class="stat-label">今日访问</div><div class="stat-value">{{ stats.todayVisits || 0 }}</div></div>
    </div>
    <div class="panel">
      <h2 style="margin-top:0">访问量 Top 5</h2>
      <el-table :data="stats.topLinks || []">
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="shortUrl" label="短链接" />
        <el-table-column prop="visitCount" label="访问量" width="120" />
      </el-table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import { getStats } from '../api/dashboard'

const stats = reactive({})
onMounted(async () => Object.assign(stats, await getStats()))
</script>
