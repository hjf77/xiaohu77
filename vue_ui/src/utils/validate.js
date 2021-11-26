/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

/**
 * @param {string} url
 * @returns {Boolean}
 */
export function validURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * @param {string} email
 * @returns {Boolean}
 */
export function validEmail(email) {
  const reg = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function isString(str) {
  if (typeof str === 'string' || str instanceof String) {
    return true
  }
  return false
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
 * 验证经度输入范围在-180-180之间，且小数可7位
 * @param {*} rule
 * @param {*} value
 * @param {*} callback
 */
export function checkLon(rule, value, callback) {
  if (value) {
    value += ''
    if (value.match(/^(\-|\+)?(((\d|[1-9]\d|1[0-7]\d|0{1,3})\.\d{0,7})|(\d|[1-9]\d|1[0-7]\d|0{1,3})|180\.0{0,6}|180)$/)) {
      callback()
    } else {
      callback(new Error('经度为-180~180,小数限7位!'))
    }
  } else {
    callback()
  }
}

/**
 * 验证纬度输入范围在-180-180之间，且小数可7位
 * @param {*} rule
 * @param {*} value
 * @param {*} callback
 */
export function checkLat(rule, value, callback) {
  if (value) {
    value += ''
    if (value.match(/^(\-|\+)?([0-8]?\d{1}\.\d{0,7}|90\.0{0,6}|[0-8]?\d{1}|90)$/)) {
      callback()
    } else {
      callback(new Error('纬度为-90~90,小数限7位'))
    }
  } else {
    callback()
  }
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
export function isInteger(rule, value, callback) {
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
export function isFax(rule, value, callback) {
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
export function isDecimal(rule, value, callback) {
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
