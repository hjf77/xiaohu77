<!--
模块名称：规则修改
开发人员：彭梦倩
创建时间:2022/11/29 10:51
-->

<template>
    <div>
      <!-- 添加或修改参数配置对话框 -->
      <pagex-form
        ref="form"
        addApi="/basic/ms/task-assign-rule/create"
        updateApi="/basic/ms/task-assign-rule/update"
        :init="init"
        :isEdit="isEdit"
        :controls="controls"
        :data="formData"
        :onSubmit="onSubmit"
      >
      </pagex-form>
    </div>
  </template>
  
  <script>
  import { deepClone } from '../../../../utils';
  export default {
    name: "ruleForm",
    props: {
      open: Boolean,
      editInit: Object,
      isEdit: Boolean,
    },
    data() {
      return {
        formData:{
            id: this.editInit.id,
            modelId: this.editInit.modelId,
            taskDefinitionKey: this.editInit.taskDefinitionKey,
            // options: ,
        },
        init:{},
        controls: [
          {
            type: 'text',
            name: "taskDefinitionName",
            label: '任务名称',
            disabledOn: "disabled",
            isValueNum: true,
            placeholder: '请输入任务名称'
          },
          {
            type: 'text',
            name: 'taskDefinitionKey',
            label: '任务标识',
            disabledOn: "disabled",
            placeholder: '请输入流程名称'
          },
          {
            type: 'select',
            name: 'type',
            label: '规则类型',
            rule:'required',
            isValueNum: true,
            placeholder: '请选择规则类型',
            dictCode: 'bpm_task_assign_rule_type',
            change: (_row, val) => {
              if (val) {
                if (val==10) {
                  this.$refs.form.setModelProp('optionsUser',[])
                  this.controls[3].isHide = false;
                  this.controls[4].isHide = true;
                }else{
                  this.$refs.form.setModelProp('optionsRole',[])
                  this.controls[3].isHide = true;
                  this.controls[4].isHide = false;
                }
              }
            }
          },
          {
              type: "select",
              name: "optionsRole",
              label: "指定角色",
              rule:'required',
              url: "/basic/ms/sysRole/findList",
              isHide: true,
              multiple: true,
              isValueNum: true,
              labelField: "roleName",
              valueField: "roleId"
          },
          {
              type: "select",
              name: "optionsUser",
              label: "指定用户",
              rule:'required',
              isValueNum: true,
              url: "/basic/ms/sysUser/findList",
              isHide: true,
              multiple: true,
              labelField: "userName",
              valueField: "userId"
          },
        ],
      }
    },
    mounted() {
    },
    created() {
      this.init = deepClone(this.editInit);
      if(this.init.type == 10){
        this.$set(this.init,"optionsRole", this.init.options);
        this.$set(this.init,"optionsUser", []);
      }else{
        this.$set(this.init,"optionsRole", []);
        this.$set(this.init,"optionsUser", this.init.options);
      }
    },
    methods: {
      onSubmit(_model){
          if(_model.type === 10){
            _model.options = _model.optionsRole;
          }else{
            _model.options = _model.optionsUser;
          }
          return true;
      }
    }
  }
  </script>
  