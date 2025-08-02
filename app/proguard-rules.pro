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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.divinelink.core.model.person.Gender { *; }
-keepclassmembers class com.divinelink.core.model.person.Gender { *; }

-keep class com.divinelink.core.model.search.SearchEntryPoint { *; }
-keepclassmembers class com.divinelink.core.model.search.SearchEntryPoint { *; }

-keep class com.divinelink.core.model.user.data.UserDataSection { *; }
-keepclassmembers class com.divinelink.core.model.user.data.UserDataSection { *; }

-keep class com.divinelink.core.model.media.MediaType { *; }
-keepclassmembers class com.divinelink.core.model.media.MediaType { *; }

-dontwarn java.lang.StringCoding$Result
-dontwarn java.lang.StringCoding
-dontwarn java.lang.StringLatin1$CharsSpliterator
-dontwarn java.lang.StringLatin1
-dontwarn java.lang.StringUTF16$CodePointsSpliterator
-dontwarn java.lang.StringUTF16
-dontwarn jdk.internal.misc.Unsafe
-dontwarn jdk.internal.misc.VM
-dontwarn org.slf4j.impl.StaticLoggerBinder
-dontwarn org.slf4j.impl.StaticMDCBinder
-dontwarn sun.nio.fs.DefaultFileSystemProvider
-dontwarn com.google.auto.service.AutoService