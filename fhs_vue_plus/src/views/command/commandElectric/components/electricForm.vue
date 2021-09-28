<!--
  模块名称：重要保电新增/编辑
  开发人员：宫燕子
  创建时间:2021/9/17 09:30
-->
<template>
  <div>
    <pagex-form
      :isEdit="isEdit"
      addApi="/ms/commandElectric/"
      :updateApi="updateApi"
      :init="init"
      :data="formData"
      :controls="controls"
      :buttons="buttons"
    >
    </pagex-form>
  </div>
</template>

<script>
import datePickerOpt from "@/mixins/datePickerOpt";
export default {
  name: "addArchives",
  props: {
    init: Object,
    isEdit: Boolean,
  },
  mixins: [datePickerOpt],
  inject: ["reloadable"],
  data() {
    return {
      addApi: "/ms/commandElectric/",
      updateApi: "/ms/commandElectric/",
      formData: {
        id: this.init.id,
      },
      controls: [
        {
          type: "text",
          name: "name",
          label: "保电任务名称",
          rule: "required",
          placeholder: "请输入",
        },
        {
          type: "select",
          name: "electricGrade",
          label: "保电等级",
          rule: "required",
          dictCode: "electric_grade",
          width: "300",
        },
        {
          formatVal: "yyyy-MM-dd HH:mm:ss",
          name: "startTime",
          label: "任务执行开始时间",
          type: "dates",
          formatType: "datetime",
          rule: "required",
          pickerOptions: {},
          changeFn: (val) => {
            this.startTime = val;
            this.rewriteEndPickerOptions("controls", 3, "datetime");
          },
        },
        {
          formatVal: "yyyy-MM-dd HH:mm:ss",
          name: "endTime",
          label: "任务执行结束时间",
          type: "dates",
          formatType: "datetime",
          rule: "required",
          pickerOptions: {},
          changeFn: (val) => {
            this.endTime = val;
            this.rewriteStartPickerOptions("controls", 2);
          },
        },

        {
          type: "textarea",
          name: "describes",
          label: "保电任务描述",
          placeholder: "请输入",
          rule: { min: 1, max: 200 },
        },
        {
          type: "textarea",
          name: "plan",
          label: "保电任务计划",
          placeholder: "请输入",
          rule: { min: 1, max: 200 },
        },
        {
          type: "text",
          name: "importantCount",
          label: "重要保电用户数量",
          placeholder: "请输入",
        },
        {
          type: "treeSelect",
          name: "company",
          label: "执行单位",
          api: "/ms/sysOrganization/selectTree",
          labelField: "name",
          valueField: "id",
          multiple: false,
        },
        {
          type: "text",
          name: "placeUser",
          label: "保电用户场所",
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "longitude",
          label: "经度",
          placeholder: "请输入",
        },
        {
          type: "text",
          name: "latitude",
          label: "纬度",
          placeholder: "请输入",
        },
        {
          type: "radio",
          name: "isCommon",
          label: "是否共同完成",
          options: [
            { value: 0, label: "否" },
            { value: 1, label: "是" },
          ],
        },
        {
          type: "uploadFileAsync",
          name: "enclosureId",
          label: "附件",
          // isTip: true,
          tipContent: "附件提示文字",
          isDownload: true,
          isDelete: true,
        },
      ],
    };
  },
  created() {
    if (this.isEdit) {
      // 初始化时间校验
      this.startTime = this.init.startTime;
      this.endTime = this.init.endTime;
      this.rewriteStartPickerOptions("controls", 2);
      this.rewriteEndPickerOptions("controls", 3, "dates");
    }
  },
};
</script>