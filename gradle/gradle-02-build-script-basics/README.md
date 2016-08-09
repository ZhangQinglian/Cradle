# Projects和Tasks  
在gradle中，所有的事物都基于两个概念：**projects**和**tasks**。  
每一个gradle的构建系统都由一个或多以project组成。例如一个project可以表示一个jar包或者一个web app的构建。又或者是一个android应用的编译打包。  
每一个project都由一个或若干个task组成。一个task包含了一些构建过程中的原子不可拆分的片段。一个task可能用来编译一些java的类文件，生成Javadoc，又或者部署你的代码到管理仓库。  

在上一讲中我们打印了helloworld，那么这次我们就使用task来打印helloworld：  

```groovy
task hello {
    doLast {
        println 'Hello world!'
    }
}
```  
接着在命令行输入gradle -q hello,其输出如下：  

```sh
Hello world!
```
执行这行命令，到底发生了什么？首先这个脚本定义了一个task叫做hello，让后向其添加一个action。当你执行gradle hello的时候，gradle会执行这个task，接着按照顺序来执行你插入其中的action。一个action可以是一个简单的闭包，并包含一些groovy的代码。

## doLast的简写  

```groovy
task hello << {
    println 'Hello world!'
}
```  
这里还是定义了一个hello的task，使用<<来作为doLast的简写。

## 脚本即代码  
虽然在编写gradle脚本的时候用的是gradle提供的语法，但是我们却可以在其中很自由的使用groovy，如下：  

```groovy
task upper << {
    String someString = 'mY_nAmE'
    println "Original: " + someString 
    println "Upper case: " + someString.toUpperCase()
}
```
或者

```gradle
task count << {
    4.times { print "$it " }
}
```

是不是觉得很爽？Groovy灵活而优秀的语法在gradle中可以被很友好的保留了。

## task之间的依赖关系

task之间是可以建立依赖关系的，我们看实际代码：

```gradle
task hello << {
    println 'Hello world!'
}
task intro(dependsOn: hello) << {
    println "I'm Gradle"
}
```

当我们使用命令gradle -q intro，其输出结果如下：

```sh
Hello world!
I'm Gradle
```

懒依赖：当添加一个task依赖的时候，这个task当前可以是不存在的：

```gradle
task taskX(dependsOn: 'taskY') << {
    println 'taskX'
}
task taskY << {
    println 'taskY'
}
```

## 动态task

Groovy的强大之处不仅在于可以用来定义task的具体动作，还可以用来动态定义task。

```gradle
4.times { counter ->
    task "task$counter" << {
        println "I'm task number $counter"
    }
}
```

这时候我们就生成了task0,task1,task2,task3这四个task。

## 使用已经存在的task

一旦task被创建，那么我们就可以使用task对应的API了。还是使用上面的例子：

```gradle
4.times { counter ->
    task "task$counter" << {
        println "I'm task number $counter"
    }
}
task0.dependsOn task2, task3
```

我们运行gradle -q task0:

```sh
I'm task number 2
I'm task number 3
I'm task number 0
```

除了上述的使用现有的task建立以来关系外，还能使用现有的task添加action：

```gradle
task hello << {
    println 'Hello Earth'
}
hello.doFirst {
    println 'Hello Venus'
}
hello.doLast {
    println 'Hello Mars'
}
hello << {
    println 'Hello Jupiter'
}
```

我们运行gradle -q hello:

```sh
Hello Venus
Hello Earth
Hello Mars
Hello Jupiter
```

## task额外属性

我们可以给task添加我们需要的额外属性，例如添加属性myProperty，可以使用ext.myProperty来初始化这个属性，接着这个属性就可以被正常的读写了。

```gradle
task myTask {
    ext.myProperty = "myValue"
}

task printTaskProperties << {
    println myTask.myProperty
}
```

## 定义通用方法

在gradle中我们可以自定一些常用的方法来给多个task使用，如下：

```gradle
task getThreeNumber <<{
    int[] numbers = getNumbers(3);
    numbers.each {
        println "number is $it"
    }
}


int[] getNumbers(int size){

    def random = new Random()

    int[] numbders = new int[size]
    for(int i = 0 ; i<size ; i++){
        numbders[i] = random.nextInt(100)
    }
    numbders
}
```

我们运行gradle -q getThreeNumber:

```gradle
bogon:gradle-02-build-script-basics scott$ gradle -q getThreeNumber
number is 42
number is 35
number is 8
```

## 默认task

gradle允许用户定义一个或多个默认task，当没有指定运行的task的时候，默认的task就会被执行。

```gradle

defaultTasks 'clean', 'run'

task clean << {
    println 'Default Cleaning!'
}

task run << {
    println 'Default Running!'
}

task other << {
    println "I'm not a default task!"
}
```

我们运行gradle -q:

```sh
Default Cleaning!
Default Running!
```

## 总结
OK，以上就是gradle中project和task基本概念了。看到这读者们是不是也跃跃欲试了吗？那还等什么，装上gradle，一起玩起来！