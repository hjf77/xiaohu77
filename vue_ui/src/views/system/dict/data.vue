<template>
  <div class="app-container">
    <pagex-crud
      v-if="crud"
      :filters="filters"
      :columns="columns"
      :api="api"
      :querys="querys"
      :buttons="buttons"
    >
      <template v-slot:form="prop">
        <!-- 新增 修改 弹框-->
        <el-dialog slot="form" :title="title"  v-if="open" :visible.sync="open"  class="pagex-dialog-theme">
          <addDictio :dictGroupCode="groupCode"  :init="init" :isEdit="isEdit"></addDictio>
        </el-dialog>
      </template>
    </pagex-crud>
  </div>
</template>

<script>
import crudMixins from "@/mixins/crudMixins";
import addDictio from "@/views/system/dict/components/addDictio.vue";

export default {
  name: "Data",
  components: {
    addDictio
  },
  mixins: [crudMixins],
  data() {
    return {
      groupCode: '',
      api: '/basic/ms/dictItem/pagerAdvance',
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [
        {
          title: '新增',
          name: 'add',
          type: 'primary',
          size: 'mini',
          icon: 'el-icon-plus',
          click: () => {
            this.title = '新增';
            this.open = true;
            this.isEdit = false;
            this.init = {};
          }
        }
      ],
      querys: [
        {
          operation: "=",
          property: "dictGroupCode",
          relation: "AND",
        }
      ],
      columns: [
        {label: '分组编码', name: 'dictGroupCode'},
        {label: '字典code', name: 'dictCode'},
        {label: '字典翻译', name: 'dictDesc'},
        {label: '字典排序', name: 'orderNum'},
        {
          label: '操作',
          name: 'operation',
          type: 'textBtn',
          textBtn: [
            {
              name: "编辑",
              type: "bottom",
              size: 'mini',
              click: (_row, name) => {
                this.$set(this, 'init', _row)
                this.title = '编辑';
                this.open = true;
                this.isEdit = true;
              }
            },
            {
              name: "删除",
              type: "danger",
              api: '/basic/ms/dictItem/',
              size: 'mini',
              idFieldName: 'dictId'
            }
          ],
        }
      ],
      filters: []
    };
  },
  created() {
    this.groupCode = this.$route.query && this.$route.query.groupCode;
    this.querys[0].value = this.groupCode

    if (this.groupCode) {
      this.$nextTick(() => {
        this.crud = true
      })
    }
  },
  methods: {}
};
</script>
