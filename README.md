# DiyWidget [![Download](https://api.bintray.com/packages/jixiaoyong/maven/library/images/download.svg)](https://bintray.com/jixiaoyong/maven/library/_latestVersion)
一些Android开发中常用的自定义控件和工具类



# 使用方法

1. `project/build.gradle`

   ```groovy
   buildscript {
       
       repositories {
           ...
           //以下两个maven库选一个即可
           jcenter() //此处为稳定版本，默认就有
           maven { url 'https://dl.bintray.com/jixiaoyong/maven/' }//此处可以获取到最新开发版本
       }
   ```

2. `app/build.gradle`

   ```groovy
   dependencies {
       ...
       implementation 'com.github.jixiaoyong:library:latest_version'
   }
   ```

