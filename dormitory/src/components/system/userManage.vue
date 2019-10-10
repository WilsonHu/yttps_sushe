<template xmlns:v-on="http://www.w3.org/1999/xhtml" xmlns:v-bind="http://www.w3.org/1999/xhtml">
    <div>
        <el-row>
            <el-col :span="3">
                <el-input v-model="key"
                          placeholder="请输入姓名或账号" clearable
                          auto-complete="off"></el-input>
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
                    prop="account"
                    label="账号">
            </el-table-column>
            <el-table-column
                    align="center"
                    prop="name"
                    label="姓名">
            </el-table-column>
            <el-table-column
                    align="center"
                    prop="floorName"
                    label="楼号">
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
                            @click="handleEdit(scope.$index, scope.row)">编辑
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
        <el-dialog :before-close="handleCloseTitle" title="增加用户" :visible.sync="addDialogVisible" width="60%">
            <el-form :model="form">
                <el-col :span="8">
                    <el-form-item label="账号：" :label-width="formLabelWidth">
                        <el-input v-model="form.account"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="姓名：" :label-width="formLabelWidth">
                        <el-input v-model="form.name"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="密码：" :label-width="formLabelWidth">
                        <el-input v-model="form.password"></el-input>
                    </el-form-item>
                </el-col>

                <el-col :span="8">
                    <el-form-item label="宿管电话：" :label-width="formLabelWidth">
                        <el-input v-model="form.phone"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="楼栋编号：" :label-width="formLabelWidth">
                        <el-input v-model="form.floorNo" placeholder="例:34"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="楼栋名称：" :label-width="formLabelWidth">
                        <el-input v-model="form.floorName" placeholder="例:东34号"></el-input>
                    </el-form-item>
                </el-col>
            </el-form>
            <el-alert v-if="isError" style="margin-top: 10px;padding: 5px;"
                      :title="errorMsg"
                      type="error"
                      :closable="false"
                      show-icon>
            </el-alert>
            <div slot="footer" class="dialog-footer" style="margin-bottom: 20px">
                <el-button @click="handleExit" icon="el-icon-close" type="danger">取 消</el-button>
                <el-button type="primary" @click="onAdd" icon="el-icon-check">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="编辑用户" :before-close="handleCloseTitle" :visible.sync="modifyDialogVisible" width="60%">
            <el-form :model="modifyForm">
                <el-row style="margin-top: 10px">
                    <el-col :span="8">
                        <el-form-item label="账号：" :label-width="formLabelWidth">
                            <el-input v-model="modifyForm.account"
                                      :disabled="modifyForm.account == 'admin'"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="姓名：" :label-width="formLabelWidth">
                            <el-input v-model="modifyForm.name"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="密码：" :label-width="formLabelWidth">
                            <el-input v-model="modifyForm.password"></el-input>
                        </el-form-item>
                    </el-col>

                    <el-col :span="8">
                        <el-form-item label="宿管电话：" :label-width="formLabelWidth">
                            <el-input v-model="modifyForm.phone"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="楼栋编号：" :label-width="formLabelWidth">
                            <el-input v-model="modifyForm.floorNo" placeholder="例:34"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="楼栋名称：" :label-width="formLabelWidth">
                            <el-input v-model="modifyForm.floorName" placeholder="例:东34号"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <el-alert v-if="isError" style="margin-top: 20px;padding: 5px;"
                      :title="errorMsg"
                      type="error"
                      :closable="false"
                      show-icon>
            </el-alert>
            <div slot="footer" class="dialog-footer" style="margin-bottom: 20px;margin-top: 20px">
                <el-button @click="onClose" icon="el-icon-close" type="danger">取 消</el-button>
                <el-button type="primary" @click="onEidt" icon="el-icon-check">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="提示" :visible.sync="deleteConfirmVisible" width="30%">
            <span>确认要删除账号为[ <b>{{selectedItem.account}}</b> ]的用户吗？</span>
            <span slot="footer" class="dialog-footer">
	    <el-button @click="deleteConfirmVisible = false" icon="el-icon-close">取 消</el-button>
	    <el-button type="primary" @click="onConfirmDelete" icon="el-icon-check">确 定</el-button>
	  </span>
        </el-dialog>
    </div>
</template>

