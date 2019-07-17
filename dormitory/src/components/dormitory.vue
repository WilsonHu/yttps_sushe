<template xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">
    <div style="width: 100%;height: 100%;background-color: #F6F4FF">
        <el-row :gutter="0">
            <el-col :span="8">
                <div class="grid-content bg-purple">
                    <div style="height: 930px">
                        <div style="height: 150px; width:85% ;background-color: white;margin-left:60px;margin-top: 20px "
                             class="well well-lg">
                            <i style=" font:20px Extra Small;  color: #6a747c;">宿舍信息</i>
                            <el-button type="info" plain @click="add" style="float: right;" v-if="userInfo.name=='admin'">添加</el-button>
                            <el-button type="info" plain @click="floorInfo" style="float: right;" v-if="userInfo.name=='admin'">修改</el-button>
                            <div style="border: 1px solid silver;margin-top: 15px"></div>
                            <div>
                                <el-col :span="5" :offset="2">
                                    <p style="margin-left: 20px;margin-top: 20px">
                                        <span>楼号</span><br>
                                        <i style=" font:30px Extra Small;  color: black;">{{userInfo.floorNo}}</i>
                                    </p>
                                </el-col>
                                <el-col :offset="4" style="border: 1px solid silver;width: 1px;height: 82px"></el-col>
                                <el-col :span="5" :offset="4">
                                    <p style="margin-left: 20px;margin-top: 20px">
                                        <span>入住总人数</span><br>
                                        <i style=" font:30px Extra Small;  color: black;">{{attendanceNumList.total}}</i>
                                    </p>
                                </el-col>
                            </div>
                        </div>
                        <div class="well well-lg"
                             style="background-color: white;height: 610px;margin-left:60px; width:85% ">
                            <i style=" font:20px Extra Small;  color: #6a747c;">出入信息统计</i>
                            <el-tabs v-model="infoManage" @tab-click="handleClick" style="margin-top: -20px">
                                <el-tab-pane label="当前" name="first">
                                    <el-row style="margin-top: 30px;">
                                        <el-col>
                                            <img src="../assets/img/person.png" width="50px" height="50px"/>
                                        </el-col>
                                        <el-col :offset="4" style="margin-top: -50px">
                                            <span>当前考勤学生数</span><br/>
                                            <span style="font-size: 30px;">{{attendanceNumList.attendanceNum}}</span>
                                        </el-col>
                                        <el-col :span="8" :offset="7">
                                            <div id="myChart"
                                                 style="width: 350px;height: 180px;margin-top: -150px"></div>
                                        </el-col>

                                    </el-row>
                                    <el-row style="margin-top: 60px">
                                        <el-col>
                                            <img src="../assets/img/house.png" width="50px" height="50px"/>
                                        </el-col>
                                        <el-col :offset="4" style="margin-top: -50px">
                                            <span>当前在寝学生人数</span><br/>
                                            <span style="font-size: 30px;">{{attendanceNumList.inDormitory}}</span>

                                        </el-col>
                                        <el-col :span="8" :offset="7">
                                            <div id="attendace"
                                                 style="width: 350px;height: 180px;margin-top: -150px"></div>
                                        </el-col>
                                    </el-row>
                                    <el-row style="margin-top: 60px">
                                        <el-col>
                                            <img src="../assets/img/out.png" width="50px" height="50px"/>
                                        </el-col>
                                        <el-col :offset="4" style="margin-top: -50px">
                                            <span>当前外出学生人数</span><br/>
                                            <span style="font-size: 30px;">{{attendanceNumList.outDormitory}}</span>
                                        </el-col>
                                        <el-col :span="8" :offset="7">
                                            <div id="outSide"
                                                 style="width: 350px;height: 180px;margin-top: -150px"></div>
                                        </el-col>
                                    </el-row>
                                </el-tab-pane>
                                <el-tab-pane label="夜归考勤" name="second" style="background-color: #F6F4FF;">
                                    <div style="height: 500px;overflow:auto" id="div1">
                                        <ul style="margin-top: 10px;margin-left: -10px">
                                            <li v-for="i in nightFallList">
                                                <el-card class="box-card">
                                                    <div>
                                                        <el-row>
                                                            <el-col :span="2">
                                                                <img :src="i.imageId" width="60px"
                                                                     height="60px"
                                                                     style="border-radius: 50%;margin-top: -10px">
                                                            </el-col>
                                                            <el-col :span="5" :offset="3">
                                                                <span>{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                            </el-col>
                                                            <el-col :span="18" :offset="10" style="margin-top: -40px">
                                                                <span>{{i.pass_time}}</span><span
                                                                    style="margin-left: 10px">【{{i.type}}】</span>
                                                            </el-col>

                                                        </el-row>
                                                        <p style="margin-left: 80px;margin-top: -50px">
                                                        </p>
                                                    </div>
                                                </el-card>

                                            </li>
                                        </ul>
                                    </div>
                                    <span class="mes"></span>
                                    <span style="margin-left: 50px" class="count"></span>
                                </el-tab-pane>
                            </el-tabs>
                        </div>
                        <div style="height: 120px; width:85% ;background-color: white;margin-left:60px "
                             class="well well-lg">
                            <div>
                                <el-col :span="3">
                                    <img src="../assets/img/18973492613_张三.jpg" width="90px" height="90px"
                                         style="border-radius: 50%">
                                </el-col>
                                <el-col :span="8" :offset="3">
                                    <p>
                                        <label style="font-size: 23px;">宿管</label> <span
                                            style="font-size: 25px; margin-left: 20px">{{userInfo.name}}</span>
                                    </p>
                                    <p>
                                        <label>电话:</label><span>{{userInfo.phone}}</span>
                                    </p>
                                </el-col>
                            </div>
                        </div>
                    </div>
                </div>
            </el-col>
            <el-col :span="8">
                <div class="grid-content bg-purples">
                    <!--<el-tabs v-model="activeName" @tab-click="handleClick" class="middle">
                        <el-tab-pane label="用户管理" name="first">
                            用户管理
                        </el-tab-pane>
                        <el-tab-pane label="配置管理" name="second">
                            配置管理
                        </el-tab-pane>
                        <el-tab-pane label="角色管理" name="third">
                            角色管理
                        </el-tab-pane>
                        <el-tab-pane label="定时任务补偿" name="fourth">
                            定时任务补偿
                        </el-tab-pane>
                    </el-tabs>-->
                </div>
            </el-col>
            <el-col :span="8">
                <div class="grid-content bg-purple">
                    <div style="height: 930px">
                        <div style="height: 90px; width:85% ;background-color: transparent;margin-left:60px "
                             class="well well-lg">
                            <i class="title">通行记录</i>
                            <el-row style="margin-top:-30px;">
                                <el-input style="width: 200px;margin-left: 150px;border-radius: 20%"
                                          placeholder="按姓名或宿舍号查询" clearable
                                          auto-complete="off" v-model="name">
                                    <i slot="prefix" class="el-input__icon el-icon-search"></i>
                                </el-input>
                                <el-button type="info" plain @click="search">搜索</el-button>
                            </el-row>
                        </div>

                        <div style="height: 810px;width:85% ;background-color:transparent;margin-left:60px "
                             class="well well-lg">
                            <el-tabs v-model="activeName" @tab-click="handleClick" class="right" type="card">
                                <el-tab-pane label="全部" name="first">
                                    <div style="height: 700px;overflow:auto" id="div2">
                                        <ul style="margin-top: 10px;margin-left: -10px ">
                                            <li v-for="i in accessList">
                                                <el-card class="box-card" style="border-radius: 20px;width:430px ">
                                                    <div>
                                                        <el-row>
                                                            <el-col :span="2">
                                                                <img :src="require('../assets/img/'+(i.type=='进'?'in':(i.type=='未注册'?'unregistered':(i.type=='黑名单'?'blacklist':'forbid')))+'.png')"
                                                                     style=" height: 70px;width:65px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                                            </el-col>
                                                            <el-col :span="2" :offset="2">
                                                                <span>{{i.pass_time.split(" ")[1]}}</span>
                                                            </el-col>
                                                            <el-col :span="2" :offset="5">
                                                                <img :src="i.imageId" width="60px"
                                                                     height="60px"
                                                                     style="height: 60px;width:60px;border-radius: 50%;margin-top: -16px;margin-left: -20px">
                                                            </el-col>
                                                            <el-col :span="5" :offset="3">
                                                                <span>{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                            </el-col>
                                                            <el-col :span="18" :offset="10" style="margin-top: -40px">
                                                                <span style="margin-left: 180px"
                                                                      :style="{color:(i.type=='进'?'green':(i.type=='未注册'?'orange':(i.type=='黑名单'?'red':'blue')))}">{{i.type}}</span>
                                                            </el-col>
                                                        </el-row>
                                                        <p style="margin-left: 80px;margin-top: -50px">
                                                        </p>
                                                    </div>
                                                </el-card>
                                            </li>
                                        </ul>
                                        <span class="mess"></span>
                                    </div>

                                </el-tab-pane>
                                <el-tab-pane label="通行" name="second">
                                    <div style="height: 700px;overflow:auto" id="div3">
                                        <ul style="margin-top: 10px;margin-left: -10px">
                                            <li v-for="i in accessList" v-if="i.type=='进'">
                                                <el-card class="box-card" style="border-radius: 20px;width:430px ">
                                                    <div>
                                                        <el-row>
                                                            <el-col :span="2">
                                                                <img :src="require('../assets/img/'+'in'+'.png')"
                                                                     style=" height: 70px;width:65px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                                            </el-col>
                                                            <el-col :span="2" :offset="2">
                                                                <span>{{i.pass_time.split(" ")[1]}}</span>
                                                            </el-col>
                                                            <el-col :span="2" :offset="5">
                                                                <img :src="i.imageId" width="60px"
                                                                     height="60px"
                                                                     style="height: 60px;width:60px;border-radius: 50%;margin-top: -16px;margin-left: -20px">
                                                            </el-col>
                                                            <el-col :span="5" :offset="3">
                                                                <span>{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                            </el-col>
                                                            <el-col :span="18" :offset="10" style="margin-top: -40px">
                                                                <span style="margin-left: 180px;color: green">{{i.type}}</span>
                                                            </el-col>
                                                        </el-row>
                                                        <p style="margin-left: 80px;margin-top: -50px">
                                                        </p>
                                                    </div>
                                                </el-card>
                                            </li>
                                        </ul>
                                        <span class="mess"></span>
                                    </div>


                                </el-tab-pane>
                                <el-tab-pane label="未注册" name="third">
                                    <div style="height: 700px;overflow:auto" id="div4">
                                        <ul style="margin-top: 10px;margin-left: -10px">
                                            <li v-for="i in accessList" v-if="i.type=='未注册'">
                                                <el-card class="box-card" style="border-radius: 20px;width:430px ">
                                                    <div>
                                                        <el-row>
                                                            <el-col :span="2">
                                                                <img :src="require('../assets/img/'+'unregistered'+'.png')"
                                                                     style=" height: 70px;width:65px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                                            </el-col>
                                                            <el-col :span="2" :offset="2">
                                                                <span>{{i.pass_time.split(" ")[1]}}</span>
                                                            </el-col>
                                                            <el-col :span="2" :offset="5">
                                                                <img :src="i.imageId" width="60px"
                                                                     height="60px"
                                                                     style="height: 60px;width:60px;border-radius: 50%;margin-top: -16px;margin-left: -20px">
                                                            </el-col>
                                                            <el-col :span="5" :offset="3">
                                                                <span>{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                            </el-col>
                                                            <el-col :span="18" :offset="10" style="margin-top: -40px">
                                                                <span style="margin-left: 180px;color: orange">{{i.type}}</span>
                                                            </el-col>
                                                        </el-row>
                                                        <p style="margin-left: 80px;margin-top: -50px">
                                                        </p>
                                                    </div>
                                                </el-card>
                                            </li>
                                        </ul>
                                        <span class="mess"></span>
                                    </div>

                                </el-tab-pane>
                                <el-tab-pane label="禁行" name="fourth">
                                    <div style="overflow: auto;height: 700px" id="div5">
                                        <ul style="margin-top: 10px;margin-left: -10px">
                                            <li v-for="i in accessList" v-if="i.type=='禁止'">
                                                <el-card class="box-card" style="border-radius: 20px;width:430px ">
                                                    <div>
                                                        <el-row>
                                                            <el-col :span="2">
                                                                <img :src="require('../assets/img/'+'forbid'+'.png')"
                                                                     style=" height: 70px;width:65px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                                            </el-col>
                                                            <el-col :span="2" :offset="2">
                                                                <span>{{i.pass_time.split(" ")[1]}}</span>
                                                            </el-col>
                                                            <el-col :span="2" :offset="5">
                                                                <img :src="i.imageId" width="60px"
                                                                     height="60px"
                                                                     style="height: 60px;width:60px;border-radius: 50%;margin-top: -16px;margin-left: -20px">
                                                            </el-col>
                                                            <el-col :span="5" :offset="3">
                                                                <span>{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                            </el-col>
                                                            <el-col :span="18" :offset="10" style="margin-top: -40px">
                                                                <span style="margin-left: 180px;color: blue">{{i.type}}</span>
                                                            </el-col>
                                                        </el-row>
                                                        <p style="margin-left: 80px;margin-top: -50px">
                                                        </p>
                                                    </div>
                                                </el-card>
                                            </li>
                                        </ul>

                                        <span class="mess"></span>


                                    </div>
                                </el-tab-pane>
                                <el-tab-pane label="警报" name="five">
                                    <div style="overflow: auto;height: 700px" id="div6">
                                        <ul style="margin-top: 10px;margin-left: -10px">
                                            <li v-for="i in accessList" v-if="i.type=='禁止'">
                                                <el-card class="box-card" style="border-radius: 20px;width:430px ">
                                                    <div>
                                                        <el-row>
                                                            <el-col :span="2">
                                                                <img :src="require('../assets/img/'+'blacklist'+'.png')"
                                                                     style=" height: 70px;width:65px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                                            </el-col>
                                                            <el-col :span="2" :offset="2">
                                                                <span>{{i.pass_time.split(" ")[1]}}</span>
                                                            </el-col>
                                                            <el-col :span="2" :offset="5">
                                                                <img :src="i.imageId" width="60px"
                                                                     height="60px"
                                                                     style="height: 60px;width:60px;border-radius: 50%;margin-top: -16px;margin-left: -20px">
                                                            </el-col>
                                                            <el-col :span="5" :offset="3">
                                                                <span>{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                            </el-col>
                                                            <el-col :span="18" :offset="10" style="margin-top: -40px">
                                                                <span style="margin-left: 180px;color: red">{{i.type}}</span>
                                                            </el-col>
                                                        </el-row>
                                                        <p style="margin-left: 80px;margin-top: -50px">
                                                        </p>
                                                    </div>
                                                </el-card>
                                            </li>
                                        </ul>

                                        <span class="mess"></span>

                                    </div>
                                </el-tab-pane>
                            </el-tabs>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>

        <el-dialog :visible.sync="viewDialogVisible" width="65%" title="楼栋对应设备">
            <el-table
                    :data="tableData"
                    border
                    highlight-current-row
                    empty-text="暂无数据..."
                    style="width: 100%;"
                    height="579px">
                <el-table-column
                        align="center"
                        prop="floorNo"
                        label="楼栋">
                </el-table-column>
                <el-table-column
                        align="center"
                        prop="deviceId"
                        label="设备名">
                </el-table-column>
                <el-table-column
                        align="center"
                        prop="type"
                        label="进出类型">
                    <template scope="scope">
                        <el-tag type="success" v-if="scope.row.type=='0'">进</el-tag>
                        <el-tag type="danger" v-if="scope.row.type=='1'">出</el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                        align="center"
                        width="200"
                        label="操作">
                    <template scope="scope">
                        <el-button
                                class="btn_confirm"
                                size="small"
                                icon="el-icon-edit"
                                type="primary"
                                @click="editDevice(scope.row)">修改
                        </el-button>
                        <el-button
                                :offset="1"
                                size="small"
                                icon="el-icon-tickets"
                                type="danger"
                                @click="deleteDevice(scope.row)">删除

                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

            <div class="block" style="text-align: center; margin-top: 20px">
                <el-pagination
                        background
                        @current-change="handleCurrentChange"
                        :current-page="currentPage"
                        :page-size="pageSize"
                        layout="total, prev, pager, next, jumper"
                        :total="tableDataTotal">
                </el-pagination>
            </div>

        </el-dialog>

        <el-dialog :visible.sync="adddeviceDialogVisible" title="添加设备" :show-close="true">
            <el-row justify="center">
                <el-form label-position="right" label-width="100px" :model="form" style="margin-left: 150px">
                    <el-row>
                        <el-form-item label="楼栋号">
                            <el-col>
                                <el-input type="text" v-model="form.floorNo" style="width: 85%"></el-input>
                            </el-col>
                        </el-form-item>
                    </el-row>
                    <el-row>
                        <el-form-item label="设备Id">
                            <el-input type="text" v-model="form.deviceId" style="width: 85%"></el-input>
                        </el-form-item>
                    </el-row>
                    <el-row>
                        <el-form-item label="进出校状态">
                            <el-radio v-model="form.type" label="0">进楼设备</el-radio>
                            <el-radio v-model="form.type" label="1">出楼设备</el-radio>
                        </el-form-item>
                    </el-row>
                </el-form>
                <el-row type="flex" justify="center">
                    <el-col :span="3" :offset="1">
                        <el-button type="primary" v-on:click="onAdd()">添加</el-button>
                    </el-col>
                    <el-col :span="3" :offset="1">
                        <el-button v-on:click="deviceExit()" type="danger">取消</el-button>
                    </el-col>
                </el-row>
            </el-row>
        </el-dialog>

        <el-dialog :visible.sync="modifydeviceDialogVisible" title="修改设备信息" :show-close="true">
            <el-row justify="center">
                <el-form label-position="right" label-width="100px" :model="deviceForm" style="margin-left: 150px">
                    <el-row>
                        <el-form-item label="楼栋">
                            <el-col>
                                <el-input type="text" v-model="deviceForm.flooorNo" style="width: 85%"></el-input>
                            </el-col>
                        </el-form-item>
                    </el-row>
                    <el-row>
                        <el-form-item label="设备Id">
                            <el-input type="text" v-model="deviceForm.deviceId" style="width: 85%"></el-input>
                        </el-form-item>
                    </el-row>
                    <el-row>
                        <el-form-item label="进出校状态">
                            <el-radio v-model="deviceForm.type" label="0">进校设备</el-radio>
                            <el-radio v-model="deviceForm.type" label="1">出校设备</el-radio>
                        </el-form-item>
                    </el-row>
                </el-form>
                <el-row type="flex" justify="center">
                    <el-col :span="3" :offset="1">
                        <el-button type="primary" v-on:click="onEdit()">修改</el-button>
                    </el-col>
                    <el-col :span="3" :offset="1">
                        <el-button v-on:click="modifydeviceDialogVisible=false" type="danger">取消</el-button>
                    </el-col>
                </el-row>
            </el-row>
        </el-dialog>


    </div>
</template>

<script>
    let _this;
    import echarts from 'echarts'
    import request from '../api/request'

    var timeOut;
    var timeFetchAccess;
    export default {
        name: "dormitory",
        data() {
            _this = this
            return {
                infoManage: 'first',
                //2100, 800, 500, 200, 2000, 2100, 780, 200, 500, 800, 1900, 500
                attendanceNumber: [],
                indormitoryNumber: [],
                outdormitoryNumber: [],
                attendAndInOrOut: ['', '', '', '', '', '', '', '', '', '', '', ''],
                count: 10,
                userInfo: "",
                pageSize: EveryPageNum,//每一页的num
                currentPage: 1,
                accessPageOrSize: {
                    AllcurrentPage: 1,
                    passCurrentPage: 0,
                    regisCurrentPage: 0,
                    warnCurrentPage: 0,
                    alarmCurrentPage: 0
                },
                deviceId: [],
                accessList: [],
                index: 0,
                attendanceNumList: {
                    total: 0,
                    attendanceNum: 0,
                    inDormitory: 0,
                    outDormitory: 0
                },
                tableData: [],
                tableDataTotal: 0,
                nightPage: 1,
                nightPageSize: 5,
                nightFallList: [],
                passAccessList: [],
                passTotal: 0,
                passCount: 0,
                nightFallListLength: 0,
                nightTotal: 0,
                activeName: 'first',
                accesscount: 0,
                picName: '',
                totalRecords: 0,
                imageId: '',
                name: "",
                viewDialogVisible: false,
                modifydeviceDialogVisible: false,
                adddeviceDialogVisible:false,
                deviceForm: {
                    id: 0,
                    flooorNo: '',
                    deviceId: '',
                    type: ''
                },
                form: {
                    id:'',
                    floorNo: '',
                    deviceId: '',
                    type: ''
                },
            }
        },
        methods: {
            handleClick(tab, event) {
                switch (_this.activeName) {
                    case "first":
                        _this.handleScroll2();
                        break;
                    case "second":
                        _this.handleScroll3();
                        break;
                    case "third":
                        _this.handleScroll4();
                        break;
                    case "fourth":
                        _this.handleScroll5();
                        break;
                    case "five":
                        _this.handleScroll6();
                        break;
                }
                $(".mes").html("")
                $(".mess").html("")
                $(".count").html("")
            },
            search() {

            },
            floorInfo() {
                _this.viewDialogVisible = true
                _this.fetchFloorDevice();
            },
            add(){
                _this.adddeviceDialogVisible=true;
            },
            editDevice(item) {
                _this.modifydeviceDialogVisible = true;
                _this.deviceForm.id = item.id;
                _this.deviceForm.flooorNo = item.floorNo;
                _this.deviceForm.deviceId = item.deviceId;
                _this.deviceForm.type = item.type.toString();
            },
            deleteDevice(item) {
                let params = new URLSearchParams();
                params.append("id", item.id)
                request({
                    url: HOST + 'floor/device/delete',
                    method: 'post',
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        showMessage(_this, '删除成功', 1)
                        _this.getFloorDevice(item.flooorNo)
                        _this.fetchFloorDevice();
                    } else {
                        showMessage(_this, '删除失败', 0)
                    }
                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },
            onAdd(){
                let params = new URLSearchParams();
                if (_this.verifyForm(_this.form)) {
                    params.append("jsonDate", JSON.stringify(_this.form))
                    request({
                        url: HOST + "floor/device/add",
                        method: 'post',
                        data: params
                    }).then(res => {
                        if (res.data.code == 200) {
                            _this.adddeviceDialogVisible = false;
                            showMessage(_this, '添加成功', 1)
                            _this.fetchStudentCount();
                            _this.fetchDevice();
                        } else {
                            showMessage(_this, '添加失败', 0)
                            _this.deviceExit()
                        }
                    }).catch(error => {
                        showMessage(_this, error, 0)
                        _this.deviceExit()
                    })
                }
            },
            deviceExit() {
                _this.adddeviceDialogVisible = false
                _this.form = {}
            },
            onEdit(item) {
                let params = new URLSearchParams();
                params.append("jsonDate", JSON.stringify(_this.deviceForm))
                request({
                    url: HOST + 'floor/device/update',
                    method: 'post',
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.modifydeviceDialogVisible = false;
                        showMessage(_this, '修改成功', 1)
                        _this.getFloorDevice(_this.deviceForm.flooorNo)
                        _this.fetchFloorDevice();

                    } else {
                        _this.modifydeviceDialogVisible = false;
                        showMessage(_this, '修改失败', 0)
                    }
                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },
            fetchFloorDevice() {
                let params = new URLSearchParams();
                params.append("page", _this.currentPage)
                params.append("size", _this.pageSize)
                request({
                    url: HOST + "floor/device/list",
                    method: "post",
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.tableData = res.data.data.list;
                        _this.tableDataTotal = res.data.data.total;
                    } else {
                        showMessage(_this, "获取通行记录失败", 0)
                    }
                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },
            handleCurrentChange(val) {
                _this.currentPage = val
            },
            SetEchart() {
                let myChart = echarts.init(document.getElementById('myChart'))
                let attendace = echarts.init(document.getElementById('attendace'))
                let outSide = echarts.init(document.getElementById('outSide'))
                var option = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
                        },
                    },
                    grid: {
                        left: '4%',
                        right: '3%',
                        bottom: '5%',
                        containLabel: true
                    },
                    xAxis: {
                        show: false,
                        type: 'category',
                        data: _this.attendAndInOrOut
                    },
                    yAxis: {
                        show: false,
                        type: 'value'
                    },
                    series: [
                        {
                            type: 'line',
                            itemStyle: {
                                normal: {
                                    barBorderColor: 'rgba(0,0,0,0)',
                                    color: 'rgba(250,0,24,12)'
                                },
                                emphasis: {
                                    barBorderColor: 'rgba(0,0,0,0)',
                                    color: 'rgba(0,0,0,0)'
                                }
                            },
                        },
                        {
                            name: '人数',
                            type: 'line',
                            stack: '总量',
                            label: {},
                            data: _this.attendanceNumber,
                            areaStyle: {normal: {}}
                        }
                    ]
                }
                var option1 = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
                        },
                    },
                    grid: {
                        left: '4%',
                        right: '3%',
                        bottom: '5%',
                        containLabel: true
                    },
                    xAxis: {
                        show: false,
                        type: 'category',
                        data: _this.attendAndInOrOut
                    },
                    yAxis: {
                        show: false,
                        type: 'value'
                    },
                    series: [
                        {
                            type: 'line',
                            itemStyle: {
                                normal: {
                                    barBorderColor: 'rgba(0,0,0,0)',
                                    color: 'rgba(250,0,24,12)'
                                },
                                emphasis: {
                                    barBorderColor: 'rgba(0,0,0,0)',
                                    color: 'rgba(0,0,0,0)'
                                }
                            },
                        },
                        {
                            name: '人数',
                            type: 'line',
                            stack: '总量',
                            label: {},
                            data: _this.indormitoryNumber,
                            areaStyle: {normal: {}}
                        }
                    ]
                }
                var option2 = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                            type: 'line'        // 默认为直线，可选为：'line' | 'shadow'
                        },
                    },
                    grid: {
                        left: '4%',
                        right: '3%',
                        bottom: '5%',
                        containLabel: true
                    },
                    xAxis: {
                        show: false,
                        type: 'category',
                        data: _this.attendAndInOrOut
                    },
                    yAxis: {
                        show: false,
                        type: 'value'
                    },
                    series: [
                        {
                            type: 'line',
                            itemStyle: {
                                normal: {
                                    barBorderColor: 'rgba(0,0,0,0)',
                                    color: 'rgba(250,0,24,12)'
                                },
                                emphasis: {
                                    barBorderColor: 'rgba(0,0,0,0)',
                                    color: 'rgba(0,0,0,0)'
                                }
                            },
                        },
                        {
                            name: '人数',
                            type: 'line',
                            stack: '总量',
                            label: {},
                            data: _this.outdormitoryNumber,
                            areaStyle: {normal: {}}
                        }
                    ]
                }
                myChart.setOption(option);
                attendace.setOption(option1);
                outSide.setOption(option2);
            },
            handleScroll() {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop = document.getElementById("div1").scrollTop
                //变量windowHeight是可视区的高度
                var windowHeight = document.getElementById("div1").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight = document.getElementById("div1").scrollHeight
                //滚动条到底部的条件
                if (scrollTop + windowHeight == scrollHeight) {
                    if (_this.nightFallListLength == _this.nightTotal) {
                        $(".mes").html("已无更多数据")
                        $(".count").html("总数：" + _this.nightFallListLength)
                        return
                    }
                    _this.nightPage += 1;
                    _this.fethcNightFall(_this.userInfo.floorNo, 1)
                }
            },
            fetchAccess(floorDevice, name) {
                let params = new URLSearchParams();
                params.append("page", _this.currentPage)
                params.append("size", _this.pageSize)
                params.append("floorDevice", floorDevice)
                if (name != null || name != "") {
                    params.append("name", name)
                }
                request({
                    url: HOST + "/access/list",
                    method: "post",
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.accessList = res.data.data.list;
                        _this.totalRecords = res.data.data.total;
                        _this.accesscount = _this.accessList.length;
                        let list = res.data.data.list
                        for (let i = 0; i < list.length; i++) {
                            _this.getImage(list[i].imageId, list[i]);
                        }
                    } else {
                        showMessage(_this, "获取通行记录失败", 0)
                    }

                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },
            fetchAttendanceAndInOrOut() {
                request({
                    url: HOST + "access/attendanceCount",
                    method: "post",
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.attendanceNumList.attendanceNum = res.data.data.attendanceNum
                        _this.attendanceNumList.inDormitory = res.data.data.inDormitory
                        _this.attendanceNumList.outDormitory = res.data.data.outDormitory

                    } else {
                        showMessage(_this, "获取进出记录失败", 0)
                    }
                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },
            handleTotal() {
                if (_this.attendanceNumber.length != _this.attendAndInOrOut.length) {
                    _this.attendanceNumber[_this.index] = _this.attendanceNumList.attendanceNum;
                }
                if (_this.indormitoryNumber.length != _this.attendAndInOrOut.length) {
                    _this.indormitoryNumber[_this.index] = _this.attendanceNumList.inDormitory;
                }
                if (_this.outdormitoryNumber.length != _this.attendAndInOrOut.length) {
                    _this.outdormitoryNumber[_this.index] = _this.attendanceNumList.outDormitory;
                }
                _this.index++;
                _this.SetEchart();
            },
            handleScroll2() {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop2 = document.getElementById("div2").scrollTop
                if (scrollTop2 > 1) {
                    clearInterval(timeFetchAccess)
                } else {
                    clearInterval(timeFetchAccess)
                    _this.accessPageOrSize.AllcurrentPage = 1;
                    timeFetchAccess = setInterval(function () {
                        _this.fetchAccess(_this.deviceId, "")
                    }, 3000)
                }
                //变量windowHeight是可视区的高度
                var windowHeight2 = document.getElementById("div2").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight2 = document.getElementById("div2").scrollHeight
                //滚动条到底部的条件
                if (scrollTop2 + windowHeight2 == scrollHeight2) {

                    //判断查询出来的数据长度是否等于总数量，如果不等于，则继续查，如果等于，则return出去，不再继续查
                    if (_this.accesscount == _this.totalRecords) {
                        $(".mess").html("无更多数据")
                        return
                    }
                    _this.accessPageOrSize.AllcurrentPage += 1;
                    let params = new URLSearchParams();
                    params.append("page", _this.accessPageOrSize.AllcurrentPage)
                    params.append("size", _this.pageSize)
                    params.append("floorDevice", _this.userInfo.floorNo)
                    request({
                        url: HOST + "/access/list",
                        method: 'post',
                        params: params
                    }).then(res => {
                        if (res.data.code == 200) {
                            let list = res.data.data.list
                            for (let i = 0; i < list.length; i++) {
                                _this.accessList.push(list[i]);
                            }
                            for (let i = 0; i < list.length; i++) {
                                _this.getImage(list[i].imageId, list[i]);
                            }

                            _this.accesscount = _this.accessList.length;
                        } else {
                            showMessage(_this, "获取查询数据失败！");
                        }
                    }).catch(error => {
                        console.log(error)

                    })

                }

            },
            handleScroll3() {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop3 = document.getElementById("div3").scrollTop
                if (scrollTop3 > 1) {
                    clearInterval(timeFetchAccess)
                } else {
                    _this.accessPageOrSize.passCurrentPage = 1;
                }
                //变量windowHeight是可视区的高度
                var windowHeight3 = document.getElementById("div3").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight3 = document.getElementById("div3").scrollHeight
                //滚动条到底部的条件
                if (scrollTop3 + windowHeight3 == scrollHeight3) {

                    //判断查询出来的数据长度是否等于总数量，如果不等于，则继续查，如果等于，则return出去，不再继续查
                    if (_this.accesscount == _this.totalRecords) {
                        $(".mess").html("无更多数据")
                        return
                    }
                    _this.accessPageOrSize.passCurrentPage += 1;
                    let params = new URLSearchParams();
                    params.append("page", _this.accessPageOrSize.passCurrentPage)
                    params.append("size", _this.pageSize)
                    params.append("floorDevice", _this.userInfo.floorNo)
                    request({
                        url: HOST + "/access/list",
                        method: 'post',
                        params: params
                    }).then(res => {
                        if (res.data.code == 200) {
                            let list = res.data.data.list
                            for (let i = 0; i < list.length; i++) {
                                _this.accessList.push(list[i]);
                            }
                            for (let i = 0; i < list.length; i++) {
                                _this.getImage(list[i].imageId, list[i]);
                            }

                            _this.accesscount = _this.accessList.length;
                        } else {
                            showMessage(_this, "获取查询数据失败！");
                        }
                    }).catch(error => {
                        console.log(error)

                    })

                }

            },
            handleScroll4() {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop4 = document.getElementById("div4").scrollTop
                clearInterval(timeFetchAccess)
                //变量windowHeight是可视区的高度
                var windowHeight4 = document.getElementById("div4").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight4 = document.getElementById("div4").scrollHeight
                //滚动条到底部的条件
                if (scrollTop4 + windowHeight4 == scrollHeight4) {
                    //判断查询出来的数据长度是否等于总数量，如果不等于，则继续查，如果等于，则return出去，不再继续查
                    if (_this.accesscount == _this.totalRecords) {
                        $(".mess").html("无更多数据")
                        return
                    }
                    _this.accessPageOrSize.regisCurrentPage += 1;
                    let params = new URLSearchParams();
                    params.append("page", _this.accessPageOrSize.regisCurrentPage)
                    params.append("size", _this.pageSize)
                    params.append("floorDevice", _this.userInfo.floorNo)
                    request({
                        url: HOST + "/access/list",
                        method: 'post',
                        params: params
                    }).then(res => {
                        if (res.data.code == 200) {
                            let list = res.data.data.list
                            for (let i = 0; i < list.length; i++) {
                                _this.accessList.push(list[i]);
                            }
                            for (let i = 0; i < list.length; i++) {
                                _this.getImage(list[i].imageId, list[i]);
                            }
                            _this.accesscount = _this.accessList.length;
                        } else {
                            showMessage(_this, "获取查询数据失败！");
                        }
                    }).catch(error => {
                        console.log(error)

                    })

                }

            },
            handleScroll5() {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop5 = document.getElementById("div5").scrollTop
                if (scrollTop5 > 1) {
                    clearInterval(timeFetchAccess)
                } else {
                    _this.accessPageOrSize.warnCurrentPage = 1;
                }
                //变量windowHeight是可视区的高度
                var windowHeight5 = document.getElementById("div5").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight5 = document.getElementById("div5").scrollHeight
                //滚动条到底部的条件
                if (scrollTop5 + windowHeight5 == scrollHeight5) {

                    //判断查询出来的数据长度是否等于总数量，如果不等于，则继续查，如果等于，则return出去，不再继续查
                    if (_this.accesscount == _this.totalRecords) {
                        $(".mess").html("无更多数据")
                        return
                    }
                    _this.accessPageOrSize.warnCurrentPage += 1;
                    let params = new URLSearchParams();
                    params.append("page", _this.accessPageOrSize.warnCurrentPage)
                    params.append("size", _this.pageSize)
                    params.append("floorDevice", _this.userInfo.floorNo)
                    request({
                        url: HOST + "/access/list",
                        method: 'post',
                        params: params
                    }).then(res => {
                        if (res.data.code == 200) {
                            let list = res.data.data.list
                            for (let i = 0; i < list.length; i++) {
                                _this.accessList.push(list[i]);
                            }
                            for (let i = 0; i < list.length; i++) {
                                _this.getImage(list[i].imageId, list[i]);
                            }
                            _this.accesscount = _this.accessList.length;
                        } else {
                            showMessage(_this, "获取查询数据失败！");
                        }
                    }).catch(error => {
                        console.log(error)

                    })

                }

            },
            handleScroll6() {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop6 = document.getElementById("div6").scrollTop
                if (scrollTop6 > 1) {
                    clearInterval(timeFetchAccess)
                } else {
                    _this.accessPageOrSize.alarmCurrentPage = 1;
                }
                //变量windowHeight是可视区的高度
                var windowHeight6 = document.getElementById("div6").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight6 = document.getElementById("div6").scrollHeight
                //滚动条到底部的条件
                if (scrollTop6 + windowHeight6 == scrollHeight6) {

                    //判断查询出来的数据长度是否等于总数量，如果不等于，则继续查，如果等于，则return出去，不再继续查
                    if (_this.accesscount == _this.totalRecords) {
                        $(".mess").html("无更多数据")
                        return
                    }
                    _this.accessPageOrSize.alarmCurrentPage += 1;
                    let params = new URLSearchParams();
                    params.append("page", _this.accessPageOrSize.alarmCurrentPage)
                    params.append("size", _this.pageSize)
                    params.append("floorDevice", _this.userInfo.floorNo)
                    request({
                        url: HOST + "/access/list",
                        method: 'post',
                        params: params
                    }).then(res => {
                        if (res.data.code == 200) {
                            let list = res.data.data.list
                            for (let i = 0; i < list.length; i++) {
                                _this.accessList.push(list[i]);
                            }
                            for (let i = 0; i < list.length; i++) {
                                _this.getImage(list[i].imageId, list[i]);
                            }

                            _this.accesscount = _this.accessList.length;
                        } else {
                            showMessage(_this, "获取查询数据失败！");
                        }
                    }).catch(error => {
                        console.log(error)

                    })

                }

            },
            fethcNightFall(floorNo, statu) {

                let parmas = new URLSearchParams();
                parmas.append("page", _this.nightPage)
                parmas.append("size", _this.nightPageSize);
                parmas.append("floorNo", floorNo)
                request({
                    url: HOST + "access/getNightfall",
                    method: "post",
                    data: parmas
                }).then(res => {
                    if (res.data.code == 200) {
                        if (statu == 0) {
                            _this.nightFallList = res.data.data.list[0].absentees;
                            _this.nightTotal = res.data.data.total;
                        } else {
                            let list = res.data.data.list[0].absentees;
                            for (let j = 0; j < list.length; j++) {
                                _this.nightFallList.push(list[i])
                            }
                        }
                        _this.nightFallListLength = _this.nightFallList.length;
                        for (let i = 0; i < _this.nightFallList.length; i++) {
                            _this.getImage(_this.nightFallList[i].imageId, _this.nightFallList[i])
                        }

                    } else {
                        showMessage(_this, "晚归考勤查询失败")
                    }
                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },
            fetchNight() {
                var date = new Date();
                let time = date.getHours()
                if (time == 5 || time > 5) {
                    _this.fethcNightFall(_this.userInfo.floorNo, 0);
                }
            },
            getImage(id, item) {
                request({
                    url: HOST + 'image/' + id,
                    async: false,
                    method: 'post'
                }).then(res => {
                    if (res.data.code == 200) {
                        item.imageId = 'data:image/jpg;base64,' + res.data.data;
                    } else {
                        showMessage("没有查到图片");
                    }

                }).catch(error => {
                    console.log(error);
                })

            },
            passAccessRecord(type, currentPage) {
                let params = new URLSearchParams();
                params.append("page", currentPage)
                params.append("size", _this.pageSize)
                params.append("floorDevice", _this.deviceId)
                params.append("type", type)
                request({
                    url: HOST + "/access/getAccess",
                    method: "post",
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.passAccessList = res.data.data.list;
                        _this.passTotal = res.data.data.total;
                        _this.passCount = _this.accessList.length;
                        let list = res.data.data.list
                        for (let i = 0; i < list.length; i++) {
                            _this.getImage(list[i].imageId, list[i]);
                        }
                    } else {
                        showMessage(_this, "获取通行记录失败", 0)
                    }

                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },
            getFloorDevice(floorNo) {
                let params = new URLSearchParams();
                params.append("floorNo", floorNo)
                request({
                    url: HOST + "floor/device/getDevice",
                    method: "post",
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        let floorDevice = res.data.data;
                        for (let i = 0; i < floorDevice.length; i++) {
                            _this.deviceId.push(floorDevice[i].deviceId)
                        }
                        _this.fetchAccess(_this.deviceId, "")
                    } else {
                        showMessage(_this, '设备信息获取失败', 0)
                    }
                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },
            verifyForm(formObj) {
                let result = true;
                if (formObj.deviceId == null || formObj.deviceId == "") {
                    showMessage(_this, "设备id不能为空", 0)
                    result = false;
                } else if (formObj.floorNo == null || formObj.floorNo == "") {
                    showMessage(_this, "设备名称不能为空", 0)
                    result = false;
                }

                return result;
            },
        },


        created() {
            this.userInfo = JSON.parse(sessionStorage.getItem("user"))
            _this.getFloorDevice(_this.userInfo.floorNo);

        },
        mounted() {
            _this.fetchAttendanceAndInOrOut();
            timeOut = setTimeout(function () {
                _this.handleTotal()
            }, 200)
            timeFetchAccess = setInterval(function () {
                _this.fetchAccess(_this.deviceId, "")
            }, 3000)
            setInterval(function () {
                _this.handleTotal();
            }, 1000 * 60 * 60)
            _this.fetchNight();
            document.getElementById("div1").addEventListener("scroll", _this.handleScroll);
            document.getElementById("div2").addEventListener("scroll", _this.handleScroll2);
            document.getElementById("div3").addEventListener("scroll", _this.handleScroll3);
            document.getElementById("div4").addEventListener("scroll", _this.handleScroll4);
            document.getElementById("div5").addEventListener("scroll", _this.handleScroll5);
            document.getElementById("div6").addEventListener("scroll", _this.handleScroll6);

        },
        destroyed() {
            document.getElementById("div1").removeEventListener("scroll", _this.handleScroll)
            document.getElementById("div2").removeEventListener("scroll", _this.handleScroll2)
            document.getElementById("div3").removeEventListener("scroll", _this.handleScroll3)
            document.getElementById("div4").removeEventListener("scroll", _this.handleScroll4)
            document.getElementById("div5").removeEventListener("scroll", _this.handleScroll5)
            document.getElementById("div6").removeEventListener("scroll", _this.handleScroll6)
            clearTimeout(this.timeOut)
            clearInterval(timeFetchAccess)
        },


    }

</script>

<style>
    .title {
        font: 25px Extra Small;
        color: #6a747c;
        font-weight: bold;

    }

    .el-tabs__item {
        color: black;
    }

    .el-tabs__nav-scroll {
        float: right;
    }

    .bg-purple {
        background: #f6f4ff;

    }

    .bg-purples {
        background: #4a6687;

    }

    .grid-content {
        border-radius: 4px;
        min-height: 935px;
    }

    li {
        list-style: none;
        margin-top: 18px;
    }

    .text {
        font-size: 14px;
    }

    .item {
        padding: 15px 0;
    }

    .box-card {
        height: 70px;
        width: 400px;
    }


    /*右边选项卡字体*/
    .el-tabs__item {
        color: black;
        margin-left: 15px;

    }

    .bottom {
        margin-top: 13px;
        line-height: 12px;
    }

    /*右边标签页*/
    .right .el-tabs__nav-scroll {
        float: left;
        margin-left: 20px;
    }

    /*右边全部第一个卡片*/
    .el-col-offset-0 {
        margin-left: 8.33333%;
    }

    .el-col-20 {
        margin-left: 20px;
        width: 95.33333%;
    }

    .el-row {
        margin-bottom: 10px;

    &
    :last-child {
        margin-bottom: 0;
    }

    }
    .el-col {
        border-radius: 4px;
    }

    .el-tabs--card > .el-tabs__header .el-tabs__item.is-active {
        border-bottom-color: #409eff;
    }

    .grid-content {
        border-radius: 4px;
        min-height: 935px;
    }

</style>