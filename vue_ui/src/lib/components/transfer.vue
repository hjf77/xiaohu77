<template>
  <div style="text-align: center">
    <template>
      <el-transfer
        style="text-align: left; display: inline-block;width: 100%"
        v-bind="$attrs"
        v-on="$listeners"
        :filterable="filterable"
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
    filterable: {
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
    //显示的字段
    labelField: {
      type: String,
      default: () => "title",
    },
    //赋值的 字段
    valueField: {
      type: String | Number,
      default: () => "id",
    },
    methodType: {
      type: String,
      default: 'POST'
    },
    querys: {
      type: Object,
      default: () => null,
    }
  },
  data() {
    return {
      ownerOption: [],
      propsField:{
        label:null,
        key:null,
      }
    }
  },
  created() {
    this.propsField.label = this.labelField;
    this.propsField.key = this.valueField;
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
        this.$pagexRequest({
          url: this.url,
          data: this.querys,
          method: this.methodType,
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
</script>

<style lang="scss" scoped>
::v-deep .el-transfer-panel__item {
  display: block;
}
</style>
