import Login from '../components/Login.vue'
import NotFound from '../components/404.vue'
import Dormitory from '../components/dormitory.vue'
import Home from '../components/home.vue'

import System from '../components/system.vue'
import UserManage from '../components/system/userManage.vue'
import FloorDeviceManage from '../components/system/floorDeviceManage.vue'
import StudentAccessRecord from '../components/system/studentAccessRecord.vue'

export default [
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
        component: Dormitory,
        hidden: true
    },
    {
        path: '/home',
        component: Home,
        redirect: '/home/system/userManage',
        children: [
            {
                path: '/home/system',
                component: System,
                name: 'System',
                meta: "系统管理",
                icon: 'el-icon-s-tools',
                hidden: false,
                children: [
                    {
                        path: '/home/system/userManage',
                        component: UserManage,
                        name: 'UserManage',
                        meta: '用户管理'
                    },
                    {
                        path: '/home/system/floorDeviceManage',
                        component: FloorDeviceManage,
                        name: 'floorDeviceManage',
                        meta: '楼层设备管理'
                    },
                    {
                        path: '/home/system/studentAccessRecord',
                        component: StudentAccessRecord,
                        name: 'studentAccessRecord',
                        meta: '学员通行记录'
                    }
                ]
            }
        ]
    },
    {
        path: '*',
        redirect: '/home',
        hidden: true
    },
]
