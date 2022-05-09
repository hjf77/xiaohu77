<--
本vue文件严禁写任何业务代码，如果需要扩展请通过插槽扩展。
请注意属性名命名
by wanglei
-->


<template>
  <div class="crud-container">
    <div class="clear"></div>
    <div class="search" v-if="filters && filters.length > 0">
      <el-form id="searchForm" size="small" :inline="true" :model="query" v-if="filters">
        <el-form-item v-for="(item,index) in filters" :key="item.name" :label="item.formname ? item.formname:item.label" :class="{'displayNone':(index + 1 > filterDefaultShow) && isMoreSearch}" >
          <el-input
            v-model="query.params[item.name]"
            v-if="item.type === 'text'"
            prefix-icon="el-icon-search"
            :placeholder="item.placeholder"
            clearable
          ></el-input>

          <pagex-select
            v-bind="item"
            v-model="query.params[item.name]"
            v-if="item.type === 'select'"
            placeholder="请选择"
          ></pagex-select>

          <pagex-formTreeSelect
            v-bind="item"
            v-model="query.params[item.name]"
            v-if="item.type === 'treeSelect'"
            :api="item.api"
          ></pagex-formTreeSelect>

          <el-date-picker
            v-model="query.params[item.name]"
            v-if="item.type === 'date-picker'"
            type="daterange"
            range-separator="~"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :default-time="['00:00:00', '23:59:59']"
          >
          </el-date-picker>
          <el-date-picker
            v-model="query.params[item.name]"
            v-if="item.type === 'datetimerange'"
            type="datetimerange"
            range-separator="~"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          >
          </el-date-picker>

          <pagex-checkbox
            v-bind="item"
            v-model="query.params[item.name]"
            v-if="item.type === 'checkbox'"
            :dictCode="item.dictCode"
          ></pagex-checkbox>
        </el-form-item>
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
          <el-button
            size="mini"
            type="text"
            @click="moreSearch"
            v-show="(filters.length) > filterDefaultShow"
          >{{ isMoreSearch ? '展开' : '收起' }}
            <i v-if="isMoreSearch" class="el-icon-arrow-down el-icon--right"></i>
            <i v-else class="el-icon-arrow-up el-icon--right"></i>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 留的自定义插槽 -->
    <slot name="topSlot"></slot>
    <!-- 表格上方共呢个区域 -->
    <div class="list">
      <div v-for="(i, v) in realButtons" :key="v" class="btn_div">
        <!--左边的按钮-->
        <div class="lf" v-if="!i.isRight">
          <el-button
            v-hasPermi="i.permission ? i.permission : 'none'"
            :type="i.type"
            :icon="i.icon"
            :size="i.size"
            @click="check(i)"
            style="margin: 0px 20px 20px 0px;"
          >{{ i.title }}
          </el-button>
        </div>
        <!--右边的按钮-->
        <div class="lr" v-else>
          <el-button
            v-hasPermi="i.permission ? i.permission : 'none'"
            :type="i.type"
            :icon="i.icon"
            :size="i.size"
            style="margin: 0px 0px 20px 20px;"
            @click="check(i)"
          >{{ i.title }}
          </el-button>
        </div>
      </div>
      <!--表格-->
      <el-table
        class="crud-table"
        :header-cell-style="{'text-align':'center'}"
        style="width: 100%"
        size="small"
        :data="data"
        @selection-change="handleSelectionChange"
        @row-click="lineClick && rowClick(arguments)"
        :highlight-pageNumber-row="highlight"
        :height="height"
        stripe
        row-key="id"
        :tree-props="{children: 'children'}"
      >
        <template v-for="(item, index) in realColumns">
          <!--多选框-->
          <el-table-column
            :key="index"
            v-if="item.type === 'selection'"
            type="selection"
            align="center"
          >
          </el-table-column>
          <!--序号-->
          <el-table-column
            :key="index"
            v-else-if="item.type === 'index'"
            type="index"
            :label="item.label"
            :fixed="item.fixed"
            :width="item.width"
            align="center"
          >
            <template slot-scope="scope">
              <span>{{ scope.row.index }}</span>
            </template>
          </el-table-column>
          <!--默认列-->
          <el-table-column
            :prop="item.name"
            v-else-if="!item.type"
            :label="item.label"
            :key="item.name"
            :width="item.width"
            :min-width="item.minWidth"
            align="center"
          >
          </el-table-column>
          <!--自己指定格式化-->
          <el-table-column
            :prop="item.name"
            :show-overflow-tooltip="true"
            v-else-if="item.type === 'formart'"
            :label="item.label"
            :key="'operation' + index"
            align="center"
            :width="item.width"
          >
            <template slot-scope="scope">
              <div
                @click="proxyClick(scope.row, item)"
                v-html="columnFormart(scope.row, item)"
              ></div>
            </template>
          </el-table-column>
          <!--带tips-->
          <el-table-column
            :prop="item.name"
            v-else-if="item.type === 'popover'"
            :label="item.label"
            :key="'operation' + index"
            :show-overflow-tooltip="true"
            :width="item.width"
            :align="item.align || 'center'"
            :fixed="item.fixed"
          >
          </el-table-column>

          <el-table-column
            :prop="item.name"
            v-else-if="item.type === 'operation'"
            :label="item.label"
            :key="'operation' + index"
          >
          </el-table-column>
          <!--按钮-->
          <el-table-column
            v-else-if="item.type === 'textBtn'"
            :label="item.label"
            :width="item.width || 210"
            :min-width="item.minWidth"
            :align="item.align || 'center'"
            :fixed="item.fixed"
          >
            <template slot-scope="scope">
              <el-button
                v-for="(val, index) in item.textBtn"
                v-if="proxyBtnIf(scope.row, index, val)"
                :key="index"
                :type="val.type"
                :size="val.size || 'mini'"
                :disabled="proxyBtnDisabled(scope.row, index, val)"
                plain
                @click="handleClick(scope.row, val)"
              >
                {{ val.title }}
              </el-button>
            </template>
          </el-table-column>
        </template>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handlepageNumberChange"
        :pageNumber-page="query.pageNumber"
        :page-size="query.pageSizeNumber"
        :page-sizes="page_sizes"
        layout="total, sizes, prev, pager, next, jumper"
        background
        v-show="data.length"
        :total="total"
        v-if="isNeedPager"
      >
      </el-pagination>

      <slot name="form"></slot>
    </div>
  </div>
