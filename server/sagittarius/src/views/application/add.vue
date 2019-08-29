<template>
    <div>
        <commonHeader/>

        <el-row style="margin-top: 20px;">

            <el-col :span="8" :offset="8">
                <el-form ref="form" :model="form" label-position="left" label-width="80px">
                    <el-form-item label="应用名称">
                        <el-input v-model="form.name" style="width: 300px;"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="saveApplication">立即创建</el-button>
                    </el-form-item>
                </el-form>
            </el-col>

        </el-row>

    </div>
</template>

<script>
    import commonHeader from '@/components/common-header.vue'

    export default {
        name: 'application-add',
        components: {
            commonHeader
        },
        data() {

            return {
                form : {
                    name : ''
                }
            }

        },
        methods: {
            saveApplication() {


                this.axios.put('http://localhost:8081/application',this.form).then((res) => {
                    if (res.data.code == 0) {
                        this.$router.push("/");
                    } else {

                    }
                });

            }
        },
        created() {
        }

    }
</script>