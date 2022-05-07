<!--
  模块名称：一对多表单
  开发人员：王磊
  创建时间:2021/7/26 14:23
-->
<template>
  <div>
    <!-- 编辑参数配置对话框 -->
    <pagex-form
      :isEdit="false"
      :data="formData"
      :isVee="false"
      :controls.sync="controls"
      :isHaveAddBtn="false"
      :isHaveCancelBtn="false"
      ref="testForm"
    >
    </pagex-form>
  </div>
</template>
<script>
import {mapGetters} from "vuex";

export default {
  name: "one2xform",
  computed: {
    ...mapGetters(["user"])
  },
  provide() {
    return {
      reloadable: this,
    };
  },
  data() {
    return {
      formData: {
        isEnable: 1,
        agreementNo: "202205070001",
        goods:[{  goodCode:'111'}]
      },
      controls: [
        {
          type: "label",
          name: "agreementNo",
          label: "协议号"
        },
        {
          type: "select",
          name: "isEnable",
          label: "状态",
          visibleOn: "disabled",
          dictCode: "isEnable",
          isValueNum: true,
        },
        {
          type: "text",
          name: "contractNo",
          label: "合同编号",
          rule: [{required: true, message: '请输入合同编号', trigger: 'blur'}]
        },
        {
          type: "buttons",
          name: "buttons",
          width:'1500px',
          buttons: [
            {
              name: "加一行",
              click: function (_v,_model) {
                //console.log(_v.$refs.goods[0].addRow);
                _v.$refs.goods[0].addRow();
              }
            },
            {
              name: "导入",
              click: function (_v,_model) {

              }
            },
            {
              name: "导出",
              click: function (_v,_model) {

              }
            }
          ]
        },
        {
          type: "one2x",
          name: "goods",
          width:'1500px',
          optionsSetts:[],
          defaultValue:{
            goodCode:'12345'
          },
          controls: [
            {
              type: 'text',
              name: 'goodCode',
              label: '商品编码',
              rule: [{  required: true, message: '请输入商品编码', trigger: 'blur' }],
              one2xBlur: (newValue, _datas, index,optionsSetts) => {
                _datas[index].goodName = '可口可乐500ML';
                optionsSetts[index].goodspecifications = [{
                  id: '1',
                  title: '1*9'
                }, {
                  id: '2',
                  title: '1*12'
                }];
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
      ]
    }
  },
  created() {

  },
  methods: {}
};
</script>
