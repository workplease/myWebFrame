# mySpring
> 代码请移步到 master 分支
轻量级 Java Web

### 1.工具类说明

1. ArrayUtil：数组工具类，用于进行对数组是否为空和非空进行判断。
2. CastUtil：类型操作工具类，用于处理一些数据转型操作。但核心逻辑是先转为 String 类，然后调用其中的方法，再将 String 类转为其他数据类型。
3. ClassUtil：类加载器，用于提供与类操作相关的方法，比如获取类加载器，加载类，获取指定包名下的所有类等。
4. CodecUtil：编码与解码操作工具类，用于编码和解码操作，在后面进行安全框架使用时需要用到。
5. CollectionUtil：集合工具类，用于提供一些集合操作。判断 Collection 与 Map 是否为空或者非空。
6. DBUtil：数据库工具类，封装数据库的常用操作，例如获取连接，关闭连接，辅助实现 AOP 。
7. FileUtil：文件工具类，封装文件的常用操作，例如获取文件名，创建文件，上传文件等。
8. JsonUtil：JSON 工具类，用于处理 JSON 和 POJO 之间的转换。
9. PropsUtil：属性文件工具类，用于读取配置文件的一些信息。其中最重要的是 loadProps  方法，我们只需要传递一个属性文件的名称，即可返回一个 Properties 对象，然后再根据 getString、getInt、getBoolean 这些方法由 key 获取指定类型的 value，同时也可指定 defaultValue 作为默认值。 
10. ReflectUtil：反射工具类，封装 Java 反射相关的 API，对外提供更好用的工具方法。
11. StreamUtil：流操作工具类，用于常用的流操作。
12. StringUtil：字符串工具类，用于提供一些字符串操作，比如判断是否为空，分割固定格式的字符串等。

### 2. mySpring 框架初步搭建

１）定义框架配置项：

```
zzz.framework.jdbc.driver=com.mysql.jdbc.Driver
zzz.framework.jdbc.url=jdbc:mysql://localhost:3306/demo
zzz.framework.jdbc.username=root
zzz.framework.jdbc.password=root

zzz.framework.app.base_package=rog.smart4j.testFramework    #项目的基础包名
zzz.framework.app.jsp_path=/WEB-INF/view/   #JSP 的基础路径
zzz.framework.app.asset_path=/asset/    #静态资源文件的基础路径
```

2）加载配置项：

有了配置项之后就要对其进行加载，首先创建一个 ConfigConstant 的常量类，让它来维护配置文件中相关的配置项名称，然后借助 PropUtil 工具类能实现 ConfigHelper，核心就是定义一些静态方法，让它们获取 smart.properties 配置文件中的配置项。

3）开发一个类加载器：

我们需要开发一个 “类加载器” 来加载该基础包名下的所有类，比如使用了某注解的类，或者实现了某接口的类，等等。定义一个 ClassUtil 工具类，提供与类操作相关的方法。

由于我们在 smart.properties 配置文件中指定了 zzz.framework.app.base_package，它是整个应用的基础包名，通过 ClassUtil 加载的类都要基于该基础包名，于是定义 ClassHelper 助手类，让它分别获取应用包下的所有类、应用包名下所有 Service 类、应用包下所有 Controller 类。（同样需要定义四个注解类，比较简单，不赘诉）

4）实现 Bean 容器：

使用 ClassHelper 类可以获取所有加载的类，但无法通过类来实例化对象。因此，提供一个反射工具类来封装 Java 反射相关的 API，对外提供更好用的工具方法。

我们需要获取所有被框架管理的 Bean 类，此时需要调用 ClassHelper 类的 getBeanClassSet 方法，随后需要循环调用 ReflectionUtil 类的 newInstance 方法，根据类来实例化对象，最后将每次创建的对象存放在一个静态的 Map<Class<?\>,Object\> 中。我们需要随时获取该 Map，还需要通过该 Map 的 key（类名）去获取所对应的 value（Bean 对象）。

5）实现依赖注入功能：

我们并不是直接通过 new 的方式来实例化对象，而是通过框架本身进行实例化，像这类实例化的过程，称为 IoC（控制反转），也可以理解为将某个类需要依赖的成员注入到这个类中。

实现方式是：先通过 BeanHelper 获取所有 Bean Map（是一个 Map<Class<?\>,Object\> 结构，记录了类与对象的映射关系）。然后遍历这个映射关系，分别取出 Bean 类与 Bean 实例，进而通过反射获取类中所有的成员变量。继续遍历这些成员变量，在循环中判断当前成员变量中是否带有 Inject 注解，若带有该注解，则从 Bean Map 中根据 Bean 类取出 Bean 实例，最后通过 ReflectionUtil 的 setField 方法来修改当前成员变量的值。

