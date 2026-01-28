-keepclassmembers class * {
    @com.github.jing332.script.annotation.ScriptInterface <methods>;
}

-keep class com.github.jing332.script.runtime.**{ *;}
-keep class com.github.jing332.script.simple.**{ *;}
# 关键修复：保护 engine 包（RhinoScriptEngine 在此包中）
-keep class com.github.jing332.script.engine.**{ *;}

-keep class org.mozilla.javascript.**  { *; }