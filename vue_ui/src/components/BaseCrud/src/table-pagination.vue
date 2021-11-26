<template>
  <el-pagination
    class="pagex-paglination"
    @size-change="handleSizeChange"
    @current-change="handleCurrentChange"
    :current-page.sync="crud.query.page"
    :page-size="crud.query.rows"
    :page-sizes="crud.page_sizes"
    layout="total, sizes, prev, pager, next, jumper"
    background
    v-show="crud.tableData.length"
    :total="crud.total"
    v-if="isNeedPager"
    :style="alignStyle"
  >
  </el-pagination>
</template>

<script>
export default {
  name: "crud-pagination",
  inject: ["crud"],
  props: {
    isNeedPager: {
      type: Boolean,
      default: true
    },
    //对其方式
    align: {
      type: String,
      default: 'right'
    }
  },

  computed: {

    alignStyle(){
      return {
        'text-align': this.align
      }
    }

  },
  methods: {
    handleSizeChange(val) {
      this.crud.query.rows = val;
      this.crud.getList();
    },
    handleCurrentChange(val) {
      this.crud.query.page = val;
      this.crud.getList();
    },

  }
}
</script>

<style lang="scss" scoped>
.pagex-paglination {
  margin: 20px 0px;
  height:40px;

}
</style>
