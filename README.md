# Cradle
Gradle学习笔记

## 什么是Gradle
在接触Android Studio之前，很多人像我一样都不知道Gradle为何物，即使是开始使用Android Studio了也很少去注意Gradle，只是觉得它是一个用于编译Android的配置文件。那么Gradle究竟是什么，到底是不是只是配置文件这么简单，我只想说我真的是‘too young，too naive’

![](http://o6p4e1uhv.bkt.clouddn.com/ggfnvbn.jpg)

OK，还是老规矩先看官方是怎么介绍Gradle的：

> We would like to introduce Gradle to you, a build system that we think is a quantum leap for build technology in the Java (JVM) world. 

大概就是说“Gradle是基于JVM（java虚拟机）的构建系统”。也就是说它和`Ant`，`Makefile`类似，是通过一整套自定义的框架来完成项目构建。Ant我没接触过，但大名顶顶的Makefile那诡异的语法曾经可是把我虐惨了，做过Android平台开发的小伙伴应该都懂的。

可想而之，Gradle应该也不是什么省油的灯😂。所以为了不被Gradle虐惨还是来静静地学习一下Gradle吧。

## 从入门到放弃？
最近网上出现了这么一个说法“从入门到放弃”，比如[何弃疗？《xxx，从入门到放弃》](http://www.jianshu.com/p/f9e1b928e3fe),这位作者是从It行业的工作环境角度来分析出现这一说法的原因。但就学习一门编程语言本身而言，确实会出现从入门到放弃的现象。上学的时候大家都学过C语言，那以后觉得会点if else，for循环，指针啥的感觉自己入了门，牛逼哄哄，但直到工作中需要我去分析和移植一些驱动程序，修改Linux内核代码的时候我才恍然大悟，这还是我之前学的C语言吗？咋看不懂咧。。。😱

当然以上都是一些题外话，我要说这些的意思就是学习本身是一件持续的事情，不能觉得入了门就等于会了这门语言，更不能觉得会写hello world就入门了。

## 正式开坑

### Groovy基础

- [Groovy基本句法](https://github.com/ZhangQinglian/Cradle/tree/master/groovy-syntax)
- [Groovy操作符概览](https://github.com/ZhangQinglian/Cradle/tree/master/groovy-operators)
- [Groovy脚本与类](https://github.com/ZhangQinglian/Cradle/tree/master/groovy-structure)
- [Groovy中的面向对象](https://github.com/ZhangQinglian/Cradle/tree/master/groovy-object)
- [Groovy中的闭包](https://github.com/ZhangQinglian/Cradle/tree/master/groovy-closures)
- [写出gradle风格的groovy代码](https://github.com/ZhangQinglian/Cradle/tree/master/groovy-gradle_style)


### Gradle基础  
- [Hello world!](https://github.com/ZhangQinglian/Cradle/tree/master/gradle/gradle-01-helloworld)