6）加载 Controller：

我们需要创建一个 ControllerHelper 类，让其来处理如下逻辑：

通过 ClassHelper，我们可以获取所有定义了 Controller 注解的类，可以通过反射获取该类中所有带 Action 注解的方法（简称 “Action 方法” ），获取 Action 注解中的请求表达式，进而获取请求方法与路径，封装一个请求对象（Request）与处理对象（Handler），最后将 Request 与 Handler 建立一个映射关系，放入一个 Action Map 中，并提供一个可根据请求方法与请求对象获取处理对象的方法。

Request（请求信息）：包含 requestMethod（请求方法）和 requestPath（请求路径）。

Handler（处理对象）：包含 controllerClass（Controller 类）和 actionMethod（Action 方法）。

7）初始化框架：

需要定义一个入口程序来加载我们定义的 Helper 类，实际上就是加载它们的静态块。

8）请求转发器：

我们需要定义一个 Servlet，让它来处理所有的请求。从 HttpServletRequest 对象中获取请求方法与请求路径，通过 ControllerHelper 的 getHandler 方法来获取 Handler 对象。当拿到 Handler 对象后，我们可以方便的获取 Controller 的类，进而通过 BeanHelper 的 getBean 的方法可以获取 Controller 的实例对象。

相关的类：

1. Param：请求的参数对象，可以通过参数名获取指定类型的参数值，也可以获取所有参数的 Map 结构。
2. View：返回的视图对象，其中包含了视图路径和该视图中所需的数据，该模型数据是一个 Map 类型的 “键值对”，可以在视图中根据模型的键名获取键值。
3. Data：封装了一个 Object 的模型数据，框架会将该对象写入 HttpServletResponse 对象中，从而直接输出至浏览器。
4. DispatcherServlet：用于处理所有的请求，根据请求信息从 ControllerHelper 中获取到对象的 Action 方法，然后使用反射技术调用 Action 方法，同时需要具体的传入方法参数，最后拿到返回值并判断返回值的类型，进行相应的处理。

### 3. 使框架具有 AOP 特性

1）AOP 技术简介：

AOP——面向切面编程，切面是 AOP 中的一个术语，表示从业务逻辑中分离出来的横切逻辑，比如说性能监控、日志记录、权限控制等，这些功能都可以从核心的业务逻辑代码中抽离出去，也就是说，通过 AOP 可以解决代码耦合问题，使职责更加单一。

2）开发 AOP 框架：

1. 定义切面注解：在框架中定义一个 Aspect 的注解，通过 @Target(ElementType.TYPE) 来设置该注解只能用在类上，该注解中包含一个名为 value 的属性，它是一个注解类，用来定义 Controller 这类注解。

2. 搭建代理框架：

   - Proxy 接口：代理接口，包含一个 doProxy 方法，传入一个 ProxyChain，用于执行 “链式操作”。
   - ProxyChain 类：代理链，定义了一系列的成员变量，包括 targetClass（目标类）、targetObject（目标对象）、targetMethod（目标方法）、methodProxy（方法代理）、methodParams（方法参数），此外害包括 proxyList（代理列表）、proxyIndex（代理索引），这些成员变量在构造器中进行初始化，并且提供了几个重要的获值方法。
   - ProxyManager 类：代理管理器，提供一个代理对象的方法，输入一个目标类和一组 Proxy 接口实现，输出一个代理对象。
   - AspectProxy 抽象类：切面代理，提供一个模板方法，并在本抽象类实现中扩展相应的抽象方法。

3. 加载 AOP 框架：

   - BeanHelper：添加一个 setBean 方法，将 Bean 实例放入到 Bean Map 中。
   - ClassHelper：添加两个方法，一是获取应用包名下某父类（或接口）的所有子类（或实现类），二是获取应用包名下带有某注解的所有类。
   - AopHelper：添加三个方法，一是获取 Aspect 注解中设置的注解类，若该注解不是 Aspect 类，则可调用 ClassHelper 的 getClassSetByAnnotation 方法获取相关类，并把这些类放入目标类集合中，最终返回这个集合；二是获取代理类及其目标类集合之间的映射关系，一个代理类可对应一个目标类；三是根据代理类与目标类集合的关系分析出目标类与代理对象列表之间的映射关系。最后在 AopHelper 中通过一个静态块来初始化整个 AOP 框架：获取代理类及其目标类集合的映射关系，进一步获取目标类与代理对象列表的映射关系，进而遍历整个映射关系，从中获取目标类与代理对象列表，调用 ProxyManager 的 createProxy 方法获取代理对象，调用 BeanHelper 的 setBean 方法，将该代理对象重新放入 Bean Map 中。
   
