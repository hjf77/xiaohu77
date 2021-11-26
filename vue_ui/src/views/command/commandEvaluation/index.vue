

<!--
  模块名称：辅助指挥
  开发人员：金丹雅
  创建时间:2021/9/17 09:30
-->
<template>
  <base-container>
    <pagex-crud
      ref="crud"
      :filters="filters"
      :columns="columns"
      :api="api"
      :sortSett="sortSett"
      :buttons="buttons"
      :querys="querys"
      :isThisOrg="true"
    >
      <template v-if="open" v-slot:form="prop">
        <!-- 新增 修改 弹框-->
        <el-dialog class="pagex-dialog-theme" slot="form" :title="title" :visible.sync="open">
          <!-- <dataUpload v-if="title == '辅助材料'" @closeDialog="closeDialog"></dataUpload>
          <schedulingOfResources v-if="title == '资源调度'" @closeDialog="closeDialog"></schedulingOfResources> -->
        </el-dialog>
      </template>
    </pagex-crud>
  </base-container>
</template>

<script>
import crudMixins from "../../../mixins/crudMixins";
import { deepClone } from "../../../lib/utils";
// import dataUpload from "./components/dataUpload";
export default {
  name: "commandAuxiliary",
  components: {
    // dataUpload
  },
  mixins:[crudMixins],
  data() {
    return {
      api: '/ms/commandAuxiliary/selectAuxiliaryListPage',
      title:'',
      open:false,
      sortSett:[{
        direction: "DESC",
        property: "updateTime"
      }],
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [],
      columns: [
        { label: "序号", name: "", type: "index",width: "80"},
        {label: '信息标题', name: 'name', type: "popover"},
        {label: '事发时间', name: 'incidentTime', type: "popover"},
        {label: '事件发生地点', name: 'eventTypeName', type: 'popover'},
        {label: '上报时间', name: 'reportTime', type: "popover"},
        {label: '经办人', name: 'handler', type: "popover"},
        {label: '现场联络员', name: 'liaison', type: "popover"},
        {label: '责任单位', name: 'parentCompanyIdName', type: "popover"},
        {label: '状态', name: 'eventLevelName', type: 'popover',width: "90"},
        {
          label: "操作",
          name: "operation",
          type: "textBtn",
          width: "380",
          textBtn: [
            {
              name: "总结",
              type: "success",
              size: "mini",
              // permission: ["preparatoryCase:summary"],
              click: (_row) => {
                this.title = "总结"
                this.open=true
                // this.$pagexRequest({
                //   url: "/ms/preparatoryCase/info/" + _row.id,
                //   method: "get",
                // }).then((res) => {
                //   this.$set(this, "init", res);
                //   // 仅仅为了改变数组preparatoryCaseAccessorys的顺序
                //   this.$set(this.init, "preparatoryCaseAccessorys", this.init.preparatoryCaseAccessorys.reverse());
                //   this.title = "详情";
                //   this.open = true;
                // });
              },
            },
            {
              name: "详情",
              type: "success",
              size: "mini",
              click: (_row) => {
                this.$pagexRequest({
                  url: "/ms/preparatoryCase/info/" + _row.id,
                  method: "get",
                }).then((res) => {
                  this.$set(this, "init", res);
                  this.title = "详情";
                  this.open = true;
                });
              },
            },
          ]
        }
      ],
      filters: [
        {
          formname: "事发时间:",
          name: "resourceType",
          type: "date-picker",
          operation: "between",
        },
        {
          formname: "状态:",
          name: "resourceType",
          type: "select",
          dictCode: "event_type",
          operation: "=",
        },
        {formname: '信息标题', name: 'resourceName', placeholder: "信息标题", type: 'text',operation: "like"},
      ],
      querys: [],
    }
  },
  created() {},
  methods: {
    closeDialog(){
      this.$set(this,"open",false)
    }
  }
};
</script>
<style lang="scss" scoped>
</style>
