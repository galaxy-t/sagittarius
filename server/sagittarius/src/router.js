import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/application/detail',
      name: 'application-detail',
      component: () => import('./views/application/detail.vue')
    },
    {
      path: '/application/add',
      name: 'application-add',
      component: () => import('./views/application/add.vue')
    },
    {
      path: '/application',
      name: 'application-index',
      component: () => import('./views/application/index.vue')
    },
    {
      path: '/',
      name: 'index',
      component: () => import('./views/index')
    }
  ]
})