<script>
    let _this;
    import util from '../../assets/js/util.js'

    export default {
        name: "userManage",
        data() {
            _this = this;
            return {
                isError: false,
                errorMsg: '',
                totalRecords: 0,
                selectedItem: {},
                addDialogVisible: false,
                modifyDialogVisible: false,
                deleteConfirmVisible: false,
                tableData: [],
                //分页
                pageSize: EveryPageNum,//每一页的num
                currentPage: 1,
                startRow: 1,
                fileList: [],

                key: '',
                modifyForm: {
                    id: '',
                    account: "",
                    name: "",
                    password: "",
                    phone: '',
                    floorNo: "",
                    floorName: ''
                },
                form: {
                    id: "",
                    account: "",
                    name: "",
                    password: "",
                    phone: '',
                    floorNo: "",
                    floorName: ''
                },
                formLabelWidth: '100px',
                allRoles: [],
                loadingUI: false,
                userInfo: "",
                managerInfo: {},
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
                    _this.imageUrl = '';
                })
                    .catch(_ => {

                    });
            },
            search() {
                _this.loadingUI = true
                $.ajax({
                    url: HOST + "user/list",
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
                                message: '查询用户信息失败',
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
            juadge(item) {
                if (_this.managerInfo.roleId == item.roleId) {
                    return false;
                } else if (item.roleId > _this.managerInfo.roleId) {
                    return false;
                } else {
                    return true;
                }
            },
            handleAdd() {
                _this.isError = false;
                _this.errorMsg = '';
                _this.addDialogVisible = true;
            },
            handleEdit(index, item) {
                _this.isError = false;
                _this.errorMsg = "";
                _this.selectedItem = item;
                _this.modifyForm.id = item.id;
                _this.modifyForm.account = item.account;
                _this.modifyForm.name = item.name;
                _this.modifyForm.password = item.password;
                _this.modifyForm.phone = item.phone;
                _this.modifyForm.floorNo = item.floorNo;
                _this.modifyForm.floorName = item.floorName;
                _this.isError = _this.validateForm(_this.modifyForm, true);
                _this.modifyDialogVisible = true;
            },
            handleDelete(index, item) {
                _this.selectedItem = copyObject(item);
                if (_this.selectedItem) {
                    _this.deleteConfirmVisible = true;
                }
            },
            onConfirmDelete() {
                _this.deleteConfirmVisible = false;
                $.ajax({
                    url: HOST + 'user/delete',
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
                                message: '删除用户成功',
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
            handleCurrentChange(val) {
                _this.currentPage = val;
                _this.search();
            },
            handleExit() {
                _this.form = {}
                _this.addDialogVisible = false;
            },
            validateForm(formObj, isEdit) {
                var pat = /[`~!@#$%^&*()_\-+=<>?:"{}|,\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/im;

                var iserror = false;
                if (!iserror && formObj.account == null || formObj.account == "") {
                    iserror = true;
                    this.errorMsg = '账号不能为空';
                } else if (pat.test(formObj.account)) {
                    iserror = true;
                    this.errorMsg = '账号包含特殊字符！';
                } else if (!iserror && formObj.name == null || formObj.name == "") {
                    iserror = true;
                    this.errorMsg = '姓名不能为空';
                } else if (pat.test(formObj.name)) {
                    iserror = true;
                    this.errorMsg = '姓名包含特殊字符！';
                } else if (!iserror && formObj.floorNo == "" || formObj.floorNo == null) {
                    iserror = true;
                    this.errorMsg = '请输入楼栋编号';
                } else if (!iserror && formObj.floorName == "" || formObj.floorName == null) {
                    iserror = true;
                    this.errorMsg = '请输入楼栋名称';
                }
                return iserror;
            },
            onAdd() {
                this.isError = _this.validateForm(this.form, false)
                $.ajax({
                    url: HOST + "user/add",
                    type: 'post',
                    dataType: 'json',
                    data: {
                        jsonData: JSON.stringify(_this.form)
                    },
                    success: function (data) {
                        if (data.code == 200) {
                            _this.$notify({
                                title: '提示',
                                message: '用户添加成功',
                                type: 'success'
                            });
                            _this.addDialogVisible = false
                            _this.from = {}
                            _this.search();
                        } else {
                            _this.$notify({
                                title: '提示',
                                message: '用户添加失败',
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
            },

            onClose() {
                _this.modifyDialogVisible = false;
                _this.modifyForm.password = ''
            },
            onEidt() {
                this.isError = this.validateForm(_this.modifyForm, true);
                if (!_this.isError) {
                    $.ajax({
                        url: HOST + "user/update",
                        type: 'post',
                        dataType: 'json',
                        data: {
                            jsonData: JSON.stringify(_this.modifyForm)
                        },
                        success: function (data) {
                            if (data.code == 200) {
                                _this.modifyDialogVisible = false;
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
            onkeydown: function (e) {
                var ev = document.all ? window.event : e;
                if (ev.keyCode == 13) {//enter key
                    _this.search();
                }
            },
        },
        mounted() {
            _this.File = HOST + "user/upload";
            window.addEventListener('keydown', _this.onkeydown);
            _this.managerInfo = JSON.parse(sessionStorage.getItem('user'));
            if (_this.managerInfo.roleId == 4) {
                _this.key = _this.managerInfo.account;
            }
            _this.search();
        },
        created: function () {
            this.userInfo = JSON.parse(sessionStorage.getItem("user"));
            // this.initAllRoles();
        },
        destroyed() {
            window.removeEventListener('keydown', _this.onkeydown);
        }
    }
</script>

<style>
    .upload-demo input {
        display: none;
    }
</style>