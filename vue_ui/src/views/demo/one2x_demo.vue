<template>
  <div>
    <el-form :model="model">
      <el-button @click="addRow">加一行</el-button>
      <el-button @click="consoleJson">控制器打印json</el-button>
      <pagex-one2x :controls="controls" attrName="goods" ref="goods" v-model="model.goods" :init="init" :optionsSetts.sync="optionsSetts" :defaultValue="{
    goodCode:'12345'
  }" :isEdit="false"></pagex-one2x>
    </el-form>
  </div>
</template>
<script>

export default {
  name: "buyGoods",
  data() {
    return {
      model:{
        goods: [{  goodCode:'111'}],
      },
      optionsSetts:[],
      init: function (rows, optionsArray) {
        rows.forEach((index, item) => {
          // item.goodCode  去ajax
         /* this.optionsSetts.goodspecifications = [{
            id: '1',
            name: '1*9'
          }, {
            id: '2',
            name: '1*12'
          }];*/
        })
      },
      controls: [
        {
          type: 'text',
          name: 'goodCode',
          label: '商品编码',
          rule: [{  required: true, message: '请输入商品编码', trigger: 'blur' }],
          one2xBlur: (newValue, _datas, index) => {
            //ajax
            _datas[index].goodName = '可口可乐500ML';
            this.optionsSetts[index].goodspecifications = [{
              id: '1',
              title: '1*9'
            }, {
              id: '2',
              title: '1*12'
            }];

            /*options.goodspecifications = [{
              id: '1',
              title: '1*9'
            }, {
              id: '2',
              title: '1*12'
            }];*/
          },
          import: true,
          export: true,
          notRepeat: true,
          with: 100,
        },
        {
          type: 'label',
          name: 'goodName',
          label: '商品名称'
        },
        {
          type: 'select',
          name: 'goodspecifications',
          label: '规格',
          options:[{
            id: '1',
            title: '1*12'
          }]
        },
        {
          type: 'select',
          name: 'taxRadio',
          dictCode: 'taxRadio',
          label: '税率',
          one2xSelectOn: (newValue, _row, index, options) => {

          }
        },
      ],
    }
  },
  methods: {
    addRow() {
      this.$refs['goods'].addRow();
    },
    consoleJson(){
      console.log(this.goods);
    }
  }
}
</script>
