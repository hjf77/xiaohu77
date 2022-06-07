<!--
  模块名称：采购协议录入
  开发人员：马军伟
  创建时间: 2022-5-30
-->

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
      ref="agreementInputForm"
    >
    </pagex-form>
  </base-container>
</template>

<script>
import {mapGetters} from "vuex";
import {deepClone} from "@/utils";
import {parseTime} from "@/lib/utils";
import datePickerOpt from "@/mixins/datePickerOpt";

export default {
  name: "AgreementForm",
  mixins: [datePickerOpt],
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
      init: {},
      optionsInitSetts: [],
      initFinsh: false,
      formData: {
        goodsVOs: [],
        total : 1,
        supplierIdName: ''
      },
      controls: [
        {
          type: "label",
          name: "no",
          label: "协议号",
        },
        {
          type: "label",
          name: "statusName",
          label: "状态",
        },
        {
          type: "select",
          name: "orderPartyId",
          url:"/purchase/ms/supplierOrderParty/findList?orderPartyCode=",
          labelField: "name",
          valueField: "id",
          title: 'orderPartyIdName',
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
          rule: [{required: true, message: '请输入合同编号', trigger: 'change'},
            {min: 1, max: 30, message: '合同编号长度1~30', trigger: 'change'},
            {required: true, pattern: /^[0-9]*$/, message: '合同编码只能输入数字', trigger: 'change'},
          ]
        },
        {
          type: "dates",
          name: "startTime",
          label: "开始时间",
          formatVal: "yyyy-MM-dd",
          rule: [{required: true, message: '请输入合同编号', trigger: 'change'}],
          pickerOptions: {},
          changeFn: (val) => {
            this.startTime = val;
            this.rewriteEndPickerOptions("controls", 5);
          },
        },
        {
          type: "dates",
          name: "endTime",
          label: "结束时间",
          formatVal: "yyyy-MM-dd",
          rule: [{required: true, message: '请输入合同编号', trigger: 'change'}],
          pickerOptions: {},
          changeFn: (val) => {
            this.endTime = val;
            this.rewriteStartPickerOptions("controls", 4);
          },
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
          name: "createUserUserName",
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
          name: "updateUserUserName",
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
          // ifFun: () => {
          //   return this.$route
          // },
          buttons: [
            {
              name: "保存",
              click: (_form, _model) => {
                const _this = this
                _form.validate(function(_newModel){
                  _this.save(_newModel)
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
              ifFun: () => {
                return this.$route.query.id
              },
              click: (_v,_model) => {
                this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                  this.del()
                })
              }
            },
            {
              name: "加一行",
              click: function (_v,_model) {
                _v.$refs.goodsVOs[0].addRow();
              }
            },
            // {
            //   name: "模拟导入(append后台解析数据)",
            //   click: function (_v,_model) {
            //     _v.$refs.goodsVOs[0].appendRows([{goodCode:'1314111',goodName:'白象方便面',goodspecifications:'1',taxRadio:1}] ,[{goodspecifications:[{
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
          name: "goodsVOs",
          width:'1600px',
          label: "",
          optionsSetts:[],
          defaultValue:{
            goodCode:''
          },
          //当数据发生改变的时候触发
          onDataChange:(_newDatas)=>{
            this.$refs.agreementInputForm.setModelProp('total',_newDatas.length);
          },
          controls: [
            {
              type: 'text',
              name: 'goodsCode',
              label: '商品编码',
              fixed: true,
              rule: [{  required: true, message: '请输入商品编码', trigger: 'blur' }],
              one2xBlur: async (newValue, _datas, index, optionsSetts, _inputThis) => {
                try {
                  const res = await this.$pagexRequest({
                    methods: "GET",
                    url: "/vmock/ms/agreementInput/getGoodInfo?goodCode=" + newValue,
                  })
                  this.$set(_datas[index],'goodsId',res.goodsId)
                  this.$set(_datas[index],'goodsBarcode',res.goosBarcode)
                  this.$set(_datas[index],'goodsName',res.goodsName)
                  this.$set(_datas[index],'specificationId',res.specificationId)
                  this.$set(_datas[index],'unit',res.orgName)
                  this.$set(_datas[index],'rate',res.rate)
                  this.$set(_datas[index],'taxUnitPrice',res.taxUnitPrice)
                  this.$set(_datas[index],'excludeTaxUnitPrice',res.excludeTaxUnitPrice)
                  this.$set(_datas[index],'purchasePrice',res.purchasePrice)
                  this.$set(_datas[index],'minOrderNum',res.minOrderNum)
                  this.$set(_datas[index],'isReturn',res.isReturn)
                  optionsSetts[index].specificationId = [{
                    id: 0,
                    title: '1*9'
                  }, {
                    id: 1,
                    title: '1*12'
                  }];
                  _inputThis.$emit("update:datas", _datas);
                  _inputThis.$emit("update:one2xOptionsSetts", optionsSetts);
                }catch (e) {
                  console.log(e.message)
                }
              },
              import: true,
              export: true,
              notRepeat: true,
            },
            {
              type: 'label',
              name: 'goodsBarcode',
              label: '商品条码'
            },
            {
              type: 'label',
              name: 'goodsName',
              label: '商品名称'
            },
            {
              type: 'select',
              name: 'specificationId',
              label: '包装规格',
              options:[],
              rule: [{  required: true, message: '请输入商品编码', trigger: 'blur' }],
            },
            {
              type: 'label',
              name: 'unit',
              label: '单位',
              options:[],
            },
            {
              type: 'select',
              name: 'rate',
              label: '税率',
              dictCode: "rate",
              isValueNum: true,
              rule: [{  required: true, message: '请输入税率', trigger: 'blur' }],
              change: (newValue, _row, index, options) => {

              }
            },
            {
              type: 'label',
              name: 'taxUnitPrice',
              label: '含税单价',
            },
            {
              type: 'label',
              name: 'excludeTaxUnitPrice',
              label: '去税单价',
            },
            {
              type: 'label',
              name: 'purchasePrice',
              label: '采购价',
            },
            {
              type: 'dates',
              name: 'startTime',
              formatVal: "yyyy-MM-dd",
              label: '开始时间',
              rule: [{  required: true, message: '请选择开始时间', trigger: 'change' }]
            },
            {
              type: 'dates',
              name: 'endTime',
              formatVal: "yyyy-MM-dd",
              label: '结束时间',
              rule: [{  required: true, message: '请选择结束时间', trigger: 'change' }],
              changeFn: (val, fff, fffff) => {
                debugger
              }
            },
            {
              type: 'select',
              name: 'isReturn',
              label: '是否可退',
              dictCode: "yesOrNo",
              isValueNum: true,
              rule: [{  required: true, message: '请选择是否可退', trigger: 'change' }],
            },
            {
              type: 'label',
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
        this.formData.no = '协议号自动生成'
        this.formData.status = '1';
        this.formData.statusName = '未审核';
        this.formData.createTime = parseTime(new Date(), '{y}-{m}-{d} {h}:{i}:{s}');
        this.formData.createUserUserName = this.$store.state.user.user.userName;
        this.formData.updateTime = parseTime(new Date(), '{y}-{m}-{d} {h}:{i}:{s}');
        this.formData.updateUserUserName = this.$store.state.user.user.userName;
        this.formData.orgId = this.$store.state.user.user.organizationId;
        this.formData.orgName = this.$store.state.user.user.orgName;
      this.initFinsh = true
    }
  },
  methods: {
    initData() {
      this.$pagexRequest({
        method: "GET",
        url: `/purchase/ms/agreementAgreement/getAgreementGoosInfo?id=${this.$route.query.id}`
      })
        .then((res) => {
          this.init = deepClone(res)
          // 下拉搜素设置title
          this.formData.orderPartyIdName = this.init.orderPartyIdName;
          this.formData.id = this.init.id;
          this.formData.no = this.init.no;
          this.formData.detailNum = this.init.detailNum
          this.formData.supplierId = this.init.supplierId
          this.formData.status = this.init.status;
          this.formData.orgId = this.init.orgId;
          this.optionsInitSetts = res.selectDataList

          // 初始化时间校验
          this.startTime = this.init.startTime;
          this.endTime = this.init.endTime;
          this.rewriteStartPickerOptions("controls", 4);
          this.rewriteEndPickerOptions("controls", 5);

          this.initFinsh = true
        })
        .catch((res) => {
          console.log(res);
        });
    },

    // 保存
    save(form) {
      // 商品时间校验
      if (form.goodsVOs && form.goodsVOs.length > 0) {
        let unqualifiedList = []
        let indexList = []
        form.goodsVOs.forEach((item, index) => {
          if (new Date(item.startTime).getTime() > new Date(item.endTime).getTime()) {
            unqualifiedList.push(item)
            indexList.push(index + 1)
          }
        })
        if (unqualifiedList.length > 0) {
          this.$message.warning(`第${indexList.join('，')}条数据数据有误！，开始时间不能大于结束时间`)
        }
      }
      let isEdit = !!this.$route.query.id
      // 删除公共字段信息,后端默认设置
      if (!isEdit) form.no = '20220602000011'
      delete form.createTime;
      delete form.createUser;
      delete form.updateTime;
      delete form.updateUser;
      this.$pagexRequest({
        method: isEdit ? 'PUT' : 'POST',
        url: "/purchase/ms/agreementAgreement",
        data: form
      }).then((res) => {
        this.$message.success('保存成功')
        this.$store.dispatch('tagsView/delView', this.$route).then(() => {
          this.$router.push({path:"/agreement/agreementAgreement"});
        })
      }).catch((e) => {
        console.log(e.message);
      })
    },

    // 审核
    audit(form) {
      this.$pagexRequest({
        method: "PUT",
        url: "/purchase/ms/agreementInput/updateField"
      }).then((res) => {
        this.$message.success('审核成功')
      }).catch((e) => {
        console.log(e.message);
      })
    },

    // 删除
    del() {
      if (!this.$route.query.id) return this.$message.warning('该数据有误,id不存在')
      this.$pagexRequest({
        method: "DELETE",
        url: `/purchase/ms/agreementAgreement/${this.$route.query.id}`,
      }).then((res) => {
        this.$message({
          type: 'success',
          message: '删除成功!'
        });
        this.$store.dispatch('tagsView/delView', this.$route).then(() => {
          this.$router.push({path:"/agreement/agreementAgreement"});
        })
      }).catch((e) => {
        console.log(e.message);
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.base-container {
  width: 100%;
  min-height: calc(100vh - 125px);
  padding: 20px 0;
  box-sizing: border-box;
  position: relative;
  background: #FFFFFF;
  margin: 20px;
}
::v-deep .el-input--prefix .el-input__inner {
  padding: 0 30px 0 15px;
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
::v-deep .el-form-item:nth-child(n+13) {
  .el-form-item__label {
    width: 30px !important;
  }
}
</style>
