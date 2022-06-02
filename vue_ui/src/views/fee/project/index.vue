<template>
  <base-container :isOrg="true">
    <template slot="org">
      <div>
      <el-button class="addclick" size="small" @click="addClick">新增</el-button>
      </div>
      <el-tree
      :data="treeData"
      :props="defaultProps"
      ref="tree"
      node-key = "id"
      @node-click="nodeClick"
      >
      </el-tree>
    </template>
   <div v-if="isFormShow">
      <feeProject :isFormShow.sync="isFormShow" :init="init" :isEdit="isEdit" :parentId="parentId"></feeProject>
  </div>
  <div v-else>
      <div>暂无数据</div>
  </div>

  </base-container>
</template>

<script>
import feeProject from "@/views/fee/project/components/projectForm.vue";
import crudMixins from '@/mixins/crudMixins';
export default {
  mixins: [crudMixins],
  components: {
    feeProject,
  },
  data() {
    return {
      isFormShow: false,
      addIsShow: false,
      parentId: null,
      isEdit: false,
      defaultProps:{
        label:'name',
        children:'children'
      },
      codemsg: {},
      treeData: [],
      // addApi: '/fee/ms/feeProject',
      // param: {
      //   parentId: '',
      //   data: '0102',
      //   isEnable: '0'
      // },

    };
  },
  provide() {
    return {
      reloadable: this,
    };
  },
  created() {
  },
  mounted() {
    this.getTree()

  },

  methods: {
    // 重新加载数据
    reload() {
      this.getTree();
    },
    getTree(){
      this.$pagexRequest({
            url: "/purchase/ms/feeProject/tree",
            data: {},
            method: "POST",
          }).then(res=>{
             this.treeData = res
          })
    },

    nodeClick(node){
      if(node.children.length === 0) {
        this.isEdit = true;
        this.parentId = null;
        this.init = node.data;
        this.isFormShow = false
        this.$nextTick(() => {
          this.isFormShow = true
        })
      } else {
        this.isFormShow = false
        this.parentId = node.id
        this.init = {}
      }
    },
    // 新增事件
    addClick() {
      if(!this.parentId){
          this.msgError('请选中分类节点后再点击新增按钮');
          return;
      }
      this.isEdit = false;
      this.isFormShow = false
      this.$nextTick(() => {
          this.isFormShow = true
      })
    },


  },
};
</script>
<style lang="scss">
.el-tree .el-tree-node__expand-icon.expanded {
  -webkit-transform: rotate(0deg);
  transform: rotate(0deg);
}
.el-tree .el-icon-caret-right:before {
  content: "\e723";
}
.el-tree .el-tree-node__expand-icon.expanded.el-icon-caret-right:before {
  content: "\e722";
}
.addclick{
  margin-left: 240px;
  margin-bottom: 30px;
}
</style>
