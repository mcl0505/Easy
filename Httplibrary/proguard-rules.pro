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
-keep,allowobfuscation @retrofit2.http.* public *    # 保留所有使用了@http注解的方法

# 保留所有的Retrofit接口以及它们的方法签名
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*


-keep class com.mh.http.** { *; }

#TODO  moshi
# 保留Moshi的适配器工厂
-keepclassmembers class * extends com.squareup.moshi.FromJson {
    *** call$(...);
}
-keepclassmembers class * extends com.squareup.moshi.ToJson {
    *** call$(...);
}

# 保留带有@JsonClass注解的类
-keep @com.squareup.moshi.JsonClass class * { <fields>; }

# 保留所有包含$Types的内部类
-keepclasseswithmembers class * {
    @com.squareup.moshi.JsonQualifier <annotations>;
}

# 保留所有的枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#TODO OkHttp
# 保留OkHttp的请求头
-keepclassmembernames class okhttp3.Request$Builder {
    okhttp3.Headers headers$$okhttp;
}
# 保留OkHttp的响应体
-keepclassmembernames class okhttp3.Response$Builder {
    okhttp3.Headers headers$$okhttp;
}
