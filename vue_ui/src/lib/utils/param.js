export function handleStrParam(_str, _pageParam) {
    if (!_str) {
        return _str;
    }
    let regParam = /[^\{\}]+(?=\})/g;
    let paramsNames = _str.match(regParam);
    if(!paramsNames){
        return _str;
    }
    paramsNames.forEach(function (_paramName) {
        _str = _str.replace('${' + _paramName + '}', getParamVaue(_paramName, _pageParam));
    });
    return _str;
}

//替换参数
function getParamVaue(_paramName, _pageParam) {
    let _sourceParamName = _paramName;
    let tempParamValue = null;
    //手动指定从session取
    if (_paramName.indexOf('session.') == 0) {
        _paramName = _paramName.substr(8, _paramName.length - 8);
        tempParamValue = (_paramName.indexOf('.') != -1) ? getSessionJson(_paramName.substr(0, (_paramName.indexOf('.') + 1))) : getSession(_paramName);
    } else // 指定从local取
    if (_paramName.indexOf('local.') == 0) {
        _paramName = _paramName.substr(6, _paramName.length - 6);
        tempParamValue = (_paramName.indexOf('.') != -1) ? getLocalJson(_paramName.substr(0, (_paramName.indexOf('.') + 1))) : getLocal(_paramName);
    } else if (_paramName.indexOf('param.') == 0 && _pageParam) {
        _paramName = _paramName.substr(6, _paramName.length - 6);
        tempParamValue = (_paramName.indexOf('.') != -1) ? _pageParam[_paramName.substr(0, (_paramName.indexOf('.') + 1))] : _pageParam[_paramName];
    } else {
        //从param开始推断 看看哪个里面有，如果三个里面都没有，则返回null
        if(_pageParam){
            tempParamValue = (_paramName.indexOf('.') != -1) ? _pageParam[_paramName.substr(0, (_paramName.indexOf('.') + 1))] : _pageParam[_paramName];
        }
        if (!tempParamValue  && tempParamValue!=0) {
            tempParamValue = (_paramName.indexOf('.') != -1) ? getSessionJson(_paramName.substr(0, (_paramName.indexOf('.') + 1))) : getSession(_paramName);
        }
        if (!tempParamValue  && tempParamValue!=0) {
            tempParamValue = (_paramName.indexOf('.') != -1) ? getLocalJson(_paramName.substr(0, (_paramName.indexOf('.') + 1))) : getLocal(_paramName);
        }
    }
    //没拿到原样返回
    if (!tempParamValue && tempParamValue!=0) {
        return '${' + _sourceParamName + '}';
    }
    let dianIndex = _paramName.indexOf('.');
    //如果没有点，则代表tempParamValue已经是目标值，如果存在点，则tempParamValue应该是一个json，然后从json拿数据
    tempParamValue = (dianIndex != -1) ? tempParamValue[_paramName.substr(dianIndex, (_paramName.length - dianIndex + 1))] : tempParamValue;
    return tempParamValue;
}

function setLocal(_name, _value) {
    if (isJsonOrArray(_value)) {
        localStorage.setItem(_name, JSON.stringify(_value));
    }
    localStorage.setItem(_name, _value + '');
}

function getLocalJson(_name) {
    return JSON.parse(localStorage.getItem(_name));
}

function getLocal(_name) {
    return localStorage.getItem(_name);
}


function setSession(_name, _value) {
    if (isJsonOrArray(_value)) {
        sessionStorage.setItem(_name, JSON.stringify(_value));
    }
    sessionStorage.setItem(_name, _value + '');
}

function getSessionJson(_name) {
    return JSON.parse(sessionStorage.getItem(_name));
}

function getSession(_name) {
    return sessionStorage.getItem(_name);
}


function isJsonOrArray(_source) {
    return typeof (_source === 'object');
}

