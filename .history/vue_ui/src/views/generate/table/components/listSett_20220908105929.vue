<!--
  模块名称：列表配置
  开发人员：wanglei
  创建时间: 2022-08-31
-->
<template>
  <div>
    <!-- 编辑参数配置对话框 -->
    <div v-if="isListShow">
      <pagex-form
        v-if="initFinsh"
        :isEdit="true"
        :data="formData"
        :isVee="false"
        :controls.sync="controls"
        :isHaveSaveBtn="false"
        :isHaveAddBtn="false"
        :init="init"
        :optionsInitSetts="optionsInitSetts"
        :isHaveCancelBtn="false"
        :isHaveInitBtn="false"
        ref="listSettForm"
      >
      </pagex-form>
      <el-button @click="submit()">下一步配置过滤条件</el-button>
    </div>
    <div v-else>
      <el-button @click="goback()">上一步</el-button>
      <pagex-form
        :isEdit="true"
        :isVee="false"
        :controls.sync="form"
        :isHaveSaveBtn="false"
        :isHaveAddBtn="false"
        :isHaveCancelBtn="false"
        :isHaveInitBtn="false"
      >
      </pagex-form>
      <formCreate></formCreate>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import formCreate from "../../../formCreate.vue";

export default {
  name: "listSett",
  components: {
    formCreate,
  },
  computed: {
    ...mapGetters(["user"]),
  },
  provide() {
    return {
      reloadable: this,
    };
  },
  data() {
    return {
      tableSchema: "fhs-demo",
      tableName: "t_pub_file",
      init: {
        tableSchema: "fhs-demo",
        tableName: "t_user",
        tableComment: "用户",
        fields: [
          {
            label: "username",
            name: "用户名",
            width: "150",
            isListShow: 1,
            isTrans: 0,
            transType: "dict",
            dictCode: "sex",
            transTable: "",
            transDB: "",
            transField: "",
            uniqueField: "",
          },
        ],
      },
      optionsInitSetts: [],
      initFinsh: false,
      formData: {
        fields: [],
      },
      controls: [
        {
          type: "label",
          name: "tableSchema",
          label: "数据库名",
        },
        {
          type: "label",
          name: "tableName",
          label: "表名",
        },
        {
          type: "label",
          name: "tableComment",
          label: "表备注",
        },
        {
          type: "one2x",
          name: "fields",
          width: "1500px",
          optionsSetts: [],
          optionsInitSetts: [],
          controls: [
            {
              type: "label",
              name: "name",
              label: "字段名",
            },
            {
              type: "label",
              name: "label",
              label: "备注",
            },
            {
              type: "switch",
              name: "isListShow",
              label: "列表是否展示",
            },
            {
              type: "switch",
              name: "isTrans",
              label: "是否需要翻译",
            },
            {
              type: "select",
              name: "dictCode",
              label: "字典分组",
              valueField: "valueField",
              labelField: "labelField",
            },
            {
              type: "select",
              name: "transTable",
              label: "表",
              valueField: "valueField",
              labelField: "labelField",
              change: (row, newValue, _index) => {
                this.loadTableFields(_index, newValue);
              },
            },
            {
              type: "select",
              name: "transField",
              label: "翻译字段",
              cascade: true,
              valueField: "valueField",
              labelField: "labelField",
            },
            {
              type: "select",
              name: "uniqueField",
              cascade: true,
              label: "唯一键",
              valueField: "valueField",
              labelField: "labelField",
            },
          ],
        },
      ],
      isListShow: true,
      form: [
        {
          type: "label",
          name: "tableSchema",
          label: "数据库名",
        },
        {
          type: "label",
          name: "tableName",
          label: "表名",
        },
        {
          type: "label",
          name: "tableComment",
          label: "表备注",
        },
      ]
    };
  },
  created() {
    this.tableSchema = this.$route.query && this.$route.query.tableSchema;
    this.tableName = this.$route.query && this.$route.query.tableName;
  },
  mounted() {
    this.initData();
  },
  methods: {
    async initData() {
      this.init = await this.$pagexRequest({
        url: `/basic/ms/table/getTableInfo?tableSchema=${this.tableSchema}&tableName=${this.tableName}&configType=listColumn`,
        method: "get",
      });
      this.controls[3].optionsInitSetts = this.controls[3].optionsSetts;
      this.init.fields.forEach((item) => {
        this.controls[3].optionsSetts.push({ transField: [], uniqueField: [] });
      });
      this.init.fields.forEach((item, _index) => {
        if (item.transTable) {
          this.loadTableFields(_index, item.transTable);
        }
      });

      let dictGroups = await this.$pagexRequest({
        url: "/basic/ms/dictGroup/findList",
        method: "get",
      });
      console.log('dictGroups',dictGroups);

      dictGroups.forEach((item) => {
        item.valueField = item.groupCode;
        item.labelField = item.groupName + "(" + item.groupCode + ")";
      });

      this.controls[3].controls[4].options = dictGroups;

      let tables = await this.$pagexRequest({
        url: `/basic/ms/table/findList?tableSchema=${this.tableSchema}`,
        method: "get",
      });

      tables.forEach((item) => {
        item.valueField = item.tableName;
        item.labelField = item.tableComment + "(" + item.tableName + ")";
      });

      this.controls[3].controls[5].options = tables;

      this.$nextTick(() => {
        this.$set(this, "initFinsh", true);
      });
    },
    loadTableFields(_index, _tableName) {
      this.$pagexRequest({
        url: `/basic/ms/table/getTableInfo?tableSchema=${this.tableSchema}&tableName=${_tableName}`,
        method: "get",
      }).then((res) => {
        let fields = res.fields;
        fields.forEach((item) => {
          item.valueField = item.name;
          item.labelField = item.label + "(" + item.name + ")";
        });
        this.$set(this.controls[3].optionsSetts[_index], "transField", fields);
        this.$set(this.controls[3].optionsSetts[_index], "uniqueField", fields);
      });
    },
    submit() {
      this.isListShow = false;
      let formData = {};
      formData.dbName = this.tableSchema;
      formData.tableName = this.tableName;
      formData.remark = this.tableComment;
      formData.listColumnSett = JSON.stringify(
        this.$refs.listSettForm.getModel().fields
      );
      this.$pagexRequest({
        url: `/basic/ms/table/updateTableGenerateConfig`,
        method: "put",
        data: formData,
      }).then((res) => {
        console.log(res);
      });
    },
    goback(){
      this.isListShow = true;
    }
  },
};
</script>
