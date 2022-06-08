<template>
  <base-container :isOrg="true">
    <template slot="org">
      <div class="label-btn">
        <el-button type="text" @click="expandTree">全部展开</el-button>
        <el-button type="text" @click="collapseTree">全部折叠</el-button>
        <el-button type="text" @click="getQi">显示启用</el-button>
        <el-button type="text" @click="getTing">显示停用</el-button>
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
      :default-expand-all = "isExpand"
      :default-expanded-keys="idArr"
      :default-checked-keys="[]"
      :filter-node-method="filterNode"
      @node-click="nodeClick"
      @check-change="handleCheckChange"
      >
      <span class="custom-tree-node" slot-scope="{ node }">  
        <span :title="node.label" class="em-tree-text">{{ node.label }}</span>  
      </span>
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
      parentId: null,
      idArr:[],
      isEdit: false,
      defaultProps:{
        label:'name',
        children:'children'
      },
      codemsg: {},
      treeData: [],
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
      console.log(node)
      if(node.data.level != 1) {
        this.isEdit = true;
        this.parentId = null;
        this.init = node.data;
        node.data.isUpdate=true
        this.idArr.push(node.id)
        console.log(node.id)
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
    //页面刷新
    enableFn(e){
      this.isFormShow = false
      console.log(e)
      if(e != undefined){
        this.getTree()
        this.$pagexRequest({
        url:`purchase/ms/feeProject/${e}` ,
        method: "get",
        }).then(res=>{
            this.init = res
            this.isFormShow = true
        })
      }
    },

    //展开树
    expandTree(){
      // this.$pagexRequest({
      //     url: "/purchase/ms/feeProject/tree",
      //     data: {},
      //     method: "POST",
      // }).then(res=>{
      //     this.treeData = []
      //     this.treeData = res
      //      // console.log(this.treeData)
      //     //  this.expandFunc(this.treeData)
      // })
      this.isExpand = true
      this.expandFunc(this.treeData)
    },
    collapseTree(){
      this.isExpand = false
      this.expandFunc(this.treeData)
    },
        
	// 遍历树形数据，通过设置每一项的expanded属性，实现展开与折叠
    expandFunc(data) {
        data.forEach(item=> {
          this.$refs.tree.store.nodesMap[item.id].expanded = this.isExpand
          if (item.children.length>0) {
            this.expandFunc(item.children)
          }
        })
    },
    filterNode(value, treeData){
      if (!value) return true;
      return treeData.data.isEnable== value ;
    },    //显示启用
    getQi(){
       // this.filterNode(,this.treeData)
      this.$refs.tree.filter('1')
    },
    // 显示停用
    getTing(){
      this.$refs.tree.filter('0')
    },
    

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
.custom-tree-node {
  // flex: ;
display: flex;//这里的display属性不用修改（使树节点末尾的按钮右对齐）
align-items: center;
justify-content: space-between;
font-size: px;
padding-right: px;
overflow: hidden;
white-space: nowrap;
text-overflow: ellipsis;
width: 100%;//宽度必须是这个，不能使用px或者min-width或者min-width等，因为外层使用了el-card包裹是可以拉动的，换言之树节点的宽度随时可变
& span.em-tree-text{
    display: inline-block;//block一样
overflow: hidden;
white-space: nowrap;
width:100%;
text-overflow: ellipsis;
  }
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
