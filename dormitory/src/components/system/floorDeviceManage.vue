<template>
    <div>
        <el-row>
            <el-col :span="3">
                <el-select v-model="key" clearable  placeholder="请选择楼号" style="width: 100%">
                    <el-option
                            v-for="item in floorNo"
                            v-if="item.floorNo!=null"
                            :key="item.floorNo"
                            :label="item.floorNo"
                            :value="item.floorNo">
                    </el-option>
                </el-select>
            </el-col>

            <el-col :span="3" style="margin-left: 20px">
                <el-button
                        icon="el-icon-search"
                        type="primary"
                        @click="search">查询
                </el-button>
            </el-col>
            <el-col :span="3" :offset="3" style="float: right">
                <el-button
                        icon="el-icon-plus"
                        type="primary"
                        @click="handleAdd">添加
                </el-button>
            </el-col>
        </el-row>
        <el-table
                v-loading="loadingUI"
                element-loading-text="获取数据中..."
                :data="tableData"
                style="margin-top: 20px"
                border>
            <el-table-column
                    width="75"
                    align="center"
                    label="序号">
                <template scope="scope">
                    {{scope.$index+startRow}}
                </template>
            </el-table-column>
            <el-table-column
                    align="center"
                    label="楼号">
                <template scope="scope">
                    <span>{{scope.row.floorNo}}号楼</span>
                </template>
            </el-table-column>
            <el-table-column
                    align="center"
                    prop="deviceId"
                    label="设备Id">
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
                    prop="deviceRtsp"
                    label="设备视频流">
            </el-table-column>
            <el-table-column
                    align="center"
                    label="操作"
                    width="200">
                <template scope="scope">
                    <el-button
                            size="small"
                            icon="el-icon-edit"
                            type="primary"
                            @click="handleEdit(scope.row)">编辑
                    </el-button>
                    <el-button
                            size="small"
                            type="danger"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.$index, scope.row)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog title="添加楼层设备" :before-close="handleCloseTitle" :visible.sync="addFloorDeviceDialog" size="tiny">
            <el-row>
                <el-form>
                    <el-col :span="20">
                        <el-form-item label="楼号：" :label-width="formLabelWidth">
                            <el-select v-model="from.floorNo" clearable  placeholder="请选择楼号" style="width: 100%">
                                <el-option
                                        v-for="item in floorNo"
                                        v-if="item.floorNo!=null"
                                        :key="item.floorNo"
                                        :label="item.floorNo"
                                        :value="item.floorNo">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="20">
                        <el-form-item label="设备ip：" :label-width="formLabelWidth">
                            <el-input type="text" v-model="from.deviceId" auto-complete="off">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="7">
                        <el-form-item label="进出类型：" :label-width="formLabelWidth">
                            <el-radio v-model="from.type" label="0">进</el-radio>
                            <el-radio v-model="from.type" label="1">出</el-radio>
                        </el-form-item>
                    </el-col>
                    <el-col :span="20">
                        <el-form-item label="设备视频：" :label-width="formLabelWidth">
                            <el-input type="text" v-model="from.deviceRtsp" auto-complete="off">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-form>
            </el-row>
            <el-alert
                    :title="errorMsg"
                    v-show="isError"
                    type="error"
                    show-icon>
            </el-alert>
            <span slot="footer" class="dialog-footer">
                    <el-button @click="handleCancel()">取 消</el-button>
                    <el-button type="primary" @click="add(from)">确 定</el-button>
                </span>

        </el-dialog>

        <el-dialog title="楼层设备的修改" :before-close="handleCloseTitle" :visible.sync="modifyFloorDeviceDialog" size="tiny">
            <el-row>
                <el-form>
                    <el-col :span="20">
                        <el-form-item label="楼号：" :label-width="formLabelWidth">
                            <el-select v-model="modifyFrom.floorNo" clearable  placeholder="请选择楼号" style="width: 100%">
                                <el-option
                                        v-for="item in floorNo"
                                        v-if="item.floorNo!=null"
                                        :key="item.floorNo"
                                        :label="item.floorNo"
                                        :value="item.floorNo">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="20">
                        <el-form-item label="设备ip：" :label-width="formLabelWidth">
                            <el-input type="text" v-model="modifyFrom.deviceId" auto-complete="off">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="7">
                        <el-form-item label="进出类型：" :label-width="formLabelWidth">
                            <el-radio v-model="modifyFrom.type" label="0">进</el-radio>
                            <el-radio v-model="modifyFrom.type" label="1">出</el-radio>
                        </el-form-item>
                    </el-col>
                    <el-col :span="20">
                        <el-form-item label="设备视频：" :label-width="formLabelWidth">
                            <el-input type="text" v-model="modifyFrom.deviceRtsp" auto-complete="off">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-form>
                <el-alert v-if="isError" style="margin-top: 20px;padding: 5px;"
                          :title="errorMsg"
                          type="error"
                          :closable="false"
                          show-icon>
                </el-alert>
            </el-row>

            <span slot="footer" class="dialog-footer">
                    <el-button @click="handleExit">取 消</el-button>
                    <el-button type="primary" @click="onEdit(modifyFrom)">确 定</el-button>
                </span>

        </el-dialog>

        <el-dialog title="提示" :visible.sync="deleteConfirmVisible" width="30%">
            <span style="margin-left: -10%">确认要删除[ <b>{{selectedItem.floorNo}}号楼的信息吗</b> ]？</span>
            <span slot="footer" class="dialog-footer">
	    <el-button @click="deleteConfirmVisible = false" icon="el-icon-close">取 消</el-button>
	    <el-button type="primary" @click="onConfirmDelete" icon="el-icon-check">确 定</el-button>
	  </span>
        </el-dialog>
    </div>
</template>

