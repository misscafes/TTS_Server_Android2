-keepclassmembers class * {
    @com.github.jing332.script.annotation.ScriptInterface <methods>;
}

# 关键修复：保护所有 script 相关类（JavaScriptEngine 是抽象类，混淆后会导致实例化失败）
-keep class com.github.jing332.script.**{ *;}
-keep class com.github.jing332.script.runtime.**{ *;}
-keep class com.github.jing332.script.simple.**{ *;}
-keep class com.github.jing332.script.engine.**{ *;}

-keep class org.mozilla.javascript.**  { *; }