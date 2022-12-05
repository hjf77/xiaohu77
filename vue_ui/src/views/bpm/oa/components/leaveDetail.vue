<!--
  模块名称：请假详情
  开发人员：彭梦倩
  创建时间: 2022-12-01
-->

<template>
  <div>
    <!-- 详情显示 -->
    <pagex-formDetail
      class="form-detail"
      :controls="controls"
      :init="init"
      :labelWidth="120"
      :isCancle="false"
    >
    </pagex-formDetail>
  </div>
</template>
<script>
export default {
  name: "leaveDetail",
  props: {
    id: String,
    bpmleaveId: String,
  },
  created() {
    const leaveId = this.id || this.bpmleaveId;
    if (leaveId) {
      this.$pagexRequest({
        url: '/basic/ms/oa-leave/get?id=' + leaveId,
        method: 'get'
        }).then((res) => {
          this.$set(this, "init", res.data);
      })
    }
  },
  data() {
    return {
      init: {},
      controls: [
        {
          type: "text",
          name: "startTime",
          label: "开始时间",
          detail: "startTime",
        },
        {
          type: "text",
          name: "endTime",
          label: "结束时间",
          detail: "endTime",
        },
        {
          type: "select",
          name: "type",
          label: "请假类型",
          dictCode: 'bpm_oa_leave_type',
          // detail: "transMap.typeName",
          isValueNum: true,
        },
        {
          type: "text",
          name: "reason",
          label: "原因",
          detail: "reason",
        },
      ]
    };
  },
};
</script>
<style lang="scss" scoped>
</style>