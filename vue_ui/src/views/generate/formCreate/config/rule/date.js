import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '日期选择器';
const name = 'datePicker';

export default {
    icon: 'icon-date',
    label,
    name,
    rule() {
        return {
            type: name,
            field: uniqueId(),
            title: label,
            info: '',
            props: {},
        };
    },
    props() {
        return [
            {
                type: 'select',
                field: 'formatType',
                title: '显示类型',
                options: [{ label: 'year', value: 'year' }, { label: 'month', value: 'month' }, {
                    label: 'date',
                    value: 'date'
                }, { label: 'dates', value: 'dates' }, { label: 'week', value: 'week' }, {
                    label: 'datetime',
                    value: 'datetime'
                }, { label: 'datetimerange', value: 'datetimerange' }, {
                    label: 'daterange',
                    value: 'daterange'
                }, { label: 'monthrange', value: 'monthrange' }]
            },
            {
                type: 'switch',
                field: 'clearable',
                title: '是否显示清除按钮',
                value: true
            },
            {
                type: 'switch',
                field: 'disabled',
                title: '禁用'
            }
        ];
    }
};
