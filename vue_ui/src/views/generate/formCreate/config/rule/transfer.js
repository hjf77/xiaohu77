import uniqueId from '@form-create/utils/lib/unique';

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
            {
                type: 'Struct',
                field: 'data',
                title: 'Transfer 的数据源',
                props: { defaultValue: [] }
            },
        ];
    }
};
