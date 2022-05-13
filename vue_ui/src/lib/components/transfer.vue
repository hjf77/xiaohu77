<template>
  <div style="text-align: center">
    <template>
      <el-transfer
        style="text-align: left; display: inline-block;width: 100%"
        v-bind="$attrs"
        v-on="$listeners"
        :filterable="isFilterable"
        :data="ownerOption"
        :titles="titles"
        :button-texts="buttonTexts"
        @change="_change"
        :format="{
        noChecked: '${total}',
        hasChecked: '${checked}/${total}'
      }"
      ></el-transfer>
    </template>
  </div>
</template>

<script>

export default {
  name: "Transfer",
  props: {
    titles: {
      type: Array,
      default: () => {
        return ['所有', '已选中']
      }
    },
    isFilterable: {
      type: Boolean,
      default: true
    },
    buttonTexts: {
      type: Array,
      default: () => {
        return ['','']
      }
    },
    url: {
      type: String,
      default: ''
    },
    httpMehod: {
      type: String,
      default: 'POST'
    },
    querys: {
      type: Object,
      default: () => null,
    },
    propsField:{
      type: Object,
      default: () => {
        return {
          label:"name",
          key:"id",
        }
      }
    }
  },
  data() {
    return {
      ownerOption: []
    }
  },
  created() {
    if (this.url) {
      this.getData();
    }
  },
  methods: {
    _change() {
      this.$emit('change')
    },

    // 加载数据
    async getData() {
      if (this.querys) {
        this.$pagexRequest({
          url: this.url,
          data: this.querys,
          method: this.httpMehod,
        }).then((res) => {
          let allData = [];
          res.forEach((item) => {
            allData.push({
              key: item[this.propsField.key],
              label: item[this.propsField.label],
            })
          })
          this.ownerOption = allData
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-transfer-panel__item {
  display: block;
}
</style>
