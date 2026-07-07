<template>
  <section class="panel">
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="搜索活动、人群、渠道" clearable style="max-width:320px" />
      <el-button @click="load">搜索</el-button>
      <el-button type="primary" @click="dialogVisible=true">新建活动</el-button>
    </div>

    <el-table :data="rows">
      <el-table-column prop="name" label="活动名称" min-width="160" />
      <el-table-column prop="targetAudience" label="目标人群" min-width="160" />
      <el-table-column prop="channel" label="渠道" width="140" />
      <el-table-column prop="linkCount" label="短链数" width="100" />
      <el-table-column prop="visitCount" label="访问量" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope"><el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{ scope.row.status === 1 ? '进行中' : '停用' }}</el-tag></template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="新建投放活动" width="560px">
      <el-form :model="form" label-position="top">
        <el-form-item label="活动名称"><el-input v-model="form.name" placeholder="例如：Java后端简历项目展示" /></el-form-item>
        <el-form-item label="目标人群"><el-input v-model="form.targetAudience" placeholder="例如：HR / 技术面试官" /></el-form-item>
        <el-form-item label="投放渠道"><el-input v-model="form.channel" placeholder="例如：BOSS、实习僧、朋友圈" /></el-form-item>
        <el-form-item label="活动说明"><el-input v-model="form.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="create">保存</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { createCampaign, listCampaigns } from '../api/campaign'

const rows = ref([])
const dialogVisible = ref(false)
const query = reactive({ page: 1, size: 20, keyword: '' })
const form = reactive({ name: '', targetAudience: '', channel: '', description: '', status: 1 })

async function load() {
  const res = await listCampaigns(query)
  rows.value = res.records || []
}
async function create() {
  await createCampaign(form)
  ElMessage.success('创建成功')
  dialogVisible.value = false
  form.name = ''
  form.targetAudience = ''
  form.channel = ''
  form.description = ''
  load()
}
onMounted(load)
</script>
