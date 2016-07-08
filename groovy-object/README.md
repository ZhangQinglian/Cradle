# Groovy中的面向对象

前面说到groovy支持脚本和类，前面一节已将简单说了脚本和类之间的关系，这一节主要介绍一下groovy中类的相关知识，即面向对象相关知识。

## 1.类型

### 1.1 原始类型

groovy中支持的原始数据类型与java相同，分别是boolean，char，short，int，long，float，double。

### 1.2 类

groovy中的类与java中很相似，但有以下几点是groovy特有的：

- `public`修饰的字段会被自动转换成属性变量，这样可以避免很多冗余的get和set方法。
- 如果属性或方法没有访问权限修饰符，那么默认是public，而java中是proteced。
- 类名不需要和文件名相同。
- 一个文件中可以定义多个一级类。如没有定义类，则这个groovy文件被认为是脚本文件。

#### 1.2.1 普通类
groovy的普通类和java类似，使用new关键字获得实例。

#### 1.2.2 内部类

内部类也基本类似，下面给一个例子：

```groovy
class Outer2 {
    private String privateStr = 'some string'

    def startThread() {
       new Thread(new Inner2()).start()
    }

    class Inner2 implements Runnable {
        void run() {
            println "${privateStr}."
        }
    }
}
```

#### 1.2.3 抽象类

抽象类也与java基本类似：

```groovy
abstract class Abstract {         
    String name

    abstract def abstractMethod() 

    def concreteMethod() {
        println 'concrete'
    }
}
```

### 1.3 接口
groovy的接口和java也基本类似，支持接口继承接口。

### 1.4 构造方法

groovy的构造方法和java就有略微不同了，groovy的构造方法支持`位置参数`和`命名参数`，下面具体看。

#### 1.4.1 位置参数构造方法

位置构造参数跟java中的通常构造方法类似，不同位置的参数具有不同的含义。如下：

```groovy
class PersonConstructor {
    String name
    Integer age

    PersonConstructor(name, age) {          
        this.name = name
        this.age = age
    }
}

def person1 = new PersonConstructor('Marie', 1)  
def person2 = ['Marie', 2] as PersonConstructor  
PersonConstructor person3 = ['Marie', 3]        
```

具体调用构造方法的时候groovy多了两种写法。因为位置已经固定，所以即使`PersonConstructor person3 = ['Marie', 3]`这样的写法groovy也能从内部给你做初始化。

#### 1.4.2 命名参数构造方法

命名参数构造方法不需要用户定义，当一个类没有构造方法的时候，其默认有一个命名参数构造方法。

```groovy
class PersonWOConstructor {                                  
    String name
    Integer age
}

def person4 = new PersonWOConstructor()                      
def person5 = new PersonWOConstructor(name: 'Marie')         
def person6 = new PersonWOConstructor(age: 1)                
def person7 = new PersonWOConstructor(name: 'Marie', age: 2) 
```

### 1.5 方法
定义groovy的方法也很简单，可使用关键字`def`或者返回值就行。groovy中的方法都有返回值，如果没有写`return`语句，groovy会计算方法中的最后一行语句并将其结果返回。

下面是四种不同的方法定义：

```groovy
def someMethod() { 'method called' }                           
String anotherMethod() { 'another method called' }             
def thirdMethod(param1) { "$param1 passed" }                   
static String fourthMethod(String param1) { "$param1 passed" }
```

#### 1.5.1 方法的命名参数

在自定义的方法中要使用命名参数的话，就要使用Map作为唯一参数，如下：

```groovy
def foo(Map args) { "${args.name}: ${args.age}" }
foo(name: 'Marie', age: 1)
```

#### 1.5.2 方法的默认参数

groovy方法支持默认参数，这样就是的其参数变得可选，当参数没有被填入，则会使用默认参数：

```groovy
def foo(Map args) { "${args.name}: ${args.age}" }
foo(name: 'Marie', age: 1)
```

#### 1.5.3 方法的可变长参数

这个在java中也是存在的，举个简单的例子：

```groovy
def foo(Map args) { "${args.name}: ${args.age}" }
foo(name: 'Marie', age: 1)
```

