<template>
  <el-scrollbar class="scrollbar" :style="styleName">
    <div class="form-detail-container">
      <el-form class="pageX-form-detail" ref="form" :model="init" :inline="true" :label-width="labelWidth+'px'" :style="{ '--labelWidth': labelWidth+'px' }">
        <template v-for="(item,index) in list" v-if="!item.isDetailHide">
          <el-form-item :class="{'labelLeft': item.isAndClass}" v-if="item.type == 'textarea'" :label="item.label+' :'" :key="index"
                        :style="{width:item.width?(item.width+'px'):'100%'}">
            <div class="type-textarea-span" :title="init[item.name]">{{ init[item.name] ? init[item.name] : '无' }}</div>
          </el-form-item>

          <el-form-item v-else-if="item.type == 'transfer'" :label="item.label+' :'" :key="index"
                        :style="{width:item.width?(item.width+'px'):'100%'}">
            <div class="type-textarea-span" :title="init[item.name]">{{ init[item.name] ? init[item.name] : '无' }}</div>
          </el-form-item>

          <el-form-item v-else-if="item.type == 'file' || item.type == 'uploadFileAsync'" :label="item.label+' :'" :key="index"
                        :style="{width:item.width?(item.width+'px'):'100%'}">
            <div v-if="init[item.name]" class="type-file-span">
              <pagex-fileDetail :fileIds="init[item.name]" :is-download="item.isDownload"></pagex-fileDetail>
            </div>
            <div v-else class="type-textarea-span">无</div>
          </el-form-item>

          <el-form-item v-else-if="item.type == 'uploadPicture'" :label="item.label+' :'" :key="index"
                        :style="{width:item.width?(item.width+'px'):'100%'}">
            <div v-if="init[item.name]" class="type-file-span">
              <pagex-previewPicture :fileIds="init[item.name]"></pagex-previewPicture>
            </div>
            <div v-else class="type-textarea-span">无</div>
          </el-form-item>

          <el-form-item v-else-if="item.type == 'select'" :label="item.label+' :'" :key="index"
                        >
            <div class="type-text-span" :style="{width:item.width?(item.width+'px'):'305px'}">
              <span v-if="item.click && init[item.name]" style="cursor: pointer;color: #0477E1" @click="handleClick(item, init[item.prop])" :title=" init[item.name]">{{ init[item.name] ? init[item.name] : '无' }}</span>
              <span v-else :title=" init[item.name]">{{ init[item.name] ? init[item.name] : '无' }}</span>
            </div>

          </el-form-item>
          <el-form-item v-else-if="item.type == 'textareaEditor'" :label="item.label+' :'" :key="index"
                        :style="{width:item.width?(item.width+'px'):'100%'}">
            <div class="type-textarea-span" v-html="init[item.name] || '无'" :title="init[item.name] | htmlText"></div>
          </el-form-item>
          <el-form-item v-else-if="item.type == 'slot'" :label="item.label+' :'" :key="index"
                        :style="{width:item.width?(item.width+'px'):'100%'}">
            <slot
              v-bind:data="init[item.name]"
              v-if="item.type === 'slot'"
              :name="item.name"
              :title="init[item.name]"
            ></slot>
          </el-form-item>
          <el-form-item v-else="item.type == 'text'" :label="item.label+' :'" :key="index"
                        >
            <div class="type-text-span" :style="{width:item.width?(item.width+'px'):'305px'}" :title=" init[item.name]">{{ init[item.name] ? init[item.name] : '无' }}</div>
          </el-form-item>
        </template>
      </el-form>
      <slot></slot>
      <div class="form-btn">
        <el-button
          class="save-btn form-btn-item"
          v-for="(itemBtn, index) in buttons"
          :key="index"
          @click="btnClick(itemBtn)"
        >{{ itemBtn.name }}
        </el-button
        >
        <el-button v-if="isCancle" @click="cancel" class="form-btn-item">取消</el-button>
      </div>
    </div>
  </el-scrollbar>
</template>

<script>
import {deepClone} from "../utils";

export default {
  name: "FormDetail",
  props: {
    labelWidth:{
      type:Number,
      default:()=>120
    },
    buttons: {
      type: Array,
      default: () => [],
    },
    isCancle: {
      type: Boolean,
      default: true
    },
    init: {
      type: Object,
      default: {}
    },
    controls: {
      /**
       {
          type: "text",  {text,textarea,file,select}类型
          name: "mobile", model模型
          label: "手机号", label
          width:300 宽度
        },
       */
      type: Array,
      default: () => [],
    },
    fileAllList: {
      type: Array,
      default: () => [],
    },
    dialogHeight:{
      type:String,
      default:"auto"
    },
    namespace: {
      type: String,
      default: null,
    }
  },
  filters: {
    htmlText(val) {
      const reg=/<\/?.+?\/?>/g
      return val ? val.replace(reg, '') : '无'
    }
  },
  inject: ["reloadable"],
  computed:{
    styleName(){
      return {
        height:this.dialogHeight
      }
    }
  },
  data() {
    return {
      list: []
    };
  },
  created() {
    this.initControls();
  },
  methods: {
    // 确认事件
    btnClick(itemBtn) {
      itemBtn.click.call()
    },
    /**
     * 初始化Controls
     */
    initControls() {
      const typeList = ['select', 'checkbox', 'treeSelect', 'radio', 'transfer']
      const tempList = deepClone(this.controls)
      let list = [];
      tempList.forEach(item => {
        let status = typeList.indexOf(item.type) != -1
        if (status) {
          item.name = item.name + 'Name'
        }
        if(item.ifFun){
          if(item.ifFun.call()){
            list.push(item);
          }
        }else{
          list.push(item);
        }
      })
      this.$set(this, 'list', list || [])
    },


    /**
     * 取消并企图关闭弹窗
     *
     */
    cancel(){
      // 通过 $EventBus 刷新指定页面的列表
      if (this.namespace) {
        this.$EventBus.$emit(this.namespace + '_closeDialog')
      }else{
        this.reloadable.$parent.$parent.$parent.open = false;
        this.reloadable.$parent.$parent.open = false;
        this.reloadable.$parent.open = false;
      }
    },

    /**
     * 点击事件
     */
    handleClick(item,val) {
      item.click.call(this, val)
    }
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
    letter-spacing: 1px;
  }

  .type-textarea-span {
    width: 100%;
    letter-spacing: 1px;
    overflow: hidden;
    display: -webkit-box;
    text-overflow: ellipsis;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    display: -webkit-inline-box;
  }

  .type-file-span {
    width: 100%;
    > p {
      margin: 0;
      background: rgba(0, 0, 0, 0.1);
      padding: 0 10px;
      border-radius: 4px;
      cursor: pointer;
      > .file-size {
        float: right;
      }
    }
  }
}

::v-deep .type-textarea-span>p {
    margin: 0;
}
::v-deep .type-textarea-span>p>span {
  background-color: #ffffff!important;
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
}
::v-deep .el-form-item__content {
  width: calc(100% - var(--labelWidth))
}

::v-deep .save-btn {
  color: #fff;
  border: #0f9b9c;
  background: #0f9b9c;
}
::v-deep .labelLeft .el-form-item__label{
  padding-left: 30px;
  padding-top: 8px;
  text-align: left;
  line-height: 18px;
}
</style>
