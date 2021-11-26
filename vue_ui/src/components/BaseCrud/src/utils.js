import $moment from "moment";

export default {


  methods:{

    //深拷贝
    deepClone(source) {
      if (!source && typeof source !== 'object') {
        throw new Error('error arguments', 'deepClone')
      }
      const targetObj = source.constructor === Array ? [] : {}
      Object.keys(source).forEach(keys => {
        if (source[keys] && typeof source[keys] === 'object') {
          targetObj[keys] = this.deepClone(source[keys])
        } else {
          targetObj[keys] = source[keys]
        }
      })
      return targetObj
    },

    //防抖节流
    debounce(func, delay = 100) {
      func()
    },


    //日期格式化
    datePicker(val) {
      let arr = [];
      if(val){
        arr.push($moment(val[0]).format("YYYY-MM-DD HH:mm:ss"));
        arr.push($moment(val[1]).format("YYYY-MM-DD HH:mm:ss"));
      }
      return arr;
    },
  }
}
