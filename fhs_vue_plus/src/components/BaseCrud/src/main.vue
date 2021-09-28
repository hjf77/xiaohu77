<template>
  <div>
    <table-filter ref="tableFilter" :filters="filters"></table-filter>
    <div class="list">
      <table-button :option="buttons" v-if="buttons.length"></table-button>
      <el-table
        v-loading="loading"
        class="crud-table"
        header-align="center"
        style="width: 100%"
        size="small"
        :data="tableData"
        stripe
      >
        <template v-for="(column, index) in columnOption">
          <column :column-option="column" :key="index" :table-option="option"></column>
        </template>
      </el-table>
      <table-pagination v-if="isPagination"></table-pagination>
    </div>
    <dialog-form
      ref="dialogForm"
    ></dialog-form>
  </div>
</template>

<script>
import Column from "./column";
import DialogForm from "./dialog-form";
import TableFilter from "./table-filter";
import TablePagination from "./table-pagination";
import TableButton from "./table-button";
import utils from "./utils";
export default {
  name: "BaseCrud",
  components: {TableButton, TablePagination, TableFilter,DialogForm, Column},
  provide() {
    return {
      crud: this
    }
  },
  mixins:[utils],
  props: {
    beforeClose: Function,
    beforeOpen: Function,
    option: {
      type: Object,
      default: null
    },
    // tableData的默认值
    value:[],
    // 是否设置tableData的默认值
    isLocal:{
      type:Boolean,
      default:false
    },
    isPagination: {
      type:Boolean,
      default:true
    }
  },
  data() {
    return {
      query: {
        rows: 10,
        page: 1,
        params: {},
      },
      tableData: [],
      loading: false
    }
  },
  computed: {
    //表格column列
    columnOption() {
      return this.option.columns || []
    },

    //列表Api
    listApi() {
      return this.option.listApi || this.option.api || ""
    },

    //新增api
    addApi() {
      return this.option.addApi || this.option.api || ""
    },

    //编辑api
    editApi() {
      return this.option.editApi || this.option.api || ""
    },

    //删除api
    delApi() {
      return this.option.delApi || this.option.api || ""
    },

    //详情api
    detailApi() {
      return this.option.detailApi || this.option.api || ""
    },

    //表格上方的按钮组
    buttons() {
      return this.option.buttons || []
    },
    //查询条件
    filters() {
      return this.option.filters || []
    },

    //列表查询参数
    querys() {
      return this.option.querys || []
    },
    //列表查询参数-扩展
    paramsQuery() {
      return this.option.paramsQuery || {}
    },

    //是否需要分页
    isNeedPager() {
      return this.option.isNeedPager || true
    }


  },
  mounted() {
    if(!this.isLocal){
      this.getList();
    }else{
      this.tableData = this.deepClone(this.value)
    }
  },
  methods: {
    //获取列表数据
    async getList() {
      this.loading = true
      if (this.listApi) {
        const res = await this.$pagexRequest({
          url: this.listApi,
          data: this.formartReqFilter(),
          method: "POST",
        });
        if (this.isNeedPager) {
          if (res.rows) {
            this.tableData = res.rows;
          } else {
            this.data = [];
            this.$emit("nullvalue", 0);
          }
          if (res.total) {
            this.total = res.total;
          }
        } else {
          this.tableData = res;
        }
        this.loading = false
      }
      setTimeout(() => {
        this.loading = false
      }, 2000)
    },

    //删除数据
    delObj(row, val) {
      this.$confirm("是否删除此数据?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(async () => {
        let id = val.idFieldName ? row[val.idFieldName] : row.id
        let url = `${this.delApi}${id}`;
        const result = await this.$pagexRequest({
          url: url,
          method: "DELETE",
        })

        if (result.code === 200) {
          this.reset();
          this.$message({
            type: "success",
            message: "删除成功!",
          });
        } else {
          this.$message({
            type: "error",
            message: res.message,
          });
        }
      }).catch(() => {

      });

    },


    /**
     * 格式化参数
     * @returns {{sorter: ([{property: string, direction: string}]|[{property: string, direction: string}]|{default: function(): [], type: Array | ArrayConstructor}|*), pagerInfo: {showTotal: boolean, pageSize, page: (number|*)}, groupRelation: string, params: Object | ObjectConstructor, querys: any}}
     */
    formartReqFilter: function () {
      let result = {
        groupRelation: "AND",
        pagerInfo: {
          page: this.query.page,
          pageSize: this.query.rows,
          showTotal: true,
        },
        params: this.paramsQuery,
        querys: JSON.parse(JSON.stringify(this.querys)),
        sorter: this.option.sortSett || [],
      };
      this.filters.forEach((item, index) => {
        if (
          this.query.params[item.name] &&
          !(item.operation == "IN" && this.query.params[item.name].length == 0)
        ) {
          result.querys.push({
            group: item.group || "main",
            operation: item.operation,
            property: item.name,
            relation: "AND",
            value:
              item.type == "date-picker" || item.type == "datetimerange"
                ? this.datePicker(this.$refs['tableFilter'].query[item.name])
                : this.query.params[item.name],
          });
        }
      });
      return result;
    },


    //重置
    reset() {

      this.query.params = {}
      this.getList()
    },

    //添加一条数据
    rowAdd() {
      this.$refs.dialogForm.show("add");
    },

    //编辑数据
    rowEdit({row}) {
      this.$refs.dialogForm.tableForm = row
      this.$refs.dialogForm.show("edit");
    },

    //打开详情
    rowDetail({row}) {
      this.$refs.dialogForm.tableForm = row
      this.$refs.dialogForm.show("detail");
    },
  }
}
</script>

<style lang="scss" scoped>
.list {
  background: #ffffff;
  padding: 20px;
  border-radius: 4px;
}
</style>
