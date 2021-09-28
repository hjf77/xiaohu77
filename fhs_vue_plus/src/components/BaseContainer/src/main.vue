<template>
  <div class="base-container" :class="{isOrg:isOrg}" v-loading="loading">
      <div class="left-box" v-if="isOrg">
          <slot name="org"></slot>
      </div>
      <div class="right-box clearfix">
        <slot></slot>
      </div>
  </div>
</template>

<script>
export default {
  name: "BaseContainer",
  data(){
    return {
      loading:false
    }
  },
  computed:{
    isOrg(){
        return this.$slots.org
    }
  },

}
</script>

<style lang="scss" scoped>
  .base-container {
    width:100%;
    height:100%;
    padding:20px;
    box-sizing: border-box;
    position: relative;
    background: #F7F7F7;
    >.left-box {
      width:300px;
      height:100%;
      clear: both;
      float:left;
    }

  }
  .isOrg {
    .right-box {
      width: calc(100% - 320px);
      margin-left: 320px;
      min-height: calc(100% - 40px);
    }
  }

  .clearfix {
    &:after {
      visibility: hidden;
      display: block;
      font-size: 0;
      content: " ";
      clear: both;
      height: 0;
    }
  }
</style>
