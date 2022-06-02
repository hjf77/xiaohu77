<!--
  模块名称：供应商资料管理
  开发人员：何静静
  创建时间: 2022-5-30
-->
<template>
  <base-container>
    <pagex-crud
      class="crud-class"
      ref="crud"
      :filters="filters"
      :columns="columns"
      :api="api"
      :sortSett="sortSett"
      :buttons="buttons"
      :querys="querys"
      namespace="supplierSupplier"
    >
      <template v-slot:form="">
        <!-- 新增 弹框-->
        <pagex-dialog
          v-if="open"
          class="pagex-dialog-theme"
          slot="form"
          :title="title"
          :visible.sync="open"
          namespace="supplierSupplier"
        >
          <!-- 新增 -->
          <pagex-form
            :addApi="addApi"
            :controls="controls"
            :onSubmit="onSubmit"
            :isHaveAddBtn="true"
            :isVee="false"
            :isEdit="isEdit"
            namespace="supplierSupplier"
          ></pagex-form>
        </pagex-dialog>
      </template>
    </pagex-crud>
  </base-container>
</template>

<script>
import crudMixins from '@/mixins/crudMixins';
//这个路径可能需要改下

export default {
  name: 'logLogin',
  mixins: [crudMixins],
  data() {
    return {
      // api: '/basic/ms/logLogin/pagerAdvance',
      api: '/purchase/ms/supplierSupplier/pagerAdvance',
      // 列表排列顺序（更新时间）
      sortSett: [
        {
          direction: 'DESC',
          property: 'createTime',
        },
      ],
      title: '新增',
      // addApi: '/basic/ms/sysUser/',
      addApi:'/purchase/ms/supplierSupplier',
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [
        {
          title: '新增',
          type: 'primary',
          size: 'mini',
          permission: ['supplierSupplierDataManagement:add'],
          click: (_row) => {
            this.title = '新增';
            // this.$set(this, "init", {organizationId:this.org.id})
            this.isEdit = false;
            this.open = true;
          },
        },
        {
          title: '导出',
          type: '',
          size: 'mini',
          // permission: ['sysUser:add'],
          click: (_row) => {
            this.title = '导出';
            this.$set(this, 'init', { organizationId: this.org.id });
            this.isEdit = false;
            this.open = true;
          },
        },
      ],
      controls: [
        {
          type: 'text',
          name: 'name',
          label: '供应商名称',
          rule: [{
            required: true, message: '供应商名称不能为空', trigger: 'blur',
          }, {
            min: 1,
            max: 128,
            message: '供应商名称字符长度1-128位之间',
          }],
        },
        {
          type: 'select',
          name: 'status',
          label: '状态',
          rule: [{
            required: true, message: '状态不能为空', trigger: 'change',
          }],
          dictCode: 'supplierState',
          isValueNum: true,
        },
      ],
      onSubmit: function (model) {
        return true;
      },
      columns: [
        { label: '序号', type: 'index',fixed: 'left' },
        { label: '供应商代码', name: 'supplierCode', width: 150, fixed: 'left' },
        { label: '供应商名称', name: 'name', width: 150, fixed: 'left' },
        { label: '简称', name: 'shortName' },
        { label: '状态', name: 'transMap.statusName', width: 80 },
        { label: '公司地址', name: 'address', width: 150 },
        { label: '供应商类型', name: 'transMap.supplierTypeName', width: 150 },
        { label: '进项税率', name: 'transMap.rateName', width: 150 },
        { label: '联系人', name: 'contact', width: 150 },
        { label: '联系电话', name: 'contactMobile', width: 150 },
        { label: '创建人', name: 'transMap.createUserUserName', width: 150 },
        { label: '创建时间', name: 'createTime', width: 150 },
        {
          label: '操作',
          name: 'operation',
          type: 'textBtn',
          fixed: 'right',
          textBtn: [
            {
              title: '编辑',
              type: 'bottom',
              size: 'mini',
              permission: ['supplierSupplierDataManagement:update'],
              click: (_row) => {
                this.$router.push({path: '/supplier/type/editForm',query:{id: _row.id}});
                this.isEdit = true;
              },
            },
            {
              title: "删除",
              type: "danger",
              size: "mini",
              permission: ['supplierSupplierDataManagement:del'],
              idFieldName:'id',
              api:'/purchase/ms/supplierSupplier/'
            }
          ],
        },
      ],
      filters: [
        {
          name: 'supplierCode',
          label: '供应商代码：',
          placeholder: '供应商代码',
          type: 'text',
        },
        {
          name: 'name',
          label: '供应商名称：',
          placeholder: '供应商名称',
          type: 'text',
        },
        {
          name: 'status',
          label: '状态：',
          type: 'select',
          operation: 'like',
          dictCode: 'supplierState',
        },
      ],
      querys: [],
    };
  },
  created() {},
  computed: {},
  methods: {},
};
</script>
<style lang="scss" scoped>
.crud-class {
  .mar-btm-20 {
    margin-bottom: 20px;
  }
}
</style>
