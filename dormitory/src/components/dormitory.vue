<template xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">
    <el-row :gutter="0">
        <el-col :span="8">
            <div class="grid-content bg-purple">
                <div style="height: 930px">
                    <div style="height: 100px">
                        <i class="el-icon-view" style=" font-size: 25px ;margin-left :60px;margin-top: 20px ">
                            <i class="title">宿舍出入管理系统</i>
                        </i>

                    </div>
                    <div style="height: 300px; width:85% ;background-color: white;margin-left:60px "
                         class="well well-lg">

                        <i style=" font:20px Extra Small;  color: #6a747c;">宿舍信息</i>

                    </div>
                    <div class="well well-lg"
                         style="background-color: white;height: 450px;margin-left:60px; width:85% ">
                        <el-input :span="3"
                                  placeholder="学生姓名" clearable
                                  auto-complete="off"></el-input>

                    </div>
                </div>
            </div>
        </el-col>
        <el-col :span="8">
            <div class="grid-content bg-purples">
                <el-tabs v-model="activeName" @tab-click="handleClick" class="middle">
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
                </el-tabs>
            </div>
        </el-col>
        <el-col :span="8">
            <div class="grid-content bg-purple">
                <div style="height: 930px">
                    <div  style="height: 100px; width:85% ;background-color: white;margin-left:60px "
                          class="well well-lg">
                        <i style="margin-left: 60px">
                            <i class="title">通行记录</i>
                            <el-input
                                    style="width: 150px;margin-top: 35px;margin-left: 300px"
                                    placeholder="学生姓名" clearable
                                    auto-complete="off">
                                <i slot="prefix" class="el-input__icon el-icon-search"></i>
                            </el-input>
                        </i>

                    </div>
                    <div  style="height: 800px; width:85% ;background-color:lucency;margin-left:60px "
                          class="well well-lg">

                    </div>
                </div>
            </div>
        </el-col>
    </el-row>
</template>

<script>
    export default {
        name: "dormitory",
        data() {
            _this = this
            return {
                activeName: 'second'
            };
        },
        methods: {
            handleClick(tab, event) {
                console.log(tab, event);
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

    .el-tabs__item {
        color: white;
    }

    .middle {
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

    .bg-purple {
        background: #f6f4ff;

    }

    .bg-purples {
        background: #4a6687;

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