### 1.6 注解

groovy中的注解跟java中的类似，但又比java中多了一些特性，下面简单介绍一下。

### 1.6.1 注解的闭包参数

在groovy中，有一个有趣的语言特性就是可以使用`闭包`作为注解的参数值。这样的注解一般在什么情况下使用呢？举个简单的例子，有些时候软件的运行时依赖其运行的环境和操作系统的，针对不同的环境或系统，表现也不一样。看一下这个例子：

```groovy
class Tasks {
    Set result = []
    void alwaysExecuted() {
        result << 1
    }
    @OnlyIf({ jdk>=6 })
    void supportedOnlyInJDK6() {
        result << 'JDK 6'
    }
    @OnlyIf({ jdk>=7 && windows })
    void requiresJDK7AndWindows() {
        result << 'JDK 7 Windows'
    }
}

```

Tasks类用于完成`alwaysExecuted`,`supportedOnlyInJDK6`,`requiresJDK7AndWindows`这三个任务，但不同的任务对环境和系统的要求都不一样，这里使用`@OnlyIf`来表明对环境和系统的需求。

```groovy
@Retention(RetentionPolicy.RUNTIME)
@interface OnlyIf {
    Class value()                    
}
```

在groovy中如果需要让注解接受闭包的话，只需要像上面这样定义一个Class类型的value值。这样OnlyIf就可以接受闭包作为其值了。

接着写处理类：

```groovy
class Runner {
    static <T> T run(Class<T> taskClass) {
        def tasks = taskClass.newInstance()                                         
        def params = [jdk:6, windows: false]                                        
        tasks.class.declaredMethods.each { m ->                                     
            if (Modifier.isPublic(m.modifiers) && m.parameterTypes.length == 0) {   
                def onlyIf = m.getAnnotation(OnlyIf)                                
                if (onlyIf) {
                    Closure cl = onlyIf.value().newInstance(tasks,tasks)            
                    cl.delegate = params                                            
                    if (cl()) {                                                     
                        m.invoke(tasks)                                             
                    }
                } else {
                    m.invoke(tasks)                                                 
                }
            }
        }
        tasks                                                                       
    }
}

```

和java类似，通过反射拿到Task对象的方法，接着获取其OnlyIf注解，如果获取成功，则提取OnlyIf的闭包进行调用。

## 2 Traits（特征）

trait是groovy中独有的面向对象的语法特性，他具备如下功能：

- 行为构成
- 运行时的接口实现
- 行为重载
- 兼容静态类型的检查和编译

Trait可以被看作是具有方法实现和状态的接口，使用`trait`关键字定义：

```groovy
trait FlyingAbility {                           
        String fly() { "I'm flying!" }          
}
```

上面就定义了一个飞行能力的特证，它的使用方法和接口一样，都是使用`implements`关键字：

```groovy
class Bird implements FlyingAbility {}          
def b = new Bird()                              
assert b.fly() == "I'm flying!" 
```

这个看上去感觉跟继承有点类似，但又不一样，trait仅仅是将其方法和状态嵌入到实现类中，而没有继承中的那种上下级的父子关系。

trait中的一些语法特性：

- trait中支持定义抽象方法，其实现类必须实现此抽象方法。
- trait中可以定义私有方法，其实现类无法访问。
- trait中的this关键字指其实现类。
- trait可以实现接口。
- trait中可定义属性，此属性会自动被附加到实现此trait的类中。
- trait可定义私有字段由于存储相关状态。
- trait可定义公共字段，但为了避免`钻石问题`，其获取方式有所不同，如下：

```groovy
trait Named {
    public String name                      
}
class Person implements Named {}            
def p = new Person()                        
p.Named__name = 'Bob'   
```

- 第一个类可以实现多个trait。
- 实现类可重写trait中的默认方法。
- trait可以继承另一个trait使用关键字extends，若要继承多个则使用implements关键字。
- 可以在运行时动态实现trais，使用关键字as。

以上简单介绍了groovy中面向对象的相关知识，更详细的资料请参考官方文档

- [Object orientation](http://www.groovy-lang.org/objectorientation.html#_annotation)

