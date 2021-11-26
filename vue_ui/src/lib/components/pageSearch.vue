<template>
  <div class="crud-container">
    <div class="clear"></div>
    <div class="search">
      <el-form size="small" :inline="true" :model="query" v-if="filters">
        <div class="q">
          <div class="w">
            <el-form-item
              :label="item.formname ? item.formname:item.label"
              v-for="item in filters"
              :key="item.name"
            >
              <el-input
                v-model="query[item.name]"
                v-if="item.type === 'text'"
                prefix-icon="el-icon-search"
                :placeholder="item.placeholder"
              ></el-input>

              <pagex-select
                v-bind="item"
                v-model="query[item.name]"
                v-if="item.type === 'select'"
                placeholder="请选择"
              ></pagex-select>

                <pagex-formTreeSelect
                    v-bind="item"
                    v-model="query[item.name]"
                    v-if="item.type === 'treeSelect'"
                    :api="item.api"
                    :style="{ width: item.width ? item.width + 'px' : '305px' }"
                ></pagex-formTreeSelect>

              <el-date-picker
                v-model="query[item.name]"
                v-if="item.type === 'date-picker'"
                type="daterange"
                range-separator="~"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                :default-time="['00:00:00', '23:59:59']"
              >
              </el-date-picker>
              <el-date-picker
                v-model="query[item.name]"
                v-if="item.type === 'datetimerange'"
                type="datetimerange"
                range-separator="~"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              >
              </el-date-picker>

              <pagex-checkbox
                v-bind="item"
                v-model="query[item.name]"
                v-if="item.type === 'checkbox'"
                :dictCode="item.dictCode"
              ></pagex-checkbox>
            </el-form-item>
          </div>
          <div class="e">
            <el-form-item v-if="filters.length">
              <el-button
                size="mini"
                type="primary"
                @click="search"
              >搜索
              </el-button>
              <el-button
                size="mini"
                type="primary"
                plain
                @click="reset"
              >重置
              </el-button>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import PagexCheckBox from "./checkbox.vue";

export default {
  components: {PagexCheckBox},
  props: {
    filters: {
      type: Array,
      default: () => [],
    },
    data: {
      type: Object,
      default: () => {},
    },
  },
  data() {
    return {
      query: {},
    };
  },
  provide() {
    return {
      reloadable: this,
    };
  },
  created() {
    if (this.data) {
      this.$set(this, "query", JSON.parse(JSON.stringify(this.data)));
    }
    this.filters.forEach((item, index) => {
      if(!item.placeholder){
        let title = item.formname ? item.formname:item.label;
        item.placeholder = (item.type=='text' ? '请输入' : '请选择') + title;
      }
    });
  },
  mounted() {
  },
  methods: {
    search() {
      this.$emit("search", this.query);
    },
    //重置
    reset() {
      this.$set(this, "query", JSON.parse(JSON.stringify(this.data)));
      this.$emit("reset");
    },
    // 刷新缓存
    reload(_groupName) {
      this.search();
    },

  }
}
</script>

<style lang="scss" scoped>
.el-form--inline .el-form-item {
  margin-right: 40px;
}
::v-deep .el-input--small .el-input__inner {
  width: 220px;
}
.crud-container {
  background: #F3F3F3;
}
.search {
  background: #ffffff;
  padding: 20px 20px 0 20px;
  margin-bottom: 20px;
  border-radius: 4px;
}

.lf {
  float: left;
}
.centerTitle{
    font-size: 16px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #333333;
}

.lr {
  float: right;
}

.q {
  display: flex;
  display: -webkit-box;
  display: -ms-flexbox;
  display: -webkit-flex;
}
.w {
  width: 100%;
}

.e {
  width: 260px;
}

</style>
