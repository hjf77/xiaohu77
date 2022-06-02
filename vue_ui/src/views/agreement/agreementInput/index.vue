<template>
  <base-container>
    <pagex-form
      v-if="initFinsh"
      :isEdit="isEdit"
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
import crudMixins from "@/mixins/crudMixins";
import {deepClone} from "@/utils";
import {parseTime} from "../../../lib/utils";

export default {
  name: "AgreementInput",
  mixins: [crudMixins],
  props: {
    init: Object,
  },
  computed: {
    ...mapGetters(["user"]),
    isEdit() {
      return !!this.$route.query.id
    }
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
        createTime: this.isEdit ? this.init.createTime : parseTime(new Date(), '{y}-{m}-{d} {h}:{i}:{s}'),
        createUser: this.isEdit ? this.init.createUser : this.$store.state.user.user.userName,
        updateTime: this.isEdit ? this.init.updateTime : parseTime(new Date(), '{y}-{m}-{d} {h}:{i}:{s}'),
        updateUser: this.isEdit ? this.init.updateUser : this.$store.state.user.user.userName,
        orgId: this.isEdit ? this.init.organizationId : this.$store.state.user.user.organizationId,
        orgName: this.isEdit ? this.init.orgName : this.$store.state.user.user.orgName,
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
          url:"/basic/ms/supplierSupplier/findList?supplierCode=",
          labelField: "name",
          valueField: "id",
          remote: true,
          label: "订单方",
          rule: [{required: true, message: '请输入订单方', trigger: 'change'}]
        },
        // {
        //   type: "select",
        //   name: "contractNo",
        //   url:"/basic/ms/dictItem/findList?dictGroupCode=",
        //   labelField: "dictDesc",
        //   valueField: "dictCode",
        //   remote:true,
        //   label: "合同编号",
        //   rule: [{required: true, message: '请输入合同编号', trigger: 'change'}]
        // },
        {
          type: "text",
          label: "合同编号",
          name: "contractNo",
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
            // {
            //   name: "模拟导入(append后台解析数据)",
            //   click: function (_v,_model) {
            //     _v.$refs.goods[0].appendRows([{goodCode:'1314111',goodName:'白象方便面',goodspecifications:'1',taxRadio:1}] ,[{goodspecifications:[{
            //         id: '1',
            //         title: '24袋一箱'
            //       }, {
            //         id: '2',
            //         title: '12袋一箱'
            //       }]}]);
            //   }
            // },
            // {
            //   name: "导出模板",
            //   click:  (_v,_model) =>{
            //     postFile('/basic/excel_template/exportTemplate?name=商品导入模板',this.controls[4].controls);
            //   }
            // }
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
              fixed: true,
              rule: [{  required: true, message: '请输入商品编码', trigger: 'blur' }],
              one2xBlur: async (newValue, _datas, index, optionsSetts, _inputThis) => {
                _datas[index].goodName = '可口可乐500ML';
                const res = await this.$pagexRequest({
                  methods: "GET",
                  url: "/vmock/ms/agreementInput/getGoodInfo?goodCode=" + newValue,
                })
                console.log(res)
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
              name: 'goodBarCode',
              label: '商品条码'
            },
            {
              type: 'label',
              name: 'goodName',
              label: '商品名称'
            },
            {
              type: 'select',
              name: 'goodspecifications',
              label: '包装规格',
              options:[],
              rule: [{  required: true, message: '请输入商品编码', trigger: 'blur' }],
            },
            {
              type: 'label',
              name: 'orgName',
              label: '单位',
              options:[],
            },
            {
              type: 'select',
              name: 'rate',
              label: '税率',
              options:[],
              rule: [{  required: true, message: '请输入税率', trigger: 'blur' }],
              change: (newValue, _row, index, options) => {

              }
            },
            {
              name: 'taxUnitPrice',
              label: '含税单价',
            },
            {
              name: 'excludeTaxUnitPrice',
              label: '去税单价',
            },
            {
              name: 'purchasePrice',
              label: '采购价',
            },
            {
              type: 'dates',
              name: 'startTime',
              label: '开始时间',
              rule: [{  required: true, message: '请选择开始时间', trigger: 'blur' }],
            },
            {
              type: 'dates',
              name: 'endTime',
              label: '结束时间',
              rule: [{  required: true, message: '请选择结束时间', trigger: 'blur' }],
            },
            {
              type: 'select',
              name: 'isReturn',
              label: '是否可退',
              rule: [{  required: true, message: '请选择是否可退', trigger: 'blur' }],
            },
            {
              name: 'minOrderNum',
              label: '最小订货数量',
            }
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
    if (this.$route.query.id) {
      this.initData()
    } else {
      this.initFinsh = true
    }
  },
  methods: {
    initData() {
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
    },
    getGoodInfo() {
      this.$pagexRequest({
        method: "GET",
        url: "/vmock/ms/agreementInput/getGoodInfo"
      })
        .then((res) => {
          console.log(res)
        })
        .catch((e) => {
          console.log(e.message);
        })
    },

    // 保存
    save(form) {
      this.$pagexRequest({
        method: "GET",
        url: "/vmock/ms/agreementInput/getGoodInfo"
      }).then((res) => {
        this.$message.success('保存成功')
      }).catch((e) => {
        console.log(e.message);
      })
    },

    // 审核
    audit(form) {
      this.$pagexRequest({
        method: "GET",
        url: "/vmock/ms/agreementInput/getGoodInfo"
      }).then((res) => {
        this.$message.success('保存成功')
      }).catch((e) => {
        console.log(e.message);
      })
    },

    // 审核
    del(form) {
      this.$pagexRequest({
        method: "GET",
        url: "/vmock/ms/agreementInput/getGoodInfo"
      }).then((res) => {
        this.$message.success('保存成功')
      }).catch((e) => {
        console.log(e.message);
      })
    }

  }
};
</script>

<style lang="scss" scoped>
.base-container {
  background-color: #FFFFFF;

}
::v-deep .el-table__row {
  .el-form-item--small.el-form-item {
    margin-bottom: 0 !important;
  }
  .el-date-editor {
    width: auto !important;
  }
}
::v-deep label {
  font-weight: normal;
}
</style>
