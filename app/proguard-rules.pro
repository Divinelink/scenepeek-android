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


# Fix for Retrofit issue https://github.com/square/retrofit/issues/3751
# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
#-keep,allowobfuscation,allowshrinking interface retrofit2.Call

-dontwarn java.**

-dontwarn org.slf4j.impl.StaticLoggerBinder

-dontwarn android.text.Editable
-dontwarn android.text.GetChars
-dontwarn android.text.Spanned
-dontwarn android.text.SpannableStringBuilder
-dontwarn android.text.SpannableString
