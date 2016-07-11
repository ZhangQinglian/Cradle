# 写出gradle风格的groovy代码

我们先来看一段gradle中的代码：

```groovy
buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:2.1.2'
    }
}

allprojects {
    repositories {
        jcenter()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}

task wrapper(type: Wrapper) {
    gradleVersion = '2.10'
}
```

我第一次看gradle代码的时候是懵逼的,

![](http://o6p4e1uhv.bkt.clouddn.com/gsfdtrysrtsfdg.jpg)

这是哪门子语言，这到底是类似xml的标记语言还是类似java的语言？我不懂。不懂没关系，学了就懂了嘛。

大家现在已经知道了gradle使用groovy写的，所以他是我前面说的类似java的语言，但他是如何做到像上面这样炫酷吊炸天的写法呢？我们接下分析一下：

首先你在gradle中看不到`;`这是因为groovy支持不写`;`

```groovy
println 'hello world'
```

下面我们看看这个：

```groovy
dependencies {
        classpath 'com.android.tools.build:gradle:2.1.2'
    }
```

这个放在groovy中怎么解读呢？首先我们需要知道的是groovy中方法调用时可以省略`()`的！！！对你没有看错，正如上面的`println 'hello world'`，ok，那不难理解上面的dependencies是一个方法名了，这里是一个**方法的调用**，而不是方法的定义。既然是方法的调用，那就可以知道`{}`实际上就是一个groovy的闭包类型的参数。而这个闭包里面又是个`classpath`的方法调用。

既然上面被我说通了，那就写个例子试试吧：

```groovy
def dependencies(Closure cl){
    cl.call();
}

def classpath(String path){
    println path
}

dependencies {
    classpath 'com.android.tools.build:gradle:2.1.2'
}
```

Look，代码运行正常。

那么我们再来看这个：

```groovy
task clean(type: Delete) {
    delete rootProject.buildDir
}
```
这个用上面的思路套进去看看呢？task是一个方法，没毛病。后面是两个参数？clean和一个闭包？这里就不对了，如果是两个参数，中间需要有`,`隔开，所以这里只有一个参数，就是clean。那这就是什么写法？这里我们又要了解groovy中的一个语法特性，

> 当一个方法的最后一个参数是闭包，可以将这个闭包写在`()`外面。

看例子：

```groovy
def foo(String s,Closure cl){
    cl(s)
}

//❶
foo('hello world'){
    println it
}

foo 'hello world',{
    println it
}
```
方法的两种特殊写法都在这了，上面讲个写法就是❶处的写法。

所以把clean理解为一个参数是对的，接着再看`clean(type: Delete)`这个就简单了，groovy中的方法参数是支持命名参数的，这里用的就是命名参数，到这里都理顺了，我们还是写一个小例子模仿一下上面的写法：

```groovy
def task(String taskName){
    println 'execute task ' + taskName
}

def clean(Map type,Closure cl){
    type.type
}

def delete(String path){

}

Delete = 'delete'

task clean(type:Delete){
    delete "path"
}
```
这里我很勉强的写出了类似的代码，但gradle中的这些写法真的是我们理解的这样子吗？我们使用代码跟踪来看看.

首先看看dependencies是不是一个方法？

```groovy
 void dependencies(Closure var1);
```
哈哈，还真是的，而且参数的确是一个闭包。

那在看看classpath是不是一个方法？

WTF，见鬼了，跟踪classpath的结果如下：


```java
Dependency add(String configurationName, Object dependencyNotation);
```

竟然跟踪到了这个add方法，而且add方法还有两个参数，这是什么鬼？更诡异的是这个add方法属于DependencyHandler.java这个接口，对你没看错，是java接口。What the hell???

冷静一下。。。

![](http://o6p4e1uhv.bkt.clouddn.com/cce4b48f8c5494ee7008eb9528f5e0fe98257eca.jpg)

我们分析一下，为什么会发生这种事情，这个难道已经超出我们认知的方位了？其实不然，虽然现在我也是啥都不知道，但我觉得这其中的原由一定能够在gradle中找到，所以就然我们正式开启学习gradle的大门吧。

![](http://o6p4e1uhv.bkt.clouddn.com/ad7fd688d43f879424026deed01b0ef41ad53a2e.jpg)

