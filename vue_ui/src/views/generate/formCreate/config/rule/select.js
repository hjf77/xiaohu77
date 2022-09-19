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
            { type: 'switch', field: 'clearable', title: '是否可以清空选项' ,value:true},
            { type: 'switch', field: 'isIsValueNum', title: 'value是否为数字' },
            { type: 'switch', field: 'filterable', title: '数据过滤' ,value:true},
            { type: 'switch', field: 'multiple', title: '是否多选'},
            {
                type: 'switch',
                field: 'remote',
                title: '是否可远程搜索'
            },
            { type: 'inputNumber', field: 'multiple-limit', title: '多选时用户最多可以选择的项目数，为 0 则不限制' }
        ];
    }
};
