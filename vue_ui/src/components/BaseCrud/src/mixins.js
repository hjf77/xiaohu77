import {handleStrParam} from "@/lib/utils/param";
import {checkPermi} from "@/utils/permission";

export default {

  data() {
    return {
      rowPermissions: {}
    }
  },
  methods: {


    /**
     * 按钮权限
     * @param _row
     * @param _btnIndex
     * @param _btn
     * @returns {boolean|*}
     */
    proxyBtnIf(_row, _btnIndex, _btn) {
      if (!this.rowPermissions[_btnIndex]) {
        return false;
      }
      if (_btn.ifFun) {
        return _btn.ifFun(_row);
      }
      if (_btn.ifAttr && _btn.ifValue) {
        if (_btn.ifOperator == "in") {
          return _btn.ifValue.includes(_row[_btn.ifAttr]);
        }
        if (_row[_btn.ifAttr] == _btn.ifValue) {
          return true;
        }
        return false;
      }
      if (_btn.ifAttr && _btn.ifOperator == "none") {
        if (_row[_btn.ifAttr]) {
          return true;
        } else {
          return false;
        }
      }
      return true;
    },


    //检查是否有权限
    checkPermissions() {
      if (this.columnOption.type == 'textBtn') {
        this.columnOption.textBtn.forEach((btn, btnIndex) => {
          if (btn.permission) {
            this.rowPermissions[btnIndex] = checkPermi(btn.permission);
          } else {
            this.rowPermissions[btnIndex] = true;
            console.log(this.rowPermissions)
          }
        })
      }
    },


    //html转字符串
    columnFormart(_row, _column) {
      console.log(_column);
      return handleStrParam(_column.formart, _row);
    },

    //按钮代理
    proxyClick(_row, _column) {
      let _this = this;
      if (_column.click) {
        _column.click.call(_this, _row);
      }
    },


  }

}
