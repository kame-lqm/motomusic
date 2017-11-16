# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 #优化  不优化输入的类文件
-dontoptimize

 #预校验
-dontpreverify

 #混淆时是否记录日志
-verbose

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#保护注解
-keepattributes *Annotation*

-ignorewarnings

-keep class com.iflytek.**{*;}
-keep class com.baidu.**{*;}
-keep class com.baidu.android.**{*;}
-keep class org.apache.http.**{*;}
-keep class com.baidu.tts.**{*;}
-keep class com.android.volley.**{*;}

-keep class com.baidu.voicerecognition.android.**{*;}
-libraryjars libs/armeabi/libmsc.so

-libraryjars libs/arm64-v8a/libmsc.so

-keepattributes Signature, InnerClasses, LineNumberTable

# android-support
-dontwarn android.support.**
-keep class android.support.** { *; }

# app
-keep class com.kame.motomusic.utils.proguard.NoProGuard { *; }
-keep class * extends com.kame.motomusic.utils.proguard.NoProGuard { *; }

# okhttputils
-dontwarn com.zhy.http.**
-keep class com.zhy.http.** { *; }

# okhttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

# okio
-dontwarn okio.**
-keep class okio.** { *; }

# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# amap
-dontwarn com.amap.api.**
-keep class com.amap.api.** { *; }

# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.** { *; }

# jid3
-dontwarn org.blinkenlights.jid3.**