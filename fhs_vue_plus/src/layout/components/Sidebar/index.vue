<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <img src="@/assets/icon/sideBarUpBg.png" class="sideBarUpBg">
    <el-scrollbar wrap-class="scrollbar-wrapper" class="menuBgImg">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        background-color="rgba(0,0,0,0)"
        :text-color="variables.menuText"
        :unique-opened="true"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item v-for="route in permission_routes" :key="route.path" :item="route" :base-path="route.path" :collapse="isCollapse"/>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Logo from './Logo'
import SidebarItem from './SidebarItem'
import variables from '@/assets/styles/variables.scss'

export default {
  components: { SidebarItem, Logo },
  data(){
    return{}
  },
  computed: {
    ...mapGetters([
      'permission_routes',
      'sidebar'
    ]),
    activeMenu() {
      const route = this.$route
      const { meta, path } = route
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu
      }
      return path
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo
    },
    variables() {
      return variables
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>
<style lang="scss" scoped>
.sideBarUpBg{
  height: 69px;
  width: 100%;
}
.menuBgImg{
  background-image:url('../../../assets/icon/sideBarButtonBg.png');
  background-repeat: no-repeat;
  background-size: contain;
  background-position: 0 bottom;
}
</style>
