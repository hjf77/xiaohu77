<template>
    <div>
        <template v-if="isHaveFatherOption">
            <el-pagination
                layout="prev, pager, next"
                :total="options.total"
                background
            >
            </el-pagination>
        </template>
        <template v-else>
            <el-pagination
                layout="prev, pager, next"
                :total="ownerOption.total"
                background
            >
            </el-pagination>
        </template>
    </div>
</template>

<script>
import { handleStrParam } from "@/lib/utils/param";
export default {
    name: "pagexPagination",
    model: {
        prop: "value",
        event: "change",
    },
    props: {
        options: {
            type: Number,
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
        value: String | Number | Boolean,
    },
    data() {
        return {
            ownerOption: [],
            isHaveFatherOption: false,
            radioValue: this.value,
        };
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