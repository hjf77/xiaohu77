<!--
  模块名称：字典分组
  开发人员：谁不写是瓜皮
  创建时间: 2022-10-28
-->
<template>
  <div>
    <!-- 详情参数配置对话框 -->
    <pagex-formDetail
      v-if="isDetail"
      :controls="controls"
      :init="init"
    >
    </pagex-formDetail>

    <!-- 编辑参数配置对话框 -->
    <pagex-form
      v-else
      :isEdit="isEdit"
      :addApi="addApi"
      :updateApi="updateApi"
      :data="formData"
      :init="init"
      :controls="controls"
      :buttons="buttons"
      :isVee="false"
    >

    </pagex-form>
  </div>
</template>
<script>
import {mapGetters} from "vuex";
export default {
  name: "dictGroupForm",
  inject: ["reloadable"],
  props: {
    isEdit: Boolean,
    init: Object,
    isDetail:Boolean,
  },
  computed: {
    ...mapGetters(["user"])
  },
  data() {
    return {
      formData:
        "{}",
      addApi: "basic/ms/dictGroup/",
      updateApi: "basic/ms/dictGroup/",
      controls: [

        {
          clearable : true,
          name : "groupName",
          rule : [
            {
              message : "请输入分组名称",
              required : true,
              trigger : "blur"
            }
          ],
          label : "分组名称",
          type : "text"
        },
        {
          clearable : true,
          name : "groupCode",
          rule : [
            {
              message : "请输入分组编码",
              required : true,
              trigger : "blur"
            }
          ],
          label : "分组编码",
          type : "text"
        }

      ]
    }
  },
  created() {

  },
  methods: {


  },
};
</script>

<style lang="scss" scoped>
::v-deep .pageX-form-detail .type-file-span {
  height: 0px;
}
</style>
