<template>
  <div class="curd-table-btn-group ">

    <div class="left-btn-group">
      <el-button
        v-for="(item,index) in option"
        :key="index"
        v-if="!item.isRight"
        :type="item.type || 'primary'"
        :size="item.size || 'small'"
        :icon="item.icon"
        @click="handleClick(item)"
      >
        <svg-icon v-if="item.svgIcon" :icon-name="item.svgIcon" :icon-class="item.svgIcon"></svg-icon>
        {{item.title}}
      </el-button>

    </div>
    <div class="right-btn-group">
      <el-button
        v-for="(item,index) in option"
        :key="index"
        v-if="item.isRight"
        :type="item.type || 'primary'"
        :size="item.size || 'small'"
        :icon="item.icon"
        @click="handleClick(item)"
      >
        <svg-icon v-if="item.svgIcon" :icon-name="item.svgIcon" :icon-class="item.svgIcon"></svg-icon>
        {{item.title}}
      </el-button>

    </div>
<!--    <div v-for="(item,index) in option" class="btn-item" :key="index">-->
<!--      <div class="lf" v-if="!item.isRight">-->
<!--        <el-button-->
<!--          :type="item.type || 'primary'"-->
<!--          :size="item.size || 'small'"-->
<!--          :icon="item.icon"-->
<!--          @click="handleClick(item)"-->
<!--        >-->
<!--          <svg-icon v-if="item.svgIcon" :icon-name="item.svgIcon" :icon-class="item.svgIcon"></svg-icon>-->
<!--          {{item.title}}-->
<!--        </el-button>-->
<!--      </div>-->
<!--      <div class="lr" v-else>-->
<!--        <el-button-->
<!--          :type="item.type || 'primary'"-->
<!--          :size="item.size || 'small'"-->
<!--          @click="handleClick(item)"-->
<!--        >-->
<!--          {{item.title}}-->
<!--        </el-button>-->
<!--      </div>-->
<!--    </div>-->
<!--    <div class="clearfix"></div>-->
  </div>
</template>

<script>
export default {
  name: "table-button",
  inject:['crud'],
  props: {
    option: {
      type: Array,
      default: []
    }
  },

  methods:{

    //按钮点击事件
    handleClick(btn) {
      // debugger
      if (btn.code) {
        switch (btn.code) {
          case "add":
            this.$emit("add", btn)
            this.crud.rowAdd()
            break
          default:
        }
      }
      if(btn.click){
        btn.click.call(this,btn);
      }
    },
  }
}
</script>

<style lang="scss" scoped>
.curd-table-btn-group {
  padding-bottom:20px;
  display: table;
  width: 100%;
}
.clearfix {
  zoom: 1;
  &:before {
    content:"";
    display:block;
    height:0;
    visibility:hidden;
    clear:both;
  }
  &:after{
    content:"";
    display:block;
    height:0;
    visibility:hidden;
    clear:both;
  }
}
  .lf {
    float:left;
  }
  .lr {
    float:right;
  }

  .btn-item {
    display: inline;
  }
  .left-btn-group {
    display: table-cell;
  }
  .right-btn-group {
    display: table-cell;
    text-align: right;
  }
</style>
