/**
 * 修改vee-validate的默认校验提示
 */
const dictionary =  {
  zh_CN: {
    messages: {
      required: (field) => {
        if(field.indexOf('是否') !=-1 || field.indexOf('密级') !=-1 || field.indexOf('类型') !=-1){
          return `请选择${field}`
        }
        if( field.indexOf('文件') !=-1 ){
          return `请上传${field}`
        }
        return `请输入${field}`
      },
    },
    attributes: {

    }
  }
}

export default dictionary

