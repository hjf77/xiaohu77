import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '输入框';
const name = 'input';

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
            {
                type: 'select',
                field: 'type',
                title: '类型',
                options: [{ label: 'text', value: 'text' }, {
                    label: 'textarea',
                    value: 'textarea'
                }, { label: 'number', value: 'number' }, { label: 'password', value: 'password' }]
            },
            { type: 'inputNumber', field: 'maxlength', title: '最大输入长度' }, {
                type: 'inputNumber',
                field: 'minlength',
                title: '最小输入长度'
            },
            { type: 'switch', field: 'clearable', title: '是否可清空' },
            {
                type: 'inputNumber',
                field: 'rows',
                info: '只对 type="textarea" 有效',
                title: '输入框行数'
            },
        ];
    }
};
