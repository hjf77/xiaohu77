<!--
  模块名称：列表配置
  开发人员：wanglei
  创建时间: 2022-08-31
-->
<template>
  <div>
    <div v-if="!showForm">
      <!-- 编辑参数配置对话框 -->
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
      <center>
        <el-button @click="submit()">保存并配置表单</el-button>
        <el-button @click="submitAndReview()">保存并预览</el-button>
      </center>

      <el-dialog
        v-if="openReview"
        class="pagex-dialog-theme"
        title="预览"
        :visible.sync="openReview"
      >
        <!-- 新增/修改/修订 -->
        <list-review
          :tableSchema="tableSchema"
          :tableName="tableName"
        >
        </list-review>
      </el-dialog>
    </div>
    <div v-if="showForm">
      <formCreate :table-name="tableName" :table-schema="tableSchema"></formCreate>
    </div>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
import listReview from "./listReview.vue";
import formCreate from "@/views/generate/formCreate/formCreate.vue";
export default {
  name: "listSett",
  components: {
    listReview,
    formCreate
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
      showForm:false,
      openReview:false,
      tableSchema: "",
      tableName: "",
      init: {
        tableSchema: "",
        tableName: "",
        tableComment: "",
        fields: [],
      },
      optionsInitSetts: [],
      initFinsh: false,
      formData: {
        fields: [],
        tableOperation: [],
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
              type: 'label',
              name: 'name',
              label: '字段名',
              fixed: 'fixed'
            },
            {
              type: 'text',
              name: 'label',
              label: '备注',
              fixed: 'fixed'
            },
            {
              type: "switch",
              name: "isListShow",
              label: "列表是否展示",
            },
            {
              type: "switch",
              name: "isDict",
              label: "是否是字典",
            },
            {
              type: 'select',
              name: 'elementType',
              label: '过滤条件类型',
              dictCode: 'filter_element_type'
            },
            {
              type: 'select',
              name: 'dictCode',
              label: '字典分组',
              valueField: 'valueField',
              labelField: 'labelField'
            },
            {
              type: 'text',
              name: 'url',
              label: '接口',
            },
            {
              type: 'text',
              name: 'valueField',
              label: 'value字段',
            },
            {
              type: 'text',
              name: 'labelField',
              label: 'label字段',
            },
          ],
        },
        {
          type: "checkbox",
          name: "tableOperation",
          label: "操作",
          dictCode: 'operator_type'
        }
      ]
    }
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
      let dictGroups = await this.$pagexRequest({
        url: "/basic/ms/dictGroup/findList",
        method: "get",
      });
      // 字典分组下拉
      dictGroups.forEach((item) => {
        item.valueField = item.groupCode;
        item.labelField = item.groupName + "(" + item.groupCode + ")";
      });
      this.controls[3].controls[5].options = dictGroups;
      this.$nextTick(() => {
        this.$set(this, "initFinsh", true);
      });
    },
    submit(_callback) {
      let formData = {};
      formData.dbName = this.tableSchema;
      formData.tableName = this.tableName;
      formData.remark = this.tableComment;
      formData.listColumnSett = JSON.stringify(
        this.$refs.listSettForm.getModel().fields
      );
      formData.tableOperation = this.$refs.listSettForm.getModel().tableOperation;
      this.$pagexRequest({
        url: `/basic/ms/table/updateTableGenerateConfig`,
        method: "put",
        data: formData,
      }).then((res) => {
         if(_callback){
           _callback(res);
         }else{
           this.showForm=true;
         }
      });
    },
    submitAndReview() {
      this.submit((res)=>{
        this.openReview = true;
      })
    },
  },
};
</script>
