<template>
    <div>
        <leaveDetail :bpmleaveId="bpmleaveId"></leaveDetail>
    </div>
</template>
<script>
import leaveDetail from '../../oa/components/leaveDetail.vue'
export default {
  name: "bpmLeaveDetail",
  components: { leaveDetail },
  data() {
    return {
        bpmleaveId: '',
    };
  },
  created() {
    this.getCurrentRoute();
  },
  methods: {
    getCurrentRoute(){
      const currentRoute = this.$router.currentRoute.fullPath;
      const queryId = currentRoute.substring(currentRoute.indexOf('&') +1)
      if (queryId) {
          this.bpmleaveId = queryId.substring(queryId.indexOf('=') +1);
      }
    }
  }
};
</script>