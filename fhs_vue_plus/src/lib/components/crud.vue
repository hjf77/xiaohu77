<template>
  <div>
    <el-form size="small" :inline="true" :model="query" v-if="filters">
      <el-form-item
        :label="item.formname"
        v-for="item in filters"
        :key="item.name"
      >
        <el-input
          v-model="query.params[item.name]"
          v-if="item.type === 'text'"
          :placeholder="item.placeholder"
        ></el-input>

        <pagex-select
          v-bind="item"
          v-model="query.params[item.name]"
          v-if="item.type === 'select'"
          placeholder="请选择"
        ></pagex-select>

        <el-date-picker
          v-model="query.params[item.name]"
          v-if="item.type === 'date-picker'"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        >
        </el-date-picker>
        <el-date-picker
          v-model="query.params[item.name]"
          v-if="item.type === 'datetimerange'"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item v-if="filters.length">
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="search"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="reset"
          >重置</el-button
        >
      </el-form-item>
    </el-form>
    <div style="margin-bottom: 20px">
      <el-button
        v-for="(i, v) in buttons"
        :key="v"
        v-hasPermi="i.permission ? i.permission : 'none'"
        v-if="i.url != ''"
        :type="i.type"
        :icon="i.icon"
        size="mini"
        @click="check(i)"
        >{{ i.title }}
      </el-button>
    </div>
    <el-table
      style="width: 100%"
      :data="data"
      @selection-change="handleSelectionChange"
      @row-click="LineClick && rowClick(arguments)"
      :highlight-current-row="highlight"
      :height="height"
    >
      <template v-for="(item, index) in columns">
        <el-table-column
          :key="index"
          v-if="item.type === 'selection'"
          type="selection"
        >
        </el-table-column>

        <el-table-column
          :key="index"
          v-else-if="item.type === 'index'"
          type="index"
          :index="indexMethod"
          :label="item.label"
          :width="item.width"
          :fixed="item.fixed"
        ></el-table-column>

        <el-table-column
          :prop="item.name"
          v-else-if="!item.type"
          :label="item.label"
          :key="item.name"
        >
        </el-table-column>
        <el-table-column
          :prop="item.name"
          :show-overflow-tooltip="true"
          v-else-if="item.type === 'formart'"
          :label="item.label"
          :key="'operation' + index"
        >
          <template slot-scope="scope">
            <div
              @click="proxyClick(scope.row, item)"
              v-html="columnFormart(scope.row, item)"
            ></div>
          </template>
        </el-table-column>

        <el-table-column
          :prop="item.name"
          v-else-if="item.type === 'popover'"
          :label="item.label"
          :key="'operation' + index"
          :show-overflow-tooltip="true"
          :width="item.width"
          align="center"
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

        <el-table-column
          :prop="item.name"
          v-else-if="item.type === 'filters'"
          :label="item.label"
          :key="'operation' + index"
          :fixed="item.fixed"
        >
          <template slot-scope="scope">
            <div v-for="i in item.filter">
              <div v-if="i.name == scope.row[item.name]">{{ i.filters }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column
          :prop="item.name"
          v-else-if="item.type === 'isShared' || item.type === 'switch'"
          :label="item.label"
          :key="'operation' + index"
          :fixed="item.fixed"
        >
          <template slot-scope="scope">
            <el-switch
              v-model="switchValue[index + '_' + item.name]"
              v-if="switchHandel(item, scope.row, index)"
              active-color="#13ce66"
              @change="switchChange(item, scope.row)"
              inactive-color="#909399"
            >
            </el-switch>
          </template>
        </el-table-column>

        <el-table-column
          :prop="item.name"
          v-else-if="item.type === 'textBtn'"
          :label="item.label"
          fixed="right"
          :width="item.width"
        >
          <template slot-scope="scope">
            <el-button
              v-for="(val, index) in item.textBtn"
              v-if="
                proxyBtnIf(scope.row, index, val) &&
                jurisdiction(scope.row, val.displayStatus)
              "
              :key="index"
              type="text"
              @click="handleClick(scope.row, val)"
            >
              {{ val.name }}
            </el-button>
          </template>
        </el-table-column>
      </template>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="query.page"
      :page-size="query.rows"
      :page-sizes="page_sizes"
      layout="total, sizes, prev, pager, next, jumper"
      background
      v-show="data.length"
      :total="total"
    >
    </el-pagination>

    <slot name="form"></slot>
  </div>
</template>

<script>
import { handleStrParam } from "@/lib/utils/param";
import Dialog from "@/lib/components/dialog";
import $moment from "moment";
import { checkPermi } from "@/utils/permission";
export default {
  components: { Dialog },
  props: {
    buttons: {
      type: Array,
      default: () => [],
    },
    LineClick: {
      type: Boolean,
      default: false,
    },
    pagination: {
      type: Boolean,
      default: false,
    },
    id: {
      type: String,
      default: "",
    },
    uid: {
      type: [String, Number],
      default: "default",
    },
    api: {
      type: String,
      default: "",
    },
    paramsQuery: Object,
    title: {
      type: String,
      default: "",
    },
    slots: {
      type: Object,
      default: () => ({}),
    },
    columns: {
      type: Array,
      default: () => [],
    },
    filters: {
      type: Array,
      default: () => [],
    },
    methods: {
      type: Object,
      default: () => ({}),
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
  },
  data() {
    return {
      switchValue: {}, //列表开关上的状态
      data: [],
      multipleSelection: [],
      total: 0,
      page_sizes: [10, 20, 50, 100],
      query: {
        rows: 10,
        page: 1,
        params: {},
      },
      rowPermissions: {},
    };
  },
  provide() {
    return {
      reloadable: this,
    };
  },
  created() {
    if (this.minPageSize) {
      this.query.rows = this.minPageSize;
      this.page_sizes.unshift(this.minPageSize);
    }
    this.columns.forEach((_item, _index) => {
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
  mounted() {},
  methods: {
    proxyBtnIf(_row, _btnIndex, _btn) {
      if (!this.rowPermissions[_btnIndex]) {
        return false;
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
    /**
     * Login_person当前登录人
     * share共享
     **/
    jurisdiction(_row, control) {
      if (control) {
        if (control.length > 1) {
          let statusList = [];
          control.forEach((item) => {
            if (item == "Login_person") {
              statusList.push(
                this.$store.getters.userInfo.username == _row.createByName
                  ? true
                  : false
              );
            } else if (item == "share") {
              statusList.push(_row.isShared == "0" ? true : false);
            }
          });
          if (statusList.findIndex((target) => target === false) == -1) {
            return true;
          } else {
            return false;
          }
        } else {
          return control[0] == "Login_person"
            ? this.$store.getters.userInfo.username == _row.createByName
              ? true
              : false
            : control[0] == "share"
            ? _row.isShared == "0"
              ? true
              : false
            : true;
        }
      } else {
        return true;
      }
    },
    switchHandel(item, _row, index) {
      this.switchValue[index + "_" + item.name] = _row[item.name] == 1;
      return true;
    },
    switchChange(item, _row) {
      let _data = { id: _row.id };
      _data[item.name] = _row[item.name] == "0" ? "1" : "0";
      this.$pagexRequest({
        url: item.url,
        data: _data,
        method: "PUT",
      }).then((res) => {
        if (res.state) {
          this.msgSuccess(res.value);
          this.getList();
        } else {
          this.msgError(res.value);
        }
      });
    },

    formartReqFilter: function () {
      let result = {
        groupRelation: "AND",
        pageBean: {
          page: this.query.page,
          pageSize: this.query.rows,
          showTotal: true,
        },
        params: this.paramsQuery,
        querys: JSON.parse(JSON.stringify(this.querys)),
        sorter: this.sortSett,
        // useJson: true
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
      // console.log(result);
      return result;
    },
    datePicker(val) {
      let arr = [];
      arr.push($moment(val[0]).format("YYYY-MM-DD HH:mm:ss"));
      arr.push($moment(val[1]).format("YYYY-MM-DD HH:mm:ss"));
      return arr;
    },
    handleClick(row, val) {
      // console.log(row, val);
      if (val.name == "删除") {
        this.$confirm("是否删除此数据?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            //isDelPathId 如果为true，则使用/dsp/****/v1/159，否则 /dsp/***/v1/?id=159
            let url = val.isDelPathId
              ? `${val.api}${row.id}`
              : `${val.api}?ids=${row.id}`;
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
                } else {
                  this.$message({
                    type: "error",
                    message: res.message,
                  });
                }
              })
              .catch((res) => {
                this.getList();
                console.log(res);
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
      if (val.name == "del") {
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
      } else if (val.name == "share") {
        if (this.multipleSelection.length == 0) {
          this.$message({
            type: "error",
            message: "暂未勾选任何数据!",
          });
        } else {
          this.$confirm("确定共享选中项吗?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
            .then(() => {
              let data = [];
              this.multipleSelection.forEach((item) => {
                data.push(item.id);
              });
              this.$pagexRequest({
                url: val.api,
                method: "post",
                data: {
                  ids: data,
                  isShared: "1",
                },
              })
                .then((res) => {
                  if (res.state) {
                    this.getList();
                    this.$message({
                      type: "success",
                      message: "共享成功!",
                    });
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
                message: "已取消共享",
              });
            });
        }
      } else if (val.name == "sharedel") {
        if (this.multipleSelection.length == 0) {
          this.$message({
            type: "error",
            message: "暂未勾选任何数据!",
          });
        } else {
          this.$confirm("确定取消共享选中项吗?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          })
            .then(() => {
              let data = [];
              this.multipleSelection.forEach((item) => {
                data.push(item.id);
              });
              this.$pagexRequest({
                url: val.api,
                method: "post",
                data: {
                  ids: data,
                  isShared: "0",
                },
              })
                .then((res) => {
                  if (res.state) {
                    this.getList();
                    this.$message({
                      type: "success",
                      message: "取消共享成功!",
                    });
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
                message: "已取消共享",
              });
            });
        }
      } else {
        if (val.click) {
          val.click.call(this, this.multipleSelection);
        }
      }
    },
    //多选框
    handleSelectionChange(val) {
      // console.log(val);
      this.multipleSelection = val;
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
      this.query.page = 1;
      this.getList();
      this.$emit("search", true);
    },
    //重置
    reset() {
      this.query.params = {};
      this.search();
    },
    reload(_groupName) {
      this.search();
    },
    async getList() {
      if (this.api) {
        const data = await this.$pagexRequest({
          url: this.api,
          data: this.formartReqFilter(),
          method: "POST",
        });
        if (data.rows == undefined) {
          if (data.length != 0) {
            this.data = data || [];
          } else {
            this.data = [];
            this.$emit("nullvalue", 0);
          }
        } else {
          if (data.rows.length != 0) {
            this.data = data.rows || [];
          } else {
            this.data = [];
            this.$emit("nullvalue", 0);
          }
        }
        if (data.total) {
          this.total = data.total;
        }
      }
    },
    handleSizeChange(val) {
      this.query.rows = val;
      this.getList();
    },
    handleCurrentChange(val) {
      this.query.page = val;
      this.getList();
    },
    indexMethod(index) {
      index = index + 1 + (this.query.page - 1) * this.query.rows;
      return index;
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-table .cell {
  display: flex;
}

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
  margin: 20px 20px 20px 0;
}
</style>
