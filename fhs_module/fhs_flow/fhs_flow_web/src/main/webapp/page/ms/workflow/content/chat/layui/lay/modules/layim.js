n+"</ul>",ti.face.index=t.tips(n,i,{tips:1,time:0,fixed:!0,skin:"layui-box layui-layim-face",success:function(i){i.find(".layim-face-list>li").on("mousedown",function(i){ii(i)}).on("click",function(){ai(l.textarea[0],"face"+this.title+" "),t.close(ti.face.index)})}}),e(document).off("mousedown",ti.faceHide).on("mousedown",ti.faceHide),
e(window).off("resize",ti.faceHide).on("resize",ti.faceHide),ii(a)},faceHide:function(){t.close(ti.face.index)},image:function(i){var a=i.data("type")||"images",e={images:"uploadImage",file:"uploadFile"},n=_(),l=j.base[e[a]]||{};layui.upload({url:l.url||"",method:l.type,elem:i.find("input")[0],unwrap:!0,type:a,success:function(i){0==i.code?(i.data=i.data||{},"images"===a?ai(n.textarea[0],"img["+(i.data.src||"")+"]"):"file"===a&&ai(n.textarea[0],"file("+(i.data.src||"")+")["+(i.data.name||"下载文件")+"]"),$()):t.msg(i.msg||"上传失败")}})},media:function(i){var a=i.data("type"),n={audio:"音频",video:"视频"},l=_();t.prompt({title:"请输入网络"+n[a]+"地址",shade:!1,offset:[i.offset().top-e(window).scrollTop()-158+"px",i.offset().left+"px"]},function(i,e){ai(l.textarea[0],a+"["+i+"]"),$(),t.close(e)})},extend:function(i){var a=i.attr("lay-filter"),e=_();layui.each(r["tool("+a+")"],function(a,t){t&&t.call(i,function(i){ai(e.textarea[0],i)},$,e)})},playAudio:function(i){var a=ti.playAudio.audio,e=a||document.createElement("audio"),n=function(){e.pause(),i.removeAttr("status"),i.find("i").html("&#xe652;")};return e.play?void(i.attr("status")?n():(a||(e.src=i.data("src")),e.play(),i.attr("status","pause"),ti.playAudio.audio=e,i.find("i").html("&#xe651;"),e.onended=function(){n()},e.onerror=function(){t.msg("播放音频源异常")})):t.msg("您的浏览器不支持audio")},playVideo:function(i){var a=i.data("src"),e=document.createElement("video");return e.play?(t.close(ti.playVideo.index),void(ti.playVideo.index=t.open({type:1,title:"播放视频",area:["460px","300px"],maxmin:!0,shade:!1,content:'<div style="background-color: #000; height: 100%;"><video style="position: absolute; width: 100%; height: 100%;" src="'+a+'" loop="loop" autoplay="autoplay"></video></div>'}))):t.msg("您的浏览器不支持video")},chatLog:function(i){var a=_();return j.base.chatLog?(t.close(ti.chatLog.index),ti.chatLog.index=t.open({type:2,maxmin:!0,title:"与 "+a.data.name+" 的聊天记录",area:["450px","100%"],shade:!1,offset:"rb",skin:"layui-box",anim:2,id:"layui-layim-chatlog",content:j.base.chatLog+"?id="+a.data.id+"&type="+a.data.type})):t.msg("未开启更多聊天记录")},menuHistory:function(i,a){var n=layui.data("layim")[j.mine.id]||{},l=i.parent(),s=i.data("type"),o=x.find(".layim-list-history"),d='<li class="layim-null">暂无历史会话</li>';if("one"===s){var r=n.history;delete r[l.data("index")],n.history=r,layui.data("layim",{key:j.mine.id,value:n}),e("#"+l.data("id")).remove(),0===o.find("li").length&&o.html(d)}else"all"===s&&(delete n.history,layui.data("layim",{key:j.mine.id,value:n}),o.html(d));t.closeAll("tips")}};i("layim",new u)}).addcss("modules/layim/layim.css?v=3.53Pro","skinlayimcss");