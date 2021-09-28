<template>
  <div style="text-align: center">
    <template>
      <el-transfer
        style="text-align: left; display: inline-block;width: 100%"
        v-bind="$attrs"
        v-on="$listeners"
        v-model="text"
        :filterable="isFilterable"
        :data="ownerOption"
        :render-content="renderFunc"
        @change="handleChange"
        :titles="titles"
        :button-texts="buttonTexts"
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
        return ['所有老师', '选中老师']
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
    value: {},
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
      },
    }
  },
  data() {
    return {
      ownerOption: [],
      text: [],
      renderFunc(h, option) {
        return <span>{ option.label }</span>;
      }
    }
  },
  watch: {
    value: {
      handler() {
        this.init()
      }
    },

    text:{
      handler(){
        this.updateValue()
      }
    }
  },
  created() {
    if (this.url) {
      this.getData();
    }
  },
  methods: {
    handleChange(value, direction, movedKeys) {
      this.$nextTick(() => {
        this.$emit("change", this.text, this.querys)
      });
    },
    init() {
      this.text = this.value
    },

    updateValue(){
      this.$emit("change", this.text, this.querys)
    },

    // 左侧全量数据
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
              label: `${item[this.propsField.label]}`,
            })
          })
          this.ownerOption = allData
          if (this.value) {
            this.text = this.value
          }
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
