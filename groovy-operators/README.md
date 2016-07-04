# Groovy操作符
这一篇简单介绍一下Groovy中的操作符。

## 算数操作符
groovy支持java中的所有操作符，下面只列举一些groovy特有的：

`**` power运算符，也叫次方。

```groovy
assert  2 ** 3 == 8
```

`**=` power的一元运算

```groovy
def f = 3
f **= 2
assert f == 9
```

## 类操作符

**Safe navigation operator**（安全导航符）用于避免空指针。当不确定一个类是否是空指针的时候使用。
 
```groovy
def person = Person.find { it.id == 123 }    
def name = person?.name                      
assert name == null                          
```

**Direct field access operator**

```groovy
class User {
    public final String name                 
    User(String name) { this.name = name}
    String getName() { "Name: $name" }       
}
def user = new User('Bob')
assert user.name == 'Name: Bob'  
```
在groovy中，`user.name`会调用对应的`getName()`方法，这是groovy的一个语法特性，后面的章节再将。如果想要直接使用这个属性可以这么写：

```groovy
assert user.@name == 'Bob'
```

**Method pointer operator**

`.&`用于将被调用的方法存入一个方法变量中，接着只要调用这个变量就相当于调用之前的那个方法。直接看代码：

```groovy
def str = 'example of method reference'            
def fun = str.&toUpperCase                         
def upper = fun()                                  
assert upper == str.toUpperCase()  
```
上面的代码将str的toUpperCase方法存入了fun中，接着调用fun等于调用了str的toUpperCase方法。

## 正则表达式

**Pattern operator**
模式操作符使用`~`，`~`后面加字符串用于创建`java.util.regex.Pattern`实例。

```groovy
def p = ~/foo/
p = ~'foo'                                                        
p = ~"foo"                                                        
p = ~$/dollar/slashy $ string/$                                   
p = ~"${pattern}" 
assert p instanceof Pattern
```

**Find operator**
查询操作符使用`=~`，在字符串上调用`=~`会生成`java.util.regex.Matcher`实例。

```groovy
def text = "some text to match"
def m = text =~ /match/                                           
assert m instanceof Matcher                                       
if (!m) {                                                         
    throw new RuntimeException("Oops, text not found!")
}
```

**Match operator**
匹配操作符使用`==~`表示，返回结果是Boolean类型。

```groovy
m = text ==~ /match/                                              
assert m instanceof Boolean                                       
if (m) {                                                          
    throw new RuntimeException("Should not reach that point!")
}
```

## 其他操作符

**Spread operator**

展开操作符`*.`，用于收集列表中的一些公共属性，然后将他们合成一个新的列表：

```groovy
class Car {
    String make
    String model
}
def cars = [
       new Car(make: 'Peugeot', model: '508'),
       new Car(make: 'Renault', model: 'Clio')]       
def makes = cars*.make                                
assert makes == ['Peugeot', 'Renault']  
```

**Range operator**

范围操作符使用`..`

```groovy
def range = 0..5                                    
assert (0..5).collect() == [0, 1, 2, 3, 4, 5]       
assert (0..<5).collect() == [0, 1, 2, 3, 4]         
assert (0..5) instanceof List                       
assert (0..5).size() == 6    
```

**Spaceship operator**

比较操作符`<=>`，内部其实是调用了`compareTo`方法：

```groovy
assert (1 <=> 1) == 0
assert (1 <=> 2) == -1
assert (2 <=> 1) == 1
assert ('a' <=> 'z') == -1
```

**Membership operator**

成员关系操作符`in`，相当于调用`isCase`方法，在`List`中，相当于`contains`方法：

```groovy
def list = ['Grace','Rob','Emmy']
assert ('Emmy' in list)                     
```
也可以使用`list.contains('Emmy')` or `list.isCase('Emmy')`


**Identity operator**

身份操作符`is`用于判断两个引用是否相同。相当于在java中的`==`,而在groovy中`==`相当于`equals`方法。

```groovy
def list1 = ['Groovy 1.8','Groovy 2.0','Groovy 2.3']        
def list2 = ['Groovy 1.8','Groovy 2.0','Groovy 2.3']        
assert list1 == list2                                       
assert !list1.is(list2) 
```

**Coercion operator**

强制操作符`as`，用于不同类型的转换：

```groovy
Integer x = 123
String s = x as String 
```

如果要给自定义的类实现强制转换，需要`asType`方法：

```groovy
class Identifiable {
    String name
}
class User {
    Long id
    String name
    def asType(Class target) {                                              
        if (target==Identifiable) {
            return new Identifiable(name: name)
        }
        throw new ClassCastException("User cannot be coerced into $target")
    }
}
def u = new User(name: 'Xavier')                                            
def p = u as Identifiable                                                   
assert p instanceof Identifiable                                            
assert !(p instanceof User) 
```

**Call operator**

Call操作符使用`()`，用来调用`call`这个方法，如下：

```groovy
class MyCallable {
    int call(int x) {           
        2*x
    }
}

def mc = new MyCallable()
assert mc.call(2) == 4          
assert mc(2) == 4  
```

## 操作符重载

groovy中支持操作符重载，重载操作符很简单，只要实现对应的方法即可，如`+`只要实现`plus`方法：

```groovy
class Bucket {
    int size

    Bucket(int size) { this.size = size }

    Bucket plus(Bucket other) {                     
        return new Bucket(this.size + other.size)
    }
}

def b1 = new Bucket(4)
def b2 = new Bucket(11)
assert (b1 + b2).size == 15 
```

## 结尾

关于groovy操作符的详细用法请参考：[http://www.groovy-lang.org/operators.html](http://www.groovy-lang.org/operators.html)