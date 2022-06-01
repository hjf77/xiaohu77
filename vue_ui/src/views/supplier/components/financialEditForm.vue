
<template>
  <base-container>
    <pagex-form
      ref="pageForm"
      :isEdit="true"
      :init="detailsInfo"
      :addApi="addApi"
      :updateApi="updateApi"
      :controls="controls"
      :onSubmit="onSubmit"
      :isVee="false"
      :isHaveAddBtn="false"
      :isHaveCancelBtn="false"
    >
    </pagex-form>
  </base-container>
</template>
<script>
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
        this.$refs.pageForm.$refs.form.validate((valid, errors) => {
          if (valid) {
            this.$emit('submitClick', 'financialEditForm')
            return true
          } else {
            this.$emit('submitClick', false)
            return false
          }
        })
      },
      controls: [
        {
          type: "text",
          name: "accountNum",
          label: "收款人账号",
          rule: [
            {required: true, message: '收款人账号不能为空', trigger: 'blur'},
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
