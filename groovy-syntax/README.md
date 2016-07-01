# Groovy基本句法
Gradle作为一个构建工具自然不会自己去创造一门语言来支撑自己，那么它用的是哪门子语言呢？什么语言能写成这样：

```groovy
task hello {
    doLast {
        println 'Hello world!'
    }
}
```
如此风骚的语法自然要归Groovy莫属了。

## 什么是Groovy
官方介绍如下：
> Apache Groovy is a powerful, optionally typed and dynamic language, with static-typing and static compilation capabilities, for the Java platform aimed at improving developer productivity thanks to a concise, familiar and easy to learn syntax. It integrates smoothly with any Java program, and immediately delivers to your application powerful features, including scripting capabilities, Domain-Specific Language authoring, runtime and compile-time meta-programming and functional programming.

大概意思是Groovy是一门运行在java平台上的强大的、可选类型的、动态语言。使用Groovy可以使你的应用具备脚本，DSL定义，运行时和编译时元编程，函数式编程等功能。

接下来将分几个小节简单介绍Groovy的语法规范。

## Groovy语法

### 注释
Groovy使用的注释有一下几种：

1.单行注释

```groovy
// a standalone single line comment
println "hello" // a comment till the end of the line
```

2.多行注释

```groovy
/* a standalone multiline comment
   spanning two lines */
println "hello" /* a multiline comment starting
                   at the end of a statement */
println 1 /* one */ + 2 /* two */

```

3.文档注释

```groovy
/**
 * A Class description
 */
class Person {
    /** the name of the person */
    String name

    /**
     * Creates a greeting method for a certain person.
     *
     * @param otherPerson the person to greet
     * @return a greeting message
     */
    String greet(String otherPerson) {
       "Hello ${otherPerson}"
    }
}
```

4.组织行

```groovy
#!/usr/bin/env groovy
println "Hello from the shebang line"
```
这类脚本注释主要用于表明脚本的路径。

### 字符串

#### 单引号字符串

单引号字符串对应java中的String，不支持插入。

```groovy
'a single quoted string'
```

#### 字符串连接

```groovy
assert 'ab' == 'a' + 'b'
```

#### 三引号字符串

```groovy
'''a triple single quoted string'''
```

三引号字符串同样对应java中的String，不支持动态插入。三引号字符串支持多行：

```groovy
def aMultilineString = '''line one
line two
line three'''
```

#### 转义

Groovy中使用`\`来进行转义

```groovy
'an escaped single quote: \' needs a backslash'
```

#### 双引号字符串

```groovy
"a double quoted string"
```

如果双引号字符串中没有插入表达式的话对应的是java中的String对象，如果有则对应Groovy中的GString对象。

Groovy中使用`${}`来表示插入表达式，`$`来表示引用表达：

```groovy
def name = 'Guillaume' // a plain string
def greeting = "Hello ${name}"

assert greeting.toString() == 'Hello Guillaume'
```

```groovy
def person = [name: 'Guillaume', age: 36]
assert "$person.name is $person.age years old" == 'Guillaume is 36 years old'
```

```groovy
shouldFail(MissingPropertyException) {
    println "$number.toString()"
}
```

#### 插入闭包表达式

```groovy
def sParameterLessClosure = "1 + 2 == ${-> 3}" 
assert sParameterLessClosure == '1 + 2 == 3'

def sOneParamClosure = "1 + 2 == ${ w -> w << 3}" 
assert sOneParamClosure == '1 + 2 == 3'
```

```groovy
def number = 1 
def eagerGString = "value == ${number}"
def lazyGString = "value == ${ -> number }"

assert eagerGString == "value == 1" 
assert lazyGString ==  "value == 1" 

number = 2 
assert eagerGString == "value == 1" 
assert lazyGString ==  "value == 2" 
```

关于闭包，暂时先看看就行，等后面具体学习完闭包以后再回来看这几个表达式就简单了。

#### 三双引号字符串

```groovy
def name = 'Groovy'
def template = """
    Dear Mr ${name},

    You're the winner of the lottery!

    Yours sincerly,

    Dave
"""

assert template.toString().contains('Groovy')
```

#### 斜杠字符串

Groovy也可以使用`/`来定义字符串，主要用于正则表达式

```groovy
def fooPattern = /.*foo.*/
assert fooPattern == '.*foo.*'
```

```groovy
def escapeSlash = /The character \/ is a forward slash/
assert escapeSlash == 'The character / is a forward slash'
```

```groovy
def multilineSlashy = /one
    two
    three/

assert multilineSlashy.contains('\n')
```

```groovy
def color = 'blue'
def interpolatedSlashy = /a ${color} car/

assert interpolatedSlashy == 'a blue car'
```

#### $/和/$字符串

```groovy
def name = "Guillaume"
def date = "April, 1st"

def dollarSlashy = $/
    Hello $name,
    today we're ${date}.

    $ dollar sign
    $$ escaped dollar sign
    \ backslash
    / forward slash
    $/ escaped forward slash
    $/$ escaped dollar slashy string delimiter
/$

assert [
    'Guillaume',
    'April, 1st',
    '$ dollar sign',
    '$ escaped dollar sign',
    '\\ backslash',
    '/ forward slash',
        '$/ escaped forward slash',
        '/$ escaped dollar slashy string delimiter'

        ].each { dollarSlashy.contains(it) }
```

### 字符

单引号字符串如果只有一个字符会被转化成`char`类型。

### 列表

Groovy中列表使用`[]`表示，其中可以包含任意类型的元素：

```groovy
def heterogeneous = [1, "a", true]  
```

使用下标进行取值和赋值

```groovy
def letters = ['a', 'b', 'c', 'd']

assert letters[0] == 'a'     
assert letters[1] == 'b'

assert letters[-1] == 'd'    
assert letters[-2] == 'c'

letters[2] = 'C'             
assert letters[2] == 'C'

letters << 'e'               
assert letters[ 4] == 'e'
assert letters[-1] == 'e'

assert letters[1, 3] == ['b', 'd']         
assert letters[2..4] == ['C', 'd', 'e'] 
```

### 数组

Groovy中复用List来充当数组，但如果要明确定义真正的数组需要使用类似java的定义方法

```groovy
String[] arrStr = ['Ananas', 'Banana', 'Kiwi']  

assert arrStr instanceof String[]    
assert !(arrStr instanceof List)

def numArr = [1, 2, 3] as int[]      

assert numArr instanceof int[]       
assert numArr.size() == 3
```

### 键值数组

Groovy中键值数组使用如下

```groovy
def colors = [red: '#FF0000', green: '#00FF00', blue: '#0000FF']   

assert colors['red'] == '#FF0000'    
assert colors.green  == '#00FF00'    

colors['pink'] = '#FF00FF'           
colors.yellow  = '#FFFF00'           

assert colors.pink == '#FF00FF'
assert colors['yellow'] == '#FFFF00'

assert colors instanceof java.util.LinkedHashMap
```


-----------------

以上简单列举了Groovy的基本语法，想要深入学习的可以参考以下网址：

* [Groovy Syntax](http://www.groovy-lang.org/syntax.html)

运行[syntax.groovy](https://github.com/ZhangQinglian/Cradle/blob/master/groovy-syntax/syntax.groovy)查看实例及运行结果


安装Groovy参考如下:
[http://www.groovy-lang.org/download.html](http://www.groovy-lang.org/download.html)