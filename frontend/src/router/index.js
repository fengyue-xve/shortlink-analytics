import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Layout from '../views/Layout.vue'
import Dashboard from '../views/Dashboard.vue'
import Campaign from '../views/Campaign.vue'
import Links from '../views/Links.vue'
import AiCampaign from '../views/AiCampaign.vue'

const routes = [
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'campaign', component: Campaign },
      { path: 'links', component: Links },
      { path: 'ai-campaign', component: AiCampaign }
    ]
  }
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (!token && to.path !== '/login' && to.path !== '/register') {
    next('/login')
    return
  }
  if (token && (to.path === '/login' || to.path === '/register')) {
    next('/dashboard')
    return
  }
  next()
})

export default router
