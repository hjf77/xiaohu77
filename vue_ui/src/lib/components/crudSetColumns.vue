<template>
  <div>
    <pagex-dialog
      v-if="open"
      title="自定义列"
      class="pagex-dialog-theme"
      slot="form"
      :visible.sync="open"
      :namespace="namespace"
    >
      <base-container>
        <div class="columns-header">
          <el-tree
            ref="treeRef"
            class="mytree"
            :data="treeData"
            node-key="name"
            show-checkbox
            draggable
            :default-checked-keys="checkedColumns"
            :allow-drop="allowDrop"
            @node-drop="handleDrop"
            @check-change="handleCheckChange"
          >
            <span class="custom-tree-node" slot-scope="{ node, data }">
              <span>{{ node.label }}</span>
              <span>
                <span>宽度</span>
                <span>
                  <el-input size="mini" v-model.trim="data.width"></el-input>
                </span>
                <span>px</span>
              </span>
            </span>
          </el-tree>
          <el-checkbox
            class="checkbox-sty"
            :indeterminate="isIndeterminate"
            v-model="checkAllColumns"
            @change="handleCheckAllChange"
          >全选</el-checkbox>
          <div class="dialog-footer">
            <el-button size="mini" type="primary" @click="submit">确 定</el-button>
            <el-button size="mini" @click="close">取 消</el-button>
          </div>
        </div>
      </base-container>
    </pagex-dialog>
  </div>
</template>

