

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
          <dataUpload v-if="title == '辅助材料'" @closeDialog="closeDialog"></dataUpload>
          <schedulingOfResources v-if="title == '资源调度'" @closeDialog="closeDialog"></schedulingOfResources>
          <detailAuxiliary v-if="title == '详情'" @closeDialog="closeDialog"></detailAuxiliary>
        </el-dialog>
      </template>
    </pagex-crud>
  </base-container>
</template>

<script>
import crudMixins from "../../../mixins/crudMixins";
import { deepClone } from "../../../lib/utils";
import dataUpload from "./components/dataUpload";
import schedulingOfResources from "./components/schedulingOfResources";
import detailAuxiliary from './components/detailAuxiliary.vue';
export default {
  name: "commandAuxiliary",
  components: {
    dataUpload,
    schedulingOfResources,
    detailAuxiliary
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
        {label: '事发类型', name: 'eventTypeName', type: 'popover'},
        {label: '事发时间', name: 'incidentTime', type: "popover"},
        {label: '上报时间', name: 'reportTime', type: "popover"},
        {label: '事件级别', name: 'eventLevelName', type: 'popover',width: "90"},
        {label: '报送单位', name: 'parentCompanyIdName', type: "popover"},
        {label: '经办人', name: 'handler', type: "popover"},
        {label: '现场联络员', name: 'liaison', type: "popover"},
        {
          label: "操作",
          name: "operation",
          type: "textBtn",
          width: "380",
          textBtn: [
            {
              name: "辅助材料",
              type: "success",
              size: "mini",
              // permission: ["preparatoryCase:dataUpload"],
              click: (_row) => {
                this.title = "辅助材料"
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
              name: "资源调度",
              type: "warning",
              size: "mini",
              // permission: ["preparatoryCase:schedulingOfResources"],
              click: (_row) => {
                this.title = "资源调度"
                this.open=true
                // this.$pagexRequest({
                //   url: "/ms/preparatoryCase/info/" + _row.id,
                //   method: "get",
                // }).then((res) => {
                //   this.$set(this, "init", res);
                //   // 仅仅为了改变数组preparatoryCaseAccessorys的顺序
                //   this.$set(this.init, "preparatoryCaseAccessorys", this.init.preparatoryCaseAccessorys.reverse());
                //   this.title = "编辑";
                //   this.isEdit = true;
                //   this.open = true;
                // });
              },
            },
            {
              name: "响应结束",
              type: "warning",
              size: "mini",
              // permission: ["preparatoryCase:endResponse"],
              click: (_row) => {
                // this.$pagexRequest({
                //   url: "/ms/preparatoryCase/info/" + _row.id,
                //   method: "get",
                // }).then((res) => {
                //   this.$set(this, "init", res);
                //   // 仅仅为了改变数组preparatoryCaseAccessorys的顺序
                //   this.$set(this.init, "preparatoryCaseAccessorys", this.init.preparatoryCaseAccessorys.reverse());
                //   this.title = "编辑";
                //   this.isEdit = true;
                //   this.open = true;
                // });
              },
            },
            {
              name: "详情",
              type: "success",
              size: "mini",
              click: (_row) => {
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
                this.$set(this, "init", _row);
                this.open = true;
                this.title = "详情";
              },
            },
          ]
        }
      ],
      filters: [
        {formname: '信息标题', name: 'resourceName', placeholder: "信息标题", type: 'text',operation: "like"},
        {
          formname: "事发类型:",
          name: "resourceType",
          type: "select",
          dictCode: "event_type",
          operation: "=",
        },
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
