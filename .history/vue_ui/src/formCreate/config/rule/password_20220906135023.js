import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '密码框';
const name = 'el-input';

export default {
    icon: 'icon-lock',
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
            {
                type: 'switch',
                field: 'showPassword',
                title: '是否显示切换密码图标',
                // value: true,
            },
            { type: 'input', field: 'width', title: '宽度' },
            { type: 'input', field: 'defaultValue', title: '默认值' },
            makeRequiredRule(),
            { type: 'input', field: 'validationRule', title: '内置验证规则' },
            { type: 'input', field: 'regular', title: '自定义正则' },
            { type: 'input', field: 'customRegular', title: '自定义正则提示' },
            { type: 'inputNumber', field: 'maxlength', title: '最大输入长度' },
            { type: 'inputNumber', field: 'minlength', title: '最小输入长度' },
            { type: 'switch', field: 'clearable', title: '是否可清空' },
        ];
    }
};
