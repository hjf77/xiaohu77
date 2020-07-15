<p align="center">
    <img src="https://images.gitee.com/uploads/images/2020/0715/160619_de86772a_339743.png" width="300">
    <br>      
    <br>      
    <p align="center">
         FHS-Framwork是一个集成了国内外诸多优秀开源项目的快速开发平台，除了在常规快速开发平台提供 用户，角色，权限，菜单，字典，审计日志，代码生成器 还拥有可以让您更少写SQL和前端代码的翻译服务以及PAGEX服务。
        <br>      
        <br>      
        <span>
            <span>
                FHS-Framwork官方交流群：976278956
            </span>
        </span>
        <br>
        <br>
        <a href="http://www.jeasyui.net/">
            <img src="https://img.shields.io/badge/easyui-1.4.3-green.svg" alt="bootstrap">
        </a> 
        <a href="https://www.layui.com">
            <img src="https://img.shields.io/badge/layui-2.5.5-blue.svg" alt="bootstrap">
        </a>  
        +
        <a href="http://spring.io/projects/spring-boot">
            <img src="https://img.shields.io/badge/spring--boot-2.2.5-green.svg" alt="spring-boot">
        </a>
        <a href="http://mp.baomidou.com">
            <img src="https://img.shields.io/badge/mybatis--plus-3.0-blue.svg" alt="mybatis-plus">
        </a>  
        <a href="http://ibeetl.com/">
            <img src="https://img.shields.io/badge/beetl-3.0.11-yellow.svg" alt="beetl">
        </a> 
    </p>
</p>

-----------------------------------------------------------------------------------------------
#### 体验地址

http://114.116.21.147:8081/   admin  123456

#### 预览图
![输入图片说明](https://images.gitee.com/uploads/images/2020/0509/110012_1d20674d_339743.png "fhs1.png")
 
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

&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;下面是使用PAGEX引擎开发的学生管理的demo
```javascript
   var modelConfig = {
    title: '学生管理', pkey: 'id', type: 'uuid', orderBy: 'update_time Desc',
    namespace: "demo_student_management", table: 't_demo_student_management',trans: true};
var listPage = {

    listFieldSett: function () {
        return [
            // 前台显示的数据
            {name: 'name', title: '姓名', width: '10%', align: 'center'},
            {name: 'sex', title: '性别', width: '10%', align: 'center',trans:'book',key:'sex',showField:'transMap.sexName'},
            {name: 'age', title: '年龄', width: '10%', align: 'center'},
            {name: 'idcard', title: '身份证', width: '20%', align: 'center'},
            {name: 'grade', title: '班级', width: '15%', align: 'center',trans:'book',key:'grade',showField:'transMap.gradeName'},
            {name: 'hobby', title: '爱好', width: '15%', align: 'center'},
            {name: 'email', title: '邮箱', width: '15%', align: 'center'},
            {name: 'mobile', title: '手机号', width: '10%', align: 'center'},
            {name: 'create_time', title: '创建时间', width: '14%', align: 'center'},
            {name:'create_user',title:'创建人',width:'10%',align:'center',trans:'auto',showField:'transMap.create_userUserName'},
            {name: 'update_time', title: '更新时间', width: '14%', align: 'center'},
            {name:'update_user',title:'修改人',width:'10%',align:'center',trans:'auto',showField:'transMap.update_userUserName'},
        ]
    },
    isColumnButton: function () {
        return false;
    },
    filters: function () {
        return [
            {name: 'name', type: 'input', title: '姓名', filterType: 'like'}
        ];
    },
    buttons: function () {
        return [
            {title:'批量导入学生',fun:'importExcel',permissionsCode:'demo_student_management:see'},
            {title:'导出学生列表',fun:'exportStuExcel',permissionsCode:'demo_student_management:see'},
        ];
    },
    disableButtons: function () {
        return ['export'];
    },
    otherFunctions: function () {
        return {
            importExcel:function(){
                openDialog('${path.basePath}/b/page-ms-demo/demo_student_import_excel', '批量导入学生');
            },
            exportStuExcel: function () {
                location.href = "${path.basePath}/ms/demoStudentManagement/exportExcel"
            }
        }
    }
};

var add = {
    formFields: function () {
        // 添加的数据
        return [
            {name: 'name', title: '姓名', required: true, type: 'input'},
            {name: 'sex', title: '性别', required: true, type: 'book',code:'sex'},
            {name: 'age', title: '年龄', required: true, type: 'input',dataType:'n'},
            {name: 'idcard', title: '身份证', required: true, type: 'input',dataType:'idNumber'},
            {name: 'grade', title: '班级', required: true, type: 'book',code:'grade'},
            {name: 'header', title: '图片', required: true, type: 'up',placeholder:'请上传图片'},
            {name: 'hobby', title: '爱好', type: 'input'},
            {name: 'email', title: '邮箱', required: true, type: 'input',dataType:'e'},
            {name: 'mobile', title: '电话', required: true, type: 'input',dataType:'m'},
        ];
    },
    otherFunctions: function () {
        return {
            ready: function () {
            },
            loadSuccess: function (info) {

            },
            onSave: function () {
            },
            saveSucess: function () {

            },
            saveError: function () {

            }
        }
    }

};
```

#### 使用说明


 &#8194;&#8194;&#8194;&#8194;1  新出炉的文档 https://gitee.com/fhs-opensource/fhs-framework/wikis/pages?sort_id=2052774&doc_id=333929

 &#8194;&#8194;&#8194;&#8194;2  视频:
&#8194;&#8194;&#8194;&#8194;https://www.bilibili.com/video/BV1DK4y1s7AL/  基础介绍和翻译服务使用</br>
&#8194;&#8194;&#8194;&#8194;https://www.bilibili.com/video/BV1Hi4y1V7pW/  pagex的使用</br>
&#8194;&#8194;&#8194;&#8194;https://www.bilibili.com/video/BV1eh411o7Td/  新项目使用fhs</br>
&#8194;&#8194;&#8194;&#8194;https://www.bilibili.com/video/BV115411Y7NR/  代码生成器使用1</br>
&#8194;&#8194;&#8194;&#8194;https://www.bilibili.com/video/BV1vK4y1s7hT/  代码生成器使用2</br>
&#8194;&#8194;&#8194;&#8194;https://www.bilibili.com/video/BV1sf4y1R7dH/  base类中的方法介绍以及activeRecord模式适用</br>



#### 写到最后

目前国内有很多快速开发平台，每个也有每个不同的特色，FHS不一定是最好的，但是我们愿意做百花齐放的快开平台的一朵鲜花，和很多脑子一热写个快开平台或者打算以此盈利不同，FHS 是长久维护的，也是全开源的，希望路过的同学给个Star 抱拳拉！
         