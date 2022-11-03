<!--
  模块名称：选择之前的表单
  开发人员：wanglei
  创建时间: 2022-05-13
-->
<template>
    <pagex-crud
      ref="crud"
      :filters="filters"
      :columns="columns"
      :api="api"
      :sortSett="sortSett"
      :buttons="buttons"
      :querys="querys"
    >
    </pagex-crud>
</template>

<script>

import crudMixins from "@/mixins/crudMixins";
//这个路径可能需要改下

import {mapGetters} from "vuex"
import operatorLogForm from "@/views/system/operatorLog/components/operatorLogForm.vue";

export default {
  name: "formHistory",
  mixins: [crudMixins],
  components: {
    operatorLogForm
  },
  props:{
    url:{
      type:String,
      default:null,
    },
    onSelect:{
      type:Function,
      default: null
    }
  },
  data() {
    return {
      api: "/basic/ms/logOperatorMain/pagerAdvance",
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
        {label: '时间', name: 'createTime'},
        {label: '请求参数', name: 'reqParam', type: 'popover', width: 150},
        {label: '返回参数', name: 'respBody', type: 'popover', width: 150},
        {label: '开发者', name: 'devOperator'},
        {
          label: '操作',
          name: 'operation',
          type: 'textBtn',
          textBtn: [
            {
              title: "选择",
              type: "bottom",
              size: 'mini',
              click: (_row) => {
                this.onSelect(JSON.parse(_row.reqParamSource));
              }
            }
          ]
        }
      ],
      filters: [
        {"name": "state", "label": "状态", "type": "select", dictCode: "state", width: '220'},
        {"name": "devOperator", "label": "开发者", "type": "text"},
        {"name": "createTime", "label": "操作时间", "type": "datetimerange", "operation": "between"},
        {
          "name": "createUser",
          "label": "操作人",
          "type": "select",
          url: "/basic/ms/sysUser/findList",
          labelField: "userName",
          valueField: "userId"
        }
      ],
      //默认查询条件为url= 并且新增表单
      querys: [{
        property:'url',
        value:'',
        operation:'like'
      },
      {
        property:'type',
        value:'0'
      }],
    };
  },
  created() {
    //如果带ms的，就以ms开头把微服务名字截掉
    this.querys[0].value = this.url.indexOf("/ms/")!=-1 ? this.url.substring(this.url.indexOf("/ms/")) : this.url;
  },
  computed: {
    ...mapGetters(["user"]),

  },
  methods: {},
};
</script>
<style lang="scss" scoped>

</style>
