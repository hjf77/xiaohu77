<!--
  模块名称：新增装备
  开发人员：GH
  创建时间:2021/9/1
-->
<template>
  <base-container>
    <template>
      <pagex-crud
        ref="crud"
        :filters="filters"
        :columns="columns"
        :api="api"
        :sortSett="sorter"
        :querys="querys"
      >
        <template v-if="open" v-slot:form="prop">
        </template>
      </pagex-crud>
    </template>
  </base-container>
</template>

<script>
import crudMixins from "../../../../../mixins/crudMixins";
import { mapGetters } from "vuex";
export default {
  name: "expert",
  mixins: [crudMixins],
  props:{
    equipmentType:{
        type:Array,
        default:()=>[]
    }
  },
  data() {
    return {
      getList:[],
      rightContent: false, //右侧内容区域
      api: "/ms/commonEquipment/findPagerAdvanceScheme",
      sorter: [
        {
          direction: "DESC",
          property: "updateTime",
        },
      ],
      filters: [
        {
          formname: "所属单位:",
          name: "companyIdName",
          type: "treeSelect",
          placeholder: "请选择",
          operation: "find_in_set",
          api: "/ms/commonEquipmentType/tree",
        },
      ],
      querys: [
        {
          group:"main",
          operation: "ext",
          property: "effectiveDate",
          relation: "AND",
          value: "(effective_date >= now() or effective_date IS NULL)"
        },
        {
          group: "main",
          operation: "find_in_set",
          property: "type",
          relation: "AND",
          value: this.equipmentType
        }
      ],
      acceptFormat: "",
      columns: [
        {label: "序号", name: "", type: "index",Width: 100},
        {label: "装备类别", name: "typeName", type: 'popover',minWidth: 200,align:'center'},
        {label: '所属单位', name: 'companyIdName', type: 'popover', width: 130 },
        {label: '关联装备', name: 'purchaseCompanyName', type: 'popover', width: 130},
        {label: '装备数量', name: 'purchaseDate', type: 'popover', width: 160},
        {label: '剩余有效天数', name: 'purchaseDate', type: 'popover', width: 160},
        {
          label: '操作',
          name: 'operation',
          type: 'textBtn',
          width: "160",
          textBtn: [
            {
              name: "移入",
              type: "primary",
              size: "mini",
              click: (_row) => {
                this.$refs.crud.data = this.$refs.crud.data.filter(function(i) {//删除所选每个学员
                    return i.id != _row.id
                });
                this.getList.push(_row)
                this.$emit('childList',this.getList)//页面通信传递数据   外部人员
                this.$refs.crud.reload()
              }
            },
          ]
        }
      ],
    };
  },
  created(){

  },
  computed: {
    ...mapGetters(["user"]),
  },
  methods: {
    /**
     * 获取org
     * @param org
     * @returns {boolean}
     */
  },
};
</script>
<style lang="scss" scoped>
::v-deep .planDialogStyle > .el-dialog {
  width: 970px !important;
}
::v-deep .planDialogStyle > .el-dialog .el-form-item__label {
  width: 122px !important;
}
::v-deep .vue-treeselect {
  width: 275px;
}
</style>
