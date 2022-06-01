<template>
  <base-container>
    <pagex-form
      v-if="initFinsh"
      :isEdit="false"
      :data="formData"
      :isVee="false"
      :controls.sync="controls"
      :isHaveSaveBtn="false"
      :isHaveAddBtn="false"
      :init="init"
      :optionsInitSetts="optionsInitSetts"
      :isHaveCancelBtn="false"
      ref="testForm"
    >
    </pagex-form>
  </base-container>
</template>

<script>
import {mapGetters} from "vuex";
import {postFile} from "@/utils/download";
import crudMixins from "@/mixins/crudMixins";
import {deepClone} from "../../../utils";

export default {
  name: "AgreementInput",
  mixins: [crudMixins],
  props: {
    init: Object,
  },
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
      optionsInitSetts: [],
      initFinsh: false,
      formData: {
        status: '未审核',
        agreementNo: "202205070001",
        createTime: '',
        createUser: '',
        updateTime: '',
        updateUser:'',
        orgId: '',
        orgName: '',
        // goods:[{  goodCode:'111'}],
        goods: [],
        total : 1
      },
      controls: [
        {
          type: "label",
          name: "agreementNo",
          label: "协议号"
        },
        {
          type: "label",
          name: "status",
          label: "状态",
        },
        {
          type: "select",
          name: "orderPartyId",
          url:"/basic/ms/dictItem/findList?dictGroupCode=",
          labelField: "dictDesc",
          valueField: "dictCode",
          remote:true,
          label: "订单方",
          rule: [{required: true, message: '请输入订单方', trigger: 'change'}]
        },
        {
          type: "select",
          name: "contractNo",
          url:"/basic/ms/dictItem/findList?dictGroupCode=",
          labelField: "dictDesc",
          valueField: "dictCode",
          remote:true,
          label: "合同编号",
          rule: [{required: true, message: '请输入合同编号', trigger: 'change'}]
        },
        {
          type: "dates",
          name: "startTime",
          label: "开始时间",
          formatVal: "yyyy-MM-dd",
          rule: [{required: true, message: '请输入合同编号', trigger: 'change'}]
        },
        {
          type: "dates",
          name: "endTime",
          label: "结束时间",
          formatVal: "yyyy-MM-dd",
          rule: [{required: true, message: '请输入合同编号', trigger: 'change'}]
        },
        {
          type: "text",
          name: "orgName",
          label: "所属组织",
          placeholder: ' ',
          disabledOn: "disabled"

        },
        {
          type: "text",
          name: "createUser",
          label: "创建人",
          placeholder: ' ',
          disabledOn: "disabled"
        },
        {
          type: "text",
          name: "createTime",
          label: "创建时间",
          placeholder: ' ',
          disabledOn: "disabled"
        },
        {
          type: "text",
          name: "updateUser",
          label: "最后修改人",
          placeholder: ' ',
          disabledOn: "disabled"
        },
        {
          type: "text",
          name: "updateTime",
          label: "最后修改时间",
          placeholder: ' ',
          disabledOn: "disabled"
        },
        {
          type: "textarea",
          name: "remark",
          label: "备注",
        },
        {
          type: "buttons",
          name: "buttons",
          width:'1500px',
          buttons: [
            {
              name: "保存",
              click: function (_form, _model) {
                _form.validate(function(_newModel){
                  console.log(_newModel);
                })
              },
            },
            {
              name: "审核",
              click: function (_v,_model) {

              }
            },
            {
              name: "删除",
              click: function (_v,_model) {

              }
            },
            {
              name: "加一行",
              click: function (_v,_model) {
                //console.log(_v.$refs.goods[0].addRow);
                _v.$refs.goods[0].addRow();
              }
            },
            {
              name: "模拟导入(append后台解析数据)",
              click: function (_v,_model) {
                _v.$refs.goods[0].appendRows([{goodCode:'1314111',goodName:'白象方便面',goodspecifications:'1',taxRadio:1}] ,[{goodspecifications:[{
                    id: '1',
                    title: '24袋一箱'
                  }, {
                    id: '2',
                    title: '12袋一箱'
                  }]}]);
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
          label: "",
          optionsSetts:[],
          defaultValue:{
            goodCode:''
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
              one2xBlur: (newValue, _datas, index,optionsSetts,_inputThis) => {
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
              options:[]
            },
            {
              type: 'select',
              name: 'taxRadio',
              label: '税率',
              options:[],
              change: (newValue, _row, index, options) => {

                // debugger

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
        }
      ]
    }
  },
  mounted() {
    this.testMock()
  },
  methods: {
    testMock() {
      this.$pagexRequest({
        method: "get",
        url: "/vmock/order/list"
      })
        .then((res) => {
          this.$set(this.formData, 'goods', res.rows)
          this.optionsInitSetts = deepClone(res.option)
          console.log(res.option)
          console.log('---------------------------------')
          this.initFinsh = true
        })
        .catch((res) => {
          console.log(res);
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.base-container {
  background-color: #FFFFFF;
}
::v-deep label {
  font-weight: normal;
}
</style>
