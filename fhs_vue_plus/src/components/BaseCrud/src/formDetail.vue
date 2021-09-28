<template>
  <div class="form-detail-container">
    <el-form class="pageX-form-detail" ref="form"  :inline="true" :label-width="labelWidth+'px'" :style="{ '--labelWidth': labelWidth+'px' }">
      <template v-for="(item,index) in formColumn">
        <el-form-item v-if="item.type == 'textarea'" :label="item.label + ' :'" :key="index"
                      :style="{width:item.width?(item.width+'px'):'100%'}">
          <div class="type-textarea-span" :title=" value[item.name]">{{ value[item.name] ? value[item.name] : '无'  }}</div>
        </el-form-item>
        <el-form-item v-else-if="item.type == 'file' || item.type == 'uploadFileAsync' || item.type == 'uploadFile'" :label="item.label + ' :'" :key="index"
                      :style="{width:item.width?(item.width+'px'):'100%'}">
          <div class="type-file-span" :title=" value[item.name]">
            <file-list :id="value['fileIds'] || value[item.name]"></file-list>
          </div>
        </el-form-item>

        <el-form-item v-else-if="item.type == 'select'" :label="item.label + ' :'" :key="index"
                      :style="{width:item.width?(item.width+'px'):'100%'}">
          <span class="type-text-span" :title=" value[item.name]">{{ value[item.name] ? value[item.name] : '无' }}</span>
        </el-form-item>
        <el-form-item v-else-if="item.type == 'slot'" :label="item.label+' :'" :key="index"
                      :style="{width:item.width?(item.width+'px'):'100%'}">
            <slot
              v-bind:data="form[item.name]"
              v-if="item.type === 'slot'"
              :name="item.name"
              :title="form[item.name]"
          ></slot>
        </el-form-item>
        <el-form-item v-else="item.type == 'text'" :label="item.label + ' :'" :key="index"
                      :style="{width:item.width?(item.width+'px'):'420px'}">
          <span class="type-text-span" :title=" value[item.name]">{{ value[item.name] ? value[item.name] : '无'  }}</span>
        </el-form-item>
      </template>
    </el-form>
    <slot></slot>
    <div class="form-btn">
      <el-button @click="cancel" class="form-btn-item">取消</el-button>
    </div>
  </div>

</template>

<script>
import utils from "./utils";
import fileList from './fileList'
export default {
  name: "CrudFormDetail",
  components:{fileList},
  mixins:[utils],
  props: {
    formOption: {
      type: Object,
      default: null
    },
    //初始化数据
    value: {
      type: Object,
      default: null
    }
  },
  computed:{
    labelWidth(){
      return this.formOption.labelWidth || 120
    },

    //表单项
    formColumn() {
      const typeList = ['select', 'checkbox', 'treeSelect', 'radio', 'transfer']
      const Column = this.deepClone(this.formOption.group)
      Column.forEach(item => {
        let status = typeList.indexOf(item.type) != -1
        if (status) {
          item.name = item.name + 'Name'
        }
      })
      return Column || []
    }
  },
  created() {
  },
  methods:{

    //取消
    cancel() {
      this.$emit("cancel")
    },
  }
}
</script>


<style lang="scss" scoped>
.form-detail-container {
  padding: 0px;
}

.pageX-form-detail {
  overflow: hidden;

  .type-text-span {
    width: 100%;
    display: inline-block;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .type-textarea-span {
    width: 100%;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 3;
    overflow: hidden;
  }

  .type-file-span {
    width: 100%;
    height: 120px;

    > p {
      margin: 0;
      background: rgba(0, 0, 0, 0.1);
      padding: 0 10px;
      border-radius: 4px;
      margin-bottom: 10px;
      cursor: pointer;

      > .file-size {
        float: right;
      }
    }
  }
}

::v-deep .el-form-item {
  margin-bottom: 0px;

  .el-form-item__label {
    font-weight: normal;
    font-size: 14px;
    font-family: Microsoft YaHei;
    color:#888;
  }
}

::v-deep .el-form {
  padding: 20px;
  margin: 20px;
  background: #ffffff;
  border-radius: 10px;
}

::v-deep .el-dialog__body {
  margin: 20px;
  background: #ffffff;
  border-radius: 10px;
}

.form-btn{
  background: #FFFFFF;
  box-sizing: border-box;
  padding: 12px 20px;
  text-align:right;
  margin-bottom:17px;
}
::v-deep .el-form-item__content {
  width: calc(100% - var(--labelWidth))
}

::v-deep .save-btn {
  color: #fff;
  border: #0f9b9c;
  background: #0f9b9c;
}
</style>
