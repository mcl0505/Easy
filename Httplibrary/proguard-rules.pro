# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#TODO retrofit
# 保留Retrofit接口中的所有方法不被混淆
-keep,allowobfuscation interface * extends retrofit2.CallAdapter
-keep,allowobfuscation interface * extends retrofit2.Converter

# 保留所有的Retrofit接口以及它们的方法签名
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*


-keep class com.mh.http.** { *; }

# Moshi 的保留规则
-keep class com.squareup.moshi.** { *; }

# 如果你使用了 Moshi 的 Kotlin 支持，保留注解以及相关的类
-keep @interface com.squareup.moshi.Json
-keep @interface com.squareup.moshi.JsonQualifier

# 保留所有用了 Moshi 注解的类及其成员
-keepclassmembers class ** {
    @com.squareup.moshi.Json <fields>;
}

# 如果你在使用 Kotlin，则需要保持数据类不被混淆
-keepclassmembers class ** {
    *** *(...);
}

# 如果你使用了 Moshi 的自定义适配器(Adapter)，也需要保留它们
-keep class **$$MoshiAdapter.** { *; }

# 保留带有@JsonClass注解的类
-keep @com.squareup.moshi.JsonClass class * { <fields>; }


# 保留所有的枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

