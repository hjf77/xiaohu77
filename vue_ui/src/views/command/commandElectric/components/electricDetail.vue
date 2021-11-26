<!--
  模块名称：重要保电详情/总结
  开发人员：宫燕子
  创建时间:2021/9/17
-->

<template>
  <div class="detailStyle">
    <div class="detailUp">
      <pagex-formDetail
        :controls="controlsBasic"
        :data="init"
        :labelWidth="132"
      ></pagex-formDetail>
    </div>
    <div class="detailUp" v-if="isDetail">
      <h4>保电总结</h4>
      <pagex-formDetail
        :controls="controlsRecord"
        :data="sumList"
        :labelWidth="132"
      ></pagex-formDetail>
    </div>
    <div v-if="isSum">
      <pagex-form
        addApi="/ms/commandElectricSummary/saveElectricSummary"
        :data="formData"
        :controls="controls"
      >
        <!-- <el-input
          prop="detail2"
          slot="detail2"
          slot-scope="item"
          v-model="item"
          type="textarea"
        /> -->
      </pagex-form>
    </div>
  </div>
</template>
<script>
import OperationRecords from "../../../../components/OperationRecords";
export default {
  name: "detailExpert",
  props: {
    sumList: Object,
    init: Object,
    isSum: Boolean,
    isEdit: Boolean,
    isDetail: Boolean,
    acceptFormat: String,
    title: String,
  },
  components: {
    OperationRecords,
  },
  inject: ["reloadable"],
  data() {
    return {
      controlsBasic: [
        {
          type: "text",
          name: "name",
          label: "保电任务名称",
          width: "280",
        },
        {
          type: "text",
          name: "electricGradeName",
          label: "保电等级",
          width: "280",
        },
        {
          type: "dates",
          name: "startTime",
          label: "任务执行开始时间",
          formatVal: "yyyy-MM-dd",
          width: "280",
        },
        {
          type: "dates",
          name: "endTime",
          label: "任务执行结束时间",
          formatVal: "yyyy-MM-dd",
          width: "280",
        },
        {
          type: "textarea",
          name: "describes",
          label: "保电任务描述",
          width: "850",
        },
        {
          type: "textarea",
          name: "plan",
          label: "保电任务计划",
          width: "850",
        },
        {
          type: "text",
          name: "importantCount",
          label: "重要保电用户数量",
          width: "280",
        },
        {
          type: "text",
          name: "companyName",
          label: "执行单位",
          width: "280",
        },
        {
          type: "text",
          name: "placeUser",
          label: "保电用户场所",
          width: "850",
        },
        {
          type: "text",
          name: "longitude",
          label: "经度",
          width: "850",
        },
        {
          type: "text",
          name: "latitude",
          label: "纬度",
          width: "850",
        },
        {
          type: "text",
          name: "isCommonName",
          label: "是否共同完成",
          width: "850",
        },
        {
          type: "uploadFileAsync",
          name: "enclosureId",
          label: "附件",
          // minWidth: "850",
          isMultiple: false,
        },
      ],
      controlsRecord: [
        {
          type: "text",
          name: "cxhibitionUsersName",
          label: "参与人员",
          width: "850",
        },
        {
          type: "text",
          name: "majorUserCount",
          label: "保电专业人次",
          width: "280",
        },
        {
          type: "text",
          name: "securityUserCount",
          label: "保电安保人次",
          width: "280",
        },
        {
          type: "text",
          name: "vehicleCount",
          label: "出动车次",
          width: "280",
        },
        {
          type: "text",
          name: "substation1",
          label: "保电一级变电站",
          width: "280",
        },
        {
          type: "text",
          name: "substation2",
          label: "保电二级变电站",
          width: "280",
        },
        {
          type: "text",
          name: "line1",
          label: "保电一级线路",
          width: "280",
        },
        {
          type: "text",
          name: "line2",
          label: "保电二级线路",
          width: "280",
        },
        {
          type: "text",
          name: "towerCount",
          label: "保电杆塔数（基）",
          width: "280",
        },
        {
          type: "text",
          name: "kilometreCount",
          label: "保电总里程数（公里）",
          width: "280",
        },
        {
          type: "text",
          name: "patrolKilometersCount",
          label: "保电巡视总里程数（公里）",
          width: "280",
        },
        {
          type: "text",
          name: "tentCount",
          label: "值守工作点帐篷（顶）",
          width: "280",
        },
        {
          type: "text",
          name: "securityCount",
          label: "保电期间发现输电线路通道安全隐患数量（处）",
          width: "280",
        },
        {
          type: "textarea",
          name: "detail1",
          label: "总结详情",
          width: "850",
        },
        {
          type: "uploadFileAsync",
          name: "detail2",
          label: "附件",
          // minWidth: "850",
          isMultiple: false,
        },
      ],
      formData: {
        isSummary: 1,
        name: this.init.name,
        electricGrade: this.init.electricGrade,
        startTime: this.init.startTime,
        endTate: this.init.endTate,
        electricId: this.init.id,
      },
      controls: [
        {
          type: "select",
          name: "cxhibitionUsers",
          label: "参与人员",
          multiple: "multiple",
          rule: "required",
          url:
            "/ms/commandElectric/selectAllUser/" +
            this.$store.getters.user.organizationId,
          labelField: "userName",
          valueField: "userId",
          placeholder: "请选择",
        },
        {
          type: "text",
          name: "majorUserCount",
          label: "保电专业人次",
          rule:{required:true,numeric:true},
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "securityUserCount",
          label: "保电安保人次",
          rule:{required:true,numeric:true},
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "vehicleCount",
          label: "出动车次",
          rule:{required:true,numeric:true},
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "substation1",
          label: "保电一级变电站",
          rule:{numeric:true},
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "substation2",
          label: "保电二级变电站",
          rule:{numeric:true},
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "line1",
          label: "保电一级线路",
          rule:{numeric:true},
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "line2",
          label: "保电二级线路",
          rule:{numeric:true},
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "towerCount",
          label: "保电杆塔数（基）",
          rule:{numeric:true},
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "kilometreCount",
          label: "保电总里程数（公里）",
          rule:{decimal:2},
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "patrolKilometersCount",
          label: "保电巡视总里程数（公里）",
          rule:{decimal:2},
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "tentCount",
          label: "值守工作点帐篷（顶）",
          rule:{numeric:true},
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "securityCount",
          label: "保电期间发现输电线路通道安全隐患数量（处）",
          rule:{numeric:true},
          placeholder: "请输入",
        },
        {
          type: "textarea",
          name: "detail1",
          label: "总结详述",
          placeholder: "请输入",
          rule: { min: 1, max: 200 },
        },
        {
          type: "uploadFileAsync",
          name: "detail2",
          label: "总结详述附件",
          tipContent: "附件提示文字",
          isDownload: true,
          isDelete: true,
        },
      ],
    };
  },
  watch: {},
  created() {},
  mounted() {},
  methods: {
    cancel() {
      this.$emit("detailCancel");
    },
  },
};
</script>
<style lang="scss" scoped>
.detailStyle {
  border-radius: 4px;
  background: #f7f7f7;
  padding-top: 20px;
}
.detailUp {
  background: #fff;
  border-radius: 10px;
  padding: 27px 35px 36px 30px;
  margin: 0 20px 20px 20px;
}
.tableStyle {
  background: #fff;
  border-radius: 10px;
  padding: 21px 30px 31px 30px;
  margin: 20px;
}
.tableUp {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}
::v-deep .el-button--primary {
  width: 90px;
  height: 30px;
  border: 1px solid #0f9b9c;
  border-radius: 5px;
  background: #fff;
  font-size: 14px;
  line-height: 14px;
  font-family: Microsoft YaHei;
  color: #0f9b9c;
  padding: 5px 0;
}
.btnStyle {
  text-align: right;
  height: 60px;
  background: #fff;
  border-radius: 4px;
  line-height: 60px;
}
.btnStyle-item {
  margin-right: 20px;
  height: 36px;
  width: 80px;
}
::v-deep .btnStyle .el-button--primary {
  color: #fff;
  background-color: #0f9b9c;
  border-color: #0f9b9c;
  margin: 0 20px 0 0;
  width: 80px;
  height: 36px;
  border-radius: 5px;
}
::v-deep .detailUp .form-btn .form-btn-item {
  display: none;
}
::v-deep .detailUp .form-btn {
  padding: 0;
}
::v-deep .tableStyle .form-btn .form-btn-item {
  display: none;
}
::v-deep .tableStyle .header {
  width: 900px;
  margin-bottom: 0px;
  padding-left: 85px;
}
::v-deep .tableStyle .header .input_text {
  width: 660px;
}
::v-deep .tableStyle .pagex-from-theme .el-form {
  margin: 0px;
}
::v-deep .el-form .el-form-item--small .el-form-item__content .el-select {
  width: 592px !important;
}
::v-deep .block {
  margin: 0;
  padding: 0;
}
::v-deep .form-detail-container {
  > .pageX-form-detail {
    padding: 0;
    margin: 0;
  }
}
::v-deep .el-form-item__label {
  width: 132px;
}
</style>
