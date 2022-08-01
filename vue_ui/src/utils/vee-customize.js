/**
*  vee 自定义规则
*  开发人员：zfw
*  创建时间:2021/9/10
*/

/**
 * 校验 同一个日期框，有开始时间和结束时间，开始时间至少小于结束时间10分钟；
 */
 let range = {
    validate: (value, args) => {
       return new Date(value[1]).getTime() >= new Date(value[0]).getTime()+10*60*1000;
    }
  };
/**
 * 校验 数字必须大于0；
 */
 let greaterThanZero = {
   validate: (value, args) => {
      return value>0;
   }
 };

 let mobile = {
  messages: {
    zh_CN:field => field + '必须是11位手机号码',
  },
  validate: value => {
    return !value  || (value.length == 11 && /^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\d{8}$/.test(value))
  }
 }
 export { range, greaterThanZero,mobile }
