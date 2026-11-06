import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from '../views/Layout.vue'

const routes = [
  {
    path: '/login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '首页', icon: 'HomeFilled' }
      },
      {
        path: 'order',
        component: () => import('../views/OrderManage.vue'),
        meta: { title: '工单管理', icon: 'Tickets', roles: ['ADMIN'] }
      },
      {
        path: 'user',
        component: () => import('../views/UserManage.vue'),
        meta: { title: '用户管理', icon: 'User', roles: ['ADMIN'] }
      },
      {
        path: 'type',
        component: () => import('../views/TypeManage.vue'),
        meta: { title: '报修类型', icon: 'Files', roles: ['ADMIN'] }
      },
      {
        path: 'notice',
        component: () => import('../views/NoticeManage.vue'),
        meta: { title: '公告管理', icon: 'Bell', roles: ['ADMIN'] }
      },
      {
        path: 'new-repair',
        component: () => import('../views/NewRepair.vue'),
        meta: { title: '我要报修', icon: 'EditPen', roles: ['RESIDENT'] }
      },
      {
        path: 'my-orders',
        component: () => import('../views/MyOrders.vue'),
        meta: { title: '我的报修', icon: 'Document', roles: ['RESIDENT'] }
      },
      {
        path: 'my-tasks',
        component: () => import('../views/WorkerOrders.vue'),
        meta: { title: '我的任务', icon: 'Tools', roles: ['WORKER'] }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 前端只控制跳转，权限判断在后端还有一层
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    next()
    return
  }
  if (!token) {
    next('/login')
    return
  }
  const user = JSON.parse(localStorage.getItem('user') || '{}')
  const roles = to.meta.roles
  if (roles && !roles.includes(user.role)) {
    next('/dashboard')
    return
  }
  next()
})

export default router
