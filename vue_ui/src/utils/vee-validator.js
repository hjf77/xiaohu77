/**
 * 修改vee-validate的默认校验提示
 */
const dictionary =  {
  zh_CN: {
    messages: {
      required: (field) => {
        field = field.replace(':', '')
        if(field.indexOf('是否') !=-1 || field.indexOf('密级') !=-1 || field.indexOf('类型') !=-1 || field.indexOf('日期') !=-1 || field.indexOf('时间') !=-1 || field.indexOf('单位') !=-1 || field.indexOf('部门') !=-1 || field.indexOf('有效期') !=-1 || field.indexOf('批准人') !=-1|| field.indexOf('专业特长') !=-1|| field.indexOf('性别') !=-1){
          return `请选择${field}`
        }
        if( field.indexOf('文件') !=-1 || field.indexOf('上传') !=-1 || field.indexOf('简历') !=-1){
          return `请上传${field.replace("上传", '')}`
        }
        return `请输入${field}`
      },
      range:(field) => {
        return `开始时间和结束时间的间隔最少为10分钟`
      },
      greaterThanZero(field){
        return `${field}必须大于0`
      }
    },
    attributes: {

    }
  }
}

export default dictionary

