<!--
模块名称：
开发人员：乔上飞
创建时间:2021/4/2 15:21
-->


<template>
  <div>
    <!-- 添加或修改参数配置对话框 -->
    <pagex-form
      addApi="/ms/wordbook/addWordbookGroup"
      updateApi="/ms/wordbook/updateWordbookGroup"
      initApi=""
      :isEdit="false"
                  :controls="[
              {
                type: 'text',
                name: 'groupName',
                label: '分组名称',
                rule:'required'
              },{
                type: 'text',
                name: 'wordbookGroupCode',
                label: '分组编码',
                rule:'required'
              }
            ]"
      >
      </pagex-form>
  </div>
</template>

<script>
import pagexForm from "@/lib/components/form";
export default {
  name: "addDict",
  components:{
    pagexForm
  },
  data() {
    return {

    }
  },
  props: {
    open: Boolean
  },
  mounted() {
  },
  created() {
  },
  methods: {}
}
</script>

<style scoped lang="scss">

</style>
