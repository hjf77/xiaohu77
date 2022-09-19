import uniqueId from '@form-create/utils/lib/unique';
import {makeOptionsRule} from "@/views/generate/formCreate/utils";

const label = '穿梭框';
const name = 'el-transfer';

const generateData = _ => {
    const data = [];
    for (let i = 1; i <= 15; i++) {
        data.push({
            key: i,
            label: `备选项 ${i}`,
            disabled: i % 4 === 0
        });
    }
    return data;
};

export default {
    icon: 'icon-transfer',
    label,
    name,
    rule() {
        return {
            type: name,
            field: uniqueId(),
            title: label,
            info: '',
            props: {
                data: generateData()
            }
        };
    },
    props() {
        return [
            makeOptionsRule('options',false),
            { type: 'switch', field: 'filterable', title: '数据过滤' ,value:true},

        ];
    }
};
