# 程序结构

这一小节主要介绍一下Groovy的程序结构。

## 包名
当你在groovy中定义类的时候需要指定包名，这和java中类似不多介绍。

## 导入
groovy中的导入也跟java类似，有一下五种：

### 默认导入

groovy默认导入了一下几个包和类：

```groovy
import java.lang.*
import java.util.*
import java.io.*
import java.net.*
import groovy.lang.*
import groovy.util.*
import java.math.BigInteger
import java.math.BigDecimal
```

### 普通导入

普通导入即全类名导入

```groovy
// importing the class MarkupBuilder
import groovy.xml.MarkupBuilder

// using the imported class to create an object
def xml = new MarkupBuilder()

assert xml != null
```

### 包导入

这个也不用多说

```groovy
import groovy.xml.*

def markupBuilder = new MarkupBuilder()

assert markupBuilder != null

assert new StreamingMarkupBuilder() != null
```

### 静态导入

```groovy
import static java.lang.String.format 

class SomeClass {

    String format(Integer i) { 
        i.toString()
    }

    static void main(String[] args) {
        assert format('String') == 'String' 
        assert new SomeClass().format(Integer.valueOf(1)) == '1'
    }
}
```

### 静态简称导入

静态简称导入在java中是没有的，这里解释一下。`Calendar`有一个静态方法`getInstance()`可以获得`Calendar`的实例，既然是静态方法我们就可以使用上面的静态导入来直接调用`getInstance()`方法，但`getInstance()`这个方法在被调用的时候有误导性，不清楚的还以为是用于获得当前类的实例，所以这时候`静态简称导入`就发挥作用了：

```groovy
import static Calendar.getInstance as now

assert now().class == Calendar.getInstance().class
```
这样我们就直接可以调用`now()`来获得`Calendar`的实例了，这样是不是清晰了很多？

## 脚本和类

