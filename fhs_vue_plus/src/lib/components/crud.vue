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
        <el-button size="small" @click="getList">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="data">
      <template v-for="(item, index) in columns">
        <el-table-column
            :prop="item.name"
            v-if="!item.type"
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
        :page-sizes="query.page_sizes"
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
import pagexButton from "@/lib/components/button";
export default {
  // inject: ["registPageEvent"],
  components:{
    pagexButton,
  },
  props: {
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
      data: [
        {
          groupId:'1',
        groupName:'zhangsan',
        wordbookGroupCode:'lisi'
        },
        {
          groupId:'2',
          groupName:'xiaoming',
          wordbookGroupCode:'xiaohong'
        }
      ],
      total: 100,
      query: {
        page_size: 10,
        page_number: 1,
        page_sizes:[25,50,75,100]
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
    columnFormart(_row,_column){
      return handleStrParam(_row.wordbookGroupCode,_row);
    },
    proxyClick(_row,_column){
      console.log(_row);
      // this.$router.push({path: '/dict/type/data/', query: {row: row}});
      this.$router.push({path: '/dict/type/data/'+ _row.groupId});
      console.log(_row)
      if(_column.click){
        _column.click.call(this,_row);
      }
    },
    async getList() {
      console.log(this.query);
      if (this.api) {
        const {data} = await this.$pagexRequest({url:this.api,data:this.query,method:'GET'});
        this.data = data.list || [];
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
