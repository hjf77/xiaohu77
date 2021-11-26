import { constantRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index.vue'
const view = path => resolve => require([`@/views/${path}`], resolve)

const permission = {
  state: {
    routes: [],
    addRoutes: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      console.log(routes);
      state.routes = constantRoutes.concat(routes)
      console.log( state.routes);
    }
  },
  actions: {
    // 生成路由
    GenerateRoutes({ commit }, outsource) {
      return new Promise(resolve => {
        // 外协人员
        if (outsource) {
          let routerObj = [
            {
              path: '/train',
              component: 'Layout',
              redirect: '',
              children: [
                {
                  path: 'exam',
                  component:'train/exam/index',
                  name: '培训考试',
                  meta: { title: '培训考试', icon: 'dashboard', noCache: true }
                }
              ]
            },
          ]
          const tempRouter = filterAsyncRouter(routerObj)
          tempRouter.push({ path: '*', redirect: '/404', hidden: true })
          commit('SET_ROUTES', tempRouter)
          resolve(tempRouter)
        } else {
          // 向后端请求路由数据
          getRouters().then(res => {
            const accessedRoutes = filterAsyncRouter(res.data)
            accessedRoutes.push({ path: '*', redirect: '/404', hidden: true })
            console.log(accessedRoutes);
            commit('SET_ROUTES', accessedRoutes)
            resolve(accessedRoutes)
          })
        }
      })
    }
  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap) {
  return asyncRouterMap.filter(route => {
    if (route.component) {
      // Layout组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else {
        route.component = view(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children)
    }
    return true
  })
}

export const loadView = (view) => { // 路由懒加载
  //return () => import(`@/views/${view}`)
  return () => Promise.resolve(require(`@/views/${view}`).default)

}

export default permission
