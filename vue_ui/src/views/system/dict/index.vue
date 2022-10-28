<!--
  模块名称：字典分组
  开发人员：谁不写是瓜皮
  创建时间: 2022-10-28
-->
<template>
  <base-container>
    <pagex-crud
      ref="crud"
      :filters="filters"
      :columns="columns"
      :api="api"
      :sortSett="sortSett"
      :buttons="buttons"
      :querys="querys"
    >
      <template v-if="open" v-slot:form="prop">
        <!-- 新增 修改 详情 弹框-->
        <pagex-dialog
          class="pagex-dialog-theme"
          slot="form"
          namespace="dictGroup"
          :title="title"
          :visible.sync="open"
          v-if="open"
        >
          <dictGroupForm
            :init="init"
            :isDetail="isDetail"
            :isEdit="isEdit"
          >
          </dictGroupForm>
        </pagex-dialog>
      </template>
    </pagex-crud>
  </base-container>
</template>

<script>
import crudMixins from "@/mixins/crudMixins";
//这个路径可能需要改下
import dictGroupForm from "./components/dictGroupForm.vue";
import {mapGetters} from "vuex"

export default {
  name: "dictGroup",
  components: {
    dictGroupForm,
  },
  mixins: [crudMixins],
  data() {
    return {
      api: "basic/ms/dictGroup/pagerAdvance",
      isEdit: false,
      open: false,
      isDetail: false,
      // 列表排列顺序（更新时间）
      sortSett: [
        {
          direction: "DESC",
          property: "updateTime",
        },
      ],
      //支持自定义按钮(颜色，图标 不设置有默认颜色有默认图标)，支持插槽形式的按钮，method扩展
      buttons: [
        {
          title: "新增",
          name: "add",
          type: "primary",
          size: "mini",
          icon: "el-icon-plus",
          permission: ["dictGroup:add"],
          click: () => {
            this.title = "新增";
            this.isEdit = false;
            this.$set(this, "init", {});
            this.open = true;
            this.isDetail = false;
          },
        },
      ],
      columns: [

        {
          label: "分组名称",
          name: "groupName"
        },
        {
          label: "分组编码",
          name: "groupCode"
        },
        {
          label: "备注",
          name: "remarks"
        }
        ,
        {
          label: "操作",
          name: "operation",
          type: "textBtn",
          width: "240",
          textBtn: [
            {
              title: "编辑",
              type: "primary",
              size: "mini",
              permission: ["dictGroup:update"],
              click: (_row) => {
                this.$set(this, "init", _row);
                this.title = "编辑";
                this.isEdit = true;
                this.open = true;
                this.isDetail = false;
              },
            },
            {
              title: "详情",
              type: "success",
              size: "mini",
              click: (_row) => {
                this.$set(this, "init", _row);
                this.title = "详情";
                this.open = true;
                this.isDetail = true;
                this.isEdit = false;
              },
            },
            {
              title: "删除",
              type: "danger",
              api: "basic/ms/dictGroup/",
              size: "mini",
              permission: ["dictGroup:del"],
              idFieldName: 'groupId',
            },
          ],
        },
      ],
      filters: [

        {
          label: "分组名称",
          name: "groupName",
          "operation": "like",
          type: "text"
        }
        ,
      ],
      querys: [],
    };
  },
  created() {
  },
  computed: {
    ...mapGetters(["user"]),

  },
  methods: {},
};
</script>
<style lang="scss" scoped>

</style>