读了[Groovy基本句法](https://github.com/ZhangQinglian/Cradle/tree/master/groovy-syntax)应该了解groovy是可以同时支持编写脚本和类的，接下来就来学习一下他们之间的关系。

先看下面的例子

```groovy
class Main {                                    
    static void main(String... args) {          
        println 'Groovy world!'                 
    }
}
```
这是java的传统写法，这里把需要执行的代码写在了main中，在groovy中达到同样的效果就简单多了：

```groovy
println 'Groovy world!'
```
虽然上面的是一行脚本语言，但在运行的时候`Groovy`还是将其转换成类来处理，类似如下的类：

Main.groovy

```groovy
import org.codehaus.groovy.runtime.InvokerHelper
class Main extends Script {                     
    def run() {                                 
        println 'Groovy world!'                 
    }
    static void main(String[] args) {           
        InvokerHelper.runScript(Main, args)     
    }
}
```

可以看出来需要执行的代码被放入了`run()`方法中。

这里我们可以简单证实一下上面的说法：

首先新建一个`main.groovy`的脚本文件，内容如下：

```groovy
println 'hello world !'
```

接着时候`groovyc`命令将`main.groovy`转换成字节码`main.class`，接着使用class文件的阅读工具查看其内容如下：

```groovy
	//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import groovy.lang.Binding;
import groovy.lang.Script;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.callsite.CallSite;

public class main extends Script {
    public main() {
        CallSite[] var1 = $getCallSiteArray();
    }

    public main(Binding context) {
        CallSite[] var2 = $getCallSiteArray();
        super(context);
    }

    public static void main(String... args) {
        CallSite[] var1 = $getCallSiteArray();
        var1[0].call(InvokerHelper.class, main.class, args);
    }

    public Object run() {
        CallSite[] var1 = $getCallSiteArray();
        return var1[1].callCurrent(this, "hello world !");
    }
}

```

虽然生成的和上面略有不同，但的确是被转换为一个继承`Script`的类，而且需要运行的代码被放在run方法中了。

接下来我们看看脚本中定义的方法会被怎么转换。

method.groovy

```groovy
println 'Hello'

int power(int n) { 2**n }

println "2^6==${power(6)}"  
```
还是按照之前的转换方法，得到结果：

```groovy
public class method extends Script {
    public method() {
        CallSite[] var1 = $getCallSiteArray();
    }

    public method(Binding context) {
        CallSite[] var2 = $getCallSiteArray();
        super(context);
    }

    public static void main(String... args) {
        CallSite[] var1 = $getCallSiteArray();
        var1[0].call(InvokerHelper.class, method.class, args);
    }

    public Object run() {
        CallSite[] var1 = $getCallSiteArray();
        var1[1].callCurrent(this, "Hello");
        return !__$stMC && !BytecodeInterface8.disabledStandardMetaClass()?var1[4].callCurrent(this, new GStringImpl(new Object[]{Integer.valueOf(this.power(6))}, new String[]{"2^6==", ""})):var1[2].callCurrent(this, new GStringImpl(new Object[]{var1[3].callCurrent(this, Integer.valueOf(6))}, new String[]{"2^6==", ""}));
    }

    public int power(int n) {
        CallSite[] var2 = $getCallSiteArray();
        return DefaultTypeTransformation.intUnbox(var2[5].call(Integer.valueOf(2), Integer.valueOf(n)));
    }
}
```

可以看到`power()`被定义在了method这个类中。

下来再来看看变量是怎么被转换的

variables1.groovy

```groovy
int x = 1
int y = 2
assert x+y == 3
```

转换结构如下：

```groovy
public class variables1 extends Script {
    public variables1() {
        CallSite[] var1 = $getCallSiteArray();
    }

    public variables1(Binding context) {
        CallSite[] var2 = $getCallSiteArray();
        super(context);
    }

    public static void main(String... args) {
        CallSite[] var1 = $getCallSiteArray();
        var1[0].call(InvokerHelper.class, variables1.class, args);
    }

    public Object run() {
        CallSite[] var1 = $getCallSiteArray();
        byte x = 2;
        byte y = 3;
        return BytecodeInterface8.isOrigInt() && !__$stMC && !BytecodeInterface8.disabledStandardMetaClass()?var1[3].callCurrent(this, Integer.valueOf(x + y)):var1[1].callCurrent(this, var1[2].call(Integer.valueOf(x), Integer.valueOf(y)));
    }
}
```

可以看出来x，y被定义为了run方法的布局变量。

接着看：

variables2.groovy

```groovy
x = 2
y = 3
println x+y
```

转换结果如下

```groovy
public class variables2 extends Script {
    public variables2() {
        CallSite[] var1 = $getCallSiteArray();
    }

    public variables2(Binding context) {
        CallSite[] var2 = $getCallSiteArray();
        super(context);
    }

    public static void main(String... args) {
        CallSite[] var1 = $getCallSiteArray();
        var1[0].call(InvokerHelper.class, variables2.class, args);
    }

    public Object run() {
        CallSite[] var1 = $getCallSiteArray();
        byte var2 = 2;
        ScriptBytecodeAdapter.setGroovyObjectProperty(Integer.valueOf(var2), variables2.class, this, (String)"x");
        byte var3 = 3;
        ScriptBytecodeAdapter.setGroovyObjectProperty(Integer.valueOf(var3), variables2.class, this, (String)"y");
        return var1[1].callCurrent(this, var1[2].call(var1[3].callGroovyObjectGetProperty(this), var1[4].callGroovyObjectGetProperty(this)));
    }
}
```

额，看不太懂了，但可以肯定x，y没有被定义成run的成员变量。

综合上面的两个变量转换的例子，请判断下面这个脚本可以正确执行吗？

```groovy
int x = 1;

def getDoubleX(){
    x*2 ;
}

println getDoubleX()
```

**答案是不能，这里的x会被定义成run的成员变量，而getDouble这个方法是访问不到x的，这里需要注意。**


以上就是关于Groovy中脚本和类的关系了，更多详细内容请参考：

 - [Program structure](http://www.groovy-lang.org/structure.html)
