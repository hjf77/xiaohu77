<!--
模块名称：
开发人员：乔上飞
创建时间:2021/4/2 15:21
-->


<template>
  <div>
    <!-- 添加或修改参数配置对话框 -->
    <pagex-form
      addApi="/basic/ms/dictItem/"
      updateApi="/basic/ms/dictItem/"
      :init="init"
      :data="formData"
      :isEdit="isEdit"
      :controls="[
              {
                type: 'text',
                name: 'dictCode',
                label: '字典code',
                rule:'required',
                placeholder: '请输入字典code'
              },
              {
                type: 'text',
                name: 'dictDesc',
                label: '字典翻译',
                rule:'required',
                placeholder: '请输入字典翻译'
              },
              {
                type: 'inputNumber',
                name: 'orderNum',
                label: '排序序号',
                rule:'required',
                options:{max:100,min:0},
                placeholder:'请输入排序序号'
              },
              {
                type: 'textarea',
                name: 'ext',
                label: '扩展信息',
              }
            ]"
    >
    </pagex-form>
  </div>
</template>

<script>
export default {
  name: "dictItemForm",
  data() {
    return {
      formData:{
        dictGroupCode:'',
        orderNum:0,
        dictId: this.init.dictId
      }
    }
  },
  props: {
    open: Boolean,
    init: Object,
    isEdit: Boolean,
    dictGroupCode: String
  },
  mounted() {
    console.log(this.isEdit)
  },
  created() {
    this.formData.dictGroupCode = this.dictGroupCode;
  },
  methods: {}
}
</script>
