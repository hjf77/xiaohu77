<!--
模块名称：
开发人员：乔上飞
创建时间:2021/4/2 15:21
-->


<template>
  <div>
    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      123123123
      <!--<el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分组名称" prop="groupName">
          <el-input v-model="form.groupName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="分组编码" prop="wordbookGroupCode">
          <el-input v-model="form.wordbookGroupCode" placeholder="请输入字典类型" />
        </el-form-item>
      </el-form>-->

    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "addDict",
  data() {
    return {}
  },
  props:{
    open:Boolean
  },
  mounted() {
  },
  created() {
  },
  methods: {}
}
</script>

<style scoped lang="scss">

</style>
