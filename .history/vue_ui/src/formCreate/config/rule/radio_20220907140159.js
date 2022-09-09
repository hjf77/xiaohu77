import uniqueId from '@form-create/utils/lib/unique';
import {makeOptionsRule, makeRequiredRule} from '../../utils/index';

const label = '单选框';
const name = 'radio';

export default {
    icon: 'icon-radio',
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
            options: [
                {value: '1', label: '选项1'},
                {value: '2', label: '选项2'},
            ]
        };
    },
    props() {
        return [
            makeOptionsRule('options'),
            { type: 'input', field: 'width', title: '宽度' },
            { type: 'input', field: 'defaultValue', title: '默认值' },
            makeRequiredRule(),
            // 清空按钮
            // 数据过滤
            { type: 'input', field: 'dictionaryBind', title: '字典绑定' },
            {type: 'switch', field: 'disabled', title: '是否禁用'},
            
            // {
            //     type: 'switch',
            //     field: 'type',
            //     title: '按钮形式',
            //     props: {activeValue: 'button', inactiveValue: 'default'}
            // }, {type: 'input', field: 'textColor', title: '按钮形式的 Radio 激活时的文本颜色'}, {
            //     type: 'input',
            //     field: 'fill',
            //     title: '按钮形式的 Radio 激活时的填充色和边框色'
            // }
        ];
    }
};
