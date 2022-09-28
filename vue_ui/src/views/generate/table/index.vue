<!--
  模块名称：系统表列表
  开发人员：wanglei
  创建时间: 2022-08-31
-->
<template>
  <base-container>
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
  </base-container>
</template>

<script>
import crudMixins from "@/mixins/crudMixins";
//这个路径可能需要改下

import {mapGetters} from "vuex"

export default {
  name: "tableGenerateConfig",
  mixins: [crudMixins],
  data() {
    return {
      settingType : '',
      api: "/basic/ms/table/pagerAdvance",
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
        {label: '数据库名', name: 'tableSchema', width: 300},
        {label: '表名', name: 'tableName', width: 300},
        {label: '表备注', name: 'tableComment'},
        {label: '创建时间', name: 'createTime', width: 150},
        {label: '编码', name: 'tableCollation', width: 150},
        {
          label: '操作',
          name: 'operation',
          type: 'textBtn',
          width: 400,
          textBtn: [
            {
              title: "代码生成",
              type: "bottom",
              size: 'mini',
              click: (_row) => {
                  this.$router.push({path: '/generate/listSett',query:{tableSchema: _row.tableSchema,tableName: _row.tableName}});
              }
            }
          ],
        }
      ],
      filters: [
        {"name": "tableSchema", "label": "数据库名", "type": "text"},
        {"name": "tableName", "label": "表名", "type": "text", "operation": "like"},
        {"name": "tableComment", "label": "表备注", "type": "text", "operation": "like"},
      ],
      querys: [],
    };
  },
  created() {
  },
  computed: {
    ...mapGetters(["user"]),

  },
  methods: {},
};
</script>
