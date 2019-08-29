<template>
    <div>

        <el-row>
            <el-col :span="24">

                <el-menu  :default-active="$route.path" router mode="horizontal" text-color="#8BC1F8" active-text-color="#409EFF">

                    <el-menu-item index="/">首页</el-menu-item>
                    <el-menu-item index="/application/">我的应用</el-menu-item>
                    <el-menu-item index="/application/add">新增应用</el-menu-item>
                </el-menu>
            </el-col>

        </el-row>

    </div>
</template>

<script>

    export default {
        name: 'commonHeader',
        data() {

            return {

            }

        },
        methods: {


        }
    }
</script>

<style scoped>
</style>