
let requestSett={
  //吧request.js的移动过来
};

let pagerSett={
  //这里配置分页大小，分页给后端传的参数名
}

let fileSett={
   asyncUploadUrl:'/upload/file',//Filedata
   downloadUrlFile:function(fileId){
     return '/downLoad/file?fileId=' + fileId;
   },
   fileListInfo:function(_fileIds){
     //后台请求，根据文件id获取到文件详情
     return [{}];
   }

}
