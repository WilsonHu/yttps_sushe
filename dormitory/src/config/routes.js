import Login from '../components/Login.vue'
import NotFound from '../components/404.vue'
import Dormitory from '../components/dormitory.vue'
export default
[
	{
		path: '/login',
		component: Login,
		name: 'Login',
		hidden: true
	},
	{
		path: '/404',
		component: NotFound,
		name: '404',
		hidden: true
	},

	{
		path: '/dormitory',
        component:Dormitory,
	},
]
