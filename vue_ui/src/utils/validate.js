/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}


/**
 * @param {Array} arg
 * @returns {Boolean}
 */
export function isArray(arg) {
  if (typeof Array.isArray === 'undefined') {
    return Object.prototype.toString.call(arg) === '[object Array]'
  }
  return Array.isArray(arg)
}


/**
 * 手机号校验
 * @param {*} rule
 * @param {*} value
 * @param {*} callback
 */
export function checkPhone(rule, value, callback) {
  if (value) {
    value += ''
    if (value.match(/^((13|14|15|16|17|18|19)[0-9]{1}\d{8})$/)) {
      callback()
    } else {
      callback(new Error('手机号码不正确！'))
    }
  } else {
    callback()
  }
}

/**
 * 邮箱校验
 * @param {*} rule
 * @param {*} value
 * @param {*} callback
 */
export function isEmail(rule, value, callback) {
  const reg = /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/;
  if (value == '' || value == undefined || value == null) {
    callback();
  } else {
    if (!reg.test(value)) {
      callback(new Error('请输入正确的邮箱地址'));
    } else {
      callback();
    }
  }
}

/**
 * 正整数校验
 * @param {*} rule
 * @param {*} value
 * @param {*} callback
 */
export function validateInteger(rule, value, callback) {
  const reg = /^[0-9]*[1-9][0-9]*$/;
  if (value == '' || value == undefined || value == null) {
    callback();
  } else {
    if (!reg.test(value)) {
      callback(new Error('请输入正整数'));
    } else {
      callback();
    }
  }
}

/**
 * 传真校验
 * @param {*} rule
 * @param {*} value
 * @param {*} callback
 */
export function validateFax(rule, value, callback) {
  const reg = /^(?:\d{3,4}-)?\d{7,8}(?:-\d{1,6})?$/;
  if (value == '' || value == undefined || value == null) {
    callback();
  } else {
    if (!reg.test(value)) {
      callback(new Error('请输入正确的传真号码'));
    } else {
      callback();
    }
  }
}

/**
 * 两位小数
 * @param {*} rule
 * @param {*} value
 * @param {*} callback
 */
export function validateDecimal(rule, value, callback) {
  const reg = /(^[1-9](\d+)?(\.\d{1,2})?$)|(^0$)|(^\d\.\d{1,2}$)/;
  if (value == '' || value == undefined || value == null) {
    callback();
  } else {
    if (!reg.test(value)) {
      callback(new Error('请输入正确的金额'));
    } else {
      callback();
    }
  }
}

export function validateIP(rule, value,callback) {
  if(value==''||value==undefined||value==null){
    callback();
  }else {
    const reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的IP地址'));
    } else {
      callback();
    }
  }
}

export function validatePhoneTwo(rule, value, callback) {
  const reg = /^((0\d{2,3}-\d{7,8})|(1[34578]\d{9}))$/;;
  if (value == '' || value == undefined || value == null) {
    callback();
  } else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的电话号码或者固话号码'));
    } else {
      callback();
    }
  }
}

export function validatePhone(rule, value,callback) {
  const reg =/^[1][3-9][0-9]{9}$/;
  if(value==''||value==undefined||value==null){
    callback();
  }else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的电话号码'));
    } else {
      callback();
    }
  }
}


export function validateIdNo(rule, value,callback) {
  const reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
  if(value==''||value==undefined||value==null){
    callback();
  }else {
    if ((!reg.test(value)) && value != '') {
      callback(new Error('请输入正确的身份证号码'));
    } else {
      callback();
    }
  }
}





export function rules(){
  return [
    {value:'ip',label:'IPV4',fun:validateIP},
    {value:'phoneTwo',label:'手机号或者固定电话',fun:validatePhoneTwo},
    {value:'phone',label:'手机号',fun:validatePhone},
    {value:'idNo',label:'身份证号',fun:validateIdNo},
    {value:'int',label:'正整数',fun:validateInteger},
    {value:'fax',label:'传真号',fun:validateFax},
    {value:'decimal',label:'小数后保留2位',fun:validateDecimal},
  ]
}
