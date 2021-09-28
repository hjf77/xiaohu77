<!--
  模块名称：重要保电
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
      <template v-if="open" v-slot:form="prop">
        <!-- 新增 修改 详情 弹框-->
        <el-dialog
          class="pagex-dialog-theme"
          slot="form"
          :title="title"
          :visible.sync="open"
          v-if="open"
        >
          <BuilImport
            :formData="formData"
            v-if="title == '批量导入'"
            excelTemplateName="commandElectric"
            excelImportApi="/ms/commandElectric/pubImportExcel"
          ></BuilImport>
          <electricForm
            v-if="title == '新增' || title == '编辑'"
            :init="init"
            :isEdit="isEdit"
            :formStatus="dialogStatus"
          ></electricForm>
          <electricDetail
            v-if="title == '详情' || title == '总结'"
            :init="init"
            :isDetail="isDetail"
            :isEdit="isEdit"
            :isSum="isSum"
            :sumList="sumList"
          ></electricDetail>
        </el-dialog>
      </template>
    </pagex-crud>
  </base-container>
</template>

<script>
import crudMixins from "@/mixins/crudMixins";
import { mapGetters } from "vuex";
import { getFile } from "@/utils/download";
import BuilImport from "@/components/BuilImport";
import electricForm from "./components/electricForm.vue";
import electricDetail from "./components/electricDetail.vue";
// import { deepClone } from "../../../../lib/utils";
export default {
  name: "commandElectric",
  components: {
    BuilImport,
    electricForm,
    electricDetail,
  },
  mixins: [crudMixins],
  data() {
    return {
      sumList: {},
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
          title: "新增",
          name: "add",
          type: "primary",
          size: "mini",
          icon: "el-icon-plus",
          permission: ["trainQuestionBank:add"],
          click: () => {
            this.title = "新增";
            this.$set(this, "init", {});
            this.open = true;
            this.isEdit = false;
            this.isSum = false;
            this.isDetail = false;
          },
        },
        {
          title: "批量导入",
          name: "add",
          type: "primary",
          size: "mini",
          icon: "el-icon-download",
          // permission: ["trainQuestionBank:bulkImport"],
          click: () => {
            this.title = "批量导入";
            this.isEdit = false;
            this.open = true;
          },
        },
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
          title: "保电统计",
          type: "warning",
          size: "mini",
          isRight: true,
          click: () => {
            this.$router.push({ path: "/commandElectric/electric/" });
          },
        },
      ],
      columns: [
        { label: "", name: "", type: "selection" },
        { label: "序号", name: "", type: "index", width: "100" },
        { label: "保电任务名称", name: "name", type: "popover" },
        { label: "保电等级", name: "electricGradeName", type: "popover" },
        { label: "任务执行开始时间", name: "startTime", type: "popover" },
        { label: "任务执行结束时间", name: "endTime", type: "popover" },
        { label: "是否总结", name: "isSummaryName", type: "popover" },
        {
          label: "操作",
          name: "operation",
          type: "textBtn",
          width: "500",
          textBtn: [
            {
              name: "总结",
              type: "primary",
              size: "mini",
              click: (_row) => {
                this.title = "总结";
                this.$set(this, "init", _row);
                this.open = true;
                this.isEdit = false;
                this.isSum = true;
                this.isDetail = false;
              },
              ifFun: (_row) => {
                return _row.isSummary == 0;
              },
            },
            {
              name: "编辑",
              type: "primary",
              size: "mini",
              click: (_row) => {
                this.$set(this, "init", _row);
                this.title = "编辑";
                this.isEdit = true;
                this.open = true;
                this.isDetail = false;
                this.isSum = false;
              },
            },
            {
              name: "详情",
              type: "success",
              size: "mini",
              click: (_row) => {
                this.$set(this, "init", _row);
                this.$pagexRequest({
                  url: "/ms/commandElectricSummary/findByElectricId/" + _row.id,
                  method: "GET",
                })
                  .then((res) => {
                    this.sumList = res.data;
                    this.title = "详情";
                    this.open = true;
                    this.isDetail = true;
                    this.isEdit = false;
                    this.isSum = false;
                  })
                  .catch((res) => {
                    console.log(res);
                  });
              },
            },
            {
              name: "删除",
              type: "danger",
              size: "mini",
              permission: ["watchSchedulingLog:del"],
              click: (_row) => {
                this.$confirm("是否删除此数据?", "提示", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning",
                })
                  .then(async () => {
                    const result = await this.$pagexRequest({
                      url: "/ms/trainQuestionBank/" + _row.id,
                      method: "DELETE",
                    });
                    if (result.code === 200) {
                      this.$message({
                        type: "success",
                        message: "删除成功!",
                      });
                      this.refreshList();
                    } else {
                      this.$message({
                        type: "error",
                        message: result.message,
                      });
                    }
                  })
                  .catch(() => {});
              },
            },
          ],
        },
      ],
      filters: [
        {
          formname: "保电等级:",
          name: "electricGrade",
          type: "select",
          dictCode: "electric_grade",
          operation: "=",
        },
        {
          formname: "任务执行开始时间:",
          name: "startTime",
          type: "date-picker",
          operation: "between",
        },
        {
          name: "name",
          label: "保电任务名称",
          type: "text",
          operation: "like",
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
