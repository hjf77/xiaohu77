// 本组件使用suchjs来生成自定义规则  https://www.suchjs.com/
import ReRegExp from "reregexp";
//let globalSuch = {};
// key 字典类型，value 字典生成方法
let extRule = {
  'userName': /^[A-Za-z0-9]{4,10}$/,
  'email': /^[a-zA-Z]\w{5,17}@163\.com/,
  'idCard': /\d{17}[\d|x]|\d{15}/,
  'ip': /(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)/,
  'url': /^http:\/\/[a-zA-Z]\w{5,17}\.com/,
  'int': /[1-9]\d*/,
  'date': new Date(),
  'mobile': /^1(3[0-9]|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\d{8}$/,
  'name': /^王生安|李鑫灏|薛佛世|蔡壮保|钱勤堃|潘恩依|陈国柏|魏皑虎|周卓|汤辟邦|张顺谷|张悌斯|张灶冲|易江维|孙来笙|饶展林|岳列洋|时党舒|周迟蒲|源智|义诚|振梁|彪儒|群钧|圣华|麟广|新翔|麟超|森侨|智志|振树|新天|希齐|然司|彦崴|雄嘉|勤荣|虹雄|岩喜|寒驹|蓝贤|智瑄|渝天|景汉|基力|尧鸣|辰纶|尧源|河顺|岩寒|熙尧|启颖|祥志|咏源|劲齐|晨龙|烽梁|勤诚|禹钢|启鸣|杉中|阳兆|茂旭|畅善|信灿|良少|思顺|信凯|军寒|克龙|海铭|绍鑫|山麟咏麟|瑄善|宥强|翔广|新尧|彬树|念荣$/
};

//根据正则生成字符串
function genStr(regexp) {
  const r1 = new ReRegExp(regexp);
  return r1.build();
}

//生成随机数
function random(m, n) {
  let result = parseInt(Math.random() * (n - m) + m);
  console.log(m + '--' + n + '--' + result);
  return result;
}

//填充表单数据
function init(_form, _rules, _vue) {
  for (let ruleKey in _rules) {
    let rule = _rules[ruleKey];
    // 字符串的话可能是固定值或者字典
    if (typeof rule === "string") {
      if (rule.indexOf('@') != -1) {
        rule = rule.replace('@', '');
        //如果在字典规则中不存在
        if (!extRule[rule]) {
          console.warn(rule + '不存在')
          continue;
        }
        _vue.$set(_form, ruleKey, genStr(extRule[rule]));
      } else {
        _vue.$set(_form, ruleKey, rule);
      }
      //数字直接赋值
    } else if (typeof rule === "number") {
      _vue.$set(_form, ruleKey, rule);
    } else if (typeof rule === "object") {
      // 如果给了值则用值
      if (rule.value) {
        _vue.$set(_form, ruleKey, rule.value);
      }
      //给了option要给key
      if (rule.options && rule.options.length > 0) {
        _vue.$set(_form, ruleKey, rule.options[random(0, rule.options.length)].valueField);
      }
      if (rule.rule) {
        _vue.$set(_form, ruleKey, genStr(rule.rule));
      }
      if (_form[ruleKey]) {
        if (rule.change) {
          rule.change(_form[ruleKey]);
        }
      }
      if (rule.sleep) {
        sleep(rule.sleep);
      }
    }
  }
}


//睡眠 用于ajax 接收返回结果
function sleep(delay) {
  var start = (new Date()).getTime();
  while ((new Date()).getTime() - start < delay) {
    continue;
  }
}

let initer = {
  init: init
}

export default initer

