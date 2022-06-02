
<template>
<div>
  <pagex-form
    ref="form"
    :isEdit="isEdit"
    :init="init"
    :data="formData"
    :controls="controls"
    :isHaveAddBtn="true"
    :isHaveCancelBtn="false"
    :buttons="buttons"
    :isVee="false"
    :addApi="addApi"
    :updateApi="updateApi"
  >
  </pagex-form>
</div>

</template>
<script>
import crudMixins from "@/mixins/crudMixins";
export default {
  name: "feeProject",
  props: {
    isFormShow: {
      type: Boolean,
      default: false
    },
    isEdit: {
      type: Boolean,
      default: false
    },
    addIsShow: {
      type: Boolean,
      default: false
    },
    init:{
      type: Object,
      default: ()=>{
        return {};
      }
    },
    parentId: {
      type: Number,
      default: ''
    },
    codemsg: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      addApi: "/purchase/ms/feeProject",
      updateApi: "/purchase/ms/feeProject",
      formData:{
        id: this.init.id,
        parentId: this.init.parentId ? this.init.parentId : this.parentId,
        projectCode:'',
        status:this.init.status ? this.init.status : 1
      },
      controls: [
        {
          type: "select",
          name: 'parentId',
          label: "费用类别",
          url:'/purchase/ms/feeProject/findList?level=1',
          labelField: "name",
          rule: [
            {
              required: true,
              message: "费用类别不能为空",
              trigger: "change",
            },
          ],
        },
        {
          type: "text",
          name: "projectCode",
          label: "费用项目代码",
          rule: [
            {
              required: true,
              message: "费用项目代码不能为空",
              trigger: "change",
            },
          ],
        },
        {
          type: "text",
          name: "name",
          label: "费用项目名称",
          rule: [
            {
              required: true,
              message: "费用项目名称不能为空",
              trigger: "change",
            },
            {
              min: 1,
              max: 32,
              message: "费用项目名称字符长度1-32位之间",
            },
          ],
        },
        {
          type: "select",
          name: "mainType",
          label: "费用主体类型",
          dictCode: "costMainType",
          isValueNum: true,
          rule: [
            {
              required: true,
              message: "费用主体类型不能为空",
              trigger: "change",
            },
          ],
        },
        {
          type: "select",
          name: "rate",
          label: "税率",
          placeholder: "请选择",
          dictCode: "rate",
          isValueNum: true,
          rule: [
            { required: true, message: "税率不能为空", trigger: "change" },
          ],
        },
        {
          type: "select",
          name: "proceedsPayType",
          label: "收付方向",
          dictCode: "receiptsDirection",
          isValueNum: true,
          rule: [
            { required: true, message: "收付方向不能为空", trigger: "change" },
          ],
        },
        {
          type: "select",
          name: "ticketDeduct",
          label: "票扣",
          dictCode: "yesOrNo",
          isValueNum: true,
          rule: [
            { required: true, message: "票扣不能为空", trigger: "change" },
          ],
        },
        {
          type: "select",
          name: "proceedsMethod",
          label: "收款方式",
          dictCode: "paymentMethods",
          isValueNum: true,
          rule: [
            { required: true, message: "收款方式不能为空", trigger: "change" },
          ],
        },
        {
          type: "text",
          name: "fixedAmount",
          label: "固定金额",
        },
        {
          type: "text",
          name: "fixedAmountGoTax",
          label: "固定去税金额",
        },
        {
          type: "select",
          name: "settlementWay",
          label: "结案方式",
          dictCode: "settlementWay",
          isValueNum: true,
          rule: [
            { required: true, message: "结案方式不能为空", trigger: "change" },
          ],
        },
        {
          type: "select",
          name: "status",
          label: "状态",
          disabledOn:'disabled',
          dictCode: "isEnable",
          isValueNum: true,
        },
        {
          type: "textarea",
          name: "remark",
          label: "备注",
        },
        {
          type: "select",
          name: "settlementDatasource",
          label: "结案数据来源",
          placeholder: "请选择",
          dictCode: "settlementSource",
          isValueNum: true,
          rule: [
            { required: true, message: "结案数据来源不能为空", trigger: "change" },
          ],
          multiple: true,
        },

      ],
      buttons: [
        {
          name: "启用",
          type: "",
          size: "mini",
          ifFun:(_model)=>{
            console.log(_model.id);
            return _model.id && _model.status == 0;
          },
          click: (_row) => {
            this.title = "启用";
            this.isEdit = false;
            this.open = false;
          },
        },
        {
          name: "禁用",
          type: "",
          size: "mini",
          ifFun:(_model)=>{
            return _model.id && _model.status == 1;
          },
          click: (_row) => {
            this.title = "禁用";
            this.$set(this, "init", { organizationId: this.org.id });
            // api:
            this.isEdit = false;
            this.open = false;
          },
        },
        {
          name: "删除",
          type: "",
          size: "mini",
          ifAttr:'id',
          click: (_row) => {
            this.title = "删除";
            this.isEdit = false;
            // url:'/ms/feeProject/?id= + parentId'
          },
        },
      ],
    };
  },

  created() {
    console.log(this.parentId);
    console.log(this.isEdit);
  },
  mounted(){
    if(!this.isEdit){
      this.changeParentId();
    }
  },
  methods: {
    changeParentId(){
       this.$pagexRequest({
        url: "/purchase/ms/feeProject/findCode?id=" + this.$refs.form.getModel().parentId,
        method: "GET",
      }).then(res=>{
          this.$refs.form.setModelProp('projectCode',res.data);
      })
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
