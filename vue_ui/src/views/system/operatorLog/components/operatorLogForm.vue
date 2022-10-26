<!--
模块名称：
开发人员：乔上飞
创建时间:2021/4/2 15:21
-->


<template>
  <div>
    <!-- 添加或修改参数配置对话框 -->
    <pagex-form
      :init="init"
      :isEdit="true"
      :isHaveAddBtn="false"
      :isHaveInitBtn="false"
      :isHaveCancelBtn="false"
      :controls="[
              {
                type: 'text',
                name: 'namespace',
                label: '命名空间'
              },
              {
                type: 'text',
                name: 'url',
                label: 'url'
              },
               {
                type: 'text',
                name: 'pkeyStr',
                label: '主键'
              },
              {
                type: 'text',
                name: 'ip',
                label: 'ip'
              },
              {
                type: 'text',
                name: 'reqMethod',
                label: '请求方法'
              },
              {
                type: 'textarea',
                name: 'reqParam',
                label: '请求参数',
                width:'1400'
              },
              {
                type: 'textarea',
                name: 'reqParamSource',
                label: '原始请求参数',
                width:'1400'
              },
              {
                type: 'textarea',
                name: 'sysLog',
                label: '系统日志',
                width:'1400',
                rows:18
              },

            ]"
    >
    </pagex-form>
    <div style="padding-bottom: 30px;text-align: center">
      <el-button type="primary" size="small"
                 v-clipboard:copy="init.reqParamSource"
                 v-clipboard:success="onCopy"
      >
        复制请求参数
      </el-button>
      <el-button @click="close" size="small" >
        关闭
      </el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "operatorLogForm",
  data() {
    return {
      formData:{
      }
    }
  },
  props: {
    init: Object
  },
  mounted() {
  },
  created() {

  },
  methods: {
    onCopy(){
      this.msgSuccess("请求参数已经复制");
    },
    close(){
      this.$emit('update:visible', false);
    }
  }
}
</script>
