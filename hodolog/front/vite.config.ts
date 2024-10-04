import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  // 어떤 요청을 하게되면 /posts로 시작하는 요청이 오면 localhost:8080으로 보내겠다고 설정을 할 수 있다.
  // api 경로
  server: {
    proxy: {
      "/api": { target:"http://localhost:8080",
      rewrite: (path) => path.replace("/^\/api/",""),
      },
    },
  },
})
