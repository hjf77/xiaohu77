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
                FHS-Framwork官方交流QQ群：976278956 微信群请加个人微信后邀请入群，微信在最后
            </span>
        </span>
        <br>
        <br>
        <a href="https://vitejs.cn/">
            <img src="https://img.shields.io/badge/vite-2-green.svg" alt="bootstrap">
        </a> 
        <a href="https://cn.vuejs.org/">
            <img src="https://img.shields.io/badge/vue-2-blue.svg" alt="bootstrap">
        </a> 
 <a href="https://element.eleme.cn/">
            <img src="https://img.shields.io/badge/elementui-2-yellow.svg" alt="bootstrap">
        </a> 
        +
        <a href="http://spring.io/projects/spring-boot">
            <img src="https://img.shields.io/badge/spring--boot-2.5.6-green.svg" alt="spring-boot">
        </a>
         <a href="http://spring.io/projects/spring-boot">
            <img src="https://img.shields.io/badge/spring--cloud-2020.0.4-green.svg" alt="spring-boot">
        </a>
        <a href="http://mp.baomidou.com">
            <img src="https://img.shields.io/badge/mybatis--plus-3.4-blue.svg" alt="mybatis-plus">
        </a>  
        <a href="http://ibeetl.com/">
            <img src="https://img.shields.io/badge/saToken-2.7-yellow.svg" alt="beetl">
        </a> 
    </p>
</p>

-----------------------------------------------------------------------------------------------
# 体验地址(求star)

http://47.109.105.44/   admin  123456

官网：http://fhs-opensource.top/ 

# 预览图

![输入图片说明](img/fhs.jpg)
![输入图片说明](https://foruda.gitee.com/images/1663741224303275569/6356ba0a_339743.png "codegen.png")
# 3. 技术栈
![输入图片说明](img/jiagou.jpg)

# 4. 和其他框架差异化特性
## - &#8194;&#8194;&#8194;&#8194;翻译组件
&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;翻译组件可以只通过注解把你表里的id转换为名称，把你的字典码转换为字典注释(比如0转换为男1转换为女)，详情见：https://gitee.com/fhs-opensource/easy_trans
## - &#8194;&#8194;&#8194;&#8194;All in One模式 开发 微服务模式部署
&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;开发的时候所有的模块集中到一个springboot应用启动(简单 而且省内存)，测试和生产环境使用微服务部署
## - &#8194;&#8194;&#8194;&#8194;更简单的微服务调用
&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194; 简化微服务开发和调用。详情：https://gitee.com/fhs-opensource/easy_cloud

## - &#8194;&#8194;&#8194;&#8194;更好用的校验框架
&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;集成了validate-springboot-starter，在兼容hibernate Validator和javax validation的同时，支持了更多自定义玩法。
内置常用验证规则：比如手机号验证，正则验证，ip,邮箱，长度，范围，数字，小数，中国车牌号，身份证，长度， url, 图书ISBN编号,文件后缀,文件大小 等常用验证规则

## - &#8194;&#8194;&#8194;&#8194;JSON驱动的VUE 列表和表单组件
&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;JSON驱动的组件可以大大降低vue前端开发代码量。
## - &#8194;&#8194;&#8194;&#8194;代码生成器
&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;可视化拖拉拽即可完成简单的表单开发，生成代码后可进行自定义业务逻辑处理，生成的代码均使用JSON驱动组件，代码量比传统生成的代码少50%以上。
## - &#8194;&#8194;&#8194;&#8194;高级查询API
&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;提供高级查询api，单表查询基本不需要写sql。
## - &#8194;&#8194;&#8194;&#8194;Mybatis Plus多表查询
&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194;&#8194; 具体用法见MP官方仓库 https://gitee.com/baomidou/mybatis-plus4。
## - &#8194;&#8194;&#8194;&#8194;Mybatis Plus链式查询

```
new User().select(User.SCHOOLID,User.USERID,User.NAME).userId().eq(1).innerJoin(School.class).orgName().like("一").select(School.ID,School.REMARK,School.SCHOOLNAME).list();
```

# 使用说明

 &#8194;&#8194;&#8194;&#8194;1  新出炉的文档 http://fhs-opensource.top/components/readme.html
 
 &#8194;&#8194;&#8194;&#8194;2  word格式的文档 在sql&docs目录
 
 &#8194;&#8194;&#8194;&#8194;3  quik start
 
 &#8194;&#8194;&#8194;&#8194; A 准备好redis和mysql <br/>
 &#8194;&#8194;&#8194;&#8194; B 创建数据库，导入sql&docs 下的fhs-demo.sql （如果遇到部分sql执行失败，请手动执行） <br/>
 &#8194;&#8194;&#8194;&#8194; C sql&docs的mybatis_plus_advance_ext-1.0.0.jar 拖到idea中安装此插件，此插件类似lombok，可以对PO进行增强  <br/>
 &#8194;&#8194;&#8194;&#8194; D 修改fhs_app/fhs_app_all_in_one/src/main/resources/application-dev.yml redis和mysql配置 <br/>
 &#8194;&#8194;&#8194;&#8194; E 启动fhs_app/fhs_app_all_in_one/src/main/java/com/fhs/app/SingleApplication.java <br/>

&#8194;&#8194;&#8194;&#8194; 如果遇到下载不了的jar，请使用中央仓库官方地址：https://repo1.maven.org/maven2/ 




# 写到最后

目前国内有很多快速开发平台，每个也有每个不同的特色，FHS不一定是最好的，但是我们愿意做百花齐放的快开平台的一朵鲜花，和很多脑子一热写个快开平台或者打算以此盈利不同，FHS 是长久维护的，也是全开源的，希望路过的同学给个Star 抱拳拉！

# 作者微信

![输入图片说明](img/qr.jpg)