# Gradle中的Hello world!

首先下载和安装gradle可以参考[官网下载地址](https://gradle.org/gradle-download/)，建议下载带有源码和文档的，以便后期查阅。

下载完以后打开终端输入`gradle -v`有如下信息输出，表示安装成功：

```sh
bogon:gradle scott$ gradle -v

------------------------------------------------------------
Gradle 2.14
------------------------------------------------------------

Build time:   2016-06-14 07:16:37 UTC
Revision:     cba5fea19f1e0c6a00cc904828a6ec4e11739abc

Groovy:       2.4.4
Ant:          Apache Ant(TM) version 1.9.6 compiled on June 29 2015
JVM:          1.8.0_92 (Oracle Corporation 25.92-b14)
OS:           Mac OS X 10.11.6 x86_64
```

接着在你的工作目录下新建一个`build.gradle`文件，并键入如下内容：  
```gradle
println 'hello world!'
```  
接着在该目录下使用gradle命令即可输出如下信息:  

```sh
bogon:gradle-01-helloworld scott$ gradle
hello world!
:gradle-01-helloworld:help

Welcome to Gradle 2.14.

To run a build, run gradle <task> ...

To see a list of available tasks, run gradle tasks

To see a list of command-line options, run gradle --help

To see more detail about a task, run gradle help --task <task>

BUILD SUCCESSFUL

Total time: 2.524 secs
```  
如果不想看到多余的help信息，可以使用`gradle -x help`来过滤掉help这个task。

上面提到了task这个关键字将在下一节做具体讲解。