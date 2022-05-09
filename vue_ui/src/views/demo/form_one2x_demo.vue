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
import {postFile} from "../../utils/download";
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
        goods:[{  goodCode:'111'}],
        total : 1,
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
          disabledOn: "disabled",
          dictCode: "isEnable",
          isValueNum: true,
        },
        {
          type: "select",
          name: "contractNo",
          url:"/basic/ms/dictItem/findList?dictGroupCode=",
          labelField: "dictDesc",
          valueField: "dictGroupCode",
          remote:true,
          label: "合同编号",
          rule: [{required: true, message: '请输入合同编号', trigger: 'change'}]
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
              name: "导出模板",
              click:  (_v,_model) =>{
                postFile('/basic/excel_template/exportTemplate?name=商品导入模板',this.controls[4].controls);
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
          //当数据发生改变的时候触发
          onDataChange:(_newDatas)=>{
              this.$refs.testForm.setModelProp('total',_newDatas.length);
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
              cellWidth: '10',
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
              import: true,
              cellWidth: '10',
              one2xSelectOn: (newValue, _row, index, options) => {

              }
            },
          ],
        },
        {
          type: "labels",
          name: "labelsxx",
          width:'1500px',
          labels: [
            {
              title: "商品品项数",
              name: "total",
            }
          ]
        },
      ]
    }
  },
  created() {

  },
  methods: {

  }
};
</script>
