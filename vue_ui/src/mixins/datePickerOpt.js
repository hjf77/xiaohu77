/**
 *
 * startTime 开始时间
 * endTime 结束时间
 *
 */

export default {

  data() {
    return {
      startTime: undefined,
      endTime: undefined,
    }
  },

  methods: {
    /**
     * 结束时间调用
     * @param arrName
     * @param index
     * @returns {boolean}
     */
    rewriteStartPickerOptions(arrName, index, isDefault) {
      let currentTime = Date.now() - 1000 * 60 * 60 * 24;
      const selectTime = new Date(this.endTime).getTime() - 1000 * 60 * 60 * 24;
      if (isDefault) {
        this[arrName][index]['pickerOptions'] = {
          disabledDate: (time) => {
            return (selectTime ? time.getTime() > selectTime : false) || time.getTime() < currentTime
          }
        }
      } else {
        this[arrName][index]['pickerOptions'] = {
          disabledDate: (time) => {
            return selectTime ? time.getTime() > selectTime : false
          }
        }
      }
    },
    /**
     * 开始时间调用
     * @param arrName
     * @param index
     * @returns {boolean}
     */
    rewriteEndPickerOptions(arrName, index, type) {
      let selectTime = this.startTime ? new Date(this.startTime).getTime() : Date.now() - 1000 * 60 * 60 * 24
      if (type === 'dates') {
        this[arrName][index]['pickerOptions'] = {
          disabledDate: (time) => {
            return selectTime > (time.getTime() + (1000 * 60 * 60 * 24))
          }
        }
      } else {
        this[arrName][index]['pickerOptions'] = {
          disabledDate: (time) => {
            return selectTime > time.getTime()
          }
        }
      }
    }
  }
}
