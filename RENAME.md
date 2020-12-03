# mySpring
自定义的 Java Web 框架，一个简易的 Spring

## mySpring 项目说明

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
