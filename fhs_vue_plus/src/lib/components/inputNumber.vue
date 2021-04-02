<template>
    <div>
        <template v-if="isHaveFatherOption">
            <el-input-number
                v-model="countValue"
                :min="options.min || 0"
                :max="options.max || 10"
                :label="options.label || ''"
            ></el-input-number>
        </template>
        <template v-else>
            <el-input-number
                v-model="countValue"
                :min="ownerOption.min||0"
                :max="ownerOption.max||10"
                :label="ownerOption.labelField || ''"
            ></el-input-number>
        </template>
    </div>
</template>

<script>
import { handleStrParam } from "@/lib/utils/param";
export default {
    name: "pagexIpnutNumber",
    model: {
        prop: "value",
        event: "change",
    },
    props: {
        options: {
            type: Object,
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
        value: {
            type:Number
        },
    },
    data() {
        return {
            ownerOption: [],
            isHaveFatherOption: false,
            countValue: this.value,
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