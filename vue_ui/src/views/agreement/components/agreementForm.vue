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
import crudMixins from "@/mixins/crudMixins";
import {deepClone} from "@/utils";
import {parseTime} from "../../../lib/utils";

export default {
  name: "AgreementForm",
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
      rateList:[],
      isReturnList:[],
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
          url:"/purchase/ms/supplierSupplier/findList?supplierCode=",
          labelField: "name",
          valueField: "id",
          title: 'supplierIdName',
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
              click: function (_v,_model) {

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
          width:'1500px',
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
              name: 'goodCode',
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
                  this.$set(_datas[index],'goosBarcode',res.goosBarcode)
                  this.$set(_datas[index],'goodsName',res.goodsName)
                  this.$set(_datas[index],'specificationId',res.specificationId)
                  this.$set(_datas[index],'orgName',res.orgName)
                  this.$set(_datas[index],'rate',res.rate)
                  this.$set(_datas[index],'taxUnitPrice',res.taxUnitPrice)
                  this.$set(_datas[index],'excludeTaxUnitPrice',res.excludeTaxUnitPrice)
                  this.$set(_datas[index],'purchasePrice',res.purchasePrice)
                  this.$set(_datas[index],'minOrderNum',res.minOrderNum)
                  this.$set(_datas[index],'isReturn',res.isReturn)
                  optionsSetts[index].specificationId = [{
                    id: '1',
                    title: '1*9'
                  }, {
                    id: '2',
                    title: '1*12'
                  }];
                  optionsSetts[index].rate = this.rateList;
                  optionsSetts[index].isReturn = this.isReturnList;
                  _inputThis.$emit("update:datas", _datas);
                  _inputThis.$emit("update:one2xOptionsSetts", optionsSetts);
                }catch (e) {
                  console.log(e.message)
                }
              },
              import: true,
              export: true,
              cellWidth: '10',
              notRepeat: true,
            },
            {
              type: 'label',
              name: 'goosBarcode',
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
              rule: [{  required: true, message: '请选择开始时间', trigger: 'blur' }],
            },
            {
              type: 'dates',
              name: 'endTime',
              formatVal: "yyyy-MM-dd",
              label: '结束时间',
              rule: [{  required: true, message: '请选择结束时间', trigger: 'blur' }],
            },
            {
              type: 'select',
              name: 'isReturn',
              label: '是否可退',
              options:[],
              rule: [{  required: true, message: '请选择是否可退', trigger: 'blur' }],
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
  created() {
    this.getDictList('rate').then((value) => {
      this.rateList = this.formatDictList(value)
    })
    this.getDictList('yesOrNo').then((value) => {
      this.isReturnList = this.formatDictList(value)
    })
  },
  mounted() {

    if (this.$route.query.id) {
      setTimeout(()=>{
        //这里就写你要执行的语句即可，先让数据库的数据加载进去数组中你在从数组中取值就好了
        this.initData()
      },800)

    } else {
        this.formData.status = '1';
        this.formData.statusName = '未审核';
        this.formData.no = "202205070001";
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
          this.formData.supplierIdName = this.init.supplierIdName;
          // 初始化 optionsInitSetts 为空数组
          this.optionsInitSetts = []
          if (res.goodsVOs && res.goodsVOs.length > 0) {
            res.goodsVOs.forEach((item) => {
              // 如果没有selectDataList，则给空[], 不然商品喝规格对应不上
              if (!item.selectDataList) item.selectDataList = []


              item.selectDataList.push({
                rate: this.rateList
              })
              item.selectDataList.push({
                isReturn: this.isReturnList
              })
              this.optionsInitSetts.push(item.selectDataList)
            })
          }

          console.log(this.rateList)
          console.log(this.isReturnList)
          console.log(this.optionsInitSetts)
          console.log('--------------------------')

          this.initFinsh = true
        })
        .catch((res) => {
          console.log(res);
        });
    },

    // 保存
    save(form) {
      // 删除公共字段信息,后端默认设置
      delete form.createTime;
      delete form.createUser;
      delete form.updateTime;
      delete form.updateUser;
      this.$pagexRequest({
        method: "POST",
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
        method: "GET",
        url: "/purchase/ms/agreementInput/getGoodInfo"
      }).then((res) => {
        this.$message.success('保存成功')
      }).catch((e) => {
        console.log(e.message);
      })
    },

    // 删除
    del(id) {
      this.$pagexRequest({
        method: "GET",
        url: `/purchase/ms/agreementAgreement/${id}`,
      }).then((res) => {
        this.$message.success('删除成功')
      }).catch((e) => {
        console.log(e.message);
      })
    },

    // 获取字典数据
    async getDictList(dictCode) {
      const res = await this.$pagexRequest({
        method: "GET",
        url: `/basic/ms/dictItem/findList?dictGroupCode=${dictCode}`,
      })
      return res
    },

    // 格式化字典数据
    formatDictList(list) {
      let tempList = []
      list.forEach((item) => {
        tempList.push({
          id: parseInt(item.dictCode),
          title: item.dictDesc
        })
      })
      return tempList
    }
  }
};
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
</style>
