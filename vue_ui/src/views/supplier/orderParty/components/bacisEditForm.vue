
<template>
  <base-container>
    <pagex-form
      ref="pageForm"
      :controls="controls"
      :isEdit="true"
      :init="detailsInfo"
      :isVee="false"
      :isHaveAddBtn="false"
      :isHaveCancelBtn="false"
      namespace="supplierOrderParty"
    >
    </pagex-form>
    
     
  </base-container>
</template>
<script>
// import cityList from '@/assets/city/pca-code.json';
import { englishNumbersReg } from "@/utils/validate.js";
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
      controls: [
        {
          type: 'text',
          name: 'orderPartyCode',
          label: '订单方代码',
          disabledOn: 'disabled',
          rule: [
            { required: true, message: '订单方代码不能为空', trigger: 'blur' },
          ],
        },
        {
          type: 'text',
          name: 'name',
          label: '订单方名称',
          rule: [
            { required: true, message: '订单方名称不能为空', trigger: 'blur' },
            {
              min: 1,
              max: 128,
              message: '订单方名称字符长度1-128位之间',
            },
          ],
        },
        {
          type: 'text',
          name: 'contractCode',
          label: '合同编号',
          rule: [
            { pattern: englishNumbersReg, trigger: 'blur', message: '合同编号格式有误' },
            
          ],
        },
        {
          type: 'select',
          name: 'supplierId',
          label: '所属供应商',
          url: '/purchase/ms/supplierSupplier/findList',
          methodType: 'GET',
          valueField: 'id',
          labelField: 'name',
          rule: [
            { required: true, message: '所属供应商不能为空', trigger: 'change' },
          ],
        },
        {
          type: 'select',
          name: 'status',
          label: '订单方状态',
          url: '/purchase/ms/supplierOrderStatus/findList',
          methodType: 'GET',
          valueField: 'id',
          labelField: 'name',
          rule: [
            { required: true, message: '订单放状态不能为空', trigger: 'change' },
          ],
        },
        {
          type: 'select',
          name: 'cooperationWays',
          label: '合作方式',
          dictCode: 'cooperationWays',
          isValueNum: true,
          rule: [
            { required: true, message: '合作方式不能为空', trigger: 'change' },
          ],
        },
        {
          type: 'select',
          name: 'categoryId',
          label: '所属大类',
          dictCode: 'categoryId',
          isValueNum: true,
          rule: [
            { required: true, message: '所属大类不能为空', trigger: 'change' },
          ],
        },
        {
          type: 'text',
          name: 'principalIdUserName',
          label: '负责人',
          disabledOn: 'disabled',
          rule: [
            { required: true, message: '负责人不能为空', trigger: 'blur' },
          ],
        },
        {
          type: 'text',
          name: 'orgIdName',
          label: '所属部门',
          disabledOn: 'disabled',
          rule: [
            { required: true, message: '所属部门不能为空', trigger: 'blur' },
          ],
        },
        {
          type: 'select',
          name: 'defaultStorage',
          label: '默认仓储',
          dictCode: 'defaultStorage',
          isValueNum: true,
        },
        {
          type: 'select',
          name: 'defaultLocation',
          label: '默认仓位',
          dictCode: 'defaultLocation',
          isValueNum: true,
        },
      ],
    };
  },
  created() {
    console.log(this.detailsInfo);
  },
  methods: {
    validate(_callback){
       this.$refs.pageForm.validate(_callback);
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
