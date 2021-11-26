<template>
  <base-container>
     我是首页，你们爱放啥放啥把！
  </base-container>
</template>

<script>

export default {
  name: 'Index',
  components: {

  },
  data() {
    return {

    }
  },
  methods:{

  }

}
</script>

<style lang="scss" scoped>
::v-deep .el-tabs__nav-wrap::after {
  background-color: #FFFFFF;
}
::v-deep .list {
  padding: 0!important;
}

::v-deep .el-tabs__active-bar {
  width: 70px!important;
  height: 3px;
}
::v-deep .el-tabs__nav {
  > .el-tabs__item {
    padding: 0!important;
    width: 70px!important;
    text-align: center!important;
  }
}
::v-deep .el-tabs__header {
  margin: 0 0 10px 0;
}
.titleCss {
  height: 50px;
  line-height: 50px;
  .title-text {
    font-size: 16px;
    font-weight: bold;
    color: #333333;
    margin-left: 8px;
  }
  .title-icon {
    display: inline-block;
    width: 4px;
    height: 15px;
    background: #0F9B9C;
  }
}
.title-more {
  float: right;
  height: 14px;
  font-size: 14px;
  font-weight: 400;
  color: #0F9B9C;
  cursor: pointer;
}
.more {
  line-height: 50px;
  margin-right: 30px;
}
::v-deep .el-table tr {
  height: 50px!important;
}
::v-deep .el-table__header-wrapper th {
  height: 50px!important;
}
</style>
