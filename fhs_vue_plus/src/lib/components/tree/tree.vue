<!--树形组件-->
<template>
  <div class="mytree">
    <el-tree v-if="isView"
      :indent="0"
      class="tree-line"
      :data="dataList"
      :default-expand-all="false"
      @node-click="handleNodeClick"
      :expand-on-click-node="false"
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <el-tooltip
          class="item"
          effect="light"
          :content="data.name"
          placement="top"
          :open-delay="500"
          v-if="data.name.length >= length"
        >
          <span v-if="data.isBold === 1" style="font-weight:bold;font-size: 16px;cursor:pointer;">{{ data.name.substring(0, length) }}...</span>
          <span v-else>{{ data.name.substring(0, length) }}...</span>
        </el-tooltip>
        <template v-else>
            <span v-if="data.isBold === 1" style="font-weight:bold;font-size: 16px;cursor:pointer;">{{ data.name }}</span>
            <span v-else>{{ data.name }}</span>
        </template>
        <div
          class="operation"
          v-if="
            (data.isAddable == 1 ||
              data.isDelable == 1 ||
              data.isEditable == 1) &&
            (append != '' || remove != '' || modify != '') &&(
             isAdd  ||  isDel || isEdit )
          "
        >
          <el-dropdown>
            <span class="el-dropdown-link">
              <i class="el-icon-more"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <template v-for="value in append">
                <span @click.stop="increase(value, data.data)" :key="value.id">
                  <el-dropdown-item
                    icon="el-icon-plus"
                    v-if="
                      value.addable == true &&
                      data.isAddable == 1 &&  isAdd
                    "
                    >增加</el-dropdown-item
                  >
                </span>
              </template>
              <template v-for="value in remove">
                <span @click.stop="deletes(value, data)" :key="value.id">
                  <el-dropdown-item
                    icon="el-icon-delete"
                    v-if="
                      value.delable == true &&
                      data.isDelable == 1 && isDel
                    "
                    >删除</el-dropdown-item
                  >
                </span>
              </template>
              <template v-for="value in modify">
                <span @click.stop="update(value, data.data)" :key="value.id">
                  <el-dropdown-item
                    icon="el-icon-edit"
                    v-if="
                      value.editable == true &&
                      data.isEditable == 1 && isEdit
                    "
                    >修改</el-dropdown-item
                  >
                </span>
              </template>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </span>
    </el-tree>
    <slot name="form"></slot>
  </div>
</template>
<script>
import { getData } from "@/api/tree";
import { checkPermi } from "@/utils/permission";
import { jurisdictionmethod } from "@/utils/jurisdiction";
export default {
  props: {
    treeUrl: String,
    querys: Object,
    length: Number,
    jurisdiction: String,
    selectedDefault: {
      type: Boolean,
      default: false,
    },
    append: {
      type: Array,
      default: () => [],
    },
    remove: {
      type: Array,
      default: () => [],
    },
    modify: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      dataList: [],
      type: false,
      // 要判断的权限字段
      PermissionField: ["add", "update", "del", "view"],
      isView:false,
      isEdit:false,
      isDel:false,
      isAdd:false,
      storageRights: [],
    };
  },
  provide() {
    return {
      reloadable: this,
    };
  },
  mounted() {
    this.brush();
    this.permissionMethod();
  },
  methods: {
    // 请求数据
    RequestData() {
      getData(this.treeUrl, this.querys)
        .then((res) => {
          // console.log('this.selectedDefault===>' + this.selectedDefault)
          // console.log('this.type===>' + this.type)
          // if (this.selectedDefault && this.type == false) {
          if (this.selectedDefault) {
            // this.selected();
            this.dataList = res;
            this.type = true;
          } else {
            this.dataList = res;
          }
        })
        .catch((error) => {
          console.log(error);
        }).finally(()=>{
          this.$emit('getTree',this.dataList)
        });
    },
    // 权限方法
    permissionMethod() {
      if (this.jurisdiction != undefined) {
          this.isView = checkPermi([this.jurisdiction + ':view']);
          this.isAdd = checkPermi([this.jurisdiction + ':add']);
          this.isDel = checkPermi([this.jurisdiction + ':del']);
          this.isEdit = checkPermi([this.jurisdiction + ':update']);
          if(this.isAdd || this.isEdit || this.isDel){
              this.isView = true
          }
      }else{
          this.isView = true;
          this.isAdd = true;
          this.isDel = true;
          this.isEdit = true;
      }
    },
    //增加
    increase(e, data) {
      e.click.call(this, data);
    },
    //删除
    deletes(e, data) {
      if (e.delUrl) {
        this.$confirm(`是否删除${data.name}吗?`, "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }).then(() => {
          this.$pagexRequest({
            url: e.delUrl + data.id,
            method: "DELETE",
          })
            .then((res) => {
              if (res.state) {
                this.brush();
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
            .catch(() => {});
        });
      } else {
        e.click.call(this, data);
      }
    },
    // 修改
    update(e, data) {
      e.click.call(this, data);
    },
    // 点击时候的回调
    handleNodeClick(e) {
      this.$emit("clickBack", e);
    },
    // 刷新
    brush() {
      this.RequestData();
    },
    reload() {
      this.brush();
    },
    // 默认选中第一个
    selected() {
      this.$nextTick().then(() => {
        const firstNode = document.querySelector(".el-tree-node");
        firstNode.click();
      });
      // this.$nextTick(()=>{
      //   const firstNode = document.querySelector(".el-tree-node");
      //   firstNode.click();
      // })
    },
    loadNode(node, resolve) {
      if (node.level === 0) {
        return resolve([{ name: "region1" }, { name: "region2" }]);
      }
      if (node.level > 3) return resolve([]);

      var hasChild;
      if (node.data.name === "region1") {
        hasChild = true;
      } else if (node.data.name === "region2") {
        hasChild = false;
      } else {
        hasChild = Math.random() > 0.5;
      }

      setTimeout(() => {
        var data;
        if (hasChild) {
          data = [
            {
              name: "zone" + this.count++,
            },
            {
              name: "zone" + this.count++,
            },
          ];
        } else {
          data = [];
        }

        resolve(data);
      }, 500);
    },
  },
};
</script>

<style scoped>


.operation {
  position: absolute;
  right: 0;
  top: 5px;
  background: white;
}

.mytree >>> .tree-line .el-tree-node {
  padding-left: 8px !important;
  font-size: 14px;
}

.mytree >>> .tree-line .el-tree-node::before {
  border-left: 1px dashed #e0e0e0;
}

.mytree >>> .tree-line .el-tree-node::after {
  border-top: 1px dashed #e0e0e0;
}

.mytree >>> .tree-line .el-tree-node__expand-icon.is-leaf::before {
  border-top: 1px dashed #e0e0e0;
}
</style>
