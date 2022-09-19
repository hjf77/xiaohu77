<!--
模块名称：
开发人员：乔上飞
创建时间:2021/4/2 15:21
-->


<template>
  <div>
    <!-- 添加或修改参数配置对话框 -->
    <pagex-form
      v-if="isInit"
      addApi=""
      updateApi=""
      :init="{}"
      :isVee="false"
      :data="formData"
      :isEdit="false"
      :controls="controls"
      :isHaveInitBtn="isHaveInitBtn"
    >
    </pagex-form>
  </div>
</template>

<script>
export default {
  name: "formReview",
  data() {
    return {
      isInit:false,
      isHaveInitBtn:false,
      formData:{

      },
      controls:[

      ]
    }
  },
  props: {
    tableSchema: String,//数据库名字
    tableName: String,//表名字
  },
  created() {
    this.init();
  },
  methods: {
    async init(){
      let formSett = await this.$pagexRequest({
        url: `/basic/ms/table/formViewJson?tableSchema=${this.tableSchema}&tableName=${this.tableName}`,
        method: "get",
      });
      this.$set(this, "controls", formSett.controls);
      this.$set(this, "formData", JSON.parse(formSett.defaultValueData));
      this.isInit = true;
    }
  }
}
</script>
