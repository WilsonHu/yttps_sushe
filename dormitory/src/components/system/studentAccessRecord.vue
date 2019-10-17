<template>
    <div>
        <el-col class="well well-lg" style="background-color: white;">
            <el-row>
                <el-col>
                    <el-form :model="filters" label-position="right" label-width="60px">
                        <el-col :span="5">
                            <el-form-item label="日期:">
                                <el-date-picker
                                        v-model="beginTime"
                                        type="datetimerange"
                                        value-format="yyyy-MM-dd HH:mm:ss"
                                        range-separator="-"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期">
                                </el-date-picker>
                            </el-form-item>
                        </el-col>
                        <el-col :span="5" style="margin-left: 150px">
                            <el-form-item label="楼栋">
                                <el-select v-model="positionType" placeholder="请选择">
                                    <el-option
                                            v-for="item in floorName"
                                            v-if="item.floorName!=null"
                                            :key="item.floorName"
                                            :label="item.floorName"
                                            :value="item.floorName">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-form>

                    <el-col :span="3" style="float: right">
                        <el-button
                                icon="el-icon-tickets"
                                size="normal"
                                type="primary"
                                @click="exExecle">导出
                        </el-button>
                    </el-col>
                    <el-col :span="3" style="float: right">
                        <el-button
                                icon="el-icon-search"
                                size="normal"
                                type="primary"
                                @click="search">搜索
                        </el-button>
                    </el-col>
                    <el-table
                            v-loading="loadingUI"
                            element-loading-text="获取数据中..."
                            :data="userInfo"
                            border
                            style="width: 100%;">
                        <el-table-column
                                width="75"
                                align="center"
                                label="序号">
                            <template scope="scope">
                                {{scope.$index+1}}
                            </template>
                        </el-table-column>
                        <el-table-column
                                prop="name"
                                align="center"
                                label="人员姓名">
                        </el-table-column>
                        <el-table-column
                                prop="startTime"
                                align="center"
                                width="300"
                                label="最早通行时间">
                        </el-table-column>
                        <el-table-column
                                align="center"
                                width="300"
                                prop="endTime"
                                label="最晚通行时间">

                        </el-table-column>
                    </el-table>
                    <div class="block" style="text-align: center; margin-top: 20px">
                        <el-pagination
                                background
                                @current-change="handleCurrentChange"
                                :current-page="currentPage"
                                :page-size="pageSize"
                                layout="total, prev, pager, next, jumper"
                                :total="totalRecords">
                        </el-pagination>
                    </div>
                </el-col>
            </el-row>
        </el-col>

    </div>
</template>

<script>
    var _this;
    export default {
        name: "studentAccessRecord",
        data() {
            _this = this;
            return {
                options2: [
                    {
                        value: '东34号',
                        label: '东34号'
                    }, {
                        value: '东35号',
                        label: '东35号',
                    }
                ],
                positionType: '',
                start_time: '',
                filters: {},
                userInfo: [],
                //分页
                totalRecords: 0,
                pageSize: 10,//每一页的num
                currentPage: 1,
                startRow: 1,
                beginTime: [],
                loadingUI: false,
                floorName: []
            }
        },
        methods: {
            handleSizeChange(val) {
            },
            handleCurrentChange(val) {
                this.currentPage = val;
                this.search();
            },
            checkParams() {
                let result = true;
                if (_this.beginTime == null) {
                    this.$alert('请选择日期', '提示', {
                        confirmButtonText: '确定'
                    })
                    result = false;
                }else if (_this.positionType == "") {
                    this.$alert('请选择需要查询的楼栋', '提示', {
                        confirmButtonText: '确定'
                    })
                    result = false;
                }
                return result;
            },
            search() {
                if (_this.checkParams()) {
                    _this.userInfo = new Array();
                    _this.loadingUI = true;
                    $.ajax({
                        url: HOST + "staff/queryStaffVisitRerordTime",
                        type: "POST",
                        dataType: "json",
                        data: {
                            queryStartTime: new Date(_this.beginTime[0]),
                            queryFinishTime: new Date(_this.beginTime[1]),
                            type: _this.positionType,
                            page: _this.currentPage,
                            size: _this.pageSize
                        },
                        success: function (data) {
                            if (data.code == 200) {
                                if (data.data.list == "") {
                                    alert("本月数据为空，请挑选其它时间")
                                    _this.loadingUI = false;
                                    return;
                                }
                                _this.totalRecords = data.data.total;
                                _this.userInfo = data.data.list;
                                _this.startRow = data.data.startRow;
                            }
                            _this.loadingUI = false;
                        },
                        error: function (data) {
                            showMessage(_this, '服务器访问出错', 1);
                            _this.loadingUI = false;
                        }

                    })
                }

            },

            exExecle() {
               if (_this.checkParams()){
                   $.ajax({
                       url: HOST + "staff/exportRecord",
                       type: "POST",
                       dataType: "json",
                       data: {
                           start_time: new Date(_this.beginTime[0]),
                           endTime: new Date(_this.beginTime[1]),
                           positionType: _this.positionType,
                       },
                       success: function (data) {
                           if (data.code == 200) {
                               var a = document.createElement("a");
                               a.href = MqttServer + data.data;
                               a.click();
                           }

                       },
                       error: function (data) {
                           showMessage(_this, '服务器访问出错', 0);
                       }
                   })
               }
            },
            defaultDate() {
                let date = new Date()
                let year = date.getFullYear().toString()
                let month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1).toString() : (date.getMonth() + 1).toString()
                let da = date.getDate() < 10 ? '0' + date.getDate().toString() : date.getDate().toString()
                let end = year + '-' + month + '-' + da + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
                let beg = year + '-' + month + '-01' + " 00:00:00";
                _this.beginTime = [beg, end]
            },
            fetchFloorNo() {
                $.ajax({
                    url: HOST + "user/list",
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 200) {
                            _this.floorName = data.data.list;

                        } else {
                            console.log("获取楼层失败")
                        }
                    },
                    error: function (error) {
                        _this.$notify({
                            title: '提示',
                            message: error,
                            type: 'error'
                        });
                    }
                })
            },

        },
        mounted() {
            _this.defaultDate();
            _this.fetchFloorNo();
        },
        watch: {
            positionType: function (val, oldval) {
                _this.currentPage = 1
            },
            start_time: function (val, oldval) {
                _this.currentPage = 1
            }
        }
    }
</script>

<style scoped>

</style>