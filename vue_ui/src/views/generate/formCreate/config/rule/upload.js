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
                value: '',
                options: [
                    { label: '文件异步', value: 'uploadFileAsync' },
                    { label: '文件同步', value: 'uploadFile' },
                    { label: '图片', value: 'uploadPicture' }
                ]
            },
            { type: 'switch', field: 'multiple', title: '是否支持多选文件',value:true },
            { type: 'input', field: 'acceptFormat', title: '接受上传的文件类型（图片模式无效）' },
            {
                type: 'inputNumber',
                field: 'limit',
                title: '最大允许上传个数'
            },
            {
              type: 'inputNumber',
              field: 'size',
              title: '单个文件最大(MB)'
            }
        ];
    }
};
