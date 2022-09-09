import request from '@/utils/request'
export default function field() {
    return [
        {
            type: 'select',
            field: 'fieldId',
            value: '',
            title: '字段 ID',
            options: getOptions
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

export function getTableInfo(tableSchema, tableName,callback) {
     request({
        url: `/basic/ms/table/getTableInfo?tableSchema=${tableSchema}&tableName=${tableName}&configType=listColumn`,
        method: 'GET'
      })

     return  callback()
}


function getOptions (callback){

    return callback()

}

