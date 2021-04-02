<template>
    <div>
        <template v-if="isHaveFatherOption">
            <el-checkbox
                v-model="newValue"
                v-for="item in options"
                :key="item.value"
                :label="item.value"
                >{{ item.label }}</el-checkbox
            >
        </template>

        <template v-else>
            <el-checkbox
                v-model="newValue"
                v-for="item in ownerOption"
                :key="item.valueField"
                :label="item.valueField"
                >{{ item.labelField }}</el-checkbox
            >
        </template>
    </div>
</template>

<script>
import { handleStrParam } from "@/lib/utils/param";
export default {
    name: "pagexCheckBox",
    props: {
        options: {
            type: Array,
            default: () => undefined,
        },
        url: {
            type: String,
            default: () => "",
        },
        labelField: {
            type: String,
            default: () => "title",
        },
        valueField: {
            type: String,
            default: () => "id",
        },
        param: {
            type: Object,
            default: () => {},
        },
        value:{
            type:Array,
            default:()=> []
        }
    },
    data() {
        return {
            checked:'',
            ownerOption: [],
            isHaveFatherOption: false,
        };
    },
    computed:{
         newValue: {
            get: function () {
                return this.value;
            },
            set: function (value) {
                this.$emit("input", value);
            },
        },
    },
    async mounted() {
        this.isHaveFatherOption = typeof this.options != "undefined";
        if (this.url) {
            this.loadData();
        }
    },
    methods: {
        async loadData() {
            const { data } = await this.$pagexRequest.get(
                handleStrParam(this.url, this.param)
            );

            let _options = data || [];
            let _that = this;
            _options.forEach(function (_item) {
                _item.labelField = _item[_that.labelField];
                _item.valueField = _item[_that.valueField];
            });
            if (this.isHaveFatherOption) {
                this.$emit("update:options", _options);
            } else {
                this.ownerOption = _options;
            }
        },
    },
};
</script>

<style lang="scss" scoped>
</style>