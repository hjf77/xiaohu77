import uniqueId from '@form-create/utils/lib/unique';
import { makeRequiredRule } from '../../utils';

const label = '上传';
const name = 'upload';

export default {
    icon: 'icon-upload',
    label,
    name,
    rule() {
        return {
            type: name,
            field: uniqueId(),
            title: label,
            info: '',
            props: {
                action: '',
                onSuccess(res, file) {
                    file.url = res.data.url;
                }
            }
        };
    },
    props() {
        return [
            {
                type: 'select',
                field: 'uploadType',
                title: '上传类型',
                value: 'fileAsync',
                options: [
                    { label: '文件异步', value: 'fileAsync' },
                    { label: '文件同步', value: 'fileSynchro' },
                    { label: '图片', value: 'image' }
                ]
            },
            { type: 'switch', field: 'multiple', title: '是否支持多选文件' },
            { type: 'input', field: 'accept', title: '接受上传的文件类型（图片模式无效）' },
            {
                type: 'switch',
                field: 'disabled',
                title: '是否禁用'
            },
            {
                type: 'inputNumber',
                field: 'limit',
                title: '最大允许上传个数'
            },
            {
              type: 'inputNumber',
              field: 'maxSize',
              title: '单个文件最大(MB)'
            }
        ];
    }
};
