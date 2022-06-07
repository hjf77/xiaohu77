<template>
  <div>
    <!-- 新增 -->
    <pagex-form
      :init="init"
      :addApi="addApi"
      :updateApi="updateApi"
      :controls="controls"
      :isHaveAddBtn="true"
      :isVee="false"
      :isEdit="isEdit"
      :data="defaultData"
      namespace="supplierOrderStatus"
    ></pagex-form>
  </div>
</template>


<script>
import dayjs from 'dayjs'
export default {
  name: "orderPartyStatusForm",
  data() {
    return {
      defaultData:{
        createUserUserName : this.$store.state.user.name,
        createTime :  dayjs().format('YYYY-MM-DD HH:mm:ss'),
        updateUserUserName : this.$store.state.user.name,
        updateTime :  dayjs().format('YYYY-MM-DD HH:mm:ss'),
        id: this.init.id
      },
      addApi:'/purchase/ms/supplierOrderStatus/',
      updateApi: '/purchase/ms/supplierOrderStatus/',
      controls: [
        {
          type: 'text',
          name: 'orderStatusCode',
          label: '代码',
          rule: [{
            required: true, message: '代码不能为空', trigger: 'blur',
          }, {
            min: 1,
            max: 4,
            message: '代码长度不得超过4位',
          }],
        },
        {
          type: 'text',
          name: 'name',
          label: '名称',
          rule: [{
            required: true, message: '名称不能为空', trigger: 'blur',
          }, {
            min: 1,
            max: 128,
            message: '名称字符长度1-128位之间',
          }],
        },
        {
          type: 'select',
          name: 'isEnable',
          label: '是否启用',
          rule: [{
            required: true, message: '状态不能为空', trigger: 'change',
          }],
          dictCode: 'yesOrNo',
          isValueNum: true,
        },
        {
          type: 'text',
          name: 'createUserUserName',
          label: '创建人',
          disabledOn: 'disabled',
        },
        {
          type: 'text',
          name: 'createTime',
          label: '创建时间',
          disabledOn: 'disabled'
        },
        {
          type: 'text',
          name: 'updateUserUserName',
          label: '最后修改人',
          disabledOn: 'disabled'
        },
        {
          type: 'text',
          name: 'updateTime',
          label: '最后修改时间',
          disabledOn: 'disabled'
        },
        {
          type: 'select',
          name: 'businessControl',
          label: '业务控制',
          rule: [{
            required: true, message: '业务控制栏不能为空', trigger: 'change',
          }],
          dictCode: 'businessControl',
          // isValueNum: true,
          multiple: true
        },
      ],
    }
  },
  props: {
    init: Object,
    isEdit: Boolean
  },
  mounted() {
    console.log(this.isEdit)
  },
  created() {
    this.formData.dictGroupCode = this.dictGroupCode;
  },
  methods: {}
}
</script>
