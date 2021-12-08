<template>
  <div class="app-container">
    <pagex-crud
      v-bind="crudSett"
      :namespace="namespace"
    >
      <template v-slot:form="prop">
        <!-- 新增 修改 弹框-->
        <pagex-dialog slot="form" :title="dialogTitle" :namespace="namespace"  v-if="open" :visible.sync="open"  class="pagex-dialog-theme">
          <pagex-form
            v-if="!isDetail"
            v-bind="formSett"
            :init="init"
            :isEdit="isEdit"
            :namespace="namespace"
          >
          </pagex-form>
          <pagex-formDetail
            v-if="isDetail"
            v-bind="formSett"
            :init="init"
            :namespace="namespace"
          >
          </pagex-formDetail>
        </pagex-dialog>
      </template>
    </pagex-crud>
  </div>
</template>

<script>
export default {
  name: "crudForm",
  props: {
    namespace: {
      type: String,
      default: null,
    },
    idFieldName: {
      type: String,
      default: null,
    },
    title: {
      type: String,
      default: null,
    },
    crudSett: {
      type: Object,
      default:  {

      },
    },
    formSett: {
      type: Object,
      default: {

      },
    }
  },
  data() {
    return {
      isEdit:false,
      isDetail:false,
      open:false,
      init: {},
      dialogTitle:'',
    };
  },
  created() {
    if(this.crudSett.buttons){
      this.crudSett.buttons.forEach(button=>{
        //如果没有绑定click
        if(button.title == '新增'){
          button.click = this.addFun;
          if(this.namespace){
            button.permission = [this.namespace + ':add'];
          }
        }
      });
    }
    if(this.crudSett.columns){
      this.crudSett.columns.forEach((_item, _index) => {
        if (_item.type == "textBtn") {
          _item.textBtn.forEach((btn, btnIndex) => {
            if(btn.title=='编辑'){
              btn.click = this.editFun;
              if(this.namespace){
                _item.permission = [this.namespace + ':update'];
              }
            }
            if(btn.title=='删除'){
              if(this.namespace){
                _item.permission = [this.namespace + ':del'];
              }
              if(!_item.idFieldName){
                _item.idFieldName = this.idFieldName;
              }

            }
            if(btn.title=='详情'){
              btn.click = this.detailFun;
            }
          });
        }
      });
    }
  },
  methods: {
    editFun(_row){
      this.dialogTitle = this.title ? (this.title + '编辑') : '编辑';
      this.open = true;
      this.isEdit = true;
      this.isDetail = false;
      this.init = _row;
      this.formSett.data[this.idFieldName] = _row[this.idFieldName];
    },
    detailFun(_row){
      this.dialogTitle = this.title ? (this.title + '详情') : '详情';
      this.open = true;
      this.isEdit = false;
      this.isDetail = true;
      this.init = _row;

    },
    addFun(){
      this.dialogTitle = this.title ? (this.title + '新增') : '新增';
      this.open = true;
      this.isEdit = false;
      this.isDetail = false;
      this.init = {};
    },

  }
};
</script>
