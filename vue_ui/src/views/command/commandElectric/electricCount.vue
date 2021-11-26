<!--
  模块名称：保电统计
  开发人员：宫燕子
  创建时间: 2021-09-15
-->
<template>
  <base-container>
    <pagex-crud
      ref="pagex-crud"
      :filters="filters"
      :columns="columns"
      :api="api"
      :sortSett="sortSett"
      :buttons="buttons"
      :querys="querys"
    >
    </pagex-crud>
  </base-container>
</template>

<script>
import crudMixins from "@/mixins/crudMixins";
import { mapGetters } from "vuex";
import { getFile } from "@/utils/download";
export default {
  name: "questionManage",
  mixins: [crudMixins],
  data() {
    return {
      rightContent: false, //右侧内容区域
      api: "/ms/commandElectric/pagerAdvance",
      isEdit: false,
      open: false,
      isDetail: false,
      isSum: false,
      // 列表排列顺序（更新时间）
      sortSett: [
        {
          direction: "DESC",
          property: "updateTime",
        },
      ],
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [
        {
          title: "批量导出",
          name: "add",
          type: "primary",
          size: "mini",
          icon: "el-icon-upload2",
          // permission: ["trainQuestionBank:batchExport"],
          click: (_row) => {
            const _url = `/ms/commandElectric/advanceExportExcel?fileName=重要保电.xlsx`;
            getFile(_url);
          },
        },
        {
          title: "返回",
          type: "warning",
          size: "mini",
          isRight: true,
          click: () => {
            this.$router.go(-1);
          },
        },
      ],
      columns: [
        { label: "", name: "", type: "selection" },
        { label: "序号", name: "", type: "index", width: "100" },
        { label: "单位", name: "companyName", type: "popover" },
        { label: "保电专业人数", name: "electricGradeName", type: "popover" },
        { label: "保电安保人员", name: "securityUserCount", type: "popover" },
        { label: "保电车辆", name: "vehicleCount", type: "popover" },
        { label: "保电一级变电站", name: "substation1", type: "popover" },
        { label: "保电二级变电站", name: "substation2", type: "popover" },
        { label: "保电一级线路", name: "line1", type: "popover" },
        { label: "保电二级线路", name: "line2", type: "popover" },
        { label: "保电杆塔数（基）", name: "towerCount", type: "popover" },
        { label: "保电总里程数（公里）", name: "kilometreCount", type: "popover" },
        { label: "保电巡视总里程数（公里）", name: "patrolKilometersCount", type: "popover" },
        { label: "值守工作点帐篷（顶）", name: "tentCount", type: "popover" },
        { label: "保电期间发现输电线路通道安全隐患数量（处）", name: "securityCount", type: "popover" },
      ],
      filters: [
        {
          name: "name",
          label: "执行单位",
          type: "text",
          operation: "like",
        },
        {
          formname: "任务执行开始时间:",
          name: "startTime",
          type: "date-picker",
          operation: "between",
        },
      ],
      querys: [],
    };
  },
  created() {},
  computed: {
    ...mapGetters(["user"]),
  },
  methods: {
    cancelReturn(val) {
      this.open = val;
    },
    refreshList() {
      this.$refs["pagex-crud"].getList();
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .vue-treeselect {
  width: 305px;
}
::v-deep .el-range-editor {
  width: 305px;
}
::v-deep .el-button.el-button--warning {
  margin: 5px 0px 0px 20px !important;
}
::v-deep .el-dialog__body {
  background-color: #f7f7f7;
  padding: 15px;
}
</style>
