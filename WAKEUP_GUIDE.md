# TTS Server Android 唤醒指南

本文档记录项目核心配置、常见问题修复和构建流程，用于快速唤醒开发环境并修复问题。

## 📋 项目关键配置

### 签名配置
**文件位置：** `/workspace/local.properties`

```properties
KEYSTORE_FILE=release.jks
KEY_ALIAS=TTSServer
KEY_PASSWORD=Ktouls123456
STORE_PASSWORD=Ktouls123456
```

**签名文件：** `/workspace/release.jks`

### 构建配置文件
- `.cnb.yml` - CNB云原生配置（8核CPU）
- `gradle.properties.cnb` - 云环境Gradle配置（国内镜像）
- `.ide/Dockerfile` - Docker容器配置（包含国内镜像）
- `CNB_SETUP.md` - 详细云环境搭建文档

---

## 🐛 已知问题及修复方法

### 问题1: TTS插件语言和声音列表不刷新

**症状：** 编辑插件并保存预览后，语言和声音列表不更新

**修复文件：**
- `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ui/PluginEditorViewModel.kt`
- `app/src/main/java/com/github/jing332/tts_server_android/plugin/TtsPluginEngineManager.kt`

**修复要点：**
1. `TtsPluginEngineManager.get()` 方法添加代码对比检测变化
2. `PluginEditorViewModel.updateCode()` 清除缓存并重建引擎
3. `PluginEditorViewModel.updateSource()` 强制重新加载数据

```kotlin
// TtsPluginEngineManager.kt - 添加代码对比
fun get(context: Context, plugin: Plugin): TtsPluginUiEngineV2 {
    val cachedEngine = cache.get(plugin.pluginId)
    if (cachedEngine != null && cachedEngine.plugin.code == plugin.code) {
        return cachedEngine
    }
    val engine = TtsPluginUiEngineV2(context, plugin)
    engine.eval()
    cache.put(plugin.pluginId, engine)
    return engine
}
```

---

### 问题3: TTS插件声音风格选项初始不显示

**症状：** 插件加载后，语言和声音能正常显示，但声音的风格选项等自定义UI不显示，需要手动切换声音才会出现

**修复文件：**
- `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ui/PluginTtsViewModel.kt`

**修复要点：**
1. `load()` 方法在加载声音列表后，如果已选择声音则调用 `updateCustomUI()`
2. 确保 `onVoiceChanged()` 被触发以加载风格选项等自定义UI

```kotlin
// PluginTtsViewModel.kt - 初始加载时触发 onVoiceChanged
suspend fun load(
    context: Context,
    plugin: Plugin?,
    source: PluginTtsSource,
    linearLayout: LinearLayout,
) =
    withIO {
        withMain { isLoading = true }
        try {
            initEngine(plugin, source)
            engine.onLoadData()

            withMain {
                linearLayout.removeAllViews()
                engine.onLoadUI(context, linearLayout)
            }

            updateLocales()
            updateVoices(source.locale)

            // 初始加载时如果已选择声音，触发 onVoiceChanged 以加载风格选项等自定义UI
            if (source.voice.isNotBlank() && source.locale.isNotBlank()) {
                updateCustomUI(source.locale, source.voice)
            }
        } catch (t: Throwable) {
            throw t
        } finally {
            withMain { isLoading = false }
        }
    }
```

---

### 问题2: 转发器日志不显示在UI中

**症状：** 转发器服务运行正常，但UI中看不到日志输出

**修复文件：**
- `app/src/main/java/com/github/jing332/tts_server_android/service/forwarder/SystemTtsForwardServer.kt`
- `app/src/main/java/com/github/jing332/tts_server_android/service/forwarder/SysTtsForwarderService.kt`

**修复要点：**
1. 将 `Log.d/i/e()` 替换为 `callback.log()` 
2. 添加 `sendLog()` 调用将日志发送到UI

```kotlin
// 使用 callback.log() 而不是 Log.d()
callback.log(message)
```

---

## 🔨 构建流程

