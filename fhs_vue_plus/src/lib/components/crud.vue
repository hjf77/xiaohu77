<template>
  <div>
    <el-form
        size="small"
        :inline="true"
        :model="query"
        v-if="filter && filter.controls"
    >
      <el-form-item
          :label="item.label"
          v-for="item in filter.controls"
          :key="item.name"
      >
        <el-input
            v-model="query[item.name]"
            v-if="item.type === 'text'"
            :placeholder="item.placeholder"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button size="small" type="primary" icon="el-icon-search"  @click="getList">搜索</el-button>
      </el-form-item>
    </el-form>

    <div style="margin-bottom: 20px">
      <el-button
        v-for="i in buttons"
        v-if="i.url != ''"
        :type="i.type"
        :icon="i.icon"
        size="mini"
        @click="check(i)"
      >{{i.title}}
      </el-button>
    </div>



    <el-table :data="data"  @selection-change="handleSelectionChange">
      <template v-for="(item, index) in columns">
        <el-table-column
          v-if="item.type === 'selection'"
          type="selection"
        >
        </el-table-column>
        <el-table-column
            :prop="item.name"
            v-else-if="!item.type"
            :label="item.label"
            :key="item.name"
        >
        </el-table-column>
        <el-table-column
            :prop="item.name"
            v-else-if="item.type === 'formart'"
            :label="item.label"
            :key="'operation' + index"
        >
          <template slot-scope="scope">
           <div @click="proxyClick(scope.row,item)" v-html="columnFormart(scope.row,item)"></div>
          </template>
        </el-table-column>
        <el-table-column
            :prop="item.name"
            v-else-if="item.type === 'operation'"
            :label="item.label"
            :key="'operation' + index"
        >
          <template v-if="item.buttons">
            <pagex-button
              style="margin-right: 10px"
                v-for="(item, index) in item.buttons"
                :confirmText="item.confirmText"
                :key="index"
                :level="item.level"
                :actionType="item.actionType"
                :label="item.label"
                :dialog="item.dialog"
            ></pagex-button>
          </template>
        </el-table-column>
      </template>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page.sync="query.page_number"
        :page-size="query.page_size"
        :page-sizes="page_sizes"
        layout="total, sizes, prev, pager, next, jumper"
        background
        v-if="total !== 0"
        :total="total"
    >
    </el-pagination>
  </div>
</template>

<script>
import {handleStrParam} from '@/lib/utils/param'
export default {
  // inject: ["registPageEvent"],
  props: {
    buttons:{
      type: Array,
      default: () => [],
    },
    id:{
      type: String,
      default: ''
    },
    uid: {
      type: [String, Number],
      default: 'default'
    },
    api: {
      type: String,
      default: "",
    },
    title: {
      type: String,
      default: "",
    },
    columns: {
      type: Array,
      default: () => [],
    },
    filter: {
      type: Object,
      default: () => ({}),
    },
    methods: {
      type: Object,
      default: () => ({}),
    }
  },
  data() {
    return {
      data: [],
      multipleSelection: [],
      total: 100,
      page_sizes:[10,20,50,100],
      query: {
        page_size: 10,
        page_number: 1,
      },
    };
  },
  async created() {
    console.log(this);
    this.$parent.__reload = this.getList;
    this.getList();
    // this.registPageEvent(this.uid, this.getList);
  },
  mounted() {
  },
  methods: {
    check(val) {
      if(val.click){
        val.click.call(this,this.data,this.multipleSelection);
      }
    },
    //多选框
    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log(this.multipleSelection)
    },
    columnFormart(_row,_column){
      return handleStrParam(_column.formart,_row);
    },
    proxyClick(_row,_column){
      console.log(_row);
      if(_column.click){
        _column.click.call(this,_row);
      }
    },
    async getList() {
      console.log(this.api);
      console.log(this.query);
      if (this.api) {
        const {data} = await this.$pagexRequest({url:this.api,params:this.query,method:'get'});
        this.data = data.rows || [];

        if (data.total) {
          this.total = data.total || 0;
        }
      }
    },
    handleSizeChange(val) {
      this.query.page_size = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.query.page_number = val;
      this.getList();
    },

  }
};
</script>

<style lang="scss" scoped>
::v-deep .el-table .cell{
  display: flex;
}
.el-pagination{
  float: right;
  margin: 20px 20px 20px 0;
}
</style>
