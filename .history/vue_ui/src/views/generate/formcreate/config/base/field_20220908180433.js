import request from '@/utils/request'
export default function field() {
    return [
        {
            type: 'select',
            field: 'fieldId',
            value: '',
            title: '字段 ID',
            options: getOptions()
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

export function getOptions() {
    const fieIds = sessionStorage.getItem("fieIds") || `[]`
    return JSON.parse(fieIds)
}

