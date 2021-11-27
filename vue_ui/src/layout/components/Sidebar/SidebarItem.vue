<template>
  <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest}" id="menuItem">
          <i v-if="isNest" class="diandian"></i>
          <i v-if="onlyOneChild.meta.icon" :class="onlyOneChild.meta.icon"></i>
          <span >{{onlyOneChild.meta.title}}</span>
        </el-menu-item>
      </app-link>
    </template>

    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body :class="{'collapse':collapse}">
      <template slot="title">
        <i v-if="item.meta.icon" :class="item.meta.icon"></i>
        <span >{{item.meta.title}}</span>
      </template>
      <sidebar-item
        v-for="child in item.children"
        :key="child.path"
        :is-nest="true"
        :item="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
        :collapse="collapse"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path-browserify'
import { isExternal } from '@/utils/validate'
import AppLink from './Link.vue'
import FixiOSBug from './FixiOSBug'


export default {
  name: 'SidebarItem',
  components: {  AppLink },
  mixins: [FixiOSBug],
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    },
    collapse: {
      type: Boolean,
      required: true
    }
  },
  created() {
    console.log(this.item);
  },
  data() {
    this.onlyOneChild = null
    return {}
  },
  methods: {
    hasOneShowingChild(children = [], parent) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          this.onlyOneChild = item
          return true
        }
      })

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath
      }
      if (isExternal(this.basePath)) {
        return this.basePath
      }
      return path.resolve(this.basePath, routePath)
    }
  }
}
</script>
<style lang="scss" scoped>
 @import "@/assets/styles/variables";
  .diandian{
    display: inline-block;
      width: 4px;
      height: 4px;
      border-radius: 50%;
      background: $menuText;
      margin-right: 4px;
  }
 ::v-deep .el-submenu__title {
   padding-left: 17px !important;
 }
  #menuItem:hover .diandian{
    background: $menuActiveText;
  }
  #menuItem.is-active .diandian{
    background: $menuActiveText;
  }
  .iconStyle{
    margin-right: 3px;
  }
  .collapse {
    .iconStyle {
      margin-right: 3px;
      margin-left: 18px;
    }
  }
</style>
