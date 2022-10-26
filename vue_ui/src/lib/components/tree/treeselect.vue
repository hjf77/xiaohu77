<!--树形组件-->
<template>
  <div v-if="status">
    <treeselect
      :options="dataList"
      :normalizer="normalizer"
      :placeholder="name || '请选择'"
      v-model="selectId"
      @select="repair"
    />
  </div>
</template>
<script>
import { getData } from "@/api/tree";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  model: {
    event: "change", //事件名称
  },
  props: {
    treeUrl: String,
    querys: Object,
    name: String,
    repairid: String,
  },
  components: { Treeselect },
  data() {
    return {
      url: "",
      dataList: [],
      selectId: "",
      status: true,
    };
  },
  mounted() {
    this.url = this.treeUrl;
    if (this.repairid != "") {
      this.selectId = this.repairid;
    }
  },
  watch: {
    url: function () {
      this.brush();
    },
    repairid: function (data) {
      this.status = false;
      this.$nextTick(() => {
        this.status = true;
        this.selectId = data;
      });
    },
    selectId: function (data) {
      if (data == undefined) {
        this.repair(null);
      }
    },
  },
  methods: {
    // 请求数据
    RequestData() {
      getData(this.url, this.querys)
        .then((res) => {
          this.dataList = res;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    repair(e) {
      if (e == null) {
        this.$emit("selectId", e);
      } else {
        this.$emit("change", e.id);
        this.$emit("selectId", e);
      }
    },
    // 刷新
    brush() {
      this.RequestData();
    },
    /** 转换菜单数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children,
      };
    },
  },
};
</script>

<style scoped>
</style>
