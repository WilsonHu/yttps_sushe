<template xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">
    <div style="width: 100%;height: 100%;background-color: #F6F4FF;">
        <el-row :gutter="0" style="padding-top: 50px">
            <el-col :span="8">
                <div class="grid-content bg-purple">
                    <!--页面左方三个div-->
                    <div style="height: 100%">
                        <div style="height: 150px; width:85% ;background-color: white;margin-left:60px;margin-top: 10px "
                             class="well well-lg">
                            <el-col style="font-size: 20px;color: #6a747c; font-weight: bold;margin-top: -5px">
                                <i>宿舍信息</i>
                            </el-col>
                            <div style="border: 1px solid silver;margin-top: 27px"></div>
                            <div>
                                <el-col :span="5" :offset="2">
                                    <p style="margin-left: 20px;margin-top: 20px">
                                        <span>楼号</span><br>
                                        <i style=" font:30px Extra Small;  color: black;">{{userInfo.floorNo}}</i>
                                    </p>
                                </el-col>
                                <el-col :offset="4" style="border: 1px solid silver;width: 1px;height: 8rem"></el-col>
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
                            <i style=" font:20px Extra Small; font-weight: bold;color: #6a747c;">出入信息统计</i>
                            <el-tabs v-model="infoManage" @tab-click="handleClick" style="margin-top: -20px">
                                <el-tab-pane label="当前" name="first">
                                    <el-row style="margin-top: 30px;">
                                        <el-col>
                                            <img src="../assets/img/ic_check.png" width="50px" height="50px"/>
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
                                            <img src="../assets/img/ic_inroom.png" width="50px" height="50px"/>
                                        </el-col>
                                        <el-col :offset="4" style="margin-top: -50px">
                                            <span>当前在寝学生数</span><br/>
                                            <span style="font-size: 30px;">{{attendanceNumList.inDormitory}}</span>

                                        </el-col>
                                        <el-col :span="8" :offset="7">
                                            <div id="attendace"
                                                 style="width: 350px;height: 180px;margin-top: -150px"></div>
                                        </el-col>
                                    </el-row>
                                    <el-row style="margin-top: 60px">
                                        <el-col>
                                            <img src="../assets/img/ic_outdoor.png" width="50px" height="50px"/>
                                        </el-col>
                                        <el-col :offset="4" style="margin-top: -50px">
                                            <span>当前外出学生数</span><br/>
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
                                        <ul style="margin-top: 10px;margin-left: -30px">
                                            <li v-for="i in nightFallList">
                                                <el-card style="height: 80px;width: 450px;border-radius: 20px">
                                                    <div>
                                                        <el-row>
                                                            <el-col :span="2">
                                                                <img :src="require('../assets/img/'+(i.type.split('_')[0]=='进'? 'in':'forbid')+'.png')"
                                                                     style=" height: 78px;width:70px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                                            </el-col>
                                                            <el-col :span="2" :offset="2">
                                                                <img :src="i.imageId" width="70px"
                                                                     height="70px"
                                                                     style="border-radius: 50%;margin-top: -15px">
                                                            </el-col>
                                                            <el-col :span="5" :offset="4">
                                                                <span>{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                            </el-col>
                                                            <el-col :span="18" :offset="14" style="margin-top: -50px">
                                                                <span>{{i.pass_time}}</span><span
                                                                    :style="{color:(i.type.split('_')[0]=='进'?'green':'red')}">【{{i.type.split("_")[0]}}】</span>
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
                <div class="grid-content" style="background-color: #585F79">
                    <div style="height: 500px;width: 400px;">
                        <object type='application/x-vlc-plugin' id='vlc' events='True'
                                classid='clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921'
                                codebase="http://downloads.videolan.org/pub/videolan/vlc/latest/win32/axvlc.cab"
                                width="587" height="340" style="margin-top: 50px;margin-left: 30px">
                            <param name='mrl' value='rtsp://admin:admin123@10.250.62.134:554/h264/ch33/main/av_stream'/>
                            <param name='volume' value='50'/>
                            <param name='autoplay' value='true'/>
                            <param name="enablefullscreen controls" value="1">
                            <param name="windowlessVideo" value="0">
                            <param name='loop' value='false'/>
                            <param name='fullscreen' value='false'/>
                            <param name="height" value="600">
                        </object>
                    </div>
                </div>
            </el-col>

            <el-col :span="8">
                <div class="grid-content bg-purple">
                    <div style="height: 100%">
                        <div style=" min-height: 935px;;width:85% ;background-color:transparent;margin-left:60px "
                             class="well well-lg">
                            <i class="title">通行记录</i>
                            <el-row style="margin-top:-30px;">
                                <el-input style="width: 200px;margin-left: 150px;border-radius: 20%"
                                          placeholder="按姓名查询" clearable
                                          auto-complete="off" v-model="name">
                                    <i slot="prefix" class="el-input__icon el-icon-search"></i>
                                </el-input>
                                <el-button type="info" plain @click="search">搜索</el-button>
                            </el-row>
                            <el-row>
                                <mt-navbar v-model="selected" style="background-color: #F6F4FF;">
                                    <mt-tab-item id="1">全部</mt-tab-item>
                                    <mt-tab-item id="2">通行</mt-tab-item>
                                    <mt-tab-item id="3">未注册</mt-tab-item>
                                    <mt-tab-item id="4">警报</mt-tab-item>
                                </mt-navbar>
                                <!-- tab-container -->
                                <mt-tab-container v-model="selected">
                                    <mt-tab-container-item id="1">
                                        <div style="height: 780px;overflow:auto" id="div2">
                                            <ul style="margin-top: 10px;margin-left: -10px ">
                                                <li v-for="i in accessList">
                                                    <el-card
                                                            style="border-radius: 10px;width:460px;height: 80px;margin-left: -30px">
                                                        <div>
                                                            <el-row>
                                                                <el-col :span="2">
                                                                    <img :src="require('../assets/img/'+(i.type.split('_')[0]=='进'?'in':(i.type=='未注册'?'unregistered':(i.type=='黑名单'?'blacklist':'forbid')))+'.png')"
                                                                         style=" height: 80px;width:70px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                                                </el-col>
                                                                <el-col :span="2" :offset="2"
                                                                        style="margin-top: 8px;font-size: 15px;">
                                                                    <span>{{i.pass_time.split(" ")[1]}}</span>
                                                                </el-col>
                                                                <el-col :span="2" :offset="3">
                                                                    <img :src="i.imageId" width="70px"
                                                                         height="70px"
                                                                         style="border-radius: 50%;margin-top: -16px;">
                                                                </el-col>
                                                                <el-col :span="5" :offset="4" style="font-size: 15px;">
                                                                    <span>{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                                </el-col>
                                                                <el-col :span="5" :offset="21"
                                                                        style="margin-top: -50px;font-size: 15px;">
                                                                    <span :style="{color:(i.type.split('_')[0]=='进'?'green':(i.type=='未注册'?'orange':(i.type=='黑名单'?'red':'blue')))}">{{i.type.split('_')[0]}}</span>
                                                                </el-col>
                                                            </el-row>

                                                        </div>
                                                    </el-card>
                                                </li>
                                            </ul>
                                            <span class="mess"></span>
                                        </div>

                                    </mt-tab-container-item>
                                    <mt-tab-container-item id="2">
                                        <div style="height: 780px;overflow:auto" id="div3">
                                            <ul style="margin-top: 10px;margin-left: -10px ">
                                                <li v-for="i in accessByPassList"
                                                    v-if="i.type.split('_')[0]=='进' ||i.type.split('_')[0]=='出'">
                                                    <el-card
                                                            style="border-radius: 10px;width:460px;height: 80px;margin-left: -30px">
                                                        <div>
                                                            <el-row>
                                                                <el-col :span="2">
                                                                    <img :src="require('../assets/img/'+(i.type.split('_')[0]=='进'? 'in':'forbid')+'.png')"
                                                                         style=" height: 80px;width:70px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                                                </el-col>
                                                                <el-col :span="2" :offset="2"
                                                                        style="margin-top: 8px;font-size: 15px;">
                                                                    <span>{{i.pass_time.split(" ")[1]}}</span>
                                                                </el-col>
                                                                <el-col :span="2" :offset="3">
                                                                    <img :src="i.imageId" width="70px"
                                                                         height="70px"
                                                                         style="border-radius: 50%;margin-top: -16px;">
                                                                </el-col>
                                                                <el-col :span="5" :offset="4" style="font-size: 15px;">
                                                                    <span>{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                                </el-col>
                                                                <el-col :span="5" :offset="21"
                                                                        style="margin-top: -50px;font-size: 15px;">
                                                                    <span :style="{color:(i.type.split('_')[0]=='进'?'green':(i.type=='未注册'?'orange':(i.type=='黑名单'?'red':'blue')))}">{{i.type.split('_')[0]}}</span>
                                                                </el-col>
                                                            </el-row>

                                                        </div>
                                                    </el-card>
                                                </li>
                                            </ul>
                                            <span class="mess"></span>
                                        </div>
                                    </mt-tab-container-item>
                                    <mt-tab-container-item id="3">

                                        <div style="height: 780px;overflow:auto" id="div4">
                                            <ul style="margin-top: 10px;margin-left: -10px ">
                                                <li v-for="i in accessList" v-if="i.type=='未注册'">
                                                    <el-card
                                                            style="border-radius: 10px;width:460px;height: 80px;margin-left: -30px">
                                                        <div>
                                                            <el-row>
                                                                <el-col :span="2">
                                                                    <img :src="require('../assets/img/'+'unregistered'+'.png')"
                                                                         style=" height: 80px;width:70px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                                                </el-col>
                                                                <el-col :span="2" :offset="2"
                                                                        style="margin-top: 8px;font-size: 15px;">
                                                                    <span>{{i.pass_time.split(" ")[1]}}</span>
                                                                </el-col>
                                                                <el-col :span="2" :offset="3">
                                                                    <img :src="i.imageId" width="70px"
                                                                         height="70px"
                                                                         style="border-radius: 50%;margin-top: -16px;">
                                                                </el-col>
                                                                <el-col :span="5" :offset="4" style="font-size: 15px;">
                                                                    <span>{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                                </el-col>
                                                                <el-col :span="3" :offset="20"
                                                                        style="margin-top: -50px;font-size: 15px;">
                                                                    <span style="color: orange;">{{i.type}}</span>
                                                                </el-col>
                                                            </el-row>

                                                        </div>
                                                    </el-card>
                                                </li>
                                            </ul>
                                            <span class="mess"></span>
                                        </div>
                                    </mt-tab-container-item>
                                    <mt-tab-container-item id="4">
                                        <div style="overflow: auto;height: 780px" id="div6">
                                            <ul style="margin-top: 10px;margin-left: -10px">
                                                <li v-for="i in accessList" v-if="i.type=='黑名单'">
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
                                                                <el-col :span="18" :offset="10"
                                                                        style="margin-top: -40px">
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
                                    </mt-tab-container-item>
                                </mt-tab-container>
                            </el-row>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>


    </div>
</template>

<script>
    let _this;
    import echarts from 'echarts'
    import request from '../api/request'
    import {Navbar, TabItem} from 'mint-ui';

    var timeOut;
    var timeFetchAccess;
    var timeFetchNight
    var timeFetchAccessByPass
    var timeFetchAccessByName
    var timeFetchAccessByIdentity
    export default {
        name: "dormitory",
        data() {
            _this = this
            return {
                selected: "1",
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
                    passCurrentPage: 1,
                    regisCurrentPage: 1,
                    warnCurrentPage: 1,
                    alarmCurrentPage: 1
                },
                deviceId: [],
                accessList: [],
                accessByPassList: [],
                accessByNamesList: [],
                accessByIdentityList: [],
                index: 0,
                attendanceNumList: {
                    total: 0,
                    attendanceNum: 0,
                    inDormitory: 0,
                    outDormitory: 0
                },
                nightPage: 1,
                nightPageSize: 10,
                nightFallList: [],
                passAccessList: [],
                passTotal: 0,
                passCount: 0,
                nightFallListLength: 0,
                nightTotal: 0,
                activeName: 'first',
                accesscount: 0,
                totalRecords: 0,
                imageId: '',
                name: "",


            }
        },
        methods: {
            handleClick(tab, event) {
                switch (_this.infoManage) {
                    case "second":
                        _this.nightFallList = []
                        _this.fethcNightFall(_this.deviceId)
                        break;
                }
                $(".mes").html("")
                $(".mess").html("")
                $(".count").html("")
            },

            search() {
                _this.accessList=[]

                if (_this.name!=""&&_this.name!=null){
                    clearInterval(timeFetchAccess)
                    _this.selected = "1"
                    _this.fetchNewestAccessRecordListByName(_this.deviceId, _this.name)
                } else {
                    clearInterval(timeFetchAccess)
                    //保证点击"全部"时，页面立即加载数据
                    _this.fetchNewestAccessRecordList(_this.deviceId);
                    timeFetchAccess = setInterval(function () {
                        _this.fetchNewestAccessRecordList(_this.deviceId)
                    }, 3000)

                }


            },
            floorInfo() {
                _this.viewDialogVisible = true
                _this.fetchFloorDevice();
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
                        _this.fetchNewestAccessRecordList(_this.deviceId);
                        _this.fethcNightFall(_this.deviceId)
                    } else {
                        showMessage(_this, '设备信息获取失败', 0)
                    }
                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },

            //通行记录

            fetchNewestAccessRecordList(floorDevice) {
                let params = new URLSearchParams();
                params.append("size", _this.pageSize);
                params.append("deviceId", floorDevice);
                request({
                    url: HOST + "dorm/getNewestAccessRecordList",
                    method: "post",
                    data: params
                }).then(res => {
                        if (res.data.code == 200) {
                            if (_this.accessList == null || _this.accessList == "") {
                                _this.accessList = res.data.data;
                                for (let i = 0; i < _this.accessList.length; i++) {
                                    _this.getImage(_this.accessList[i].imageId, _this.accessList[i]);
                                }
                            } else {
                                let list = res.data.data;
                                let size = _this.accessList.length;
                                for (let i = 0; i < list.length; i++) {
                                    _this.getImage(list[i].imageId, list[i]);
                                }
                                setTimeout(function () {
                                    _this.accessList = list;
                                }, 200)
                            }
                        } else {
                            showMessage(_this, "获取最新通行记录失败", 0)
                        }
                    }
                ).catch(error => {
                    showMessage(_this, "fetchNewestAccessRecordList===>:{}" + error, 0)
                })

            },
            fetchNewestAccessRecordListByPass(floorDevice, pass) {
                let params = new URLSearchParams();
                params.append("size", _this.pageSize);
                params.append("deviceId", floorDevice);
                params.append("pass", pass)
                request({
                    url: HOST + "dorm/getNewestAccessRecordListByPass",
                    method: "post",
                    data: params
                }).then(res => {
                        if (res.data.code == 200) {
                            if (_this.accessByPassList == null || _this.accessByPassList == "") {
                                _this.accessByPassList = res.data.data;
                                for (let i = 0; i < _this.accessByPassList.length; i++) {
                                    _this.getImage(_this.accessByPassList[i].imageId, _this.accessByPassList[i]);
                                }
                            } else {
                                let list = res.data.data;
                                let size = _this.accessByPassList.length;
                                for (let i = 0; i < list.length; i++) {
                                    _this.getImage(list[i].imageId, list[i]);
                                }
                                setTimeout(function () {
                                    _this.accessByPassList = list;
                                }, 500)
                            }
                        } else {
                            showMessage(_this, "获取最新通行记录失败==>ByPass", 0)
                        }
                    }
                ).catch(error => {
                    showMessage(_this, "fetchNewestAccessRecordListByPass===>:{}" + error, 0)
                })

            },
            fetchNewestAccessRecordListByName(floorDevice, name) {
                let params = new URLSearchParams();
                params.append("size", _this.pageSize);
                params.append("deviceId", floorDevice);
                params.append("name", name)
                request({
                    url: HOST + "dorm/getNewestAccessRecordListByName",
                    method: "post",
                    data: params
                }).then(res => {
                        if (res.data.code == 200) {
                            if (_this.accessList == null || _this.accessList == "") {
                                if (res.data.data!=null){
                                    _this.accessList = res.data.data;
                                    _this.totalRecords = res.data.data.length;
                                    _this.accesscount = _this.accessList.length;
                                    for (let i = 0; i < _this.accessList.length; i++) {
                                        _this.getImage(_this.accessList[i].imageId, _this.accessList[i]);
                                    }
                                } else {
                                    showMessage(_this,"数据为空",0)
                                }

                            } else {
                                let list = res.data.data;
                                let size = _this.accessList.length;
                                for (let i = 0; i < list.length; i++) {
                                    _this.getImage(list[i].imageId, list[i]);
                                }
                                setTimeout(function () {
                                    _this.accessList = list;
                                }, 200)
                            }
                        } else {
                            showMessage(_this, "获取最新通行记录失败==>ByName", 0)
                        }
                    }
                ).catch(error => {
                    showMessage(_this, "fetchNewestAccessRecordListByName===>:{}" + error, 0)
                })

            },
            fetchNewestAccessRecordListByIdentity(floorDevice, identity) {
                let params = new URLSearchParams();
                params.append("size", _this.pageSize);
                params.append("deviceId", floorDevice);
                params.append("identity", identity)
                request({
                    url: HOST + "dorm/getNewestAccessRecordListByIdentity",
                    method: "post",
                    data: params
                }).then(res => {
                        if (res.data.code == 200) {
                            if (_this.accessList == null || _this.accessList == "") {
                                if (res.data.data!=null){
                                    _this.accessList = res.data.data;
                                    for (let i = 0; i < _this.accessList.length; i++) {
                                        _this.getImage(_this.accessList[i].imageId, _this.accessList[i]);
                                    }
                                } else {
                                    showMessage(_this,"无警报信息",0)
                                }
                            } else {
                                let list = res.data.data;
                                let size = _this.accessList.length;
                                for (let i = 0; i < list.length; i++) {
                                    _this.getImage(list[i].imageId, list[i]);
                                }
                                setTimeout(function () {
                                    _this.accessList = list;
                                }, 200)
                            }
                        } else {
                            showMessage(_this, "获取最新通行记录失败==>ByIdentity", 0)
                        }
                    }
                ).catch(error => {
                    showMessage(_this, "fetchNewestAccessRecordListByIdentity===>:{}" + error, 0)
                })

            },

            //分页查询所有数据
            getAccessRecordList(floorDevice) {
                let params = new URLSearchParams();
                params.append("size", _this.pageSize);
                params.append("deviceId", floorDevice);
                if (_this.accessList != null) {
                    let legth = _this.accessList.length - 1;
                    let time = _this.accessList[legth].pass_time
                    params.append("time", time);
                } else {
                    params.append("time", "");
                }
                request({
                    url: HOST + "dorm/getAccessRecordList",
                    method: "post",
                    data: params
                }).then(res => {
                        if (res.data.code == 200) {
                            let list = res.data.data
                            for (let i = 0; i < list.length; i++) {
                                _this.accessList.push(list[i]);
                            }
                            for (let i = 0; i < list.length; i++) {
                                _this.getImage(list[i].imageId, list[i]);
                            }
                            _this.accesscount = _this.accessList.length;
                        } else {
                            showMessage(_this, "获取最新通行记录失败", 0)
                        }
                    }
                ).catch(error => {
                    showMessage(_this, "getAccessRecordList===>:{}" + error, 0)
                })

            },
            getAccessRecordListByPass(floorDevice, pass) {
                let params = new URLSearchParams();
                params.append("size", _this.pageSize);
                params.append("deviceId", floorDevice);
                if (_this.accessList != null) {
                    let legth = _this.accessList.length - 1;
                    params.append("time", _this.accessList[legth].pass_time);
                } else {
                    params.append("time", "");
                }

                params.append("pass", pass)
                request({
                    url: HOST + "dorm/getAccessRecordListByPass",
                    method: "post",
                    data: params
                }).then(res => {
                        if (res.data.code == 200) {
                            let list = res.data.data
                            for (let i = 0; i < list.length; i++) {
                                _this.accessByPassList.push(list[i]);
                            }
                            for (let i = 0; i < list.length; i++) {
                                _this.getImage(list[i].imageId, list[i]);
                            }
                            _this.accesscount = _this.accessByPassList.length;

                        } else {
                            showMessage(_this, "获取通行记录失败", 0)
                        }
                    }
                ).catch(error => {
                    showMessage(_this, "getAccessRecordListByPass===>:{}" + error, 0)
                })

            },
            getAccessRecordListByName(floorDevice, name) {
                let params = new URLSearchParams();
                let legth = _this.accessList.length - 1;
                params.append("size", _this.pageSize);
                params.append("deviceId", floorDevice);
                if (_this.accessList != null) {
                    let legth = _this.accessList.length-1;
                    params.append("time", _this.accessList[legth].pass_time);
                } else {
                    params.append("time", "");
                }
                params.append("name", name)
                request({
                    url: HOST + "dorm/getAccessRecordListByName",
                    method: "post",
                    data: params
                }).then(res => {
                        if (res.data.code == 200) {
                            let list = res.data.data
                            for (let i = 0; i < list.length; i++) {
                                _this.accessList.push(list[i]);
                            }
                            for (let i = 0; i < list.length; i++) {
                                _this.getImage(list[i].imageId, list[i]);
                            }
                        } else {
                            showMessage(_this, "获取通行记录失败", 0)
                        }
                    }
                ).catch(error => {
                    showMessage(_this, "getAccessRecordListByName===>:{}" + error, 0)
                })

            },
            getAccessRecordListByIdentity(floorDevice, identity) {
                let params = new URLSearchParams();
                params.append("size", _this.pageSize);
                params.append("deviceId", floorDevice);
                if (_this.accessList != null) {
                    let legth = _this.accessList.length-1;
                    params.append("time", _this.accessList[legth].pass_time);
                } else {
                    params.append("time", "");
                }
                params.append("identity", identity)
                request({
                    url: HOST + "dorm/getAccessRecordListByIdentity",
                    method: "post",
                    data: params
                }).then(res => {
                        if (res.data.code == 200) {
                            if (res.data.data!=null){
                                let list = res.data.data;
                                for (let i = 0; i < list.length; i++) {
                                    _this.accessList.push(list[i]);
                                }
                                for (let i = 0; i < list.length; i++) {
                                    _this.getImage(list[i].imageId, list[i]);
                                }
                            } else {
                                showMessage(_this,"已显示所有数据",1)
                            }

                        } else {
                            showMessage(_this, "获取通行记录失败", 0)
                        }
                    }
                ).catch(error => {
                    showMessage(_this, "getAccessRecordListByIdentity===>:{}" + error, 0)
                })

            },


            fethcNightFall(floorNo) {
                let parmas = new URLSearchParams();
                parmas.append("page", _this.nightPage)
                parmas.append("size", _this.nightPageSize);
                parmas.append("deviceList", floorNo)
                request({
                    url: HOST + "access/getNightfall",
                    method: "post",
                    data: parmas
                }).then(res => {
                    if (res.data.code == 200) {
                        if (_this.nightFallList == null || _this.nightFallList == "") {
                            _this.nightFallList = res.data.data.list;
                            _this.nightTotal = res.data.data.total;
                            for (let i = 0; i < _this.nightFallList.length; i++) {
                                _this.getImage(_this.nightFallList[i].imageId, _this.nightFallList[i]);
                            }
                        } else {
                            let list = res.data.data.list;
                            for (let i = 0; i < list.length; i++) {
                                _this.getImage(list[i].imageId, list[i]);
                                _this.nightFallList.push(list[i])
                            }

                            _this.nightFallListLength = _this.nightFallList.length
                        }

                    } else {
                        showMessage(_this, "晚归考勤查询失败")
                    }
                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },
            fetchNumbers(floorNo) {
                let params = new URLSearchParams()
                params.append("floor", floorNo)
                request({
                    url: HOST + "access/getNumber",
                    method: "post",
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.attendanceNumber = res.data.data.attendanceNumbers;
                        _this.indormitoryNumber = res.data.data.inDormitories;
                        _this.outdormitoryNumber = res.data.data.outDormitories;
                        for (let i = 0; i < _this.outdormitoryNumber.length; i++) {
                            if (_this.outdormitoryNumber[i] == 0) {
                                _this.outdormitoryNumber[i] = ""
                            }
                        }
                        _this.SetEchart();
                    } else {
                        showMessage(_this, "获取时段人数失败", 0)
                    }
                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },
            fetchAttendanceAndInOrOut(floorNo) {
                let params = new URLSearchParams()
                params.append("floor", floorNo)
                request({
                    url: HOST + "access/attendanceCount",
                    method: "post",
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.attendanceNumList.attendanceNum = res.data.data.attendanceNum
                        _this.attendanceNumList.inDormitory = res.data.data.inDormitory
                        _this.attendanceNumList.outDormitory = res.data.data.outDormitory
                        _this.attendanceNumList.total = res.data.data.total

                    } else {
                        showMessage(_this, "获取进出记录失败", 0)
                    }
                }).catch(error => {
                    showMessage(_this, error, 0)
                })
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
                            name: '人数',
                            type: 'line',
                            stack: '总量',
                            //_this.attendanceNumber
                            data: [10,20,50,60,80,10,95,25,10,62,48,15,10],
                            areaStyle : {
                                normal : {
                                    color : new echarts.graphic.LinearGradient(0,0,0,1,[
                                        {
                                            offset : 0,
                                            color : 'rgba(0, 136, 212, 0.3)'
                                        },
                                        {
                                            offset : 0.8,
                                            color : 'rgba(0, 136, 212, 0)'
                                        } ], false),
                                    shadowColor : 'rgba(0, 0, 0, 0.1)',
                                    shadowBlur : 10
                                }
                            },
                            //控制线条的颜色
                            itemStyle : {
                                normal : {
                                    color : 'rgb(0,136,212)',
                                    borderColor : 'rgba(0,136,212,0.2)',
                                    borderWidth : 12
                                }
                            },
                            //数据显示

                            type : 'line',
                            smooth : 0.5, //折线图的弧度(0-1之间)
                            //设置折线图中表示每个坐标点的符号 emptycircle：空心圆；
                            //emptyrect：空心矩形；circle：实心圆；emptydiamond：菱形
                            //symbol : 'emptydiamond',
                            //stack : '总量',
                            symbol : 'none',
                        },
                        { //系列列表
                            //控制线条下面区域面积的颜色
                            areaStyle : {
                                normal : {
                                    color : new echarts.graphic.LinearGradient(0,0,0,1,[
                                        {
                                            offset : 0,
                                            color : 'rgba(137, 189, 27, 0.3)'
                                        },
                                        {
                                            offset : 0.8,
                                            color : 'rgba(137, 189, 27, 0)'
                                        } ], false),
                                    shadowColor : 'rgba(0, 0, 0, 0.1)',
                                    shadowBlur : 10
                                }
                            },
                            //控制线条的颜色
                            itemStyle : {
                                normal : {
                                    color : 'rgb(137,189,27)',
                                    borderColor : 'rgba(137,189,2,0.27)',
                                    borderWidth : 12

                                }
                            },
                            //数据显示
                            type : 'line',
                            symbol : 'none',
                            //stack : '总量', //数据堆叠
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
                            name: '人数',
                            type: 'line',
                            stack: '总量',
                            label: {},
                            symbol : 'none',
                            data: _this.indormitoryNumber,
                            areaStyle : {
                                normal : {
                                    color : new echarts.graphic.LinearGradient(0,0,0,1,[
                                        {
                                            offset : 0,
                                            color : 'rgba(0, 136, 212, 0.3)'
                                        },
                                        {
                                            offset : 0.8,
                                            color : 'rgba(0, 136, 212, 0)'
                                        } ], false),
                                    shadowColor : 'rgba(0, 0, 0, 0.1)',
                                    shadowBlur : 10
                                }
                            },
                            //控制线条的颜色
                            itemStyle : {
                                normal : {
                                    color : 'rgb(0,136,212)',
                                    borderColor : 'rgba(0,136,212,0.2)',
                                    borderWidth : 12
                                }
                            },
                            //数据显示

                            type : 'line',
                            smooth : 0.5, //折线图的弧度(0-1之间)
                            //设置折线图中表示每个坐标点的符号 emptycircle：空心圆；
                            //emptyrect：空心矩形；circle：实心圆；emptydiamond：菱形
                            //symbol : 'emptydiamond',
                            //stack : '总量',
                            symbol : 'none',
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
                            name: '人数',
                            type: 'line',
                            stack: '总量',
                            label: {},
                            //_this.outdormitoryNumber
                            data:[10,26,58,26,49,51,26,59,14,45] ,
                            areaStyle : {
                                normal : {
                                    color : new echarts.graphic.LinearGradient(0,0,0,1,[
                                        {
                                            offset : 0,
                                            color : 'rgba(0, 136, 212, 0.3)'
                                        },
                                        {
                                            offset : 0.8,
                                            color : 'rgba(0, 136, 212, 0)'
                                        } ], false),
                                    shadowColor : 'rgba(0, 0, 0, 0.1)',
                                    shadowBlur : 10
                                }
                            },
                            //控制线条的颜色
                            itemStyle : {
                                normal : {
                                    color : 'rgb(0,136,212)',
                                    borderColor : 'rgba(0,136,212,0.2)',
                                    borderWidth : 12
                                }
                            },
                            //数据显示

                            type : 'line',
                            smooth : 0.5, //折线图的弧度(0-1之间)
                            //设置折线图中表示每个坐标点的符号 emptycircle：空心圆；
                            //emptyrect：空心矩形；circle：实心圆；emptydiamond：菱形
                            //symbol : 'emptydiamond',
                            //stack : '总量',
                            symbol : 'none',
                        }
                    ]
                }
                myChart.setOption(option);
                attendace.setOption(option1);
                outSide.setOption(option2);
            },
            handleScrollNight() {
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
                    _this.fethcNightFall(_this.deviceId)
                }
            },
            handleScrollAccessAll() {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop2 = document.getElementById("div2").scrollTop
                if (scrollTop2 > 1) {
                    clearInterval(timeFetchAccess)
                }
                if (scrollTop2 == 0) {
                    clearInterval(timeFetchAccess)
                    if (_this.name!=""&&_this.name!=null){
                        timeFetchAccess = setInterval(function () {
                            _this.fetchNewestAccessRecordListByName(_this.deviceId,_this.name)
                        }, 3000)
                    } else {
                        timeFetchAccess = setInterval(function () {
                            _this.fetchNewestAccessRecordList(_this.deviceId)
                        }, 3000)
                    }

                }
                //变量windowHeight是可视区的高度
                var windowHeight2 = document.getElementById("div2").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight2 = document.getElementById("div2").scrollHeight
                //滚动条到底部的条件
                if (scrollTop2 + windowHeight2 == scrollHeight2) {
                    //判断查询出来的数据长度是否等于总数量，如果不等于，则继续查，如果等于，则return出去，不再继续查
                    if (_this.name!=""&&_this.name!=null){
                        _this.getAccessRecordListByName(_this.deviceId, _this.name)
                    }else {
                        _this.getAccessRecordList(_this.deviceId)
                    }

                }

            },
            handleScrollAccessPass() {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop3 = document.getElementById("div3").scrollTop
                if (scrollTop3 > 1) {
                    clearInterval(timeFetchAccess)
                }
                if (scrollTop3 == 0) {
                    timeFetchAccess = setInterval(function () {
                        _this.fetchNewestAccessRecordListByPass(_this.deviceId, "PASS")
                    }, 3000)
                }
                //变量windowHeight是可视区的高度
                var windowHeight3 = document.getElementById("div3").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight3 = document.getElementById("div3").scrollHeight
                //滚动条到底部的条件
                if (scrollTop3 + windowHeight3 == scrollHeight3) {
                    _this.getAccessRecordListByPass(_this.deviceId, "PASS")
                }

            },
            handleScrollUnregistered() {
                var scrollTop4 = document.getElementById("div4").scrollTop
                if (scrollTop4 > 1) {
                    clearInterval(timeFetchAccess)
                }
                if (scrollTop4 == 0) {
                    clearInterval(timeFetchAccess)
                    timeFetchAccess = setInterval(function () {
                        _this.fetchNewestAccessRecordListByIdentity(_this.deviceId, "STRANGER")
                    }, 3000)
                }
                var windowHeight4 = document.getElementById("div4").clientHeight

                var scrollHeight4 = document.getElementById("div4").scrollHeight

                if (scrollTop4 + windowHeight4 == scrollHeight4) {
                    _this.getAccessRecordListByIdentity(_this.deviceId, "STRANGER")
                }

            },
            handleScrollBlack() {
                var scrollTop6 = document.getElementById("div6").scrollTop
                if (scrollTop6 > 1) {
                    clearInterval(timeFetchAccess)
                }
                if (scrollTop6 == 0) {
                    clearInterval(timeFetchAccess)
                    timeFetchAccess = setInterval(function () {
                        _this.fetchNewestAccessRecordListByIdentity(_this.deviceId, "BLACKLIST")
                    }, 3000)
                }
                var windowHeight6 = document.getElementById("div6").clientHeight


                var scrollHeight6 = document.getElementById("div6").scrollHeight

                if (scrollTop6 + windowHeight6 == scrollHeight6) {
                    _this.getAccessRecordListByIdentity(_this.deviceId, "BLACKLIST")
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

            handleSelectScroll() {
                document.getElementById("div1").addEventListener("scroll", _this.handleScrollNight);
                document.getElementById("div2").addEventListener("scroll", _this.handleScrollAccessAll);
                document.getElementById("div3").addEventListener("scroll", _this.handleScrollAccessPass);
                document.getElementById("div4").addEventListener("scroll", _this.handleScrollUnregistered);
                document.getElementById("div6").addEventListener("scroll", _this.handleScrollBlack);
            },

            handleClearTime(time) {

                clearInterval(timeFetchAccess);
                clearInterval(timeFetchAccessByPass)
                clearInterval(timeFetchAccessByIdentity)
                clearInterval(timeFetchAccessByName)
            }
        },
        created() {
            this.userInfo = JSON.parse(sessionStorage.getItem("user"))
            _this.getFloorDevice(_this.userInfo.floorNo);

        },
        mounted() {
            _this.fetchAttendanceAndInOrOut(_this.userInfo.floorName);
            _this.fetchNumbers(_this.userInfo.floorName)
            timeFetchAccess = setInterval(function () {
                _this.fetchNewestAccessRecordList(_this.deviceId)
            }, 3000)
            _this.SetEchart();
            _this.handleSelectScroll();

        },
        destroyed() {
            _this.handleSelectScroll();
            clearTimeout(timeOut, timeFetchNight)
            clearInterval(timeFetchAccess)
        },

        watch: {
            selected: function (val, oldval) {
                switch (val) {
                    case "1":
                        _this.accessList = [];
                        clearInterval(timeFetchAccess)
                        _this.fetchNewestAccessRecordList(_this.deviceId);
                        break;
                    case "2":
                        clearInterval(timeFetchAccess)
                        _this.fetchNewestAccessRecordListByPass(_this.deviceId, "PASS");
                        break;
                    case "3":
                        clearInterval(timeFetchAccess)
                        _this.fetchNewestAccessRecordListByIdentity(_this.deviceId, "STRANGER");
                        break;
                    case "4":
                        clearInterval(timeFetchAccess)
                        _this.fetchNewestAccessRecordListByIdentity(_this.deviceId, "BLACKLIST");
                        break;
                }

            }
        }

    }
</script>

<style>
    .title {
        font: 25px Extra Small;
        color: #6a747c;
        font-weight: bold;

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


    /*右边选项卡字体*/
    .el-tabs__item {
        color: black;

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