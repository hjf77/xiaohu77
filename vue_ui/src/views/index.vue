<template>
  <base-container>
    <p>JSON驱动插件演示</p>
    <h1>1、列表过滤条件收起/展开，列锁定，无total分页展示</h1>
    <div style="margin: 20px 0">
      <div style="width:100%">
        <pagex-crud
          ref="crud"
          :filters="filters"
          :columns="columns"
          :api="api"
          :sortSett="sortSett"
          :buttons="buttons"
          :querys="querys"
          :isAdvancePager="true"
        >
        </pagex-crud>
      </div>

      <div style="margin: 20px 0">
        <el-button @click="testMock">点我测试V-Mock链接</el-button>
      </div>
      <div style="width:100%;margin-top: 100px;">
        <h1>2、表单一对多能力演示</h1>
        <one2xForm></one2xForm>
      </div>
    </div>
  </base-container>
</template>

<script>
import one2xForm from "@/views/demo/form_one2x_demo.vue";
import crudMixins from "@/mixins/crudMixins";


export default {
  name: 'Index',
  components: {one2xForm},
  mixins: [crudMixins],
  data() {
    return {
      api: "/basic/ms/logLogin/pagerAdvance",
      isEdit: false,
      open: false,
      isDetail: false,
      // 列表排列顺序（更新时间）
      sortSett: [
        {
          direction: "DESC",
          property: "createTime",
        },
      ],
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [],
      columns: [
        {label: "序号", name: "index", type: "index", width:'60', fixed: 'left'},
        {label: '用户', name: 'loginUserName', type: 'popover', width: 150, fixed: 'left'},
        {label: '用户名', name: 'loginName', type: 'popover', width: 150, fixed: 'left'},

        {label: '时间', name: 'loginName', type: 'popover', width: 150, fixed: 'left'},
        {label: '地址', name: 'loginName', type: 'popover', width: 150},
        {label: '性别', name: 'loginName', type: 'popover', width: 150},

        {label: '时间', name: 'createTime', type: 'popover'},
        {label: 'ip', name: 'ipAddress', type: 'popover', width: 150},
        {label: 'ip信息', name: 'ipInfo', type: 'popover', width: 200},
        {label: '浏览器', name: 'browser', type: 'popover', width: 150},
        {label: '操作系统', name: 'os', type: 'popover', width: 150},
        {label: '状态', name: 'stateName', type: 'popover', width: 150},
        {label: '类型', name: 'typeName', type: 'popover', width: 150},
        {
          label: "操作",
          name: "operation",
          type: "textBtn",
          width: "240",
          fixed: 'right',
          textBtn: [
            {
              title: "编辑",
              type: "primary",
              size: "mini",

            },
            {
              title: "详情",
              type: "success",
              size: "mini",

            },
            {
              title: "删除",
              type: "danger",
              api: "/ms/trainStudent/",
              size: "mini"
            }
          ]
        }

      ],
      filters: [
        {"name": "ipAddress", "label": "用户名", "type": "text", "operation": "like"},
        {"name": "dddddd", "label": "用户名", "type": "text", "operation": "like"},
        {"name": "ssssss", "label": "用户名", "type": "text", "operation": "like"},
        {"name": "aaaaaa", "label": "用户名", "type": "text", "operation": "like"},
        {"name": "cccccc", "label": "用户名", "type": "text", "operation": "like"},
        {"name": "nnnnnn", "label": "用户名", "type": "text", "operation": "like"},
        {"name": "vvvvvv", "label": "用户名", "type": "text", "operation": "like"},
        {"name": "mmmmmm", "label": "用户名", "type": "text", "operation": "like"},
        {"name": "llllll", "label": "用户名", "type": "text", "operation": "like"},
        {"name": "oooooo", "label": "用户名", "type": "text", "operation": "like"},
        {"name": "pppppp", "label": "用户名", "type": "text", "operation": "like"},
        {"name": "yyyyyy", "label": "用户名", "type": "text", "operation": "like"},
      ],
      querys: [],
    }
  },
  methods: {
    handleClick(row) {
      console.log(row);
    },
    testMock() {
      this.$pagexRequest({
        method: "get",
        url: "/vmock/order/list"
      })
      .then((res) => {
        console.log(res)
      })
      .catch((res) => {
        console.log(res);
      });
    }
  }

}
</script>

<style lang="scss" scoped>
::v-deep .el-tabs__nav-wrap::after {
  background-color: #FFFFFF;
}

::v-deep .list {
  padding: 0 !important;
}

::v-deep .el-tabs__active-bar {
  width: 70px !important;
  height: 3px;
}

::v-deep .el-tabs__nav {

> .el-tabs__item {
  padding: 0 !important;
  width: 70px !important;
  text-align: center !important;
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
  height: 50px !important;
}

::v-deep .el-table__header-wrapper th {
  height: 50px !important;
}
</style>
