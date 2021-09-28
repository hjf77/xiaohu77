<template>
  <el-table-column
    v-if="columnOption.type == 'index' "
    :label="columnOption.label"
    type="index"
    align="center"
    :width="columnOption.width || 55"
  >
  </el-table-column>
  <el-table-column
    v-else-if="columnOption.type == 'selection' "
    type="selection"
    align="center"
    :width="columnOption.width || 55"
  >
  </el-table-column>

  <el-table-column
    :prop="columnOption.name"
    :show-overflow-tooltip="true"
    v-else-if="columnOption.type === 'formart'"
    :label="columnOption.label"
    :align="columnOption.align || tableOption.align || 'center'"
    :width="columnOption.width"
  >
    <template slot-scope="scope">
      <div
        @click="proxyClick(scope.row, columnOption)"
        v-html="columnFormart(scope.row, columnOption)"
      ></div>
    </template>
  </el-table-column>





  <el-table-column
    v-else-if="columnOption.type == 'textBtn' "
    :prop="columnOption.name"
    :label="columnOption.label"
    :min-width="columnOption.minWidth"
    :width="columnOption.width"
    align="center"
  >
    <template slot-scope="scope">
      <el-button
        v-for="(btn, index) in columnOption.textBtn" :key="index"
        v-if="proxyBtnIf(scope.row, index, btn)"
        :size="btn.size || 'small'"
        :type="btn.type"
        plain
        @click="handleClick(scope.row, btn)"
      >
        {{ btn.name }}
      </el-button>
    </template>


  </el-table-column>

  <el-table-column
    v-else
    :show-overflow-tooltip="columnOption.popover"
    :prop="columnOption.name || ''"
    :label="columnOption.label"
    :min-width="columnOption.minWidth"
    :width="columnOption.width"
    :align="columnOption.align || tableOption.align || 'center'"
  >
    <template v-if="columnOption.children">
      <template v-for="(item,index ) in columnOption.children">
        <column   v-if="item.type != 'formart'" :column-option="item" :key="index"></column>
        <el-table-column
          :prop="item.name"
          :show-overflow-tooltip="true"
          v-if="item.type === 'formart'"
          :label="item.label"
          :align="item.align || tableOption.align || 'center'"
          :width="item.width"
        >
          <template slot-scope="scope">
            <div
              @click="proxyClick(scope.row, item)"
              v-html="columnFormart(scope.row, item)"
            ></div>
          </template>
        </el-table-column>
      </template>
    </template>
  </el-table-column>
</template>

<script>
import mixins from "./mixins";
export default {
  name: "column",
  inject: ["crud"],
  props: {
    tableOption: {
      type: Object,
      default: () => {
        return {};
      }
    },
    columnOption: {
      type: Object,
      required: true
    }
  },
  mixins:[mixins],
  data() {
    //权限
    return {
    }

  },
  created() {
    this.checkPermissions()
  },
  methods: {
    handleClick(row, btn) {
      const _data = {
        'row': row,
        'val': btn
      }
      if (btn.code) {
        switch (btn.code) {
          case "edit":
            this.$emit("edit", _data)
            this.crud.rowEdit(_data)
            break
          case "detail":
            this.$emit("detail", _data)
            this.crud.rowDetail(_data)
            break
          case "del":
            this.$emit("del", _data)
            console.log(this)
            this.crud.delObj(row, btn)
            break
          default:
        }
      }
      if(btn.click){
        btn.click.call(this, row, btn);
      }
    },

  }
}
</script>

<style scoped>

</style>
