import request from '@/utils/request'

export default function field() {
  return [
    {
      type: 'select',
      field: 'name',
      value: '',
      title: '绑定字段',
      options: getOptions()
    },
    {
      type: 'input',
      field: 'title',
      value: '',
      title: '字段名称'
    },
    {
      type: 'input',
      field: 'placeholder',
      value: '',
      title: '提示信息',
    },
    {type: 'input', field: 'width', title: '宽度'},
    {type: 'input', field: 'defaultValue', title: '默认值'},
    {
      type: "switch",
      title: "必填",
      field: "required",
    }
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

