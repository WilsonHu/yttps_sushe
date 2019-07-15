<template xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">
    <div style="width: 100%;height: 100%;background-color: #F6F4FF">
        <el-row :gutter="0">
            <el-col :span="8">
                <div class="grid-content bg-purple">
                    <div style="height: 930px">
                        <div style="height: 150px; width:85% ;background-color: white;margin-left:60px;margin-top: 20px "
                             class="well well-lg">
                            <i style=" font:20px Extra Small;  color: #6a747c;">宿舍信息</i>
                            <div style="border: 1px solid silver"></div>
                            <div>
                                <el-col :span="5" :offset="2">
                                    <p style="margin-left: 20px;margin-top: 20px">
                                        <span>楼号</span><br>
                                        <i style=" font:30px Extra Small;  color: black;">{{userInfo.floorNo}}</i>
                                    </p>
                                </el-col>
                                <el-col :offset="4" style="border: 1px solid silver;width: 1px;height: 97px"></el-col>
                                <el-col :span="5" :offset="4">
                                    <p style="margin-left: 20px;margin-top: 20px">
                                        <span>入住总人数</span><br>
                                        <i style=" font:30px Extra Small;  color: black;">2328</i>
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
                                            <span style="font-size: 30px;">2132</span>
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
                                            <span style="font-size: 30px;">1476</span>

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
                                            <span style="font-size: 30px;">656</span>
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
                                            <li v-for="i in count">
                                                <el-card class="box-card">
                                                    <div>
                                                        <el-row>
                                                            <el-col :span="2">
                                                                <img src="../assets/img/18973492613_张三.jpg" width="60px"
                                                                     height="60px"
                                                                     style="border-radius: 50%;margin-top: -10px">
                                                            </el-col>
                                                            <el-col :span="5" :offset="3">
                                                                <span>张三</span><br/><span>高三(2)班</span>
                                                            </el-col>
                                                            <el-col :span="18" :offset="10" style="margin-top: -40px">
                                                                <span>2019-7-13 23:22:59</span><span
                                                                    style="margin-left: 10px">【彻夜未归】</span>
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
                            <el-input
                                    style="width: 200px;margin-left: 150px;margin-top: 15px"
                                    placeholder="按姓名或宿舍号查询" clearable
                                    auto-complete="off">
                                <i slot="prefix" class="el-input__icon el-icon-search"></i>
                            </el-input>

                        </div>
                        <div style="height: 810px; width:85% ;background-color:transparent;margin-left:60px "
                             class="well well-lg">
                            <el-tabs v-model="activeName" @tab-click="handleClick">
                                <el-tab-pane label="全部" name="first">
                                    <div style="overflow: auto;height: 700px" class="dd" id="div2">
                                        <el-row :gutter="20">
                                            <el-col :span="20" style="margin-top: 10px" v-for="i in accessList">
                                                <el-card :body-style="{ padding: '0px' }"
                                                         style="height: 80px;border-radius: 20px">
                                                    <div>
                                                        <img style=" height: 35px;width:35px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                                             overflow: hidden; margin-top: 10px ;padding-left: -10px"
                                                             :src="require('../assets/img/'+(i.type=='进'?'in':(i.type=='未注册'?'unregistered':(i.type=='黑名单'?'blacklist':'forbid')))+'.png')"/>
                                                        <img style=" height: 60px;width:60px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                    overflow: hidden; margin-top: 10px;margin-left: 20px"
                                                             src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"/>
                                                        <span style="margin-left: 10px">{{i.name}}</span>
                                                        <span>{{i.classes}}</span>
                                                        <span style="padding-left: 90px;" :style="{color:(i.type=='进'?'green':(i.type=='未注册'?'orange':(i.type=='黑名单'?'red':'blue')))}">{{i.type}}</span>

                                                    </div>
                                                </el-card>
                                            </el-col>
                                        </el-row>
                                        <span class="mess"></span>

                                    </div>
                                    <span>{{accesscount}}</span>
                                </el-tab-pane>
                                <el-tab-pane label="通行" name="second">
                                    <div style="overflow: auto;height: 700px" class="dd" id="div2">
                                        <el-row :gutter="20">
                                            <el-col :span="20" style="margin-top: 10px" v-for="i in accessList" v-if="i.type=='进'">
                                                <el-card :body-style="{ padding: '0px' }"
                                                         style="height: 80px;border-radius: 20px">
                                                    <div>
                                                        <img style=" height: 35px;width:35px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                                             overflow: hidden; margin-top: 10px ;padding-left: -10px"
                                                             :src="require('../assets/img/'+'in'+'.png')"/>
                                                        <img style=" height: 60px;width:60px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                    overflow: hidden; margin-top: 10px;margin-left: 20px"
                                                             src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"/>
                                                        <span style="margin-left: 10px">{{i.name}}</span>
                                                        <span>{{i.classes}}</span>
                                                        <span style="padding-left: 90px; color: green " >{{i.type}}</span>

                                                    </div>
                                                </el-card>
                                            </el-col>
                                        </el-row>
                                        <span class="mess"></span>

                                    </div>
                                </el-tab-pane>
                                <el-tab-pane label="未注册" name="third">
                                    <div style="overflow: auto;height: 700px" class="dd" id="div2">
                                        <el-row :gutter="20">
                                            <el-col :span="20" style="margin-top: 10px" v-for="i in accessList" v-if="i.type=='未注册'">
                                                <el-card :body-style="{ padding: '0px' }"
                                                         style="height: 80px;border-radius: 20px">
                                                    <div>
                                                        <img style=" height: 35px;width:35px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                                             overflow: hidden; margin-top: 10px ;padding-left: -10px"
                                                             :src="require('../assets/img/'+'unregistered'+'.png')"/>
                                                        <img style=" height: 60px;width:60px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                    overflow: hidden; margin-top: 10px;margin-left: 20px"
                                                             src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"/>
                                                        <span style="margin-left: 10px">{{i.name}}</span>
                                                        <span>{{i.classes}}</span>
                                                        <span style="padding-left: 90px; color: orange " >{{i.type}}</span>

                                                    </div>
                                                </el-card>
                                            </el-col>
                                        </el-row>
                                        <span class="mess"></span>

                                    </div>
                                </el-tab-pane>
                                <el-tab-pane label="禁行" name="fourth">
                                    <div style="overflow: auto;height: 700px" class="dd" id="div2">
                                        <el-row :gutter="20">
                                            <el-col :span="20" style="margin-top: 10px" v-for="i in accessList" v-if="i.type=='禁止'">
                                                <el-card :body-style="{ padding: '0px' }"
                                                         style="height: 80px;border-radius: 20px">
                                                    <div>
                                                        <img style=" height: 35px;width:35px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                                             overflow: hidden; margin-top: 10px ;padding-left: -10px"
                                                             :src="require('../assets/img/'+'forbid'+'.png')"/>
                                                        <img style=" height: 60px;width:60px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                    overflow: hidden; margin-top: 10px;margin-left: 20px"
                                                             src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"/>
                                                        <span style="margin-left: 10px">{{i.name}}</span>
                                                        <span>{{i.classes}}</span>
                                                        <span style="padding-left: 90px; color: blue " >{{i.type}}</span>

                                                    </div>
                                                </el-card>
                                            </el-col>
                                        </el-row>
                                        <span class="mess"></span>

                                    </div>
                                </el-tab-pane>
                                <el-tab-pane label="警报" name="five">
                                    <div style="overflow: auto;height: 700px" class="dd" id="div2">
                                        <el-row :gutter="20">
                                            <el-col :span="20" style="margin-top: 10px" v-for="i in accessList" v-if="i.type=='黑名单'">
                                                <el-card :body-style="{ padding: '0px' }"
                                                         style="height: 80px;border-radius: 20px">
                                                    <div>
                                                        <img style=" height: 35px;width:35px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                                             overflow: hidden; margin-top: 10px ;padding-left: -10px"
                                                             :src="require('../assets/img/'+'blacklist'+'.png')"/>
                                                        <img style=" height: 60px;width:60px; border: solid 2px lightskyblue; border-radius: 50%;align-items: center;justify-content: center;
                                    overflow: hidden; margin-top: 10px;margin-left: 20px"
                                                             src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png"/>
                                                        <span style="margin-left: 10px">{{i.name}}</span>
                                                        <span>{{i.classes}}</span>
                                                        <span style="padding-left: 90px; color: red " >{{i.type}}</span>

                                                    </div>
                                                </el-card>
                                            </el-col>
                                        </el-row>
                                        <span class="mess"></span>

                                    </div>
                                </el-tab-pane>
                            </el-tabs>
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

    export default {
        name: "dormitory",
        data() {
            _this = this
            return {

                infoManage: 'first',
                number: [2100, 800, 500, 200, 2000, 2100, 780, 200, 500, 800, 1900, 500],
                count: 10,
                userInfo: "",
                pageSize: EveryPageNum,//每一页的num
                currentPage: 1,
                deviceId: [],
                accessList: [],
                attendanceNumList: {
                    attendanceNum: 0,
                    inDormitory: 0,
                    outDormitory: 0
                },
                activeName: 'first',
                accesscount: 0,
                picName: '',
                totalRecords: 0,
            };
        },
        methods: {
            handleClick(tab, event) {
                console.log(tab, event);
                _this.count = 10;
                $(".mes").html("")
                $(".count").html("")
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
                        data: ['', '', '', '', '', '', '', '', '', '', '', '']
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
                            data: _this.number,
                            areaStyle: {normal: {}}
                        }
                    ]
                }
                myChart.setOption(option);
                attendace.setOption(option);
                outSide.setOption(option);
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
                    if (_this.count == 20) {
                        $(".mes").html("已无更多数据")
                        $(".count").html("总数：" + _this.count)
                        return
                    }
                    _this.count += 1;
                }

            },
            fetchAccess(floorNo) {
                console.log('fsd' + floorNo)
                let params = new URLSearchParams();
                params.append("page", _this.currentPage)
                params.append("size", _this.pageSize)
                params.append("floorDevice", floorNo)
                request({
                    url: HOST + "/access/list",
                    method: "post",
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.accessList = res.data.data.list;
                        _this.totalRecords = res.data.data.total;
                        _this.accesscount = _this.accessList.length;

                    } else {
                        showMessage(_this, "获取通行记录失败", 0)
                    }

                }).catch(error => {
                    showMessage(_this, error, 0)
                })
            },
            fetchAttendanceAndInOrOut(floorNo) {
                let params = new URLSearchParams();
                params.append("floorDevice", floorNo)
                request({
                    url: HOST + "access/attendanceCount",
                    method: "post",
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        _this.attendanceNumList.attendanceNum = res.data.attendanceNum
                        _this.attendanceNumList.inDormitory = res.data.inDormitory
                        _this.attendanceNumList.outDormitory = res.data.outDormitory
                    }
                })
            },
            handleScroll2(floorNo) {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop2 = document.getElementById("div2").scrollTop
                //变量windowHeight是可视区的高度
                var windowHeight2 = document.getElementById("div2").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight2 = document.getElementById("div2").scrollHeight
                //滚动条到底部的条件
                if (scrollTop2 + windowHeight2 == scrollHeight2) {
                    _this.currentPage += 1;
                    //判断查询出来的数据长度是否等于总数量，如果不等于，则继续查，如果等于，则return出去，不再继续查
                    if (_this.accesscount == _this.totalRecords) {
                        $(".mess").html("无更多数据")
                        return
                    }
                    let params = new URLSearchParams();
                    params.append("page", _this.currentPage)
                    params.append("size", _this.pageSize)
                    params.append("floorDevice", floorNo)
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
                            _this.accesscount = _this.accessList.length;
                        } else {
                            showMessage(_this, "获取查询数据失败！");
                        }
                    }).catch(error => {
                        console.log(error)

                    })

                }

            },
        },


        created() {
            this.userInfo = JSON.parse(sessionStorage.getItem("user"))
            let params = new URLSearchParams();
            params.append("floorNo", _this.userInfo.floorNo)
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

                    _this.fetchAccess(_this.deviceId)
                    _this.handleScroll2(_this.deviceId)
                } else {
                    showMessage(_this, '设备信息获取失败', 0)
                }
            }).catch(error => {
                showMessage(_this, error, 0)
            })

        },

        mounted() {
            _this.SetEchart();
            document.getElementById("div1").addEventListener("scroll", _this.handleScroll);
            document.getElementById("div2").addEventListener("scroll2", _this.handleScroll2);
        },
        destroyed() {
            document.getElementById("div1").removeEventListener("scroll", _this.handleScroll)
            document.getElementById("div2").removeEventListener("scroll2", _this.handleScroll2)

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
        height: 80px;
        width: 430px;
    }

    /*右边模块*/
    ::-webkit-scrollbar {
        width: 7px; /*滚动条宽度*/
        height: 7px; /*滚动条高度*/
        background-color: #f6f4ff;
    }

    .title {
        font: 25px Extra Small;
        color: #6a747c;
        font-weight: bold;
    }

    /*右边选项卡字体*/
    .el-tabs__item {
        color: black;
        padding: 0 30px;
        margin-left: 6px;

    }

    /*右边模块 蓝色滑块*/
    .el-tabs__active-bar {
        padding: 0 30px;
       margin-left: 20px;
    }

    .bottom {
        margin-top: 13px;
        line-height: 12px;
    }

    /*右边全部第一个卡片*/
    .el-col-offset-0 {
        margin-left: 8.33333%;
    }

    /*全部栏位置*/
    .el-col-20 {
        margin-left: 35px;
        margin-right: 0px;
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

    .bg-purple-dark {
        background: #99a9bf;
    }

    .bg-purple-light {
        background: #e5e9f2;
    }

    .grid-content {
        border-radius: 4px;
        min-height: 935px;
    }

    .row-bg {
        padding: 5px 0;
        background-color: #f9fafc;
    }
</style>