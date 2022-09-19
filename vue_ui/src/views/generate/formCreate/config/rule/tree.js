import uniqueId from '@form-create/utils/lib/unique';
import { makeOptionsRule, makeRequiredRule } from '../../utils/index';

const label = '树形控件';
const name = 'tree';

export default {
    icon: 'icon-tree',
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
            props: {

            },
        };
    },
    props() {
        return [
            makeOptionsRule('props.data', false),
            { type: 'switch', field: 'clearable', title: '是否可以清空选项' ,value:true},
            { type: 'switch', field: 'searchable', title: '数据过滤' ,value:true},
            { type: 'switch', field: 'multiple', title: '多选' ,value:true},
            { type: 'switch', field: 'flat', title: '平铺模式(父节点选择不自动选子节点)' ,value:true},
            { type: 'InputNumber', field: 'defaultExpandLevel', title: '默认展开层级',min:1},
            {
              type: "select",
              field: "valueConsistsOf",
              title: "传值模式",
              options: [
                {"value": "ALL", "label": "父子都传"},
                {"value": "BRANCH_PRIORITY", "label": "子全选只传父"},
                {"value": "LEAF_PRIORITY", "label": "子全选只传子"},
                {"value": "ALL_WITH_INDETERMINATE", "label": "子孙选一个父亲祖宗都传"},
              ]
            }
        ];
    }
};