<script>
    let _this;
    export default {
        name: "floorDeviceManage",
        data() {
            _this = this;
            return {
                tableData: [],
                floorNo: [],
                loadingUI: false,
                key: '',
                rtsp: '',
                selectedItem: {},
                addFloorDeviceDialog: false,
                modifyFloorDeviceDialog: false,
                deleteConfirmVisible: false,
                from: {
                    id: null,
                    floorNo: 0,
                    deviceId: '',
                    type: '0',
                    deviceRtsp: ''
                },
                modifyFrom: {
                    id: 0,
                    floorNo: 0,
                    deviceId: '',
                    type: '0',
                    deviceRtsp: ''
                },
                radio: '0',
                formLabelWidth: '100px',
                errorMsg: '',
                isError: false,
            }
        },
        methods: {
            handleCloseTitle(done) {
                this.$confirm('是否取消?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'

                }).then(() => {
                    done();
                    _this.from = {};
                    _this.modifyFrom = {}
                    _this.imageUrl = '';
                })
                    .catch(_ => {

                    });
            },
            search() {
                _this.loadingUI = true
                $.ajax({
                    url: HOST + "floor/device/list",
                    type: 'post',
                    dataType: 'json',
                    data: {
                        page: _this.currentPage,
                        size: _this.pageSize,
                        key: _this.key
                    },
                    success: function (data) {
                        if (data.code == 200) {
                            _this.tableData = data.data.list;
                            _this.totalRecords = data.data.total;
                            _this.startRow = data.data.startRow;
                        } else {
                            _this.$notify({
                                title: '提示',
                                message: '查询楼层设备失败',
                                type: 'error'
                            });
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
                _this.loadingUI = false
            },
            handleAdd() {
                _this.from = {};
                _this.addFloorDeviceDialog = true;

            },
            handleEdit(item) {
                _this.isError = false;
                _this.errorMsg = "";
                _this.modifyFrom.id = item.id;
                _this.modifyFrom.floorNo = item.floorNo;
                _this.modifyFrom.deviceId = item.deviceId;
                _this.modifyFrom.type = item.type.toString();
                if (item.deviceRtsp != null) {
                    _this.modifyFrom.deviceRtsp = item.deviceRtsp.toString();
                }
                _this.modifyFloorDeviceDialog = true;
            },
            handleDelete(index, item) {
                _this.selectedItem = copyObject(item);
                if (_this.selectedItem) {
                    _this.deleteConfirmVisible = true;
                }
            },
            handleCancel() {
                _this.addFloorDeviceDialog = false;
                _this.from = {};
            },
            handleExit() {
                _this.modifyFloorDeviceDialog = false;
                _this.modifyFrom = {};
            },
            add() {
                _this.isError = _this.validateForm(_this.from, false)
                if (_this.isError) {
                    $.ajax({
                        url: HOST + "floor/device/add",
                        type: 'post',
                        dataType: 'json',
                        data: {jsonData: JSON.stringify(_this.from)},
                        success: function (data) {
                            if (data.code == 200) {
                                _this.$notify({
                                    title: '提示',
                                    message: '信息添加成功',
                                    type: 'success'
                                });
                                _this.addFloorDeviceDialog = false
                                _this.from = {}
                                _this.search();
                            } else {
                                _this.$notify({
                                    title: '提示',
                                    message: '信息添加失败',
                                    type: 'error'
                                });
                                _this.addDialogVisible = false;
                                _this.from = {}
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
                }

            },
            onEdit() {
                this.isError = _this.validateForm(_this.modifyFrom, false)
                if (!_this.isError) {
                    $.ajax({
                        url: HOST + "floor/device/update",
                        type: 'post',
                        dataType: 'json',
                        data: {jsonData: JSON.stringify(_this.modifyFrom)},
                        success: function (data) {
                            if (data.code == 200) {
                                _this.modifyFloorDeviceDialog = false;
                                _this.search();
                                _this.$notify({
                                    title: '提示',
                                    message: '修改用户信息成功',
                                    type: 'success'
                                });
                            } else {
                                _this.errorMsg = data.message;
                                _this.isError = true;
                            }
                        },
                        error: function (error) {
                            _this.errorMsg = '服务器访问出错'
                            _this.isError = true;
                        }
                    })
                }

            },
            onConfirmDelete() {
                _this.deleteConfirmVisible = false;
                $.ajax({
                    url: HOST + 'floor/device/delete',
                    type: 'post',
                    dataType: 'json',
                    data: {
                        id: _this.selectedItem.id
                    },
                    success: function (data) {
                        if (data.code == 200) {
                            _this.deleteConfirmVisible = false;
                            _this.search()
                            _this.$notify({
                                title: '提示',
                                message: '删除成功',
                                type: 'success'
                            });
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
            fetchFloorNo() {
                $.ajax({
                    url: HOST + "user/list",
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == 200) {
                            _this.floorNo = data.data.list;

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
            validateForm(formObj, isEdit) {
                var pat = /[`~!@#$%^&*()_\-+=<>?:"{}|,\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/im;
                var iserror = false;
                if (!iserror && formObj.floorNo == null || formObj.floorNo == "") {
                    iserror = true;
                    this.errorMsg = '楼号不能为空';
                } else if (pat.test(formObj.floorNo)) {
                    iserror = true;
                    this.errorMsg = '楼号包含特殊字符';
                } else if (!iserror && formObj.deviceId == null || formObj.deviceId == "") {
                    iserror = true;
                    this.errorMsg = '请输入设备Id';
                } else if (pat.test(formObj.name)) {
                    iserror = true;
                    this.errorMsg = '设备ID包含特殊字符！';
                }
                return iserror;
            },
        },
        mounted() {
            _this.search();
            _this.fetchFloorNo();
        }
    }
</script>

<style scoped>

</style>