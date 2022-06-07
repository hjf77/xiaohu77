<template>
  <base-container :isOrg="true">
    <template slot="org">
      <div class="label-btn">
        <el-button type="text">全部展开</el-button>
        <el-button type="text">全部折叠</el-button>
        <el-button type="text">显示启用</el-button>
        <el-button type="text">显示停用</el-button>
      </div>
      <div class="tree-box">
      <div>
      <el-button class="addclick" size="small" @click="addClick">新增</el-button>
      </div>
      <el-tree class="tree-style"
      :data="treeData"
      :props="defaultProps"
      ref="tree"
      node-key = "id"
      :default-expanded-keys="idArr"
      :default-checked-keys="[]"
      @node-click="nodeClick"
      @check-change="handleCheckChange"
      >
      </el-tree>
      </div>
    </template>
    <div v-if="isFormShow">
      <feeProject  @enableFn="enableFn" :isFormShow.sync="isFormShow" :init="init" :isEdit="isEdit" :parentId="parentId"></feeProject>
    </div>
  <div v-else class="rightContent">
      <div class="text-style">暂无数据</div>
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
      idArr:[],
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
      if(node.data.level != 1) {
        this.isEdit = true;
        this.parentId = null;
        this.init = node.data;
        this.idArr.push(node.id)
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
    

    enableFn(e){
      console.log(e)
      this.isFormShow = false
      this.getTree()
        this.$pagexRequest({
        //   url:`purchase/ms/feeProject/findList/${e}`,
        //   method: "get",
        // }).then(res=>{
        //   console.log(res)
        //   this.init = res
        //   this.isFormShow = true

        url:`purchase/ms/feeProject/${e}` ,
        method: "get",
        }).then(res=>{
          this.init = res
          console.log(res)
          this.isFormShow = true
        })
    }
  },
};
</script>
<style lang="scss" scoped>
.label-btn{
  border-radius: 4px;
  background-color: #ffffff;
  // padding: 10px 0;
  height: 50px;
  display: flex;
  justify-content: space-around;
  font-size: 27px
}
.tree-box{
  border-radius: 4px;
  background-color: #ffffff;
  margin-top: 20px;
}
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
  margin: 10px 0 30px 230px;
}
.tree-style{
  margin-left:30px;
  padding-bottom: 30px;
}
.rightContent{
  position: relative;
  // display: flex;
  // justify-content: center;
  // margin: auto;
}
.text-style{
  position: absolute;
  top:50%;
  margin-top: 350px;
  left: 50%;
  margin-left: 20px;
}
</style>
