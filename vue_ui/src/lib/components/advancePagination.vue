<template>
  <div>
    <el-select v-model="newValue" placeholder=" " style="width: 80px">
      <el-option
        v-for="(item, index) in pageSizes"
        :key="index"
        :label="item"
        :value="item">
      </el-option>
    </el-select>
    <el-button :disabled="pageQuery.pageNumber === 1" @click="homePage" style="margin-left: 10px"> 首页</el-button>
    <el-button :disabled="pageQuery.pageNumber === 1" @click="prevPage"> 上一页</el-button>
    <el-button :disabled="isEndPage" @click="nextPage"> 下一页</el-button>
  </div>
</template>

<script>
export default {
  name: "pagexPagination",
  props: {
    value: {
    },
    pageSizes: {
      type: Array,
      default: () => [20, 100, 200, 300, 400]
    },
    pageQuery: {
      require: true,
      type: Object,
      default: () => {
        return {
          pageNumber: 1,
          pageSizeNumber: 10
        }
      }
    },
    isEndPage: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      newValue: undefined,
      prevText: '上一页',
      nextText: '下一页'
    };
  },
  watch:{
    value:{
      handler:function(){
        this.newValue = this.value
      },
      immediate: true
    },
    newValue: {
      handler:function(){
        this.$emit('update:pageQuery', {
          pageSizeNumber: this.newValue,
          pageNumber: 1,
          params: this.pageQuery.params
        })
        this.$parent.getList()
      },
    }
  },
  methods: {
    /**
     * 首页
     */
    homePage() {
      this.$emit('update:pageQuery', {
        pageSizeNumber: this.newValue,
        pageNumber: 1,
        params: this.pageQuery.params
      })
      this.$parent.getList()
    },
    /**
     * 上一页
     */
    prevPage() {
      this.$emit('update:pageQuery', {
        pageSizeNumber: this.newValue,
        pageNumber: this.pageQuery.pageNumber - 1,
        params: this.pageQuery.params
      })
      this.$parent.getList()
    },
    /**
     * 下一页
     */
    nextPage() {
      this.$emit('update:pageQuery', {
        pageSizeNumber: this.newValue,
        pageNumber: this.pageQuery.pageNumber + 1,
        params: this.pageQuery.params
      })
      this.$parent.getList()
    }
  },
};
</script>

<style lang="scss" scoped>

</style>
