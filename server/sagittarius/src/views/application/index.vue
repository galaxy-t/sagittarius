<template>
    <div>
        <commonHeader/>

        <el-container>

            <el-main>

                <el-button icon="el-icon-plus" type="success" @click="createApplication">
                    创建项目
                </el-button>

                <el-button type="success" plain v-for="item in applicationData" :key="item.id" @click="applicationDetail(item.id)">
                    {{item.name}}
                </el-button>

            </el-main>

        </el-container>




    </div>
</template>

<script>
    import commonHeader from '@/components/common-header.vue'

    export default {
        name: 'application-index',
        components: {
            commonHeader
        },
        data() {
            return {
                applicationData: []
            };
        },
        methods : {

            loadApplicationList() {

                this.axios({
                    method:'get',
                    url:'http://localhost:8081/application/list?userId=1'
                }).then((res) => {

                    if (res.data.code == 0) {

                        this.applicationData = res.data.data;
                    }
                });
            },
            createApplication() {
                this.$router.push("/application/add");
            },
            applicationDetail(applicationId) {

                this.$router.push({path:'/application/detail',query:{applicationId:applicationId}});
            }
        },
        created() {
            this.loadApplicationList();
        }
    }
</script>


<style scoped>
    .el-button {
        margin-top: 20px;
        margin-left: 20px;
    }
    .el-main {
        float: left;
    }
</style>