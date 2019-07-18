
import Vue from 'vue'
import App from './App.vue'
import Element from 'element-ui'
import Echarts from 'echarts'
import 'element-ui/lib/theme-chalk/index.css'
import 'babel-polyfill'
import 'url-search-params-polyfill'
Vue.use(Element)

import MintUI from 'mint-ui'
import 'mint-ui/lib/style.css'

Vue.use(MintUI)
// 引用路由
import VueRouter from 'vue-router'
// 光引用不成，还得使用

Vue.use(Echarts)

Vue.use(VueRouter)
// 引用路由配置文件
import routes from './config/routes'
// 使用配置文件规则
const router = new VueRouter({
  routes
})

router.beforeEach((to, from, next) => {
    //NProgress.start();
    if (to.path == '/login') {
        //sessionStorage.removeItem('user');
    }
    let user = JSON.parse(sessionStorage.getItem('user'));

    if (!user && to.path != '/login') {
        next({path: '/login'})
    } else {
        if(from.path.indexOf(to.path) == -1) {
            next();
        } else {
            next(false);
        }
    }
  // next();
})
new Vue({
  router,
  el: '#app',
  render: h => h(App)
})
