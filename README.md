
#    FHS-Framework 简介
 **fhs-framwork是一个集成了国内外诸多优秀开源项目的快速开发平台，除了在常规快速开发平台提供 用户，角色，权限，菜单，字典，操作日志，代码生成器 还拥有一些特殊能力。**
 
 
#### 1. 项目技术架构
![输入图片说明](https://images.gitee.com/uploads/images/2020/0509/104222_be2c1c69_339743.jpeg "技术架构.jpg")
#### 2. 思维导图
![输入图片说明](https://images.gitee.com/uploads/images/2020/0701/092840_63cea85d_339743.jpeg "思维导图.jpg")
#### 2. 技术栈
- 前端:Easyui(美化过的Easyui),Layui(首页)，Validform，My 97(定制过主题)。
- 后端校验：hibernate vilidator。
- 后端：SpringBoot2.25 + Springcloud（可选）
- ORM：Mybatis JPA Mybatis Plus
- 模板引擎：beetl
- 无后端业务的快速开发引擎:PAGEX
- 分布式配置：Apollo
- 缓存：jetcache+spring data cache
- 分布式任务：shedlock
- 文档:swagger

####3. 和其他框架差异化特性
##### -&#8194;&#8194; 翻译服务
&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;  无需表关联,将id 翻译为其他表的title/name 
![输入图片说明](https://images.gitee.com/uploads/images/2020/0509/105618_248af047_339743.jpeg "微信图片_20200430153628.jpg")

##### - &#8194;&#8194;&#8194;&#8194;一款帮你写代码的引擎-PAGEX
&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;只需要写一个js的配置文件即可实现CRUD+导出功能,引擎代码量很少,二次开发简单.预留了很多钩子函数,方便前端扩展.

&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;下面是使用PAGEX引擎的一个我们项目中月租户类型管理的demo
```javascript

    var modelConfig= {title:'月租户类型',pkey:'id',type:'uuid',orderBy:'update_time Desc',
        namespace:"parking_lease_type",table:'t_park_lease_type',trans:true,db:"park"};
    
    var listPage={
        listFieldSett:function(){
    	  return [
    		  {name:'lease_name',title:'类型名称',width:'20%',align:'center'},
              {name:'park_id',title:'停车场名称',width:'20%',isJoin:true,namespace:'parking',showField:'transMap.parkName',align:'center'},//自动表关联
              {name:'is_disable',title:'是否禁用',width:'10%',formart:'formatRowColor',align:'center',trans:'book',key:'is_disable',showField:'transMap.is_disableName'},//字典翻译
              {name:'create_user',title:'创建人',width:'8%',align:'center',trans:'user',showField:'transMap.create_userUserName'},//用户翻译
              {name:'create_time',title:'创建时间',width:'10%',align:'center'},
              {name:'update_user',title:'更新人',width:'8%',align:'center',trans:'user',showField:'transMap.create_userUserName'},
              {name:'update_time',title:'更新时间',width:'10%',align:'center'},
              {name:'is_sync',title:'是否已下发',width:'5%',align:'center',trans:'book',key:'yesOrNo',showField:'transMap.is_syncName'},//字典翻译
      ]},
      filters:function(){
          return [
              {name:'park_id',type:'select',url:'${path.basePath}/ms/x/parking/findListData',
                  valuefield:'id',textfield:'parkName',title:'停车场'},//下拉插件
              {name:'lease_name',type:'input',title:'出入口名称',filterType:'like'},
    	  ];      
      }, 
      buttons:function(){
          return [
              //自定义按钮数组
          ];
      },
      disableButtons:function(){
    	    return [];//禁用掉默认提供的按钮 默认提供了增删改查 + 导出
      },
      otherFunctions:function(){
          return {}//其他的自定义方法
      }
    };
    
    var add={ 
    	formFields:function(){//表单内容
    	     return [
                 {name:'park_id',type:'select',url:'${path.basePath}/ms/x/parking/findListData',
                     valuefield:'id',textfield:'parkName',title:'停车场',required:true,},//一个下拉
                 {name:'lease_name',title:'名称',required:true,type:'input'},//一个input
                 {name:'is_disable',title:'是否禁用',type:'switch',dft:false},//一个开关滑块
                 {name:'is_sync',title:'是否下发',type:'hide'},//一个隐藏域
    		 ];
    	},
        otherFunctions:function(){
          return {
    	     ready:function(){
    	    },
    	    loadSuccess:function(info){//加载后台数据成功的事件
    
    	    },
    	    onSave:function(){//保存前执行方法
                $('#isSync').val(0);
    	    },
    		saveSucess:function(){//保存成功执行方法
    	    },
    		saveError:function(){//保存失败执行的方法
    		    
    	    },
    	  }		
       }
    }
```
#### 预览图
![输入图片说明](https://images.gitee.com/uploads/images/2020/0509/110012_1d20674d_339743.png "fhs1.png")
#### 使用说明


 &#8194;&#8194;&#8194;&#8194;2  新出炉的文档 https://gitee.com/fhs-opensource/fhs-framework/wikis/pages?sort_id=2052774&doc_id=333929

  

#### 参与贡献获取技术支持
官方QQ 群：976278956

体验地址：http://114.116.21.147:8081/   admin  123456
         