</template>

<script>
import {handleStrParam} from "@/lib/utils/param";
import Dialog from "@/lib/components/dialog.vue";
import $moment from "moment";
import {checkPermi} from "@/utils/permission";
import PagexCheckBox from "./checkbox.vue";

export default {
  components: {PagexCheckBox, Dialog},
  props: {
    buttons: {
      type: Array,
      default: () => [],
    },
    lineClick: {
      type: Boolean,
      default: false,
    },
    namespace: {
      type: String,
      default: "",
    },
    api: {
      type: String,
      default: "",
    },
    paramsQuery: Object,
    columns: {
      type: Array,
      default: () => [],
    },
    filters: {
      type: Array,
      default: () => [],
    },
    sortSett: {
      type: Array,
      default: () => [],
    },
    querys: {
      type: Array,
      default: () => [],
    },
    highlight: {
      type: Boolean,
      default: false,
    },
    height: {
      type: String,
      default: null,
    },
    minPageSize: {
      type: Number,
      default: null,
    },
    isNeedPager: {
      type: Boolean,
      default: true,
    },
    centerShow: {
      type: Boolean,
      default: false,
    },
    tableList: {
      type: Array,
      default: () => {
        return []
      }
    },
    methodType: {
      type: String,
      default: 'POST'
    },
    filterDefaultShow:{
      type:Number,
      default:()=> 4
    }
  },
  data() {
    return {
      isMoreSearch: true,
      switchValue: {}, //列表开关上的状态
      data: [],
      multipleSelection: [],
      total: 0,
      page_sizes: [10, 20, 50, 100],
      query: {
        pageSizeNumber: 10,
        pageNumber: 1,
        params: {},
      },
      realColumns: [],
      realButtons: [],
      rowPermissions: {},
    };
  },
  provide() {
    return {
      reloadable: this,
    };
  },
  watch: {
    tableList: {
      handler() {
        this.reset()
      },
      deep: true
    }
  },
  created() {
    if (this.minPageSize) {
      this.query.pageSizeNumber = this.minPageSize;
      this.page_sizes.unshift(this.minPageSize);
    }

    this.filters.forEach((item, index) => {
      if (item.defaultValue) {
        this.query.params[item.name] = item.defaultValue
      }
      if (!item.placeholder) {
        let title = item.formname ? item.formname : item.label;
        item.placeholder = (item.type == 'text' ? '请输入' : '请选择') + title;
      }
    });
    this.columns.forEach((column) => {
      if (column.ifFun) {
        if (column.ifFun.call()) {
          this.realColumns.push(column);
        }
      } else {
        this.realColumns.push(column);
      }
    });
    this.buttons.forEach((button) => {
      if (button.ifFun) {
        if (button.ifFun.call()) {
          this.realButtons.push(button);
        }
      } else {
        this.realButtons.push(button);
      }
    })
    this.realColumns.forEach((_item, _index) => {
      if (_item.type == "textBtn") {
        _item.textBtn.forEach((btn, btnIndex) => {
          if (btn.permission) {
            this.rowPermissions[btnIndex] = checkPermi(btn.permission);
          } else {
            this.rowPermissions[btnIndex] = true;
          }
        });
      }
    });

    this.getList();
  },
  mounted() {
    this.$nextTick(() => {
      if (this.namespace) {
        this.$EventBus.$on(this.namespace + '_reload', () => {
          console.log(this.query);
          this.getList()
        })
      }
    })
  },
  beforeDestroy() {
    this.$EventBus.$off(this.namespace + '_reload')
  },
  methods: {
    // 已选择数据，禁用按钮
    proxyBtnDisabled(_row, _btnIndex, _btn) {
      if (_btn.disabledFun) {
        return _btn.disabledFun(_row);
      }
      return false
    },
    proxyBtnIf(_row, _btnIndex, _btn) {
      if (!this.rowPermissions[_btnIndex]) {
        return false;
      }
      if (_btn.ifFun) {
        return _btn.ifFun(_row);
      }
      if (_btn.ifAttr && _btn.ifValue) {
        if (_btn.ifOperator == "in") {
          return _btn.ifValue.includes(_row[_btn.ifAttr]);
        }
        if (_row[_btn.ifAttr] == _btn.ifValue) {
          return true;
        }
        return false;
      }
      if (_btn.ifAttr && _btn.ifOperator == "none") {
        if (_row[_btn.ifAttr]) {
          return true;
        } else {
          return false;
        }
      }
      return true;
    },
    formartReqFilter: function () {
      let result = {
        groupRelation: "AND",
        pagerInfo: {
          pageNumber: this.query.pageNumber,
          pageSize: this.query.pageSizeNumber,
          showTotal: true,
        },
        params: this.paramsQuery,
        querys: JSON.parse(JSON.stringify(this.querys)),
        sorter: this.sortSett,
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
                ? this.datePicker(this.query.params[item.name])
                : this.query.params[item.name],
          });
        }
      });
      return result;
    },
    datePicker(val) {
      let arr = [];
      arr.push($moment(val[0]).format("YYYY-MM-DD HH:mm:ss"));
      arr.push($moment(val[1]).format("YYYY-MM-DD HH:mm:ss"));
      return arr;
    },
    handleClick(row, val) {
      if (val.title == "删除" && !val.click) {
        this.$confirm("是否删除此数据?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            // 若设置idFieldName，则根据这个参数值删除，负责根据id删除
            const id = val.idFieldName ? row[val.idFieldName] : row.id
            // isDelPathId 如果为true，则使用/dsp/****/v1/159，否则 /dsp/***/v1/?id=159
            let url = `${val.api}${id}`;
            this.$pagexRequest({
              url: url,
              method: "DELETE",
            })
              .then((res) => {
                if (res.code === 200) {
                  this.reset();
                  this.$message({
                    type: "success",
                    message: "删除成功!",
                  });
                  //  删除成功的回调
                  if (val.clickCallBack) {
                    val.clickCallBack.call(this, row, val);
                  }
                } else {
                  this.$message({
                    type: "error",
                    message: res.message,
                  });
                }
              })
              .catch((res) => {
                this.getList();
              });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      } else {
        val.click.call(this, row, val);
      }
    },
    check(val) {
      if (val.title == "删除") {
        if (this.multipleSelection.length == 0) {
          this.$message({
            type: "error",
            message: "暂未勾选任何数据!",
          });
        } else {
          this.$confirm(
            val.Tips != undefined ? val.Tips : "是否删除此数据?",
            "提示",
            {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning",
            }
          )
            .then(() => {
              let data = [];
              this.multipleSelection.forEach((item) => {
                data.push(item.id);
              });
              let url = val.isDelPathId
                ? `${val.api}${data}`
                : `${val.api}?ids=${data}`;
              this.$pagexRequest({
                url: url,
                method: "DELETE",
              })
                .then((res) => {
                  if (res.state) {
                    this.reset();
                    this.$message({
                      type: "success",
                      message: "删除成功!",
                    });
                    val.click.call(this, res);
                  } else {
                    this.$message({
                      type: "error",
                      message: res.message,
                    });
                  }
                })
                .catch((res) => {
                  this.getList();
                });
            })
            .catch(() => {
              this.$message({
                type: "info",
                message: "已取消删除",
              });
            });
        }
      } else {
        if (val.click) {
          let ids = [];
          this.multipleSelection.forEach((item) => {
            ids.push(item.id);
          })
          val.click.call(this, this.multipleSelection, ids.join(','));
        }
      }
    },
    //多选框
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.$emit("handleSelectionChange", val);
    },
    // 点击某一行
    rowClick(row) {
      this.$emit("rowClick", row[0]);
    },
    columnFormart(_row, _column) {
      return handleStrParam(_column.formart, _row);
    },
    proxyClick(_row, _column) {
      let _this = this;
      if (_column.click) {
        _column.click.call(_this, _row);
      }
    },
    search() {
      this.query.pageNumber = 1;
      this.getList();
      this.$emit("search", true);
    },
    //重置
    reset() {
      this.query.params = {};
      this.search();
    },
    // 刷新缓存
    reload(_groupName) {
      this.search();
    },
    async getList(addData) {

      if (this.api) {
        const res = await this.$pagexRequest({
          url: this.api,
          data: this.formartReqFilter(),
          method: this.methodType,
        });
        if (this.isNeedPager) {
          if (res.records) {
            this.data = res.records;
          } else {
            this.data = [];
            this.$emit("nullvalue", 0);
          }
          if (res.total) {
            this.total = parseInt(res.total);
          }
          //  this.setTableIndex(this.data)
        } else {
          this.data = res;
          if (addData) {
            this.data.push(addData)
          }
          this.data.forEach((item, key) => {
            item.index = key + 1;
          })
        }
      } else {
        this.$set(this, "data", this.tableList)
        this.data.forEach((item, key) => {
          item.index = key + 1;
        })
      }
    },
    handleSizeChange(val) {
      this.query.pageSizeNumber = val;
      this.getList();
    },
    handlepageNumberChange(val) {
      this.query.pageNumber = val;
      this.getList();
    },
    // 树形列表index层级，实现方法（可复制直接调用）
    setTableIndex(arr, index) {
      arr.forEach((item, key) => {
        item.index = key + 1 + (this.query.pageNumber - 1) * this.query.pageSizeNumber;
        if (index) {
          item.index = index + "-" + (key + 1);
        }
        if (item.children) {
          this.setTableIndex(item.children, item.index);
        }
      })
    },

    moreSearch() {
      this.isMoreSearch = !this.isMoreSearch;
      let clientHeight = document.getElementById('searchForm').clientHeight
      console.log(clientHeight)
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
  padding: 20px;
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

.centerTitle {
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: #333333;
}

.lr {
  float: right;
}

.lr.count {
  line-height: 40px;
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

::v-deep .el-table th {
  color: #606266;
  background-color: #F3F3F3;
}

::v-deep .search .el-form-item {
  margin: 10px 0;
  padding: 0 10px;
}
.displayNone {
  display: none;
}
</style>
