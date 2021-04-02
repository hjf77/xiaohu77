<template>
  <el-select
      ref="pagexSelectRef"
      v-bind="$attrs"
      v-on="$listeners"
  >
    <template v-if="isHaveFatherOption">
      <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
      </el-option>
    </template>
    <template v-else>
      <el-option
          v-for="item in ownerOption"
          :key="item.valueField"
          :label="item.labelField"
          :value="item.valueField">
      </el-option>
    </template>
  </el-select>
</template>

<script>
import {handleStrParam} from '@/lib/utils/param'
export default {
  name: "pagexSelect",
  props: {
    options: {
      type: Array,
      default: () => undefined,
    },
    url: {
      type: String,
      default: () =>'',
    },
    labelField: {
      type: String,
      default: () =>'title',
    },
    valueField: {
      type: String,
      default: () =>'id',
    },
    param: {
      type: Object,
      default: () =>{},
    },

  },
  data() {
    return {
      ownerOption: [],
      isHaveFatherOption: false
    }
  },
  async mounted() {
    this.isHaveFatherOption = typeof (this.options) != "undefined"
    if(this.url){
      this.loadData();
    }
  },
  methods: {
    async loadData() {
      const {data} = await this.$pagexRequest.get(handleStrParam(this.url,this.param));
     
      let _options = data || [];
      let _that = this;
      _options.forEach(function (_item) {
        _item.labelField = _item[_that.labelField];
        _item.valueField = _item[_that.valueField];
      })
      if (this.isHaveFatherOption) {
        this.$emit('update:options', _options)
      } else {
        this.ownerOption = _options;
      }
    }
  }
}
</script>

<style lang="scss" scoped>

</style>