<template xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">
    <div style="width: 100%;height: 100%;background-color: #F6F4FF;padding-top: 50px">
        <audio id="error" src="./src/assets/audio/ly-17-02-22-17.mp3"></audio>
        <el-row :gutter="0">
            <el-col :span="8">
                <div class="grid-content bg-purple">
                    <!--页面左方三个div-->
                    <div style="height: 100%;margin-top: 10px">
                        <div style="height: 150px; width:85% ;background: #FFFFFF;box-shadow: 0 2px 4px 0 rgba(0,0,0,0.05);border-radius: 10px;margin-left: 60px"
                             class="well well-lg">
                            <el-col style="font-size: 20px;color: #6a747c; font-weight: bold;margin-top: -5px">
                                <i>宿舍信息</i>
                            </el-col>
                            <div style="border: 1px solid silver;margin-top: 27px"></div>
                            <div>
                                <el-col :span="5" :offset="2">
                                    <p style="margin-left: 20px;margin-top: 20px">
                                        <span>楼号</span><br>
                                        <i style=" font:36px PingFangSC-Semibold;  color: #525252;">{{userInfo.floorNo}}</i>
                                    </p>
                                </el-col>
                                <el-col :offset="4" style="border: 1px solid silver;width: 1px;height: 8rem"></el-col>
                                <el-col :span="5" :offset="4">
                                    <p style="margin-left: 20px;margin-top: 20px">
                                        <span>入住总人数</span><br>
                                        <!---->
                                        <i style="font:36px PingFangSC-Semibold;  color: #525252;">{{attendanceNumList.total}}</i>
                                    </p>
                                </el-col>
                            </div>
                        </div>

                        <div class="well well-lg"
                             style="height: 610px;margin-left:60px; width:85%;background: #FFFFFF;box-shadow: 0 2px 4px 0 rgba(0,0,0,0.05);border-radius: 10px; ">
                            <i style=" font-size: 20px; font-weight: 600;color: #6a747c;">出入信息统计</i>
                            <el-tabs v-model="infoManage" class="a"
                                     style="margin-top: -20px;">
                                <el-tab-pane label="当前" name="first">
                                    <el-row style="margin-top: 30px;">
                                        <el-col>
                                            <img src="../assets/img/ic_check.png" width="50px" height="50px"/>
                                        </el-col>
                                        <el-col :offset="4" style="margin-top: -40px">
                                            <span>当前考勤学生数</span><br/>
                                            <span style="font-size: 40px;font-family: PingFangSC-Semibold;color: #525252">{{attendanceNumList.attendanceNum}}</span>
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
                                        <el-col :offset="4" style="margin-top: -40px">
                                            <span>当前在寝学生数</span><br/>
                                            <span style="font-size: 40px;font-family: PingFangSC-Semibold;color: #525252">{{attendanceNumList.inDormitory}}</span>

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
                                        <el-col :offset="4" style="margin-top: -40px">
                                            <span>当前外出学生数</span><br/>
                                            <span style="font-size: 40px;font-family: PingFangSC-Semibold;color: #525252">{{attendanceNumList.outDormitory}}</span>
                                        </el-col>
                                        <el-col :span="8" :offset="7">
                                            <div id="outSide"
                                                 style="width: 350px;height: 180px;margin-top: -150px"></div>
                                        </el-col>
                                    </el-row>
                                </el-tab-pane>
                            </el-tabs>
                        </div>

                        <div style="height: 100px; width:85% ;margin-left:60px;background: #FFFFFF;box-shadow: 0 2px 4px 0 rgba(0,0,0,0.05);border-radius: 10px; "
                             class="well well-lg" @click="">
                            <div>
                                <el-col :span="3">
                                    <img src="../assets/img/person.png" width="70px" height="70px"
                                         style="border-radius: 50%;margin-top: -10px">
                                </el-col>
                                <el-col :span="8" :offset="3" style="margin-top: -15px">
                                    <p>
                                        <label style="font-size: 23px;">宿管</label> <span
                                            style="font-size: 25px; margin-left: 20px">{{userInfo.name}}</span>
                                    </p>
                                    <p>
                                        <label>电话:</label><span style="margin-left: 10px;font-size: 15px;">{{userInfo.phone}}</span>
                                    </p>
                                </el-col>

                                <el-popover
                                        placement="top"
                                        width="160"
                                        v-model="visible">
                                    <p>确定退出当前账号吗？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button size="mini" type="text" @click="visible = false">取消</el-button>
                                        <el-button type="primary" size="mini" @click="loginOut">确定</el-button>
                                    </div>
                                    <el-button slot="reference" style="margin-left: 15%">退出</el-button>
                                </el-popover>
                            </div>

                        </div>
                    </div>
                </div>
            </el-col>

            <el-col :span="8">
                <div class="grid-content"
                     style="background-image: linear-gradient(135deg, #676F8C 0%, #0C101D 100%);box-shadow: 0 2px 4px 0 rgba(0,0,0,0.05);">
                    <div style="height: 100%">
                        <div style=" min-height:100%;;width:90% ;background-color:transparent;margin-left:40px;margin-top: 10px"
                             class="">
                            <el-row>
                                <el-tabs v-model="visitSelected" stretch class="visitA">
                                    <el-tab-pane label="夜归考勤" name="first">
                                        <div style="height: 850px;overflow:auto;" id="div1" class="visiiDiv">
                                            <ul style="margin-top: 10px;margin-left: -10px">
                                                <li v-for="i in nightFallList">
                                                    <el-card
                                                            style="border-radius: 10px;width:100%;height: 80px;margin-left: -20px;background: #EEF2FF">
                                                        <div>
                                                            <el-row>
                                                                <el-col :span="2">
                                                                    <img :src="require('../assets/img/'+(i.type.split('_')[0]=='进'? 'info_pass':'info_forbid')+'.png')"
                                                                         style=" height: 78px;width:70px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                                                </el-col>
                                                                <el-col :span="3" :offset="3">
                                                                    <span style="font-size: 20px;">{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                                </el-col>
                                                                <el-col :span="2" :offset="1">
                                                                    <img :src="i.imageId" width="70px"
                                                                         height="70px"
                                                                         style="border-radius: 50%;margin-top: -15px">
                                                                </el-col>

                                                                <el-col :span="18" :offset="14"
                                                                        style="margin-top: -50px;">
                                                                    <span style="font-size: 18px;">{{i.pass_time}}</span><span
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
                                    <el-tab-pane label="多日未归" name="second">
                                        <div style="height: 850px;overflow:auto;" class="visiiDiv">
                                            <ul style="margin-top: 10px;margin-left: -10px ">
                                                <li v-for="i in visiRecordList">
                                                    <el-card
                                                            style="border-radius: 10px;width:100%;height: 80px;margin-left: -20px;background: #EEF2FF">
                                                        <div>
                                                            <el-row>
                                                                <el-col :span="10"
                                                                        style="margin-top: 8px;font-size: 20px;color: #434343;font-weight:bold">
                                                                    <span>{{i.time}}</span>
                                                                </el-col>
                                                                <el-col :span="2" :offset="1">
                                                                    <img v-if="i.imageId==''|| i.imageId==null"
                                                                         src="../assets/img/avator.png">

                                                                    <img :src="i.imageId" width="70px"
                                                                         height="70px"
                                                                         style="border-radius: 50%;margin-top: -16px;position: relative;z-index: 5"
                                                                         @click="big(i)">
                                                                </el-col>
                                                                <el-col :span="5" :offset="4"
                                                                        style="font-family: PingFangSC-Semibold;color:  #282828">
                                                                    <span style="font-size: 18px;font-weight:bold">{{i.name}}</span><br/><span>{{i.tagNames[0]}}</span>
                                                                </el-col>
                                                                <el-col :span="5" :offset="21"
                                                                        style="margin-top: -45px;font-size: 15px;">
                                                                    <span :style="{color:(i.type.split('_')[0]=='进'?'#9FD36B':(i.type=='未注册'?'#F3A32B':(i.type=='黑名单'?'#C42E3B':'blue')))}">【{{i.type.split('_')[0]}}】</span>
                                                                </el-col>
                                                            </el-row>
                                                        </div>
                                                    </el-card>
                                                </li>
                                            </ul>
                                        </div>
                                    </el-tab-pane>

                                </el-tabs>
                            </el-row>
                        </div>
                    </div>
                </div>
            </el-col>

            <el-col :span="8">
                <div class="grid-content bg-purple">
                    <div style="height: 100%">
                        <div style=" min-height: 935px;;width:78% ;background-color:transparent;margin-left:40px;margin-top: 10px"
                             class="">
                            <i class="title">通行记录</i>
                            <el-row style="margin-top:-30px;margin-left: 50px">
                                <el-input style="width: 230px;margin-left: 150px;border-radius: 40%"
                                          placeholder="按姓名查询" clearable
                                          auto-complete="off" v-model="name" @keyup.enter.native="search">
                                    <i slot="prefix" class="el-input__icon el-icon-search"></i>
                                </el-input>
                            </el-row>
                            <el-row>
                                <el-tabs v-model="selected" stretch @tab-click="handleAccess" class="b">
                                    <el-tab-pane label="全部" name="first">
                                        <div style="height: 800px;overflow:auto;" class="revisiDiv" id="accessAll">
                                            <ul style="margin-top: 10px;margin-left: -10px ">
                                                <li v-for="i in accessList">
                                                    <el-card
                                                            style="border-radius: 10px;width:100%;height: 80px;margin-left: -20px">
                                                        <div>
                                                            <el-row>
                                                                <el-col :span="2">
                                                                    <img :src="require('../assets/img/'+(i.type.split('_')[0]=='进'?'info_pass':(i.type=='未注册'?'info_attention':(i.type=='黑名单'?'info_warning':'info_forbidden')))+'.png')"
                                                                         style=" height: 78px;width:70px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                                                </el-col>
                                                                <el-col :span="2" :offset="2"
                                                                        style="margin-top: 8px;font-size: 15px;">
                                                                    <span>{{i.pass_time.split(" ")[1]}}</span>
                                                                </el-col>
                                                                <el-col :span="2" :offset="3">
                                                                    <img v-if="i.imageId==''|| i.imageId==null"
                                                                         src="../assets/img/avator.png">

                                                                    <img :src="i.imageId" width="70px"
                                                                         height="70px"
                                                                         style="border-radius: 50%;margin-top: -16px;position: relative;z-index: 5"
                                                                         @click="big(i)">
                                                                </el-col>
                                                                <el-col :span="5" :offset="4"
                                                                        style="font-family: PingFangSC-Semibold;color:  #282828">
                                                                    <span style="font-size: 18px;font-weight:bold">{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                                </el-col>
                                                                <el-col :span="5" :offset="20"
                                                                        style="margin-top: -50px;font-size: 15px;">
                                                                    <span :style="{color:(i.type.split('_')[0]=='进'?'#9FD36B':(i.type=='未注册'?'#F3A32B':(i.type=='黑名单'?'#C42E3B':'blue')))}">【{{i.type.split('_')[0]}}】</span>
                                                                </el-col>
                                                            </el-row>
                                                        </div>
                                                    </el-card>
                                                </li>
                                            </ul>
                                            <span class="mess" style="margin-left: 15rem;color: #2aabd2"
                                                  @click="handleMoreAccess"></span>
                                        </div>
                                    </el-tab-pane>
                                    <el-tab-pane label="通行" name="second">
                                        <div style="height: 780px;overflow:auto" class="revisiDiv" id="accessPass">
                                            <ul style="margin-top: 10px;margin-left: -10px ">
                                                <li v-for="i in accessByPassList"
                                                    v-if="i.type.split('_')[0]=='进' ||i.type.split('_')[0]=='出'">
                                                    <!--  <el-col :span="2" style="margin-top: -19px">
                                                          <img :src="require('../assets/img/'+(i.type.split('_')[0]=='进'? 'info_pass':'info_forbidden')+'.png')"
                                                               style=" height: 52px;width:50px;border-radius:50%;margin-top: -41px;margin-left: -6px;position: relative;top: 69px;left: -21px">
                                                      </el-col>-->
                                                    <el-card
                                                            style="border-radius: 10px;width:462px;height: 80px;margin-left: -20px">
                                                        <div>
                                                            <el-row>
                                                                <el-col :span="2">
                                                                    <img :src="require('../assets/img/'+(i.type.split('_')[0]=='进'? 'info_pass':'info_forbidden')+'.png')"
                                                                         style=" height: 78px;width:70px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
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
                                                                <el-col :span="5" :offset="4"
                                                                        tyle="font-family: PingFangSC-Semibold;color:  #282828">
                                                                    <span style="font-size: 18px;font-weight:bold">{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                                </el-col>
                                                                <el-col :span="5" :offset="21"
                                                                        style="margin-top: -50px;font-size: 15px;">
                                                                    <span :style="{color:(i.type.split('_')[0]=='进'?'#9FD36B':'blue')}">【{{i.type.split('_')[0]}}】</span>
                                                                </el-col>
                                                            </el-row>

                                                        </div>
                                                    </el-card>
                                                </li>
                                            </ul>
                                            <span class="mess" style="margin-left: 15rem;color: #2aabd2"
                                                  @click="handleMoreAccess('PASS')">+ 点击查看更多数据</span>
                                        </div>
                                    </el-tab-pane>
                                    <el-tab-pane label="未注册" name="third">
                                        <div style="height: 780px;overflow:auto" class="revisiDiv" id="accessRegis">
                                            <ul style="margin-top: 10px;margin-left: -10px ">
                                                <li v-for="i in accessList" v-if="i.type=='未注册'">
                                                    <el-card
                                                            style="border-radius: 10px;width:462px;height: 80px;margin-left: -20px">
                                                        <div>
                                                            <el-row>
                                                                <el-col :span="2">
                                                                    <img :src="require('../assets/img/'+'info_attention'+'.png')"
                                                                         style=" height: 78px;width:70px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
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
                                                                <el-col :span="5" :offset="4"
                                                                        tyle="font-family: PingFangSC-Semibold;color:  #282828">
                                                                    <span style="font-size: 18px;font-weight:bold">{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                                </el-col>
                                                                <el-col :span="5" :offset="20"
                                                                        style="margin-top: -50px;font-size: 15px;">
                                                                    <span style="color: #F3A32B;">【{{i.type}}】</span>
                                                                </el-col>
                                                            </el-row>

                                                        </div>
                                                    </el-card>
                                                </li>
                                            </ul>
                                            <span class="mess" style="margin-left: 15rem;color: #2aabd2"
                                                  @click="handleMoreAccess('STRANGER')">+ 点击查看更多数据</span>
                                        </div>
                                    </el-tab-pane>
                                    <el-tab-pane label="警报" name="fourth">
                                        <div style="height: 780px;overflow:auto" class="revisiDiv" id="accessBlack">
                                            <ul style="margin-top: 10px;margin-left: -10px ">
                                                <li v-for="i in accessList" v-if="i.type=='黑名单'">
                                                    <el-card
                                                            style="border-radius: 10px;width:462px;height: 80px;margin-left: -20px">
                                                        <div>
                                                            <el-row>
                                                                <el-col :span="2">
                                                                    <img :src="require('../assets/img/'+'info_warning'+'.png')"
                                                                         style=" height: 78px;width:70px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
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
                                                                <el-col :span="5" :offset="4"
                                                                        tyle="font-family: PingFangSC-Semibold;color:  #282828">
                                                                    <span style="font-size: 18px;font-weight:bold">{{i.name}}</span><br/><span>{{i.classes}}</span>
                                                                </el-col>
                                                                <el-col :span="5" :offset="20"
                                                                        style="margin-top: -50px;font-size: 15px;">
                                                                    <span style="color: #C42E3B;">【{{i.type}}】</span>
                                                                </el-col>
                                                            </el-row>

                                                        </div>
                                                    </el-card>
                                                </li>
                                            </ul>
                                            <span class="mess" style="margin-left: 15rem;color: #2aabd2"
                                                  @click="handleMoreAccess('BLACKLIST')">+ 点击查看更多数据</span>
                                        </div>
                                    </el-tab-pane>
                                </el-tabs>

                            </el-row>
                        </div>
                    </div>
                </div>
            </el-col>
        </el-row>

        <el-dialog :visible.sync="imgDialogVisible" width="40%">
            <el-row>
                <img :src="bigImg" width="680px" height="700px" style="margin-left: 20px"/>
            </el-row>
        </el-dialog>

        <el-dialog title="通行记录" :visible.sync="accessDialogVisble" size="tiny" width="60%">
            <el-row :gutter="20">
                <el-col :span="7">
                    <el-date-picker
                            v-model="beginTime"
                            type="datetimerange"
                            align="left"
                            format="yyyy-MM-dd HH:mm:ss"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            unlink-panels
                            range-separator="—"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                    </el-date-picker>
                </el-col>
                <el-col :span="5">
                    <el-input style="margin-left: 150px;"
                              placeholder="按姓名查询" clearable
                              v-model="staffName" @keyup.enter.native="getStaffByName">
                        <i slot="prefix" class="el-input__icon el-icon-search"></i>
                    </el-input>
                </el-col>
            </el-row>
            <el-row>
                <div style="height: 500px;overflow:auto;" class="revisiDiv">
                    <ul class="accesDialog" style="margin-top: 5px;margin-left: -10px ">
                        <li v-for="i in moreAccessList">
                            <el-card
                                    style="border-radius: 10px;width:462px;height: 80px;margin-left: -20px">
                                <div>
                                    <el-row>
                                        <el-col :span="2">
                                            <img :src="require('../assets/img/'+(i.type.split('_')[0]=='进'?'info_pass':(i.type=='未注册'?'info_attention':(i.type=='黑名单'?'info_warning':'info_forbidden')))+'.png')"
                                                 style=" height: 78px;width:70px;border-radius: 0 0 0 0;margin-top: -20px;margin-left: -20px">
                                        </el-col>
                                        <el-col :span="4" :offset="2"
                                                style="margin-top: 8px;font-size: 15px;">
                                            <span>{{i.pass_time.split(" ")[1]}}</span>
                                        </el-col>
                                        <el-col :span="2" :offset="1">
                                            <img v-if="i.imageId==''|| i.imageId==null"
                                                 src="../assets/img/avator.png">
                                            <img :src="i.imageId" width="70px"
                                                 height="70px"
                                                 style="border-radius: 50%;margin-top: -16px;position: relative;z-index: 5"
                                                 @click="big(i)">
                                        </el-col>
                                        <el-col :span="5" :offset="4"
                                                style="font-family: PingFangSC-Semibold;color:  #282828">
                                            <span style="font-size: 18px;font-weight:bold">{{i.name}}</span><br/><span>{{i.classes}}</span>
                                        </el-col>
                                        <el-col :span="5" :offset="21"
                                                style="margin-top: -50px;font-size: 15px;">
                                            <span :style="{color:(i.type.split('_')[0]=='进'?'#9FD36B':(i.type=='未注册'?'#F3A32B':(i.type=='黑名单'?'#C42E3B':'blue')))}">【{{i.type.split('_')[0]}}】</span>
                                        </el-col>
                                    </el-row>

                                </div>
                            </el-card>
                        </li>
                    </ul>
                </div>
            </el-row>
            <div class="block" style="text-align: center; margin-top: 20px">
                <el-pagination
                        background
                        @current-change="handleCurrentChange"
                        :current-page="accessPage"
                        :page-size="accessSize"
                        layout="total, prev, pager, next, jumper"
                        :total="totalRecords">
                </el-pagination>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    let _this;
    import echarts from 'echarts'
    import request from '../api/request'
    import {Navbar, TabItem} from 'mint-ui';

    var timeOut;
    var timeFetchAccess;
    var timeFetchNight;
    var strangerTimes;
    export default {
        name: "dormitory",
        data() {
            _this = this
            return {
                selected: "first",
                visible: false,
                infoManage: 'first',
                staffName: '',
                attendanceNumber: [],
                indormitoryNumber: [],
                outdormitoryNumber: [],
                attendAndInOrOut: ['', '', '', '', '', '', '', '', '', '', '', ''],
                userInfo: "",
                pageSize: EveryPageNum,//每一页的num
                accessSize: 10,
                currentPage: 0,
                accessPage: 0,
                totalRecords: 0,
                accessPageOrSize: {
                    passCurrentPage: 1,
                    regisCurrentPage: 1,
                    warnCurrentPage: 1,
                    alarmCurrentPage: 1
                },
                firstLoad: true,
                deviceId: [],
                accessList: [],
                accessByPassList: [],
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
                nightFallListLength: 0,
                imgDialogVisible: false,
                accessDialogVisble: false,
                warningDialogVisble: false,
                nightTotal: 0,
                bigImg: "",
                activeName: 'first',
                imageId: '',
                name: "",
                top: 0,
                moreAccessList: [],
                beginTime: [],
                submitUrl: "dorm/getAccessRecordList",
                deviceRtsp: [],
                warningTime: '',
                strangerTime: new Date(),
                visiRecordList: [],
                visitSelected: 'first'

            }
        },
        methods: {
            /*  handleCloseTitle(done) {
                  this.$confirm('是否取消?', '提示', {
                      confirmButtonText: '确定',
                      cancelButtonText: '取消',
                      type: 'warning'

                  }).then(() => {
                      moreAccessList=[]
                      done();
                  })
                      .catch(_ => {

                      });
              },*/

            big(item) {
                ;
                _this.imgDialogVisible = true;
                _this.bigImg = item.imageId
                // _this.warningDialogVisble = true;
            },
            /*   handleClick(tab, event) {
                   switch (_this.visitSelected) {
                       case "second":
                           _this.firstLoad = true;
                           _this.nightFallList = []
                           _this.fethcNightFall(_this.deviceId)
                           break;
                   }
                   $(".mes").html("")
                   $(".mess").html("")
                   $(".count").html("")
               },*/
            handleAccess() {
                switch (_this.selected) {
                    case "first":
                        clearInterval(timeFetchAccess)
                        _this.firstLoad = true;
                        _this.fetchNewestAccessRecordList(_this.deviceId)
                        timeFetchAccess = setInterval(function () {
                            _this.fetchNewestAccessRecordList(_this.deviceId);
                        }, 3000)
                        _this.submitUrl = "dorm/getAccessRecordList";
                        break;
                    case "second":
                        _this.audioStranage();
                        clearInterval(timeFetchAccess)
                        _this.firstLoad = true;
                        _this.fetchNewestAccessRecordListByPass(_this.deviceId, "STAFF");
                        timeFetchAccess = setInterval(function () {
                            _this.fetchNewestAccessRecordListByPass(_this.deviceId, "STAFF");
                        }, 3000)
                        _this.submitUrl = "dorm/getAccessRecordListByPass"
                        break;
                    case "third":
                        clearInterval(timeFetchAccess)
                        _this.firstLoad = true;
                        _this.fetchNewestAccessRecordListByIdentity(_this.deviceId, "STRANGER");
                        _this.submitUrl = "dorm/getAccessRecordListByIdentity";
                        break;
                    case "fourth":
                        clearInterval(timeFetchAccess)
                        _this.firstLoad = true;
                        _this.fetchNewestAccessRecordListByIdentity(_this.deviceId, "BLACKLIST");
                        _this.submitUrl = "dorm/getAccessRecordListByIdentity";
                        break;

                }
            },
            search() {
                _this.accessList = []
                if (_this.name != "" && _this.name != null) {
                    clearInterval(timeFetchAccess)
                    _this.selected = "first"
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
                            if (floorDevice[i].deviceRtsp != null && floorDevice[i].deviceRtsp != "") {
                                _this.deviceRtsp.push(floorDevice[i].deviceRtsp);
                            }
                        }
                        _this.fethcNightFall(_this.deviceId)
                        _this.fetchNewestAccessRecordList(_this.deviceId);
                        timeFetchAccess = setInterval(function () {
                            _this.fetchNewestAccessRecordList(_this.deviceId)
                        }, 3000)

                        strangerTimes = setInterval(function () {
                            _this.fetchStrangerAlarm()
                        }, 3000)

                    } else {
                        _this.$notify({
                            title: '错误',
                            message: '设备信息获取失败',
                            type: 'error'
                        });
                    }
                }).catch(error => {
                    _this.$notify({
                        title: '错误',
                        message: error,
                        type: 'error'
                    });
                })
            },
            defaultDate() {
                let date = new Date()
                let year = date.getFullYear().toString()
                let month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1).toString() : (date.getMonth() + 1).toString()
                let da = date.getDate() < 10 ? '0' + date.getDate().toString() : date.getDate().toString()
                let end = year + '-' + month + '-' + da + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                let beg = year + '-' + month + '-01' + " 00:00:00";
                console.log("当天:" + beg)
                _this.beginTime = [beg, end]
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
                            // [10, 20, 50, 60, 80, 10, 95, 25, 10, 62, 48, 15, 10]
                            data: _this.attendanceNumber,
                            areaStyle: {
                                normal: {
                                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                        {
                                            offset: 0,
                                            color: 'rgba(0, 136, 212, 0.3)'
                                        },
                                        {
                                            offset: 0.8,
                                            color: 'rgba(0, 136, 212, 0)'
                                        }], false),
                                    shadowColor: 'rgba(0, 0, 0, 0.1)',
                                    shadowBlur: 10
                                }
                            },
                            //控制线条的颜色
                            itemStyle: {
                                normal: {
                                    color: 'rgb(0,136,212)',
                                    borderColor: 'rgba(0,136,212,0.2)',
                                    borderWidth: 12
                                }
                            },
                            //数据显示

                            type: 'line',
                            smooth: 0.5, //折线图的弧度(0-1之间)
                            //设置折线图中表示每个坐标点的符号 emptycircle：空心圆；
                            //emptyrect：空心矩形；circle：实心圆；emptydiamond：菱形
                            //symbol : 'emptydiamond',
                            //stack : '总量',
                            symbol: 'none',
                        },
                        { //系列列表
                            //控制线条下面区域面积的颜色
                            areaStyle: {
                                normal: {
                                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                        {
                                            offset: 0,
                                            color: 'rgba(137, 189, 27, 0.3)'
                                        },
                                        {
                                            offset: 0.8,
                                            color: 'rgba(137, 189, 27, 0)'
                                        }], false),
                                    shadowColor: 'rgba(0, 0, 0, 0.1)',
                                    shadowBlur: 10
                                }
                            },
                            //控制线条的颜色
                            itemStyle: {
                                normal: {
                                    color: 'rgb(137,189,27)',
                                    borderColor: 'rgba(137,189,2,0.27)',
                                    borderWidth: 12

                                }
                            },
                            //数据显示
                            type: 'line',
                            symbol: 'none',
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
                            symbol: 'none',
                            data: _this.indormitoryNumber,
                            areaStyle: {
                                normal: {
                                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                        {
                                            offset: 0,
                                            color: 'rgba(0, 136, 212, 0.3)'
                                        },
                                        {
                                            offset: 0.8,
                                            color: 'rgba(0, 136, 212, 0)'
                                        }], false),
                                    shadowColor: 'rgba(0, 0, 0, 0.1)',
                                    shadowBlur: 10
                                }
                            },
                            //控制线条的颜色
                            itemStyle: {
                                normal: {
                                    color: 'rgb(0,136,212)',
                                    borderColor: 'rgba(0,136,212,0.2)',
                                    borderWidth: 12
                                }
                            },
                            //数据显示

                            type: 'line',
                            smooth: 0.5, //折线图的弧度(0-1之间)
                            //设置折线图中表示每个坐标点的符号 emptycircle：空心圆；
                            //emptyrect：空心矩形；circle：实心圆；emptydiamond：菱形
                            //symbol : 'emptydiamond',
                            //stack : '总量',
                            symbol: 'none',
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
                            data: _this.outdormitoryNumber,
                            areaStyle: {
                                normal: {
                                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                                        {
                                            offset: 0,
                                            color: 'rgba(0, 136, 212, 0.3)'
                                        },
                                        {
                                            offset: 0.8,
                                            color: 'rgba(0, 136, 212, 0)'
                                        }], false),
                                    shadowColor: 'rgba(0, 0, 0, 0.1)',
                                    shadowBlur: 10
                                }
                            },
                            //控制线条的颜色
                            itemStyle: {
                                normal: {
                                    color: 'rgb(0,136,212)',
                                    borderColor: 'rgba(0,136,212,0.2)',
                                    borderWidth: 12
                                }
                            },
                            //数据显示

                            type: 'line',
                            smooth: 0.5, //折线图的弧度(0-1之间)
                            //设置折线图中表示每个坐标点的符号 emptycircle：空心圆；
                            //emptyrect：空心矩形；circle：实心圆；emptydiamond：菱形
                            //symbol : 'emptydiamond',
                            //stack : '总量',
                            symbol: 'none',
                        }
                    ]
                }
                myChart.setOption(option);
                attendace.setOption(option1);
                outSide.setOption(option2);
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
                        if (res.data.data != null) {
                            for (let i = 0; i < res.data.data.list.length; i++) {
                                res.data.data.list[i].imageId = IP + "/image/" + res.data.data.list[i].imageId;
                            }
                            _this.nightFallList = res.data.data.list;
                        } else {
                            _this.$notify({
                                title: '提示',
                                message: '当前晚归数据为空',
                                type: 'success'
                            });
                        }
                    } else {
                        _this.$notify({
                            title: '错误',
                            message: '晚归查询失败',
                            type: 'error'
                        });
                    }
                }).catch(error => {
                    _this.$notify({
                        title: '错误',
                        message: error,
                        type: 'error'
                    });
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
                        _this.SetEchart();
                    } else {
                        _this.$notify({
                            title: '错误',
                            message: '获取时段人数失败',
                            type: 'error'
                        });
                    }
                }).catch(error => {
                    _this.$notify({
                        title: '错误',
                        message: error,
                        type: 'error'
                    });
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
                        _this.$notify({
                            title: '错误',
                            message: "获取进出记录失败",
                            type: 'error'
                        });
                    }
                }).catch(error => {
                    _this.$notify({
                        title: '错误',
                        message: error,
                        type: 'error'
                    });
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
                            if (res.data.data != null) {
                                for (let i = 0; i < res.data.data.length; i++) {
                                    res.data.data[i].imageId = IP + "/image/" + res.data.data[i].imageId;
                                }
                                _this.accessList = res.data.data;
                            }
                        } else {
                            _this.$notify({
                                title: '错误',
                                message: "获取最新通行记录失败",
                                type: 'error'
                            });
                        }
                    }
                ).catch(error => {
                    _this.$notify({
                        title: '错误',
                        message: "fetchNewestAccessRecordList===>:" + error,
                        type: 'error'
                    });
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
                            if (res.data.data != null) {
                                for (let i = 0; i < res.data.data.length; i++) {
                                    res.data.data[i].imageId = IP + "/image/" + res.data.data[i].imageId;
                                }
                                _this.accessByPassList = res.data.data;
                            }
                        } else {
                            _this.$notify({
                                title: '错误',
                                message: "获取最新通行记录失败==>ByPass",
                                type: 'error'
                            });
                        }
                    }
                ).catch(error => {
                    _this.$notify({
                        title: '错误',
                        message: "fetchNewestAccessRecordListByPass===>:" + error,
                        type: 'error'
                    });
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
                            if (res.data.data != null) {
                                for (let i = 0; i < res.data.data.length; i++) {
                                    res.data.data[i].imageId = IP + "/image/" + res.data.data[i].imageId;
                                }
                                _this.accessList = res.data.data;
                            }
                        } else {
                            _this.$notify({
                                title: '错误',
                                message: "获取最新通行记录失败==>ByName",
                                type: 'error'
                            });
                        }
                    }
                ).catch(error => {
                    _this.$notify({
                        title: '错误',
                        message: "fetchNewestAccessRecordListByName===>:" + error,
                        type: 'error'
                    });
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
                            if (res.data.data != null) {
                                for (let i = 0; i < res.data.data.length; i++) {
                                    res.data.data[i].imageId = IP + "/image/" + res.data.data[i].imageId;
                                }
                                _this.accessList = res.data.data;
                            }
                        } else {
                            _this.$notify({
                                title: '错误',
                                message: "获取最新通行记录失败==>ByIdentity",
                                type: 'error'
                            });
                        }
                    }
                ).catch(error => {
                    _this.$notify({
                        title: '错误',
                        message: "fetchNewestAccessRecordListByIdentity===>:" + error,
                        type: 'error'
                    });
                })

            },
            /**
             * 查询陌生人
             */
            fetchStrangerAlarm() {
                let params = new URLSearchParams();
                params.append("deviceIds", _this.deviceId);
                params.append("queryStartTime", _this.strangerTime);
                params.append("queryFinishTime", new Date());
                request({
                    url: HOST + "access/getStranageList",
                    method: 'post',
                    data: params
                }).then(res => {
                    if (res.data.code == 200 && res.data.data != null) {
                        let list = res.data.data;
                        let time = new Date();
                        let waring = require('../assets/img/info_warning.png')
                        _this.audioStranage();
                        for (let i = 0; i < list.length; i++) {
                            let da = new Date((list[0].timestamp + 1) * 1000);
                            _this.strangerTime = da;
                            var nowTime = da.getHours() + ":" + da.getMinutes() + ":" + da.getSeconds();
                            _this.$notify({
                                title: '陌生人',
                                dangerouslyUseHTMLString: true,
                                message: '<div class="warning">' +
                                    '<img style="width:315px; height:245px;margin-top: 20px;margin-left:-20px" src=' + IP + '/image/' + list[i].face_image_id + '/>' +
                                    '<img style="border-radius: 50%;margin-left: 8rem;margin-top: -3rem;width:90px; height:90px" src=' + waring + '/>' +
                                    '<span style="margin-left: 7.5rem;margin-top:50px;font-size: 22px;font-weight: 500;color: #C42E3B ">WARNING</span><br/>' +
                                    '<span style="margin-left: 6.8rem;font-size: 25px;font-weight: 500;color: #C42E3B;margin-top: 20px ">【陌生人】</span><br/>' +
                                    '<span style="margin-left: 8.3rem;font-size: 22px;font-weight: 500;color: #C42E3B;margin-top: 10rem">' + nowTime + '</span>' +
                                    '</div>',
                                position: 'top-left'
                            });
                        }
                    }
                }).catch(error => {
                    _this.$notify({
                        title: '错误',
                        message: error,
                        type: 'error'
                    });
                })
            },


            handleScrollNight() {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop = document.getElementById("div1").scrollTop
                //变量windowHeight是可视区的高度
                var windowHeight = document.getElementById("div1").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight = document.getElementById("div1").scrollHeight
                //滚动条到底部的条件
                if (_this.firstLoad) {
                    if (scrollTop + windowHeight == scrollHeight) {
                        if (_this.nightFallListLength == _this.nightTotal) {
                            $(".mes").html("已无更多数据")
                            $(".count").html("总数：" + _this.nightFallListLength)
                            return
                        }
                        _this.nightPage += 1;
                        _this.fethcNightFall(_this.deviceId)
                    }
                }

            },
            handleScrollAccessAll() {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop2 = document.getElementById("accessAll").scrollTop
                _this.top = document.getElementById("accessAll").scrollTop
                if (scrollTop2 > 1) {
                    clearInterval(timeFetchAccess)
                }
                if (scrollTop2 == 0) {
                    clearInterval(timeFetchAccess)
                    if (_this.name != "" && _this.name != null) {
                        timeFetchAccess = setInterval(function () {
                            _this.fetchNewestAccessRecordListByName(_this.deviceId, _this.name)
                        }, 3000)
                    } else {
                        timeFetchAccess = setInterval(function () {
                            _this.fetchNewestAccessRecordList(_this.deviceId)
                        }, 3000)
                    }
                }
                //变量windowHeight是可视区的高度
                var windowHeight2 = document.getElementById("accessAll").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight2 = document.getElementById("accessAll").scrollHeight
                //滚动条到底部的条件
                if (scrollTop2 + windowHeight2 == scrollHeight2) {
                    $(".mess").html("+ 点击查看更多通行记录")
                }
            },
            handleScrollAccessPass() {
                //变量scrollTop是滚动条滚动时，距离顶部的距离
                var scrollTop3 = document.getElementById("accessPass").scrollTop
                if (scrollTop3 > 1) {
                    clearInterval(timeFetchAccess)
                }
                if (scrollTop3 == 0) {
                    timeFetchAccess = setInterval(function () {
                        _this.fetchNewestAccessRecordListByPass(_this.deviceId, "STAFF")
                    }, 3000)
                }
                //变量windowHeight是可视区的高度
                var windowHeight3 = document.getElementById("accessPass").clientHeight
                //变量scrollHeight是滚动条的总高度
                var scrollHeight3 = document.getElementById("accessPass").scrollHeight
                if (scrollTop3 + windowHeight3 == scrollHeight3) {
                    $(".mess").html("+ 点击查看更多通行记录")
                }

            },
            handleScrollUnregistered() {
                var scrollTop4 = document.getElementById("accessRegis").scrollTop
                if (scrollTop4 > 1) {
                    clearInterval(timeFetchAccess)
                }
                if (scrollTop4 == 0) {
                    clearInterval(timeFetchAccess)
                    timeFetchAccess = setInterval(function () {
                        _this.fetchNewestAccessRecordListByIdentity(_this.deviceId, "STRANGER")
                    }, 3000)
                }
                var windowHeight4 = document.getElementById("accessRegis").clientHeight
                var scrollHeight4 = document.getElementById("accessRegis").scrollHeight
                if (scrollTop4 + windowHeight4 == scrollHeight4) {
                    $(".mess").html("+ 点击查看更多通行记录")
                }


            },

            handleScrollBlack() {
                var scrollTop6 = document.getElementById("accessBlack").scrollTop
                if (scrollTop6 > 1) {
                    clearInterval(timeFetchAccess)
                }
                if (scrollTop6 == 0) {
                    clearInterval(timeFetchAccess)
                    timeFetchAccess = setInterval(function () {
                        _this.fetchNewestAccessRecordListByIdentity(_this.deviceId, "BLACKLIST")
                    }, 3000)
                }
                var windowHeight6 = document.getElementById("accessBlack").clientHeight
                var scrollHeight6 = document.getElementById("accessBlack").scrollHeight
                if (scrollTop6 + windowHeight6 == scrollHeight6) {
                    $(".mess").html("+ 点击查看更多通行记录")
                }

            },
            verifyForm(formObj) {
                let result = true;
                if (formObj.deviceId == null || formObj.deviceId == "") {
                    _this.$notify({
                        title: '提示',
                        message: '设备id不能为空'
                    });
                    result = false;
                } else if (formObj.floorNo == null || formObj.floorNo == "") {
                    _this.$notify({
                        title: '提示',
                        message: '设备名称不能为空'
                    });
                    result = false;
                }

                return result;
            },
            handleSelectScroll() {
                document.getElementById("div1").addEventListener("scroll", _this.handleScrollNight);
                document.getElementById("accessAll").addEventListener("scroll", _this.handleScrollAccessAll);
                document.getElementById("accessPass").addEventListener("scroll", _this.handleScrollAccessPass);
                document.getElementById("accessRegis").addEventListener("scroll", _this.handleScrollUnregistered);
                document.getElementById("accessBlack").addEventListener("scroll", _this.handleScrollBlack);
            },
            handleMoreAccess(item) {
                _this.defaultDate();
                _this.fetchMoreAccess(item);
                _this.accessDialogVisble = true;
            },
            fetchMoreAccess(item) {
                let params = new URLSearchParams();
                params.append("page", _this.currentPage)
                params.append("size", _this.accessSize);
                params.append("deviceId", _this.deviceId);
                params.append("queryFinishTime", _this.beginTime[0]);
                params.append("queryEndTime", _this.beginTime[1]);
                if (_this.selected == 'second') {
                    params.append("pass", item);
                } else if (_this.selected == 'third' || _this.selected == 'fourth') {
                    params.append("identity", item);
                }
                request({
                    url: HOST + _this.submitUrl,
                    method: "post",
                    data: params
                }).then(res => {
                        if (res.data.code == 200) {
                            //_this.moreAccessList = res.data.data.list;
                            if (res.data.data.list != null) {
                                _this.totalRecords = res.data.data.total;
                                for (let i = 0; i < res.data.data.list.length; i++) {
                                    res.data.data.list[i].imageId = IP + "/image/" + res.data.data.list[i].imageId;
                                }
                                _this.moreAccessList = res.data.data.list;
                            } else {
                                _this.totalRecords = 0
                                _this.moreAccessList = []
                            }
                        } else {
                            _this.$notify({
                                title: '提示',
                                message: '获取最新通行记录失败'
                            });
                        }
                    }
                ).catch(error => {
                    _this.$notify({
                        title: '提示',
                        message: "getAccessRecordList===>:" + error,
                    });

                })
            },
            handleCurrentChange(val) {
                _this.defaultDate();
                _this.currentPage = val - 1;
                if (_this.staffName != null && _this.staffName != "") {
                    _this.getStaffByName();
                } else {
                    if (_this.selected == "first") {
                        _this.submitUrl = "dorm/getAccessRecordList";
                        _this.fetchMoreAccess();
                    } else if (_this.selected == "second") {
                        _this.submitUrl = "dorm/getAccessRecordListByPass";
                        _this.fetchMoreAccess("STAFF")
                    } else if (_this.selected == "third") {
                        _this.submitUrl = "dorm/getAccessRecordListByIdentity";
                        _this.fetchMoreAccess("STRANGER")
                    } else if (_this.selected == "fourth") {
                        _this.submitUrl = "dorm/getAccessRecordListByIdentity";
                        _this.fetchMoreAccess("BLACKLIST")
                    }
                }

            },
            loginOut() {
                _this.$router.push("/login")
                _this.visible = true;
                sessionStorage.removeItem("user")
                clearInterval(timeOut);
                clearInterval(timeFetchAccess)
                clearInterval(timeFetchNight)
                clearInterval(strangerTimes)
            },
            audioStranage() {
                document.getElementById("error").play();
            },
            getStaffByName() {

                let params = new URLSearchParams();
                params.append("page", _this.currentPage)
                params.append("size", _this.accessSize);
                params.append("deviceId", _this.deviceId);
                params.append("name", _this.staffName)
                params.append("queryFinishTime", _this.beginTime[0]);
                params.append("queryEndTime", _this.beginTime[1]);
                request({
                    url: HOST + "dorm/getAccessRecordListByName",
                    method: "post",
                    data: params
                }).then(res => {
                        _this.moreAccessList = [];
                        if (res.data.code == 200) {
                            if (res.data.data.list != null) {
                                _this.totalRecords = res.data.data.total;
                                for (let i = 0; i < res.data.data.list.length; i++) {
                                    res.data.data.list[i].imageId = IP + "/image/" + res.data.data.list[i].imageId;
                                }
                                _this.moreAccessList = res.data.data.list;
                            }
                        } else {
                            _this.$notify({
                                title: '提示',
                                message: '获取最新通行记录失败'
                            });
                        }
                    }
                ).catch(error => {
                    _this.$notify({
                        title: '错误',
                        message: "getAccessRecordListByName===>:" + error,
                        type: 'error'
                    });
                })

            },

            //获取最近一次抓拍记录在五天前的记录
            fetchVisitRecord(floorName) {
                let params = new URLSearchParams();
                params.append("tagName", floorName);
                request({
                    url: HOST + "access/getVisitRecord",
                    method: "post",
                    data: params
                }).then(res => {
                    if (res.data.code == 200) {
                        for (let i = 0; i < res.data.data.length; i++) {
                            res.data.data[i].imageId = IP + "/image/" + res.data.data[i].imageId;
                        }
                        _this.visiRecordList = res.data.data;
                    } else {
                        showMessage()
                    }
                }).catch(error => {
                    console.log(error)
                })
            }
        },
        created() {
            this.userInfo = JSON.parse(sessionStorage.getItem("user"))
            _this.getFloorDevice(_this.userInfo.floorNo);
            _this.fetchVisitRecord(_this.userInfo.floorName)
            setInterval(function () {
                _this.fetchVisitRecord(_this.userInfo.floorName)
            }, 1000 * 60 * 20)
        },
        mounted() {
            _this.defaultDate();
            _this.fetchAttendanceAndInOrOut(_this.userInfo.floorName);
            setInterval(function () {
                _this.fetchAttendanceAndInOrOut(_this.userInfo.floorName)
            }, 1000 * 60 * 30)
            _this.fetchNumbers(_this.userInfo.floorName)
            _this.SetEchart();
            _this.handleSelectScroll();

        }
        ,
        beforeDestroy() {
            //removeEventListener("scroll", _this.handleScrollAccessAll)
            clearInterval(timeOut);
            clearInterval(timeFetchAccess)
            clearInterval(timeFetchNight)
            clearInterval(strangerTimes)
        }
        ,

        watch: {
            imgDialogVisible: function (val, oldval) {
                if (val == false) {

                }
            }
            ,
            accessDialogVisble: function (val, oldval) {
                if (val == false) {
                    _this.accessPage = 1;
                    _this.currentPage = 0;
                }

            }
            ,

        }
        ,

    }

    var mqttReconnectInterval = null;
    var hostname = MqttServer,
        port = ServerPort,
        clientId = `client-${newGuid()}`,
        timeout = 30,
        keepAlive = 100,
        cleanSession = true,
        ssl = false;

    var client = new Paho.MQTT.Client(hostname, port, clientId);

    var options = {
        invocationContext: {
            host: hostname,
            port: port,
            path: client.path,
            clientId: clientId
        },
        timeout: timeout,
        keepAliveInterval: keepAlive,
        cleanSession:cleanSession,
        useSSL:ssl,
        onSuccess:onConnect,
        onFailure:function (e) {
            console.log(`connect failure:${e}`);
        }
    };
    $(document).ready(function () {
        client.connect(options);  //连接服务器并注册连接成功处理事件
        client.onConnectionLost=onConnectionLost;  //注册连接断开处理事件
        client.onMessageArrived=onMessageArrived; //注册消息接收处理事件
    });


    function onConnect() {
        console.log("connect successfully");
        if (mqttReconnectInterval != null) {
            clearInterval(mqttReconnectInterval);
            mqttReconnectInterval = null;
        }
        for (let item of ServerTOPIC)//订阅主题
        {
            console.log(`subscribed server topic: ${item}`);
            client.subscribe(item);
        }
    }

    function newGuid() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }

    function onConnectionLost(responseObject) {
        if (responseObject.errorCode !== 0) {
            console.log("连接已断开");
            console.log("onConnectionLost:" + responseObject.errorMessage);
            mqttReconnectInterval = setInterval(() => {
                client.connect(options);
                client.onConnectionLost = onConnectionLost;//注册连接断开处理事件
                client.onMessageArrived = onMessageArrived;//注册消息接收处理事件
            }, 2000);
        }
    }
    function onMessageArrived(message) {
        // console.log("收到消息:" + message.payloadString);
        console.log("主题：" + message.destinationName);
        var data = null;
        try {
            data = jQuery.parseJSON(message.payloadString);
            //console.log("解析出来的：data：" + JSON.stringify(data));
        } catch (e) {
            console.log(e);
        }
        if (data != null) {

            switch (message.destinationName) {
                case ServerTOPIC[0]: //visitor/response
                    _this.result = data.result;
                    _this.name = data.name;
                    _this.photo = "data:image/jpeg;base64," + data.base64;
                    if (_this.result == 1) {
                        _this.openSuccess();
                    } else {
                        _this.openError();
                    }
                    console.log("页面弹框提示后，跳转路径")
                    _this.$router.push("/userHome/invitationRegis");

                    break;
                case ServerTOPIC[1]://visitor/success
                    _this.name = data.name;
                    _this.idCard = data.id_num;
                    _this.address = data.address;
                    _this.photo = "data:image/jpeg;base64," + data.photo;
                    _this.fetchImage(data.invitation)

                    _this.$router.push("/userHome/accessManage");
                    break;
                case ServerTOPIC[2]://visitor/error
                    _this.name = data.name;
                    _this.idCard = data.id_num;
                    _this.address = data.address;
                    _this.photo = "data:image/jpeg;base64," + data.photo;
                    _this.openVerifyError();
                    _this.$router.push("/userHome/accessManage");
                    break;
                default:
                    console.log("未知主题消息...")
                    break;
            }
        }
    }

