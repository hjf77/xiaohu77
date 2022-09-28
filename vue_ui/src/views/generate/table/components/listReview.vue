<template>
  <div class="app-container">
    <pagex-crud
      v-if="crud"
      :filters="filters"
      :columns="columns"
      :api="api"
      :buttons="buttons"
    >
    </pagex-crud>
  </div>
</template>

<script>
import crudMixins from "@/mixins/crudMixins";

export default {
  name: "listReview",
  mixins: [crudMixins],
  props: {
    tableSchema: String,//数据库名字
    tableName: String,//表名字
  },
  data() {
    return {
      api: '',
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [

      ],
      columns: [
      ],
      filters: []
    };
  },
  created() {
    this.initData();
  },
  methods: {
    async initData() {
      this.api = `/basic/ms/mock/pager?tableSchema=${this.tableSchema}&tableName=${this.tableName}`
      let listSett = await this.$pagexRequest({
        url: `/basic/ms/table/listViewJson?tableSchema=${this.tableSchema}&tableName=${this.tableName}`,
        method: "get",
      });
      this.$set(this, "filters", listSett.filters);
      listSett.columns.forEach((item)=>{
        if(item.type=='textBtn'){
          item.textBtn = [];
          if(item.hasEdit){
            item.textBtn.push({
              title: "编辑",
              type: "bottom",
              size: 'mini',
              click: (_row) => {
                this.$message({
                  type: "error",
                  message: '预览点了没啥用',
                });
              }
            });
          }
          if(item.hasDel){
            item.textBtn.push({
              title: "删除",
              type: "danger",
              size: 'mini',
              click: (_row) => {
                this.$message({
                  type: "error",
                  message: '预览点了没啥用',
                });
              }
            });
          }
        }
      });
      this.$set(this, "columns", listSett.columns);
      if(listSett.isHasAdd){
        this.buttons.push({
          title: '新增',
          type: 'primary',
          size: 'mini',
          icon: 'el-icon-plus',
          click: () => {
            this.$message({
              type: "error",
              message: '预览点了没啥用',
            });
          }
        });
      }
      this.$nextTick(() => {
        this.$set(this, "crud", true);
      });
    }
  }
};
</script>
