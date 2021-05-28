<!--
模块名称：
开发人员：乔上飞
创建时间:2021/4/2 15:21
-->


<template>
  <div>
    <!-- 添加或修改参数配置对话框 -->
    <pagex-form
      addApi="/ms/wordbook/addWordbookGroup"
      updateApi="/ms/wordbook/updateWordbookGroup"
      initApi="/ms/wordbook/getWordbookGroupBean?groupId="
      :init="init"
      :controls="[
              {
                type: 'text',
                name: 'groupName',
                label: '分组名称',
                rule:'required',
                placeholder:'请输入分组名称'
              },{
                type: 'text',
                name: 'wordbookGroupCode',
                label: '分组编码',
                rule:'required',
                placeholder: '请输入分组编码'
              },{
                type: 'uploadCard',
                name: 'uploadCard',
                label: '上传文件',
                rule:'required',
              }
            ]"
    >
    </pagex-form>
  </div>
</template>

<script>
import pagexForm from "@/lib/components/form";
export default {
  name: "addDict",
  components:{
    pagexForm
  },
  data() {
    return {

    }
  },
  props: {
    open: Boolean,
    init:Object
  },
  mounted() {
  },
  created() {
  },
  methods: {}
}
</script>

<style scoped lang="scss">
::v-deep .el-form--label-left .el-form-item__label{
  text-align: right;
}
</style>
