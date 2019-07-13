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
                                        <i style=" font:30px Extra Small;  color: black;">9栋</i>
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
                                        <ul style="margin-top: 10px">
                                            <li v-for="i in count">
                                                <el-card class="box-card">
                                                    <div>
                                                        <img src="../assets/img/18973492613_张三.jpg" width="60px"
                                                             height="60px" style="border-radius: 50%;margin-top: -10px">
                                                        <p style="margin-left: 80px;margin-top: -50px">
                                                            <span>张三</span><br/><span>高三(2)班</span>
                                                        </p>
                                                        <p style="margin-left: 160px;margin-top: -40px">
                                                            <span>2019-7-13 23:22:59</span><span style="margin-left: 10px">【彻夜未归】</span>
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
                                            style="font-size: 25px; margin-left: 20px">张三</span>
                                    </p>
                                    <p>
                                        <label>电话:</label><span>18152771754</span>
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
                        <div style="height: 940px; width:85% ;background-color: white;margin-left:60px "
                             class="well well-lg">
                            <i style="margin-left:30px">
                                <i class="title">通行记录</i>
                                <!--<el-input
                                        style="width: 150px;margin-top: 35px;margin-left: 300px"
                                        placeholder="学生姓名" clearable
                                        auto-complete="off">
                                    <i slot="prefix" class="el-input__icon el-icon-search"></i>
                                </el-input>-->
                            </i>

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

    export default {
        name: "dormitory",
        data() {
            _this = this
            return {
                activeName: 'second',
                infoManage: 'first',
                number: [2100, 800, 500, 200, 2000, 2100, 780, 200, 500, 800, 1900, 500],
                count: 10
            };
        },
        methods: {
            handleClick(tab, event) {
                console.log(tab, event);
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
                        $(".count").html("总数："+_this.count)
                        return
                    }
                    _this.count += 1;
                }

            },

        },
        mounted() {
            _this.SetEchart();
            document.getElementById("div1").addEventListener("scroll", _this.handleScroll);
        },
        destroyed() {
            document.getElementById("div1").removeEventListener("scroll", _this.handleScroll)
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


    :last-child {
        margin-bottom: 0;
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
</style>