### 本地构建
```bash
# 构建正式版
./gradlew assembleAppRelease

# 构建开发版
./gradlew assembleDevRelease

# 同时构建两个版本
./gradlew assembleAppRelease assembleDevRelease --build-cache --parallel
```

### 云环境构建

#### 1. 环境准备
```bash
# 检查Java环境
java -version  # 需要 OpenJDK 17+

# 检查Android SDK
ls $ANDROID_HOME/cmdline-tools/latest/bin/

# 接受SDK许可（首次需要）
yes | $ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager --licenses
```

#### 2. 安装必要组件（如果缺失）
```bash
# 安装平台工具、平台、构建工具
$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager \
  "platform-tools" \
  "platforms;android-34" \
  "build-tools;34.0.0"
```

#### 3. 配置国内镜像（云环境）
```bash
# 复制云环境配置
cp gradle.properties.cnb gradle.properties

# 使用阿里云Maven镜像加速
```

#### 4. 执行构建
```bash
# 清理并构建两个版本
./gradlew clean assembleAppRelease assembleDevRelease --build-cache --parallel --daemon
```

---

## 📂 构建产物位置

**正式版：** `/workspace/app/build/outputs/apk/app/release/TTS-Server-vX.X.XXXXX.apk`

**开发版：** `/workspace/app/build/outputs/apk/dev/release/TTS-Server-vX.X.XXXXX.apk`

---

## 🚀 发布流程

### 1. 创建发布文件夹
```bash
mkdir -p /workspace/newapk
```

### 2. 复制并重命名APK
```bash
# 复制正式版
cp /workspace/app/build/outputs/apk/app/release/*.apk \
   /workspace/newapk/TTS-Server-vX.X.XXXXX-release.apk

# 复制开发版
cp /workspace/app/build/outputs/apk/dev/release/*.apk \
   /workspace/newapk/TTS-Server-vX.X.XXXXX-dev.apk
```

### 3. 提交到Git
```bash
git add newapk/
git commit -m "构建并发布 vX.X.XXXXX 版本

- 新增正式版APK
- 新增开发版APK"
git push origin master
```

---

## ⚙️ 关键依赖版本

- **Gradle:** 8.10.2
- **Kotlin:** 1.9.24 / 2.1.10
- **Android SDK:** API 34
- **Build Tools:** 34.0.0
- **Java:** OpenJDK 17

---

## 🔧 云环境优化

### 国内镜像配置
1. **APT镜像** - 阿里云Ubuntu镜像
2. **Android SDK镜像** - 清华大学TUNA镜像
3. **Maven/Gradle镜像** - 阿里云Maven仓库

### Gradle优化参数
- `--build-cache` - 启用构建缓存
- `--parallel` - 并行编译
- `--daemon` - 守护进程模式

---

## 💡 快速唤醒命令

```bash
# 检查环境
echo "Java: $(java -version 2>&1 | head -1)"
echo "Android SDK: $ANDROID_HOME"
echo "Gradle: $(./gradlew --version | grep Gradle)"

# 快速构建（正式版）
./gradlew clean assembleAppRelease --build-cache --parallel

# 快速构建（两个版本）
./gradlew clean assembleAppRelease assembleDevRelease --build-cache --parallel --daemon

# 构建并发布（示例）
./gradlew clean assembleAppRelease assembleDevRelease --build-cache --parallel
mkdir -p newapk
cp app/build/outputs/apk/app/release/*.apk newapk/TTS-Server-release.apk
cp app/build/outputs/apk/dev/release/*.apk newapk/TTS-Server-dev.apk
git add newapk/ && git commit -m "发布新版本" && git push origin master
```

---

## 📞 唤醒AI助手时请提供

1. **问题描述** - 具体的bug或需求
2. **日志信息** - 相关的错误日志或构建日志
3. **当前环境** - 本地/云环境，Java/Gradle版本
4. **期望结果** - 想要达成的目标

**示例唤醒词：**
> "修复TTS插件刷新问题，然后构建正式版和开发版APK并发布到远程仓库"

---

## 📝 修改历史

| 日期 | 版本 | 内容 |
|------|------|------|
| 2026-01-27 | v1.0 | 初始版本，记录插件刷新、转发器日志、云构建流程 |
