<template>
  <div class="search" v-if="filters && filters.length > 0">
    <el-form size="small" ref="headerForm" :inline="true" :model="query" v-if="filters">
      <div class="q">
        <div class="w">
          <el-form-item
            v-for="item in filters"
            :label="item.label"
            :key="item.name"
          >
            <el-input
              v-model="query[item.name]"
              v-if="item.type === 'text'"
              prefix-icon="el-icon-search"
              :placeholder="item.placeholder"
            ></el-input>
            <pagex-select
              v-bind="item"
              v-model="query[item.name]"
              v-if="item.type === 'select'"
              :placeholder="item.placeholder"
            ></pagex-select>
            <pagex-formTreeSelect
              v-bind="item"
              v-model="query[item.name]"
              v-if="item.type == 'treeSelect'"
              :api="item.api"
              :httpMehod="item.httpMehod"
              :style="{width:item.width?item.width:'300px'}"
            ></pagex-formTreeSelect>
            <el-date-picker
              v-model="query[item.name]"
              v-if="item.type === 'date-picker'"
              type="daterange"
              range-separator="~"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-time="['00:00:00', '23:59:59']"
            >
            </el-date-picker>
            <el-date-picker
              v-model="query[item.name]"
              v-if="item.type === 'datetimerange'"
              type="datetimerange"
              range-separator="~"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            >
            </el-date-picker>
            <pagex-checkbox
              v-bind="item"
              v-model="query[item.name]"
              v-if="item.type === 'checkbox'"
              :dictCode="item.dictCode"
            ></pagex-checkbox>
          </el-form-item>
        </div>
        <div class="e">
          <el-form-item v-if="filters.length">
            <el-button
              size="mini"
              type="primary"
              @click="search"
            >搜索
            </el-button>
            <el-button
              size="mini"
              type="primary"
              plain
              @click="reset"
            >重置
            </el-button>
          </el-form-item>
        </div>
      </div>


    </el-form>
  </div>

</template>

<script>
import utils from "./utils";

export default {
  name: "Header",
  inject: ["crud"],
  mixins: [utils],
  props: {
    filters: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      query: {},
    }
  },
  created() {
    this.init()
  },

  methods: {

    //重置表单
    reset() {
      this.$refs.headerForm.resetFields()
      this.query = this.$options.data().query
    },


    //初始化
    init() {
      this.filters.forEach(item => {
        this.$set(this.query, item.name, item.defaultValue ? item.defaultValue : undefined)
        this.crud.query.params = this.deepClone(this.query)
      })
    },

    //搜索
    search() {
      this.debounce(()=>{
        this.crud.query.page = 1
        this.crud.query.params = this.deepClone(this.query)
        this.crud.getList()
        this.$emit("search")
      })
    }
  }
}
</script>


<style lang="scss" scoped>
.el-pagination {
  float: right;
  margin: 20px 20px 20px 0;
}

.el-form--inline .el-form-item {
  margin-right: 40px;
}

::v-deep .el-input--small .el-input__inner {
  width: 220px;
}

.el-pagination {
  float: right;
  margin: 20px 0;
}

.crud-container {
  background: #F3F3F3;
}

.list {
  background: #ffffff;
  padding: 20px 20px 80px 20px;
  border-radius: 4px;
}

.search {
  background: #ffffff;
  padding: 20px 20px 0 20px;
  margin-bottom: 20px;
  border-radius: 4px;
}

::v-deep .crud-table th.is-leaf {
  border: none !important;
}

::v-deep .crud-table td {
  border-bottom: none !important;
}

::v-deep .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: #31A3A4;
}

.btn_div {
  display: inline;
  padding-bottom: 20px
}

.lf {
  float: left;
}

.lr {
  float: right;
}

.q {
  display: flex;
  display: -webkit-box;
  display: -ms-flexbox;
  display: -webkit-flex;
}

.w {
  width: 100%;
}

.e {
  width: 260px;
}


</style>
