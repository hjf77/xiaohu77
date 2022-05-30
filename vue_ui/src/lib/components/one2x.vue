<!--一对多组件-->
<template>
  <div>
    <el-table :data="datas" style="width: 100%;" border>
      <template v-for="(item, index) in realControls">
        <el-table-column type="selection" width="55" align="center" v-if="item.type === 'selection'">
        </el-table-column>
        <el-table-column align="center">
          <template slot="header" slot-scope="scope">
            <span style="color:#2d65dc;">{{ item.label }}</span>
            <i style="color:#F56C6C;" v-if="isRequired(item.rule)">*</i>
          </template>
          <template slot-scope="scope">
            <label v-if="item.type === 'label'">{{ scope.row[item.name] }}</label>
            <el-form-item
              v-else-if="item.type != 'label'"
              :key="scope.$index + '_' + item.name"
              :prop=" attrName + '[' + scope.$index + '].' + item.name"
              :rules="item.rule"
            >
              <pagex-input
                v-if="item.type === 'text'"
                type="text"
                v-model="datas[scope.$index][item.name]"
                autocomplete="off"
                :datas.sync="datas"
                :index="scope.$index"
                :clearable="item.clearable?item.clearable:true"
                show-word-limit
                v-bind="item"
                :one2xOptionsSetts.sync="optionsSetts"
                :style="{ width: item.width ? item.width + 'px' : '150px' }"
              ></pagex-input>
              <pagex-select
                v-bind="item"
                v-model="datas[scope.$index][item.name]"
                v-if="item.type === 'select'"
                :url="item.url"
                :options.sync="optionsSetts[scope.$index][item.name]"
                :methodType="item.methodType"
                :querys="item.querys"
                :labelField="item.labelField"
                :valueField="item.valueField"
                :isValueNum="item.isValueNum"
                :style="{ width: item.width ? item.width + 'px' : (item.multiple ? '200px' : '150px') }"
                @change="item.change?item.change.call(this,item, scope.row[item.name]):''"
              ></pagex-select>
              <el-date-picker
                v-model="datas[scope.$index][item.name]"
                :type="item.formatType || 'date'"
                :placeholder="item.placeholder"
                :value-format="item.formatVal"
                v-if="item.type === 'dates'"
                :picker-options="item.pickerOptions"
                @change="(value) => changeFn(value,item.changeFn)"
                :class="{'surplus' : item.name === 'effectiveDate'}"
                :style="{ width: item.width ? item.width + 'px' : '305px' }"
              ></el-date-picker>
            </el-form-item>
          </template>
        </el-table-column>
      </template>
      <!--按钮-->
      <el-table-column
        label="操作"
        :width="210"
        align="center"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            @click="delRow(scope.$index, scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>


<script>

import {deepClone} from "../utils";

export default {
  name: "pagexOne2x",
  props: {
    //每行的表单项
    controls: {
      type: Array,
      default: () => [],
    },
    //区别 详情 编辑 和 新增
    isEdit: {
      type: Boolean,
      default: false,
    },
    //默认值
    value: {
      type: Array,
      default: () => [],
    },
    //加一行的时候这行的默认数据
    defaultValue: {
      type: Object,
      default: () => {
      },
    },
    //加一行的时候这行的默认数据
    attrName: {
      type: String,
      default: 'datas',
    },
    //当数据变动的时候触发
    onDataChange: {
      type: Function
    },
    optionsInitSetts: {
      type: Array,
      default: () =>[]
    }
  },
  watch: {
    datas: {
      handler: function (value) {
        this.$emit("input", value);
        if (this.onDataChange) {
          this.onDataChange(value);
        }
      },
      deep: true,
    },
  },
  data() {
    return {
      realControls: [],
      datas: [],
      optionsSetts: [],
    };
  },
  created() {
    this.$set(this, 'datas', deepClone(this.value));
    if (!this.isEdit) {
      this.datas.forEach((_item, _index) => {
        this.optionsSetts[_index] = {};
        this.controls.forEach((_item) => {
          if (_item.options) {
            this.optionsSetts[_index][_item.name] = deepClone(_item.options);
          }
        });
      });
    } else {
      this.optionsSetts = deepClone(this.optionsInitSetts)
    }
    this.controls.forEach((_item) => {
      if (!_item.placeholder) {
        _item.placeholder = (_item.type == 'text' || _item.type == 'textarea') ? '请输入' : '请选择';
        _item.placeholder = _item.placeholder + _item.label;
      }
      this.realControls.push(_item);
    });

  },
  methods: {
    proxyIf(_ifFun, _default, _row) {
      if (_ifFun == "disabled") {
        return true;
      }
      return _ifFun ? _ifFun.call(_row) : _default;
    },
    //代理一些事件
    changeFn(val, itemBtn) {
      itemBtn && itemBtn(val);
    },
    //添加一行
    addRow() {
      let rowOptions = {};
      this.optionsSetts.push(rowOptions);
      this.controls.forEach((_item) => {
        rowOptions[_item.name] = _item.options;
      });
      this.datas.push(deepClone(this.defaultValue));
    },
    //删除一行
    delRow(_index, _row) {
      this.optionsSetts.splice(_index, 1);
      this.datas.splice(_index, 1);
    },
    //添加数据
    appendRows(_rows, _optionsSetts) {
      _rows.forEach((_item) => {
        this.datas.push(deepClone(_item));
      });
      _optionsSetts.forEach((_item) => {
        this.optionsSetts.push(deepClone(_item));
      });
    },
    //判断规则是否是必填
    isRequired(rules) {
      if (!rules) {
        return false;
      }
      //element验证
      if (rules instanceof Array) {
        for (let i = 0; i < rules.length; i++) {
          if (rules[i].required) {
            return true;
          }
        }
        return false;
      } else {//vee验证
        return rules.indexOf('required') !== -1;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
</style>