3）实现事务控制特性：

1. 什么是事务：

   数据库事务是作为单个工作单元处理的一系列操作。这些行动要么完全完成，要么完全不起作用。事务管理是面向 RDBMS 的企业应用程序中保证数据完整性和一致性的重要组成部分。通俗理解就是一个事情，我们要做到有始有终，必须是不可分割的整体。

2. 相关概念：

   - 事务的 ACID 特性：
     - Atomicity（原子性）
     - Consistency（一致性）
     - Isolation（隔离性）
     - Duration（持久性）
   - 由事务并发引起的问题：
     - Drity Read（脏读）
     - Unrepeatable Read（不可重复读）
     - Phanton Read（幻读）
   - JDBC 解决方案：
     - 事务隔离级别：
       - READ_UNCOMMITTED
       - READ_COMMITTED
       - REPEATEABLE_READ
       - SERIALIZABLE
   - Spring 解决方案：
     - 事务传播行为：
       - PROPAGATION_REQUIRED
       - PROPAGATION_REQUIRES_NEW
       - PROPAGATION_NESTED
       - PROPAGATION_SUPPORTS
       - PROPAGATION_NOT_SUPPORTED
       - PROPAGATION_NEVER
       - PROPAGATION_MANDATORY
     - 事务超时
     - 只读事务

3. 相关类：

   - Transaction 注解：用于定义需要事务控制的方法。
   - DatabaseHelper 类：定义事务常用的操作，比如开启事务，提交事务，回滚事务等。
   - TransactionProxy 类：事务代理切面类，实现 Proxy 接口，在 doProxy 方法中完成事务控制的相关逻辑。
   - AopHelper 类：对 createProxyMap 方法做调整，添加两个私有方法，一个用于添加普通切面代理，一个用于添加事务代理。
   
### 4. 框架优化与功能扩展

1）优化 Action 参数：

考虑到 Controller 层一些方法并不需要用到 Param 参数，于是我们需要做到如果不需要调用这个参数时要怎么将其去掉。

在之前的代码中，每次请求我们都创建了一个 Param 对象，并将其放入到 Action 参数中，我们可以考虑这样做，当框架拿到 Param 对象后，判断一下该对象是否为 null。如果为 null ，可以不将 Param 参数传入到 Action 方法中，反之则传进去。

2）提供文件上传特性：

1. 提供文件上传的场景：

   通常情况下，我们可以在前端中通过一个基于 Ajax 的 form 表单来上传文件，当表单提交时，请求就会转发到 Controller 层中进行处理。由于 Controller 中包含有 Param 参数，于是可以通过重构 Param 参数，使其拥有获取表单字段的名/值对映射（Map fieldMap），指定一个具体的文件字段名称，并调用 getFile 方法即可获得对应的文件参数对象（FileParam），随后调用 Service 层的方法将 fieldMap 和 fieldParam 这两个参数传入。
   
2. 实现文件上传功能：

   - FileParam：封装上传文件参数，内含文件表单的字段名，上传文件的文件们，文件大小，文件类型和字节输入流。
   - FormParam：封装表单参数。
   - Param：请求参数对象，包含了两个成员变量：List formParamList 与 List fileParamList，它们分别封装了表单参数和文件参数。随后提供了两个构造器，用于初始化 Param 对象，还提供了两个 get 方法，分别用于获取所有的表单参数和文件参数。返回值均为 Map 类型，其中 Map<String,Object\> 表示请求参数映射，<String,List<FileParam\>\> 表示上传文件映射。但对于同名的请求参数，通过一个特殊的分隔符进行了处理。
   - UploadHelper：文件上传助手类，提供了判断文件是否是 multipart 类型、创建请求对象、上传单个文件，批量上传文件的方法，且在该类中初始化 ServletFileUpload 对象。
     - boolean isMultipart(HttpServletRequest request)：只有在上传文件时对应的请求类型才是 multipart 类型。
     - Param createParam(HttpServletRequest request)：其中我们使用了 ServletFileUpload 对象来解析请求参数，并通过遍历所有请求参数来初始化 List formParamList 和 List fileParamList 变量的值。在遍历请求参数时，需要对当前的 FileItem 对象进行判断，若为普通表单字段，则创建 FormParam 对象，并添加到 formParamList 对象中。否则即为文件上传字段，通过 FileUtil 提供的方法来获取上传文件的真实文件名，并从 FileItem 对象中构造 FilleParam 对象，并添加到 fileParamList 对象中，最后，通过 formParamList 与 fileParamList 来构造 Param 对象并返回。
   - ConfigHelper：属性文件助手类，添加一个方法用于获取上传文件最大限制的值（该配置可以用用户自己在配置文件中定义，初始值为 100 MB）。
   - FileUtil：文件操作工具类，提供两个方法，一个用于获取真实文件名，一个用于创建文件。
   - StreamUtil：流操作工具类，UploadHelper 最后的两个上传文件需要该工具类提供方法将输入流复制到输出流。
   - RequestHelper：请求助手类，对之前 DispatcherServlet 的代码进行封装，并且通过它的 createParam 方法来初始化 Param 对象。
   - DispatcherServlet：请求转发器，将代码进行重构，最后获取的 View 对象分两种情况进行处理，直接在当前类中添加了两个私有方法 handleViewResult 与 handleDataResult 来封装代码。

