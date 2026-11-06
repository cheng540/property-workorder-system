<template>
  <el-container class="layout">
    <el-aside width="212px" class="aside">
      <div class="logo">
        <el-icon size="20"><House /></el-icon>
        <span>智慧物业工单</span>
      </div>
      <el-menu
        :default-active="route.path"
        router
        class="menu"
        background-color="#1d3557"
        text-color="#c3d4e0"
        active-text-color="#ffffff"
      >
        <el-menu-item v-for="item in menus" :key="item.path" :index="'/' + item.path">
          <el-icon><component :is="item.meta.icon" /></el-icon>
          <span>{{ item.meta.title }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-title">{{ currentTitle }}</div>
        <div class="header-right">
          <el-tag :type="roleTagType" effect="plain">{{ roleText }}</el-tag>
          <span class="user-name">{{ user.realName || user.username }}</span>
          <span v-if="user.address" class="user-addr">{{ user.address }}</span>
          <el-button link type="primary" @click="logout">退出登录</el-button>
        </div>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const user = JSON.parse(localStorage.getItem('user') || '{}')

const allMenus = router.getRoutes().filter(r => r.meta && r.meta.title)
const menus = computed(() => allMenus.filter(m => !m.meta.roles || m.meta.roles.includes(user.role)))

const currentTitle = computed(() => (route.meta && route.meta.title) || '')

const roleText = computed(() => {
  if (user.role === 'ADMIN') return '物业管理员'
  if (user.role === 'WORKER') return '维修工'
  return '住户'
})

const roleTagType = computed(() => {
  if (user.role === 'ADMIN') return 'danger'
  if (user.role === 'WORKER') return 'warning'
  return 'success'
})

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

<style scoped>
.layout {
  height: 100vh;
}

.aside {
  background-color: #1d3557;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #ffffff;
  font-size: 16px;
  font-weight: bold;
  letter-spacing: 1px;
  border-bottom: 1px solid #2a6f97;
}

.menu {
  border-right: none;
}

.header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #ffffff;
  border-bottom: 1px solid #e4e7ed;
}

.header-title {
  font-size: 17px;
  font-weight: bold;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-name {
  font-size: 14px;
  color: #606266;
}

.user-addr {
  font-size: 12px;
  color: #909399;
}

.main {
  padding: 18px;
  background-color: #f4f6f8;
}
</style>
