// import  { _local }  from "@/utils/auth";
import {
  getToken
} from '@/utils/auth'

//通过get方式下载文件 by wanglei
export function getFile(_url) {
  downLoad(_url, 'get', null);
}

export function downLoad(_url, _method, _data,_newFileName) {
  let baseURL = window.location.protocol + '//' + window.location.host + process.env.VUE_APP_BASE_API;
  function createObjectURL(object) {
    return (window.URL) ? window.URL.createObjectURL(object) : window.webkitURL.createObjectURL(object);
  }

  let xhr = new XMLHttpRequest()
  let url = baseURL + _url;
  if (url.indexOf("?") == -1) {
    url = url + "?nowtimes=" + new Date().getTime();
  } else {
    url = url + "&nowtimes=" + new Date().getTime();
  }
  xhr.open(_method, url)
  xhr.setRequestHeader('Authorization', getToken())
  xhr.responseType = 'blob'
  let formData = new FormData();
  if (_method == 'post') {
    formData = {};
    xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8')
    /*for (let key in _data) {
      formData[key] = _data[key];
    }*/
    formData = JSON.stringify(_data);
  }
  xhr.onreadystatechange = function () {
    if (xhr.readyState === 4 && xhr.status === 200) {
      const blob = new Blob([xhr.response])
      let url = window.URL.createObjectURL(blob)
      //创建一个a标签元素，设置下载属性，点击下载，最后移除该元素
      let link = document.createElement('a')
      link.href = url
      link.style.display = 'none'
      //取出下载文件名
      let fileName = decodeURI(xhr.getResponseHeader('content-disposition').split(';')[1].split('=')[1]);
      if (_newFileName) {
        let fileSuffix = fileName.substring(fileName.lastIndexOf('.'));
        fileName = _newFileName + fileSuffix
      }
      link.setAttribute('download', fileName)
      link.click()
      window.URL.revokeObjectURL(url)
    }
  }
  xhr.send(formData);
}

//通过post方式下载文件 by wanglei
export function postFile(_url, _data) {
  downLoad(_url, 'post', _data);
}
