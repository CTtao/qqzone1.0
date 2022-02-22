# qqzone1.0
- 本项目为本人学习b站尚硅谷JavaWeb视频完成的仿qq空间项目，为非联网项目。
- 数据库文件可参考
链接：https://pan.baidu.com/s/1KGMM2n_njEMT-thB-8uaLA 
提取码：svya
- 项目启动后，访问网址后缀为/page.do?operate=page&page=login
## 1 熟悉QQZone业务需求
1. 登录
2. 登录成功，显示主界面。下方显示一排排日志。左侧显示好友列表：上侧显示欢迎词。如果不是自己的空间，显示超链接：**返回自己的空间**
3. 点击日志概览，查看日志详情：
   - 日志内容（作者头像、昵称、日志标题、日志内容、日志日期）
   - 回复列表（回复者头像、昵称、回复内容、回复日期）
   - 主人回复信息
4. 删除日志
5. 删除特定回复
6. 删除主人回复
7. 添加日志、回复、主人回复
8. 点击左侧好友，进入好友空间
## 2 数据库设计
### 2.1 抽取实体
- 用户登录信息
- 用户详细信息
- 日志
- 回复
- 主人回复
### 2.2 分析属性（表项）
- 用户登录信息：账号、密码、头像、昵称
- 用户详细信息：真实姓名、星座、血型、邮箱、手机
- 日志：标题、内容、日期、作者
- 回复：内容、日期、作者、日志
- 主人回复：内容、日期、作者、回复

### 2.3 分析实体间关系

### 2.4 数据库范式
第一范式：列不可再分
第二范式：一张表只表达一层含义（只描述一件事情）
第三范式：表中每一列和主键都是直接依赖关系（不存在间接依赖）

数据库设计的范式和数据库的查询性能很多时候是相悖的，我们需要根据实际业务需要做出选择：
- 查询频次不高时，我们倾向于提高数据库设计范式，从而提高存储效率
- 查询频次较高时，我们倾向于降低规范度，允许特定的冗余，从而提高查询性能

## 3 登录功能
### 3.1 错误展示
1. URL修改数据库
2. ？
3. rsmd获取数据表项别名
4. 数据关联错误
5. 直接请求静态页面，thymeleaf不生效

## 4 index页面部分
### 4.1 显示登录者昵称，如果是自己空间不显示“返回”
### 4.2 点击左侧好友进入好友空间
1. 根据id获取指定userBasic信息，查询这个userBasic的topicList，然后覆盖friend对应的value
2. main页面应该展示friend的topicList，而不是userBasic的
3. 跳转后，出现在left部分显示了整个index页面，解决：给超链接添加target属性：target="_top"
4. top.html
   - 修改空间主人昵称
   - 返回自己空间的超链接

## 5 日志详情
1. 已知topic的id，需要根据topic的id获取特定的topic
2. 获取这个topic相关联的所有reply
3. 如果某个回复有主人回复，需要查询出来
   - 在TopicController中获取指定的topic
   - 具体的topic中有几个reply，有ReplyService实现
4. 获取到的topic的author即UserBasic对象只有id，
   - 在topicService中提供getTopic方法，可以获取到带有完整author信息的UserBasic对象
   - 内部通过UserBasicService对象用author的id获取完整的UserBasic对象
5. 同理，reply中的author也这样设置

## 6 添加回复

## 7 删除回复
1. 当回复下有主人回复时，删除会因为外键引用而失败

## 8 添加日志
1. 首先写add.html静态提交页面
2. AddController实现addTopic方法，内部使用TopicService处理
3. TopicService执行addTopic事务，内部使用TopicDAO
4. Controller跳转到新添加的日志页面

## 9 返回日志列表
添加如下超链接：使用page.do跳转
<a th:href="@{/page.do?operate=page&page=frames/main}">返回日志列表</a>
## 10 Date相关
thymeleaf使用#dates公共内置对象显示Date
${#dates.format(topic.topicDate,'yyyy-MM-dd HH-mm-ss')}
## 总结
1. 系统启动时，访问的
   - 是http://localhost:8080/pro13/page.do?operate=page&page=login
   - 而不是http://localhost:8080/pro13/login.html
   
   答：如果是后者，访问的是静态页面，thymeleaf（标签）语句不会生效。访问前者，执行ViewBaseServlet中的processTemplate()方法
2. URL访问过程
   > http://localhost:8080/pro13/page.do?operate=page&page=login
   
   > 协议 :// ServerIP : port / context root / request.getServletPath() ? query string
   1. DispatcherServlet -> urlPattern : *.do 拦截/page.do
   2. request.getServletPath() -> /page.do
   3. 解析处理字符串，将/page.do -> page
   4. 拿到page这个字符串，然后去IOC容器(BeanFactory)寻找id=page的bean对象 ->PageController.java
   5. operate值调用PageController的page方法，传参page="login"
      方法return "login";
   6. DispatcherServlet接收到字符串"login"，进行视图处理
      - 带前缀redirect：resp.sendRedirect(redirectStr);
      - 不带前缀：super.processTemplate(methodStringStr,req,resp);
   7. 此时ViewBaseServlet的processTemplate()方法执行，web.xml配置文件中的view-prefix和view-suffix
      - view-prefix：在"login"前拼接"/"
      - view-suffix：在"login"后拼接".html"

3. 目前javaWeb项目开发流程
   1. 拷贝myssm包
   2. 新建配置文件applicationContext.xml
   3. 在web.xml文件中配置前后缀，和applicationContext.xml
   4. 开发具体的业务模块：
      1. 纵向组成：
         - html页面
         - POJO
         - DAO接口和实现类
         - Service接口和实现类（事务）
         - Controller控制组件
      2. 如果html页面有thymeleaf表达式，一定要通过PageController访问
      3. 在applicationContext.xml中配置DAO、Service、Controller，以及三者之间的依赖关系
      4. DAO实现类，继承BaseDAO，实现具体的POJO接口
      5. Service层，封装业务逻辑，保证DAO层的单精度，尽量调用其他的service，不要深入到DAO
      6. Controller类编写规则
         - 在applicationContext.xml配置,id="名称"。
         - 请求 /名称.do，对应的就是 名称Controller；operate参数 对应Controller中的方法名
         - 如果是表单提交，要在标签中加上name属性，与Controller对应方法的形参名一致
         - session、request、response等使用直接赋值
      7. DispatcherServlet中步骤
         - 从application作用域中获取IOC容器
         - 解析servletPath，在IOC容器中中寻找对应的Controller组件
         - 准备operate指定的方法需要的参数
         - 调用operate方法
         - 接收到执行operate指定的方法的返回值，对返回值进行处理 -> 视图处理
      8. IOC容器是如何保存到application作用域的？
         ContextLoaderListener在容器启动时会执行初始化任务：
         1. 解析IOC配置文件，创建组件，依赖注入
         2. 将IOC容器保存到application作用域
