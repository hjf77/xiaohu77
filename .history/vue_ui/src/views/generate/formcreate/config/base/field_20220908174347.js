import request from '@/utils/request'
export default function field(tableSchema, tableName) {
    return [
        {
            type: 'select',
            field: 'fieldId',
            value: '',
            title: '字段 ID',
            options: [{
                lable: "数据库名",
value: "db_name"
            }]
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

export function getFieIdList(tableSchema, tableName) {
    return request({
        url: `/basic/ms/table/getTableInfo?tableSchema=${tableSchema}&tableName=${tableName}&configType=listColumn`,
        method: 'GET'
      })

}


export async function getOptions (tableSchema, tableName){
    const res = await getFieIdList(tableSchema,tableName)
    let options = [];
    for (let i = 0; i < res.fields.length; i++) {
      const item = res.fields[i];
      options.push({
        lable: item.label,
        value: item.name,
      });
    }
    console.log(options)
    return options
}

