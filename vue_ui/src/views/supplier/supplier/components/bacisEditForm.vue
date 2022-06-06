
<template>
  <base-container>
    <pagex-form
      ref="pageForm"
      :isEdit="true"
      :init="detailsInfo"
      :controls="controls"
      :buttons="buttons"
      :isVee="false"
      :isHaveAddBtn="false"
      :isHaveCancelBtn="false"
      namespace="supplierSupplier"
    >
    </pagex-form>
  </base-container>
</template>
<script>
import cityList from '@/assets/city/pca-code.json';
import { isEmail, mobileOrTelReg, phoneNumReg, ID18Reg, wholeNumReg } from "@/utils/validate.js";
export default {
  name: 'bacisEditForm',
  props: {
    detailsInfo: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      buttons: [],
      controls: [
        {
          type: 'text',
          name: 'supplierCode',
          label: '供应商代码',
          disabledOn: 'disabled',
          rule: [
            { required: true, message: '供应商代码不能为空', trigger: 'blur' },
            {
              min: 1,
              max: 15,
              message: '供应商代码字符长度1-15位之间',
            },
          ],
        },
        {
          type: 'text',
          name: 'name',
          label: '供应商名称',
          rule: [
            {
              required: true,
              message: '供应商名称不能为空',
              trigger: 'blur',
            },
            {
              min: 1,
              max: 128,
              message: '供应商名称字符长度1-128位之间',
            },
          ],
        },
        {
          type: 'text',
          name: 'shortName',
          label: '简称',
          
        },
        {
          type: 'select',
          name: 'status',
          label: '供应商状态',
          rule: [
            {
              required: true,
              message: '供应商状态不能为空',
              trigger: 'change',
            },
          ],
          dictCode: 'supplierState',
          isValueNum: true,
        },

        {
          type: 'select',
          name: 'rate',
          label: '进项税率',
          dictCode: 'rate',
          isValueNum: true,
          rule: [
            { required: true, message: '进项税率不能为空', trigger: 'change' },
          ],
        },
        {
          type: 'select',
          name: 'level',
          label: '供应商等级',
          dictCode: 'supplierLevel',
          isValueNum: true,
          
        },
        {
          type: 'select',
          name: 'supplierType',
          label: '供应商类型',
          dictCode: 'supplierType',
          isValueNum: true,
          rule: [
            {
              required: true,
              message: '供应商类型不能为空',
              trigger: 'change',
            },
          ],
        },
        {
          type: 'text',
          name: 'contact',
          label: '联系人',
          rule: [
            { required: true, message: '联系人不能为空', trigger: 'blur' },
            {
              min: 1,
              max: 32,
              message: '联系人字符长度1-32位之间',
            },
          ],
        },
        {
          type: 'text',
          name: 'contactMobile',
          label: '联系人电话',
          rule: [
            { required: true, message: '联系人电话不能为空', trigger: 'blur' },
            { pattern: phoneNumReg, trigger: 'blur', message: '请输入正确的联系人电话' }
          ],
        },
        {
          type: 'select',
          name: 'nature',
          label: '企业性质',
          dictCode: 'enterpriseNature',
          isValueNum: true,
          rule: [
            { required: true, message: '企业性质不能为空', trigger: 'change' },
          ],
        },
        {
          type: 'text',
          name: 'legalPerson',
          label: '企业法人',
          rule: [
            { required: true, message: '企业法人不能为空', trigger: 'blur' },
            {
              min: 1,
              max: 32,
              message: '企业法人字符长度1-32位之间',
            },
          ],
        },
        {
          type: 'text',
          name: 'idNo',
          label: '法人身份证号',
          rule: [
            {
              required: true,
              message: '法人身份证号不能为空',
              trigger: 'blur',
            },
            { pattern: ID18Reg, trigger: 'blur', message: '请输入正确的身份证号' }
          ],
        },
        {
          type: 'select',
          name: 'companyType',
          label: '企业类型',
          dictCode: 'enterpriseType',
          isValueNum: true,
          rule: [
            { required: true, message: '企业类型不能为空', trigger: 'change' },
          ],
        },
        {
          type: 'text',
          name: 'companyMobile',
          label: '公司电话',
          rule: [
            { required: true, message: '公司电话不能为空', trigger: 'blur' },
            { pattern: mobileOrTelReg, trigger: 'blur', message: '请输入正确的公司电话' }
          ],
        },
        {
          type: 'text',
          name: 'registeredCapital',
          label: '注册资金',
          afterTextContent:'万元',
          rule: [
            { required: true, message: '注册资金不能为空', trigger: 'blur' },
            { pattern: wholeNumReg, trigger: 'blur', message: '请输入正整数注册资金' }
          ],
        },
        {
          type: 'text',
          name: 'email',
          label: '供应商邮箱',
          rule: [
            { required: true, message: '供应商邮箱不能为空', trigger: 'blur' },
            { validator: isEmail, trigger: 'blur' }
            
          ],
        },
        {
          type: 'cascader',
          name: 'geographicalPosition',
          label: '所在城市',
          options: cityList,
          labelField: 'name',
          valueField: 'code',
          isValueNum: true,
          propsField: {
            label: 'name',
            value: 'code',
          },
          rule: [
            { required: true, message: '所在城市不能为空', trigger: 'blur' },
          ],
        },
        {
          type: 'text',
          name: 'brand',
          label: '经营品牌',
          rule: [
            { required: true, message: '经营品牌不能为空', trigger: 'blur' },
            {
              min: 1,
              max: 128,
              message: '经营品牌字符长度1-128位之间',
            },
          ],
        },
        {
          type: 'text',
          name: 'address',
          label: '公司地址',
          rule: [
            { required: true, message: '公司地址不能为空', trigger: 'blur' },
            {
              min: 1,
              max: 256,
              message: '公司地址字符长度1-256位之间',
            },
          ],
        },
      ],
    };
  },
  created() {
    console.log(this.detailsInfo);
    this.processData()
  },
  methods: {
    validate(_callback){
       this.$refs.pageForm.validate(_callback);
    },
    processData() {
    // 处理城市字段 编辑 接口返回[0,0,0] 不处罚校验问题
     const isTrue =  this.detailsInfo.geographicalPosition.includes('0')
     if(isTrue) {
       this.detailsInfo.geographicalPosition = []
     }
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
