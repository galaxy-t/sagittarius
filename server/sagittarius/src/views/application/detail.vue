<template>
    <div>
        <commonHeader/>

        <el-container class="container">


            <el-aside width="200px">

                <el-menu class="left_navmenu" @select="loadStar" default-active="addNamespace">


                    <el-menu-item index="addNamespace">
                        <i class="el-icon-plus"></i>
                        <span slot="title">新增命名空间</span>
                    </el-menu-item>

                    <el-menu-item v-for="item in namespaceData" :key="item.id" :index="'' + item.id">
                        <i class="el-icon-document"></i>
                        <span slot="title">{{item.name}}</span>
                    </el-menu-item>

                </el-menu>

            </el-aside>

            <el-main>

                <div id="starList" :class="{star_list_hide:isHide}">

                    <template>
                        <el-button @click="starAddVisible = true" type="primary" icon="el-icon-circle-plus-outline" >新增</el-button>
                        <el-button @click="save" type="primary" icon="el-icon-circle-check">保存</el-button>
                        <el-table :data="starData" stripe height="500" border style="width: 100%;margin-top: 5px;">
                            <el-table-column prop="starKey" label="KEY" style="width: 50%;"> </el-table-column>
                            <el-table-column prop="starValue" label="VALUE" style="width: 50%;"> </el-table-column>
                            <el-table-column label="操作">
                                <template slot-scope="scope">
                                    <el-button @click="starEditShow(scope.$index, scope.row)" size="mini" >编辑</el-button>
                                    <el-button @click="starRemove(scope.$index, scope.row)" size="mini" type="danger">删除</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </template>

                </div>

            </el-main>

        </el-container>


        <!-- 新增 Namespace 开始 -->

        <el-dialog title="新增 Namespace" :visible.sync="namespaceAddVisible">
            <el-form :model="namespaceAdd">
                <el-form-item label="KEY" label-width="120px">
                    <el-input v-model="namespaceAdd.name" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="namespaceAddVisible = false">取 消</el-button>
                <el-button @click="addNamespace" type="primary">确 定</el-button>
            </div>
        </el-dialog>

        <!-- 新增 Namespace 结束 -->


        <!-- 新增 Star 开始 -->

        <el-dialog title="新增配置" :visible.sync="starAddVisible">
            <el-form :model="starAddParams">
                <el-form-item label="KEY" label-width="120px">
                    <el-input v-model="starAddParams.starKey" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="VALUE" label-width="120px">
                    <el-input v-model="starAddParams.starValue" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="starAddVisible = false">取 消</el-button>
                <el-button @click="starAdd" type="primary">确 定</el-button>
            </div>
        </el-dialog>

        <!-- 新增 Star 结束 -->


        <!-- 修改 Star 开始 -->

        <el-dialog title="修改配置" :visible.sync="starEditVisible">
            <el-form :model="starEditParams">
                <el-form-item label="KEY" label-width="120px">
                    <el-input v-model="starEditParams.starKey" disabled autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="VALUE" label-width="120px">
                    <el-input v-model="starEditParams.starValue" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="starEditVisible = false">取 消</el-button>
                <el-button @click="starEdit" type="primary" >确 定</el-button>
            </div>
        </el-dialog>

        <!-- 修改 Star 结束 -->


    </div>
</template>

<script>
    import commonHeader from '@/components/common-header.vue'

    export default {
        name: 'application-detail',
        components: {
            commonHeader
        },
        data() {

            return {
                applicationId : '',
                namespaceId : '',
                namespaceData : [],
                isHide : true,
                namespaceAdd : {
                    applicationId : '',
                    name : ''

                },
                namespaceAddVisible : false,
                starData : [],
                starAddVisible : false,
                starAddParams : {
                    starKey : '',
                    starValue : ''
                },
                starEditVisible : false,
                starEditParams : {
                    starKey : '',
                    starValue : '',
                    row : {}
                }
            }

        },
        methods: {

            save() {

                var params = {
                    namespaceId: this.namespaceId,
                    stars: this.starData,
                };

                this.axios.put("http://localhost:8081/star", params).then((res) => {


                    console.log(res);

                    if (res.data.code == 0) {

                        this.$message({
                            showClose: true,
                            message: '保存成功!',
                            type: 'success'
                        });

                    }

                });

            },
            starEdit() {

                this.starEditParams.row.starValue = this.starEditParams.starValue;

                this.starEditVisible = false;


            },
            starEditShow(index,row) {

                this.starEditParams.starKey = row.starKey;
                this.starEditParams.starValue = row.starValue;

                this.starEditParams.row = row;

                this.starEditVisible = true;

            },
            starRemove(index,row) {
                this.starData.splice(index, 1);
            },

            starAdd() {

                var addStarTemp = {
                    starKey : this.starAddParams.starKey,
                    starValue : this.starAddParams.starValue
                };
                this.starData.push(addStarTemp);

                console.log(addStarTemp);

                this.starAddParams.starKey = '';
                this.starAddParams.starValue = '';

                this.starAddVisible = false;

            },

            showNamespace(index, indexPath) {
                this.isHide = true;
                this.namespaceAddVisible = true;
            },
            addNamespace() {
                this.namespaceAdd.applicationId = this.applicationId;

                console.log(this.namespaceAdd);

                this.axios.put("http://localhost:8081/namespace", this.namespaceAdd).then((res) => {

                    if (res.data.code == 0) {

                        this.$message({
                            showClose: true,
                            message: '新增成功!',
                            type: 'success'
                        });

                        this.loadNamespaceList();

                        this.namespaceAddVisible = false;
                    } else {

                        this.$message({
                            showClose: true,
                            message: res.data.msg,
                            type: 'error'
                        });

                    }

                });

            },
            loadStar(index, indexPath) {

                if (index == 'addNamespace') {
                    this.showNamespace();
                } else {

                    this.namespaceId = index;

                    this.starData = [];
                    this.isHide = false;

                    this.axios.get("http://localhost:8081/star/list?namespaceId=" + index).then((res) => {

                        if (res.data.code == 0) {
                            this.starData = res.data.data;
                        }

                    });

                }
            },
            loadNamespaceList() {

                this.applicationId = this.$route.query.applicationId;

                this.axios({
                    method: 'get',
                    url: 'http://localhost:8081/namespace/list?applicationId=' + this.applicationId
                }).then((res) => {
                    if (res.data.code == 0) {
                        this.namespaceData = res.data.data;
                    }
                });
            }


        },
        created() {
            this.loadNamespaceList();
        }

    }
</script>


<style>

    .left_navmenu {
        height: 97%;

    }
    .star_list_hide {
        display: none;
    }

</style>