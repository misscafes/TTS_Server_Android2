# 保持数据库实体类和序列化相关类不被混淆
-keep class com.github.jing332.database.entities.** { *; }
-keepclassmembers class com.github.jing332.database.entities.** { *; }

# 保持 MapConverters 不被混淆
-keep class com.github.jing332.database.entities.MapConverters { *; }
