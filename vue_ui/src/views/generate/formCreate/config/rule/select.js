import uniqueId from '@form-create/utils/lib/unique';
import { makeOptionsRule, makeRequiredRule } from '../../utils/index';
import request from '@/utils/request'

export function getDictList() {
    return request({
        url: `/basic/ms/dictGroup/findList`,
        method: 'GET'
    })
}

export function getDictOptions() {
    const dictKey = sessionStorage.getItem("dictKey") || `[]`
    console.log('dictKey', dictKey);
    return JSON.parse(dictKey)
}

const label = '选择器';
const name = 'select';

export default {
    icon: 'icon-select',
    label,
    name,
    rule() {
        return {
            type: name,
            field: uniqueId(),
            title: label,
            info: '',
            effect: {
                fetch: ''
            },
            props: {},
            dictCode:'',
            options: [
                { value: '1', label: '选项1' },
                { value: '2', label: '选项2' },
            ]
        };
    },
    props() {
        return [
            makeOptionsRule('options'),
            { type: 'input', field: 'width', title: '宽度' },
            { type: 'input', field: 'defaultValue', title: '默认值' },
            makeRequiredRule(),
            { type: 'inputNumber', field: 'maxlength', title: '最大输入长度' },
            {
                type: 'inputNumber',
                field: 'minlength',
                title: '最小输入长度'
            },
            { type: 'switch', field: 'clearable', title: '是否可以清空选项' },
            { type: 'input', field: 'dataFilter', title: '数据过滤' },
            {
                type: 'select',
                field: 'fieldId',
                value: '',
                title: '字段 ID',
                options: getDictOptions()
            },
            { type: 'switch', field: 'multiple', title: '是否多选' },
            {
                type: 'switch',
                field: 'disabled',
                title: '是否禁用'
            },

            {
                type: 'switch',
                field: 'filterable',
                title: '是否可搜索'
            },
            { type: 'inputNumber', field: 'multipleLimit', title: '多选时用户最多可以选择的项目数，为 0 则不限制' }
        ];
    }
};