3）与 Servlet API 解耦：

1. 设计思路：

   目前在 Controiler 中是无法调用 Servlet API 的，因为无法获取 Request 和 Response 这类对象，但考虑到代码的耦合性，尽量让 Controller 完全不使用 Servlet API 就能操作 Request 和 Reponse 对象。我们需要提供一个线程安全的对象，通过它来封装 Request 和 Response 对象，并提供一系列常用的 Servlet API，这样我们就可以在 Controller 中随时通过该对象操作 Request 和 Reponse 对象的方法。并且，该对象是线程安全的，即，每个请求线程独自拥有一份 Request 和 Response 对象，不同请求线程之间是隔离的。

2. 实现过程：

   ServletHepler：Servlet 助手类，封装 Request 和 Response 对象，提供常用的 Servlet API 工具方法，并利用 ThreadLocal 技术来保证线程安全。

   DispatcherServlet：更新 service 方法，在其中调用 ServletHelper 的 init 和 destroy 方法。

   这样的话，所有的调用都来自于同一个请求线程，DispatcherServlet 是请求线程的入口，随后请求线程会现后来到 Controller 和 Service 中，我们只需要使用 ThreadLocal 来确保 ServletHelper 对象中的 Request 和 Response 对象线程安全即可。

4）提供安全控制特性：

1. 设计思路：

   对于大部分应用系统而言，安全控制特性是很有必要的，本次考虑的是 Shiro 安全控制框架，将其整合到我们的 mySpring 中。并不是简单的调用 Shiro 框架，而是将其进行封装，提供更高级别的 API，让开发者使用起来更加简单，使开发效率更高效。但由于安全控制相对于整个框架而言，是相当独立的，于是将其作为一个插件使用，命名为 mySpring-security，只需要让开发者决定是否使用即可。

2. 准备工作：

   基本原则是：配置简单且使用方便，在配置上越少越好，在代码里只需要实现若干接口，并使用一些帮助类的 API 就能实现安全控制需求。

   - SmartSecurity：Smart Security 接口，提供以下三个方法：根据用户名获取密码，在认证时需要调用；根据用户名获取角色名集合，在授权时需要调用；根据角色名获取操作名集合，在授权时需要调用。使用时只需要实现该接口即可。
   - SecurityHelper：Security 助手类，提供了两个方法，一个用于登录，一个用于注销。
   - AuthcException：认证异常类，用于非法访问时抛出的异常。
   - AuthZException：授权异常类，用于当权限无效，即当前用户无权限访问某个操作时。

3. 具体实现：

   - SmartSecurityPlugin：Smart Security 插件，可以通过 Shiro 提供的初始化参数来定制默认的 Shiro 配置文件名，通过 ServletContext 注册 Listener 与 Filter。
   - SmartSecurityFilter：安全过滤器，继承了 ShiroFilter，内含两个自定义的 Realm：SmartJdbcRealm 和 SmartCustomRealm。
   - SmartJdbcRealm：基于 Smart 的 JDBC Realm，对 Shiro 提供的 JdbcRealm 进行了扩展，通过框架提供的 DatabaseHelper 的助手类来获取 DataSource，通过 SecurityConfig 常量类来获取相关 JDBC 配置项。
   - Md5CredentialMatcher：MD5 密码匹配器，提供基于 MD5 的密码匹配机制。
   - CodecUtil：编码与解码操作工具类，提供 MD5 的加密方法。
   - SmartCustomRealm：基于 Smart 的自定义 Realm，继承自 AuthorizingRealm，覆盖了父类的认证和授权方法。
   - SecurityConfig：从配置文件 smart.properties 中获取相关属性。
   - SecurityConstant：常量接口，辅助属性的获取。
   - HasAllRolesTag：标签类，用于判断当前用户是否拥有其中所有的角色。
   - User：注解类，用于判断当前用户是否登录。（包括：已认证与已记住）
   - AnthzAnnotationAspect：授权注解的切面，实现前置的增强机制，与之前实现注解的逻辑类似。
