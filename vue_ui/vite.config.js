import { defineConfig } from 'vite'
import {createVuePlugin} from 'vite-plugin-vue2'
import viteSvgIcons from 'vite-plugin-svg-icons';
import { injectHtml, minifyHtml } from 'vite-plugin-html';
import defaultSettings  from "./src/settings.js";
import path from "path";
const dotenv = require("dotenv");
import fs from 'fs';

function resolve(dir) {
  return path.join(__dirname, dir);
}

try {
  // 根据环境变量加载环境变量文件
  const file = dotenv.parse(fs.readFileSync(`./.env.${process.env.NODE_ENV}`), {
    debug: true
  })
  console.log(file)
  // 根据获取的 key 给对应的环境变量赋值
  for (const key in file) {
    process.env[key] = file[key]
  }
} catch (e) {
  console.error(e)
}

const name = defaultSettings.title || "fhs_demo"; // 标题

const port = process.env.port || process.env.npm_config_port || 80; // 端口

const API_LOCATION = process.env.VUE_APP_BASE_API || '/api'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    createVuePlugin(),
    minifyHtml(), // 压缩 HTML
    injectHtml({ // 入口文件 index.html 的模板注入
      injectData: { // 模板注入的数据
        system_title: name,
        icon:''
      },
    }),
    viteSvgIcons({
      // 配置路劲在你的src里的svg存放文件
      iconDirs: [path.resolve(process.cwd(), 'src/assets/icons/svg')],
      symbolId: 'icon-[dir]-[name]',
    })
  ],
  resolve:{
    alias:{
      '@': path.resolve(__dirname, "./src"),
    },
  },
  define: {
    'process.env': process.env
  },
  server: {
    //host:'',
    port:3001,
    cors:true,
    proxy: {
      '/api/basic': {
        target: 'http://127.0.0.1:8081/',
        rewrite: (path) => path.replace('/api/basic', '') // 根据环境变量配置代理
      }
    }
  //   hmr: {
  //     port: 443,
  //   }
  },
  build:{
    outDir:'dist',
    assetsDir:'static'
  }
})
