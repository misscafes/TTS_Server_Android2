-keep class io.netty.**{*;}

# 保持 lib-common 中的类不被混淆（关键修复：代码编辑器崩溃）
-keep class com.github.jing332.common.** { *; }
-keepclassmembers class com.github.jing332.common.** { *; }

