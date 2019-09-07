
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
import VueRouter from 'vue-router'


Vue.use(Echarts)

Vue.use(VueRouter)
import routes from './config/routes'
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
