
<template>
  <base-container>
    <pagex-form
      ref="pageForm"
      :isEdit="true"
      :init="detailsInfo"
      :controls="controls"
      :onSubmit="onSubmit"
      :isVee="false"
      :isHaveAddBtn="false"
      :isHaveCancelBtn="false"
      namespace="supplierSupplier"
    >
    </pagex-form>
  </base-container>
</template>
<script>
import { banknoReg } from "@/utils/validate.js";
export default {
  name: "bacisEditForm",
  props: {
    detailsInfo: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      buttons: [],
      onSubmit: function () {
        return true;
      },
      controls: [
        {
          type: "text",
          name: "accountNum",
          label: "收款人账号",
          rule: [
            { validator: banknoReg, trigger: 'blur' }
          ],
        },
      ],
    };
  },
  created() {},
  methods: {
    validate(_callback){
       this.$refs.pageForm.validate(_callback);
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
