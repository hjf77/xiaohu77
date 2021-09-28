<!--
  模块：组织机构
  作者：SunXinHao
  邮箱：393108203@qq.com
  时间：2021年7月22日 15:56:06
  版本：v1.0
  修改记录：
  修改内容：
  修改人员：
  修改时间：
-->
<template>
  <div class="tree-container" :style="{width:width +'px'}" v-loading="loading">
    <el-input v-model="deptName"
              v-if="isFilter"
              placeholder="部门名称"
              clearable
              size="small"
              prefix-icon="el-icon-search"
    >
    </el-input>
    <el-tree
      ref="tree"
      :data="data"
      :props="defaultProps"
      node-key="id"
      :filter-node-method="filterNode"
      @node-click="handleNodeClick"
      :current-node-key="currentNodeKey"
      :default-expanded-keys="treeData"
      :expand-on-click-node="false"
    >
      <div class="custom-tree-node" slot-scope="{ node, data }">
        <svg-icon class-name="folder" icon-class="folder"></svg-icon>
            <el-tooltip :disabled="node.disabled" class="item" effect="dark" :content="data.name" placement="top">
                <!-- 大于13个字符显示省略号...； -->
                <span>{{ data.name | ellipsis(13) }}</span>
            </el-tooltip>
        
      </div>
    </el-tree>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import { deepClone } from "@/lib/utils";
export default {
  name: "Organization",
  props: {
    width: {
      type: Number,
      default: () => 300
    },
    showLoading: {
      type: Boolean,
      default: () => false
    },

    //是否开启过滤器
    isFilter: {
      type: Boolean,
      default: false
    },
    api:{
      type:String,
      default:"/ms/sysOrganization/getCompanyTree?hierarchy=2"
    },
    methodType:{
      type:String,
      default: "GET"
    },
    // 默认展开当前登陆人所在部门/单位节点
    isSpecifiedNode:{
      type: Boolean,
      default: true
    },
    // 0 单位，到二级单位 1 单位，到最低级单位 2 所有的组织机构
    type: {
      type: String,
      default: '0'
    },
    isCheckedUserCompany:{
      type: Boolean,
      default: true
    }
  },
  computed: {
    ...mapGetters(["user"]),
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
    // data: {
    //   handler() {
    //     // 我这里默认展开一级, 指定几级就往里遍历几层取到 id 就可以了
    //     this.data.forEach(item => {
    //       this.treeData.push(item.id)
    //     })
    //   },
    //   deep: true
    // }
  },
  filters:{
    ellipsis:function(value, len) {
      if (!value) return ''
      if (value.length > len) {
        return value.slice(0, len) + '...';
      }
      return value
    }
  },
  data() {
    return {
      treeData:[""],
      deptName: undefined,
      loading: false,
      currentNodeKey: "",
      data: [],
      defaultProps: {
        children: 'children',
        label: 'name',
      }
    }
  },
  created() {
    this.loadData()
    // if (this.isSpecifiedNode) {
    //   // this.treeData.push(this.user.organizationId)
    //   this.currentNodeKey = this.user.organizationId
    // } else {
    //   // this.treeData.push("")
    // }
  },
  methods: {
    /**
     * 当前点击的节点
     * @returns {<void>}
     */
    handleNodeClick(data, node, itself) {
      this.$emit("check", data.data)
      let companyIdQuery = {
         operation:  'like_r',//(this.type!='2' && data.data.id.indexOf('b')==-1) ? 'like_r' : '=',
         property: this.type=='2' ? "orgId" : "companyId",//到部门,参数为orgId；
         relation: "AND",
         value: data.data.id == '001' ? '' : data.data.id,
         isThisCompany:  this.type=='2' ? data.data.id == this.$store.state.user.user.organizationId : data.data.id == this.$store.state.user.user.companyId && data.data.id.indexOf('b')!=-1,
         id:data.data.id,
         name:data.data.name
      };
      this.$emit("change", companyIdQuery)
    },

    /**
     * 加载树数据
     * @returns {Promise<void>}
     */
    async loadData() {
      this.loading = true
      let _api = this.api
      let _method = this.methodType
      if (this.type === '1') {//所有单位
        _api = '/ms/sysOrganization/getCompanyTree?isHandleId=0'

      } else if(this.type === '2') {//到部门
        _api = '/ms/sysOrganization/selectTree'
        _method = 'POST'
      }
      try {
        const tree = await this.$pagexRequest({
          url: _api,
          method: _method,
          data:{}
          }
        )
        //判断字符过长显示不下，显示提示；
        this.isTipFn(tree);

        //处理左侧树默认展开数据；
        this.treeData = [];
        tree.forEach( item => {
          this.treeData.push(item.id);
        });
        this.treeData.push(this.$store.state.user.user.companyId);
        this.$set(this, 'data', tree || [])
        this.$nextTick(() => {
          //如果是单位 不是选择组织机构，判断是否默认要选中当前登录人所在单位
          this.currentNodeKey = (this.isCheckedUserCompany && this.type!='2') ? this.$store.state.user.user.companyId : this.data[0].id;
          //如果是部门，默认选中当前登录人的部门
          if(this.type == '2'){
            this.currentNodeKey = this.$store.state.user.user.organizationId;
          }
          this.$refs['tree'].setCurrentKey(this.currentNodeKey)
          this.handleNodeClick(this.$refs['tree'].getCurrentNode(), null, null);
        })
      } catch (err) {
      }
      this.loading = false
    },
    isTipFn(node){
      node.forEach(item => {
        //大于13个字符显示提示；
        if(item.name.length>13){
          item.disabled = false;
        }else{
          item.disabled = true;
        }
        if(item.children && item.children.length > 0){
          this.isTipFn(item.children);
        }
      })
    },

    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    }
  }
}
</script>


<style lang="scss" scoped>
.custom-tree-node {
  font-size: 14px;
  color: #222222;
}

::v-deep .el-tree-node__expand-icon {
  color: #999999;
}

::v-deep .el-tree-node__expand-icon.is-leaf {
  color: transparent;
  cursor: default;
}

::v-deep .is-current {
  > .el-tree-node__content {
    > .custom-tree-node {
      color: #0e9091;
    }
  }
}

::v-deep .el-tree-node__content {
  height: 36px;
}

.tree-container {
  background: #FFFFFF;
  border-radius: 4px;
  min-height: calc(100vh - 124px);
  padding: 20px;
  box-sizing: border-box;
  height: 100%;
}
</style>
