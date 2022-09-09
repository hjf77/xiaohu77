import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '密码框';
const name = 'password';
const type = 'password';

export default {
    icon: 'icon-input',
    label,
    name,
    rule() {
        return {
            type: name,
            field: uniqueId(),
            title: label,
            info: '',
            props: {}
        };
    },
    props() {
        return [
            // {
            //     type: 'select',
            //     field: 'type',
            //     title: '类型',
            //     options: [{ label: 'text', value: 'text' }, {
            //         label: 'textarea',
            //         value: 'textarea'
            //     }, { label: 'number', value: 'number' }, { label: 'password', value: 'password' }]
            // },
            { type: 'input', field: 'width', title: '输入框宽度' },
            { type: 'input', field: 'value', title: '默认值' },
            makeRequiredRule(),
            { type: 'input', field: 'validationRule', title: '内置验证规则' },
            { type: 'input', field: 'regular', title: '自定义正则' },
            { type: 'input', field: 'customRegular', title: '自定义正则提示' },
            { type: 'inputNumber', field: 'maxlength', title: '最大输入长度'},
            { type: 'inputNumber', field: 'minlength', title: '最小输入长度' },
            { type: 'switch', field: 'clearable', title: '是否可清空' },
            // {
            //     type: 'input',
            //     field: 'placeholder',
            //     title: '输入框占位文本'
            // },
            // {
            //     type: 'switch',
            //     field: 'showPassword',
            //     title: '是否显示切换密码图标'
            // }, { type: 'switch', field: 'disabled', title: '禁用' }, {
            //     type: 'input',
            //     field: 'prefixIcon',
            //     title: '输入框头部图标'
            // }, { type: 'input', field: 'suffixIcon', title: '输入框尾部图标' }, {
            //     type: 'inputNumber',
            //     field: 'rows',
            //     info: '只对 type="textarea" 有效',
            //     title: '输入框行数'
            // }, {
            //     type: 'select',
            //     field: 'autocomplete',
            //     title: '自动补全',
            //     options: [{ label: 'on', value: 'on' }, { label: 'off', value: 'off' }]
            // }, { type: 'switch', field: 'readonly', title: '是否只读' }, {
            //     type: 'select',
            //     field: 'resize',
            //     title: '控制是否能被用户缩放',
            //     options: [{ label: 'none', value: 'none' }, { label: 'both', value: 'both' }, {
            //         label: 'horizontal',
            //         value: 'horizontal'
            //     }, { label: 'vertical', value: 'vertical' }]
            // }, { type: 'switch', field: 'autofocus', title: '自动获取焦点' }
        ];
    }
};
