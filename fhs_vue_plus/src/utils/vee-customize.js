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
    return !value  || (value.length == 11 && /^((13|14|15|17|18)[0-9]{1}\d{8})$/.test(value))
  }
 }
 export { range, greaterThanZero,mobile }
