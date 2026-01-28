# Add project specific ProGuard rules here.

# 保持 lib-common 中的类不被混淆（关键修复：代码编辑器崩溃）
-keep class com.github.jing332.common.** { *; }
-keepclassmembers class com.github.jing332.common.** { *; }

# kotlinx.serialization 规则
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions
-keep class kotlinx.serialization.** { *; }
-dontwarn kotlinx.serialization.**