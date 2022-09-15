import uniqueId from '@form-create/utils/lib/unique';
import { makeOptionsRule, makeRequiredRule } from '../../utils/index';

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
            dictCode:'',
            props: {},
            options: {}
        };
    },
    props() {
        return [
            makeOptionsRule('options'),
            { type: 'switch', field: 'disabled', title: '是否禁用' },
        ];
    }
};