<script>
import crudMixins from '@/mixins/crudMixins';
import { deepClone } from '@/utils/index.js';
export default {
  name: 'pagexCrudSetColumns',
  mixins: [crudMixins],
  props: {
    isOpenSetColumnsDialog: {
      type: Boolean,
      default: false,
    },
    namespace: {
      type: String,
      default: null,
    },
    crudSetColumnsApi: {
      type: String,
      default: '',
    },
    // 当前角色 所有的列
    allColumn: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      treeData: [
        {
          label: '',
          name: '',
          width: 0,
          index: 0,
          checked: true,
        },
      ],
      checkAllColumns: false,
      isIndeterminate: true,
      checkedColumns: [],
      viewAllColumn: [],
      userSettColumn: {},
    };
  },
  watch: {
    isOpenSetColumnsDialog: {
      handler(newValue, oldValue) {
        if (newValue) {
          this.open = true;
          this.treeDataSort();
        }
      },
      immediate: true,
    },
  },
  created() {},
  mounted() {},
  methods: {
    treeDataSort() {
      // 初始化数据
      let columnSettPermission = JSON.parse(localStorage.getItem('columnSett'));
      if (columnSettPermission[this.namespace]) {
        let columnJson = {};
        if (columnSettPermission[this.namespace] instanceof Object) {
          columnJson = columnSettPermission[this.namespace];
        } else {
          columnJson = JSON.parse(columnSettPermission[this.namespace]);
        }
        this.$set(this, 'userSettColumn', columnJson);
      } else {
        const userSettColumn = {};
        this.allColumn.forEach((item, index) => {
          userSettColumn[item.name] = {
            checked: true,
            label: item.label,
            width: item.width,
            index: index,
          };
        });
        this.$set(this, 'userSettColumn', userSettColumn);
      }

      this.viewAllColumn = deepClone(this.allColumn);
      this.viewAllColumn.forEach((item) => {
        if (this.userSettColumn[item.name]) {
          let sett = this.userSettColumn[item.name];
          item.checked = sett.checked;
          item.index = sett.index;
          item.width = sett.width;
        } else {
          item.checked = false;
          item.index = 100;
        }
      });

      this.viewAllColumn.sort((o1, o2) => {
        return o1.index - o2.index;
      });

      this.treeData = this.viewAllColumn;
      this.handleCheckAllChange(this.treeData, {}, 'type'); // 初始化全选按钮事件
    },
    // 拖拽时判定目标节点能否被放置
    // 'prev'、'inner' 和 'next'，分前、插入、后
    allowDrop(draggingNode, dropNode, type) {
      this.checkedColumns = this.$refs.treeRef.getCheckedNodes();
      if (draggingNode.data.level === dropNode.data.level) {
        if (draggingNode.data.parentId === dropNode.data.parentId) {
          return type === 'prev' || type === 'next';
        } else {
          return false;
        }
      } else {
        // 不同级进行处理
        return false;
      }
    },
    // 拖拽完成之后的触发的事件
    handleDrop(draggingNode, dropNode, dropType, ev) {
      this.handleSetChecked(this.checkedColumns);
    },
    // 全选按钮事件
    handleCheckAllChange(val, event, type) {
      let checkedName = [];
      if (type) {
        val.forEach((item) => {
          if (item.checked) {
            checkedName.push(item.name);
          }
        });
      } else {
        checkedName = this.treeData.map((item) => item.name);
        this.isIndeterminate = false;
        this.checkAllColumns = true;
      }
      this.checkedColumns = val ? checkedName : this.handleSetChecked([]);
    },
    // 设置tree - check事件勾选
    handleSetChecked(val) {
      this.$nextTick(() => {
        this.$refs.treeRef.setCheckedNodes(val);
      });
    },
    // 触发tree - check事件勾选
    handleCheckChange(data, checked, indeterminate) {
      let choiseCheckList = this.$refs.treeRef.getCheckedNodes();
      if (choiseCheckList && choiseCheckList.length === this.treeData.length) {
        this.checkAllColumns = true;
        this.isIndeterminate = false;
      } else if (
        choiseCheckList &&
        choiseCheckList.length &&
        choiseCheckList.length < this.treeData.length
      ) {
        this.checkAllColumns = false;
        this.isIndeterminate = true;
      } else if (choiseCheckList && !choiseCheckList.length) {
        this.checkAllColumns = false;
        this.isIndeterminate = false;
      }
    },
    // 弹框确认事件
    submit() {
      if(!this.namespace) return this.$message.warning('命名空间必填')
      // 获取选中的表头
      let choiseCheckList = this.$refs.treeRef.getCheckedNodes();
      // 后端需要的参数
      const choiseCheckNameList = choiseCheckList.map((item) => item.name);
      let newuserSettColumn = {};
      this.treeData.forEach((item, index) => {
        if (choiseCheckNameList.includes(item.name)) {
          newuserSettColumn[item.name] = {
            checked: true,
            label: item.label,
            width: item.width,
            index: index,
          };
        } else {
          newuserSettColumn[item.name] = {
            checked: false,
            label: item.label,
            width: item.width,
            index: index,
          };
        }
      });
      this.userSettColumn = newuserSettColumn;
      let url = `/basic/ms/settUserColumn`;
      let result = {
        columnJson: this.userSettColumn,
        namespace: this.namespace,
      };
      this.$pagexRequest({
        url: url,
        method: 'POST',
        data: result,
      })
        .then((res) => {
          const columnSettPermission = JSON.parse(
            localStorage.getItem('columnSett')
          );
          if (columnSettPermission[this.namespace]) {
            columnSettPermission[this.namespace] = this.userSettColumn;
            localStorage.setItem(
              'columnSett',
              JSON.stringify(columnSettPermission)
            );
          } else {
            const param = {};
            for (const key in param) {
              key = this.namespace;
              param[key] = this.userSettColumn;
            }
            localStorage.setItem(
              'columnSett',
              JSON.stringify({ ...columnSettPermission, ...param })
            );
          }
          // 关闭弹框时间
          this.$emit('isSetColumnsDialogStatus', false);
          // 选中的表头 需要更新父组件table显示字段 realColumn
          this.$emit('realColumn', choiseCheckList);
          this.open = false;
        })
        .catch((res) => {});
    },
    // 弹框取消事件
    close() {
      this.open = false;
      this.$emit('isSetColumnsDialogStatus', false);
    },
  },
};
</script>

<style scoped>
.columns-header .mytree ::v-deep .el-tree-node {
  padding: 10px;
  border-bottom: 1px solid #888888;
}
.columns-header .mytree {
  border-top: 1px solid #888888;
  border-left: 1px solid #888888;
  border-right: 1px solid #888888;
}
.columns-header
  .mytree
  ::v-deep
  .el-tree-node__content
  > .el-tree-node__expand-icon {
  padding: 0px;
  display: none;
}
.columns-header .mytree ::v-deep .el-input {
  width: 30%;
  padding: 0 10px 0 10px;
}
.columns-header .mytree ::v-deep .el-tree-node__content .custom-tree-node {
  display: flex;
  justify-content: space-between;
  flex: 1;
  line-height: 28px;
}
.checkbox-sty {
  padding: 10px 0 0 10px;
}
.dialog-footer {
  padding-top: 20px;
}
</style>
