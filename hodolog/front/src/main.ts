import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

// ElementPlus 사용
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// Bootstrap의 Utiltities만 사용
import 'bootstrap/dist/css/bootstrap-utilities.min.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

app.mount('#app')
