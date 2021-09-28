<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <!-- <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo">
        <h1 v-else class="sidebar-title">{{ title }} </h1>
      </router-link> -->
      <router-link key="expand" class="sidebar-logo-link" to="/">
        <img  src="@/assets/icon/sideTitle.png" style="width:100%;height:100%">
        <!-- <h1 class="sidebar-title">{{ title }} </h1>
        <h2 class="sidebar-litteTitle"> 管理信息系统 </h2> -->
      </router-link>
    </transition>
  </div>
</template>

<script>
import logoImg from '@/assets/logo/logo.png'

export default {
  name: 'SidebarLogo',
  props: {
    collapse: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      title: 'FHS-DEOM',
      logo: logoImg
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: 80px;
  line-height: 80px;
  background: #0e9091;
  text-align: center;
  overflow: hidden;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-logo {
      width: 32px;
      height: 32px;
      vertical-align: middle;
      margin-right: 12px;
    }

    & .sidebar-title {
      width: 143px;
      height: 27px;
      font-size: 27px;
      font-family: ZhenyanGB;
      font-weight: 400;
      color: #FFFFFF;
      line-height: 16px;
      text-shadow: 0px 4px 2px rgba(1, 40, 30, 0.3);
      background: linear-gradient(0deg, #33D4BA 0%, #FFFFFF 100%);
      -webkit-text-fill-color: transparent;
      background-clip: content-box;
      text-align: center;
      margin: 19px auto 9px auto;
    }
    .sidebar-litteTitle{
      width: 143px;
      height: 20px;
      font-size: 20px;
      font-family: PingFang SC;
      font-weight: bold;
      color: #9DF3E5;
      line-height: 20px;
      text-shadow: 0px 4px 2px rgba(1, 40, 30, 0.3);
      margin: 0 auto;
      letter-spacing:3px
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0px;
    }
  }
}
</style>