</script>

<style>
    .title {
        font-size: 22px;
        color: #6a747c;
        font-weight: 600;
    }

    .a .el-tabs__nav-scroll {
        float: right;
    }

    .el-notification.left {
        left: 16px;
        box-shadow: 0 30px 60px -15px #E01E1E;
    }

    .b .el-tabs__nav-scroll {
        margin-left: 20px;
    }

    .a .el-tabs__item {
        padding: 0 20px;
        height: 40px;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        line-height: 40px;
        display: inline-block;
        list-style: none;
        font-size: 17px;
        font-weight: 600;
        color: #727272;
        position: relative;

    }

    .b .el-tabs__item {
        padding: 0 30px;
        height: 40px;
        -webkit-box-sizing: border-box;
        box-sizing: border-box;
        line-height: 40px;
        display: inline-block;
        list-style: none;
        font-size: 17px;
        /* font-family: PingFangSC-Semibold;*/
        font-weight: 600;
        color: #727272;
        text-align: center;;
        position: relative;
    }

    /* .b .el-tabs__item is-top is-active {
         width: 60px;
         transform: translateX(-29px);
     }
 */
    .revisiDiv::-webkit-scrollbar {
        width: 5px;
        height: 1px;
    }

    .revisiDiv::-webkit-scrollbar-thumb {
        border-radius: 5px;
        -webkit-box-shadow: inset 0 0 5px rgba(246, 244, 255, 0.2);
        background: white;
    }

    .revisiDiv::-webkit-scrollbar-track {
        -webkit-box-shadow: inset 0 0 1px rgba(0, 0, 0, 0);
        border-radius: 10px;
        background: #F6F4FF;
    }

    .revisiDiv {
        -ms-scroll-chaining: chained;
        -ms-overflow-style: none;
        -ms-content-zooming: zoom;
        -ms-scroll-rails: none;
        -ms-content-zoom-limit-min: 100%;
        -ms-content-zoom-limit-max: 500%;
        -ms-scroll-snap-type: proximity;
        -ms-scroll-snap-points-x: snapList(100%, 200%, 300%, 400%, 500%);
        -ms-overflow-style: none;
        overflow: auto;
    }

    .visiiDiv::-webkit-scrollbar {
        width: 5px;
        height: 1px;
    }

    .visiiDiv::-webkit-scrollbar-thumb {
        border-radius: 5px;
        -webkit-box-shadow: inset 0 0 5px rgba(246, 244, 255, 0.2);
        background: #0C101D;
    }

    .visiiDiv::-webkit-scrollbar-track {
        -webkit-box-shadow: inset 0 0 1px rgba(0, 0, 0, 0);
        border-radius: 10px;
        background: #0C101D;
    }

    .visiiDiv {
        -ms-scroll-chaining: chained;
        -ms-overflow-style: none;
        -ms-content-zooming: zoom;
        -ms-scroll-rails: none;
        -ms-content-zoom-limit-min: 100%;
        -ms-content-zoom-limit-max: 500%;
        -ms-scroll-snap-type: proximity;
        -ms-scroll-snap-points-x: snapList(100%, 200%, 300%, 400%, 500%);
        -ms-overflow-style: none;
        overflow: auto;
    }

    .visitA .el-tabs__item {
        color: white;
        font-size: 17px;
    }

    .accesDialog li {
        float: left;
        margin-left: 60px;
    }

    .el-tabs__active-bar {
        background-color: #93A0D0
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

    .warning .el-dialog {
        position: relative;
        margin: 0 auto 50px;
        border-radius: 19px;
        -webkit-box-shadow: 0 1px 3px rgba(0, 0, 0, .3);
        box-shadow: 0 30px 60px -15px #E01E1E;
        box-sizing: border-box;
        width: 50%;
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

    .el-button--info.is-plain {
        color: #909399;
        background: #f4f4f5;
        border-color: #d3d4d6;
        border-radius: 15px;
    }

    .el-input--suffix .el-input__inner {
        padding-right: 30px;
        border-radius: 15px;
    }

    .el-date-editor--daterange.el-input, .el-date-editor--daterange.el-input__inner, .el-date-editor--timerange.el-input, .el-date-editor--timerange.el-input__inner {
        width: 393px;
    }

</style>