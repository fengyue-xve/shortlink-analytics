<template>
  <section class="panel">
    <div class="header" style="margin-bottom: 8px">
      <div>
        <h2 style="margin:0">AI投放助手</h2>
        <p style="margin:6px 0 0;color:#64748b">根据短链接目标、用户人群和卖点生成推广文案与渠道建议。</p>
      </div>
    </div>

    <el-form :model="form" label-position="top" style="max-width:760px">
      <el-form-item label="目标链接"><el-input v-model="form.originalUrl" placeholder="https://example.com" /></el-form-item>
      <el-form-item label="产品/活动名称"><el-input v-model="form.productName" placeholder="例如：Java后端项目演示页" /></el-form-item>
      <el-form-item label="目标人群"><el-input v-model="form.targetAudience" placeholder="例如：HR、技术面试官、Java学习者" /></el-form-item>
      <el-form-item label="核心卖点"><el-input v-model="form.sellingPoint" placeholder="例如：短链接跳转、Redis缓存、访问统计" /></el-form-item>
      <el-form-item label="计划渠道"><el-input v-model="form.channel" placeholder="例如：BOSS打招呼、简历附件、朋友圈" /></el-form-item>
      <el-button type="primary" :loading="loading" @click="submit">生成投放建议</el-button>
    </el-form>

    <div v-if="result" style="margin-top:22px">
      <el-alert v-if="result.fallback" type="warning" show-icon :closable="false" title="当前未配置 DEEPSEEK_API_KEY，展示的是本地模板兜底结果。" />
      <el-alert v-else type="success" show-icon :closable="false" title="已调用 AI 生成投放建议。" />

      <h3>{{ result.title }}</h3>
      <p style="white-space:pre-wrap;line-height:1.7">{{ result.shortIntro }}</p>

      <h3>推广文案</h3>
      <el-card v-for="item in result.copywritingOptions" :key="item" style="margin-bottom:10px">{{ item }}</el-card>

      <h3>渠道建议</h3>
      <ul><li v-for="item in result.channelSuggestions" :key="item">{{ item }}</li></ul>

      <h3>风险提示</h3>
      <ul><li v-for="item in result.riskTips" :key="item">{{ item }}</li></ul>
    </div>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { generateCampaign } from '../api/ai'

const loading = ref(false)
const result = ref(null)
const form = reactive({
  originalUrl: '',
  productName: '',
  targetAudience: '',
  sellingPoint: '',
  channel: ''
})

async function submit() {
  if (!form.originalUrl) {
    ElMessage.warning('请先填写目标链接')
    return
  }
  loading.value = true
  try {
    result.value = await generateCampaign(form)
  } finally {
    loading.value = false
  }
}
</script>
