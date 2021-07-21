<!--树形组件-->
<template>
  <div>
    <treeselect
      :options="treeDatas"
      :normalizer="normalizer"
      :placeholder="placeholder"
      v-model="newValue"
      :multiple="multiple"
      :value-consists-of="'LEAF_PRIORITY'"
    />
  </div>
</template>
<script>
    import Treeselect from "@riophae/vue-treeselect";
    import "@riophae/vue-treeselect/dist/vue-treeselect.css";

    export default {
        model: {
            prop: "value",
            event: "change",
        },
        props: {
            api: String,
            value: {
                default: () => {
                    return '';
                },
            },
            queryFilter: {
                type: Object,
                default: () => {
                    return {};
                },
            },
            placeholder: {
                type: String,
                default: () => {
                    return '请选择';
                },
            },
            idField: {
                type: String,
                default: () => {
                    return 'id';
                },
            },
            labelField: {
                type: String,
                default: () => {
                    return 'name';
                },
            },
            multiple:{
              type: Boolean,
              default: () => {
                return false;
              },
            },
            httpMehod:{
              type: String,
              default: () => {
                return 'POST';
              }
            }
        },
        computed: {
            newValue: {
                get: function () {
                  console.log( 'wl-------');
                  console.log(this.value);
                    return this.value;
                },
                set: function (value) {
                    this.$emit("change", value);
                },
            },
        },
        components: {Treeselect},
        data() {
            return {
                treeDatas: [],
            };
        },
        mounted() {
            this.requestData();
        },
        watch: {},
        methods: {
            // 请求数据
            requestData() {
              if(this.httpMehod == 'POST'){
                this.$pagexRequest({
                  url: this.api,
                  data: this.queryFilter,
                  method: "POST",
                })
                .then((res) => {
                  this.treeDatas = res;
                });
              }else if(this.httpMehod == 'GET') {
                this.$pagexRequest.get(this.api)
                .then((res) => {
                  this.treeDatas = res;
                });
              }

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
