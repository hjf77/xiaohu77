
<template>
  <base-container>
    <div class="title-style">上传营业执照</div>
    <pagex-uploadPicture
      :limit="1"
      namespace="supplierSupplier">
    </pagex-uploadPicture>
  </base-container>
</template>
<script>
export default {
  name: 'licenseEditForm',
  data() {
    return {};
  },
  created() {},
  methods: {
  },
};
</script>

<style lang="scss" scoped>
.title-style {
    font-size: 14px;
    color: #0f9b9c;
    font-weight: 700;
    line-height: 30px;
  }
</style>
