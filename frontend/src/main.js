import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import * as Icons from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

// 图标统一注册，页面里直接写 <el-icon><Search /></el-icon>
for (const [key, component] of Object.entries(Icons)) {
  app.component(key, component)
}

app.use(ElementPlus, { locale: zhCn })
app.use(router)
app.mount('#app')
