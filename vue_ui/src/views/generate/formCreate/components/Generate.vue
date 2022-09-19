<!--
模块名称：
开发人员：乔上飞
创建时间:2021/4/2 15:21
-->


<template>
  <div>
    <!-- 添加或修改参数配置对话框 -->
    <pagex-form
      ref="genForm"
      :init="init"
      :isHaveCancelBtn="false"
      :isHaveAddBtn="false"
      :data="formData"
      :isHaveInitBtn="false"
      :controls="[
              {
                type: 'select',
                name: 'genType',
                label: '生成页面',
                rule:'required',
                options: [
                    {labelField:'all',valueField:'表单+列表' },
                    {labelField:'form',valueField:'只表单' },
                    {labelField:'list',valueField:'只列表' }
                  ]
              },
              {
                type: 'text',
                name: 'author',
                label: '开发者姓名',
                rule:'required',
              },
              {
                type: 'select',
                name: 'microServiceName',
                dictCode:'microServiceName',
                label: '微服务',
                rule:'required',
              }
            ]"
    >
    </pagex-form>
    <div style="text-align: center"><el-button @click="generate()">生成代码</el-button></div>
  </div>
</template>

<script>
import { postFile } from '@/utils/download'
import { deepClone } from "@/lib/utils";
export default {
  name: "Generate",
  data() {
    return {
      formData:{
        genType:'all',
        author:'谁不写是瓜皮',
        microServiceName: 'basic'
      }
    }
  },
  props: {
    tableSchema: String,//数据库名字
    tableName: String,//表名字
  },
  mounted() {

  },
  created() {
  },
  methods: {
    generate(){
      let _form = this.$refs['genForm'].validate((_model)=>{
        let _form = deepClone(_model);
        _form.tableSchema = this.tableSchema;
        _form.tableName = this.tableName;
        postFile('/basic/ms/generateCode/generate',_form);
      })

    }
  }
}
</script>
