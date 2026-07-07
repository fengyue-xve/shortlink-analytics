<template>
  <section class="panel">
    <div class="toolbar">
      <el-input v-model="query.keyword" placeholder="搜索标题、原链接、短码" clearable style="max-width:320px" />
      <el-button @click="load">搜索</el-button>
      <el-button type="primary" @click="dialogVisible=true">新建短链接</el-button>
    </div>

    <el-table :data="rows">
      <el-table-column prop="title" label="标题" width="160" />
      <el-table-column prop="originalUrl" label="原始链接" min-width="240" show-overflow-tooltip />
      <el-table-column prop="shortUrl" label="短链接" min-width="220" />
      <el-table-column prop="visitCount" label="访问量" width="100" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope"><el-tag :type="scope.row.status === 1 ? 'success' : 'info'">{{ scope.row.status === 1 ? '启用' : '禁用' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" width="190">
        <template #default="scope">
          <el-button size="small" @click="copy(scope.row.shortUrl)">复制</el-button>
          <el-button size="small" type="danger" @click="disable(scope.row.id)">禁用</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="新建短链接" width="520px">
      <el-form :model="form" label-position="top">
        <el-form-item label="所属活动">
          <el-select v-model="form.campaignId" placeholder="可选" clearable style="width:100%">
            <el-option v-for="item in campaigns" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="原始链接"><el-input v-model="form.originalUrl" placeholder="https://example.com" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible=false">取消</el-button><el-button type="primary" @click="create">保存</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { createLink, deleteLink, listLinks } from '../api/link'
import { simpleCampaigns } from '../api/campaign'

const rows = ref([])
const campaigns = ref([])
const dialogVisible = ref(false)
const query = reactive({ page: 1, size: 20, keyword: '' })
const form = reactive({ campaignId: null, title: '', originalUrl: '' })

async function load() {
  const res = await listLinks(query)
  rows.value = res.records || []
}
async function create() {
  await createLink(form)
  ElMessage.success('创建成功')
  dialogVisible.value = false
  form.campaignId = null
  form.title = ''
  form.originalUrl = ''
  load()
}
async function disable(id) {
  await deleteLink(id)
  ElMessage.success('已禁用')
  load()
}
async function copy(text) {
  await navigator.clipboard.writeText(text)
  ElMessage.success('已复制')
}
onMounted(async () => {
  campaigns.value = await simpleCampaigns()
  load()
})
</script>
