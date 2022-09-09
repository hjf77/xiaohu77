export default function field() {
    return [
        {
            type: 'select',
            field: 'field',
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