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
      updateApi=""
      initApi=""
      :init="init"
      :isEdit="false"
      :controls="[
              {
                type: 'text',
                name: 'wordbookGroupCode',
                label: '字典编码',
                placeholder: '请输入字典编码'
              },{
                type: 'text',
                name: 'wordbookCode',
                label: '字典code',
                rule:'required',
                placeholder: '请输入字典code'
              },
              {
                type: 'text',
                name: 'wordbookDesc',
                label: '字典翻译',
                rule:'required',
                placeholder: '请输入字典翻译'
              },{
                type: 'text',
                name: 'wordbookDescEN',
                label: '翻译英文',
                placeholder: '请输入翻译英文'
              },
              {
                type: 'text',
                name: 'wordbookDescTW',
                label: '翻译繁体',
                placeholder: '请输入翻译繁体'
              },
              {
                type: 'inputNumber',
                name: 'orderNum',
                label: '排序序号(ASC)',
                rule:'required',
                size:'medium',
                placeholder:'请输入排序序号(ASC)',
                options:{label:'衣服',min:0, max:100}
              },
            ]"
    >
    </pagex-form>
  </div>
</template>

<script>
// import pagexForm from "@/lib/components/form";
export default {
  name: "addDict",
  components: {
    // pagexForm
  },
  data() {
    return {}
  },
  props: {
    open: Boolean,
    init: Object,
    isEdit: Boolean
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
  width: 117px !important;
}
::v-deep .el-form-item__content{
  margin-left: 117px !important;
}
</style>
