import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '数字';
const name = 'inputNumber';

export default {
    icon: 'icon-number',
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
                type: 'inputNumber',
                field: 'max',
                title: '设置计数器允许的最大值'
            },
            { type: 'inputNumber', field: 'min', title: '设置计数器允许的最小值' },
            { type: 'switch', field: 'clearable', title: '是否可清空' },
            { type: 'switch', field: 'disabled', title: '是否禁用计数器' },
        ];
    }
};
