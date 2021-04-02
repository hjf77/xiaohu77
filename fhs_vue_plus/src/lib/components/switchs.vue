<template>
    <div>
        <template v-if="isHaveFatherOption">
            <el-switch
                :active-value="activeValue"
                :inactive-value="inactiveValue"
                :active-text="options.label"
                v-model="switchValue"
                active-color="#13ce66"
                inactive-color="#303133"
            >
            </el-switch>
        </template>
        <template v-else>
            <el-switch
                :active-value="activeValue"
                :inactive-value="inactiveValue"
                :active-text="ownerOption.label"
                v-model="switchValue"
                active-color="#13ce66"
                inactive-color="#303133"
            >
            </el-switch>
        </template>
    </div>
</template>

<script>
import { handleStrParam } from "@/lib/utils/param";
export default {
    name: "pagexSwitchs",
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
           type: String| Boolean| Array,
        },
    },
    data() {
        return {
            ownerOption: [],
            isHaveFatherOption: false,
            switchValue:this.value,
            activeValue:1,
            inactiveValue:0	
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
         isArray(obj){
            return Array.isArray(obj)
        }
    },
};
</script>

<style lang="scss" scoped>
</style>