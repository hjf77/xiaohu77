import request from '@/utils/request'
export default function field() {
    return [
        {
            type: 'select',
            field: 'fieldId',
            value: '',
            title: '字段 ID',
            options: [
                // { label: 'text', value: 'text' },
                // { label: 'textarea', value: 'textarea' },
                // { label: 'number', value: 'number' },
                // { label: 'password', value: 'password' }
            ]
        },
        {
            type: 'input',
            field: 'title',
            value: '',
            title: '字段名称',
        },
        {
            type: 'input',
            field: 'info',
            value: '',
            title: '提示信息',
        },
    ];
}

export function getOptions(tableSchema, tableName) {
    // let init = this.$pagexRequest({
    //     url: `/basic/ms/table/getTableInfo?tableSchema=${this.tableSchema}&tableName=${this.tableName}&configType=listColumn`,
    //     method: "get",
    // });
    // let options = {}
    // init.fields.forEach((item) => {
    //     options.push({
    //         lable: item.label,
    //         value: item.name,
    //     });
    // });
    // return options;
    return request({
        url: `/basic/ms/table/getTableInfo?tableSchema=${tableSchema}&tableName=${tableName}&configType=listColumn`,
        method: 'GET'
      })
}