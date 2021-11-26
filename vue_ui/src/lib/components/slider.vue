<template>
    <div>
        <template v-if="isHaveFatherOption">
            <el-slider
                v-model="sliderValue"
                :min="options.min || 0"
                :max="options.max || 10"
                :label="options.label || ''"
            ></el-slider>
        </template>
        <template v-else>
            <el-slider
                v-model="sliderValue"
                :min="ownerOption.min||0"
                :max="ownerOption.max||10"
                :label="ownerOption.labelField || ''"
            ></el-slider>
        </template>
    </div>
</template>

<script>
import { handleStrParam } from "@/lib/utils/param";
export default {
    name: "pagexSlider",
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
            value1: 0,
            ownerOption: [],
            isHaveFatherOption: false,
            sliderValue: this.value,
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