# 待办事项（TODO）

## 版本变更记录

### v1.26.053014（批量切换标签 + JobCancellationException 修复）

#### 问题现象
- 系统 TTS 使用过程中偶现「加载配置失败：kotlinx.coroutines.JobCancellationException: Parent job is Cancelling」

#### 根因分析
- `SystemTtsService.onCreate()` 中 `mScope = CoroutineScope(Dispatchers.IO)` 使用了**普通 `Job`**
- 普通 `Job` 具有 fail-fast 特性：任意子协程异常会导致整个 Scope 的 Job 进入 Cancelling 状态
- 此后在该 Scope 中启动的新协程（如 `initManager()` 里的 `mTtsManager!!.init()`）会在 suspend 点抛出 `JobCancellationException`
- 该异常被 `AbstractMixSynthesizer.init()` 的 `catch (e: Exception)` 捕获，转化为 `ErrorEvent.Repository`，最终在日志中显示为「加载配置失败」

#### 修复内容
- `SystemTtsService.kt`：`mScope` 改为 `CoroutineScope(SupervisorJob() + Dispatchers.IO)`
  - `SupervisorJob` 确保子协程之间相互独立，一个任务失败不会拖垮整个 Service 的协程生命周期
- 新增 `import kotlinx.coroutines.SupervisorJob`

---

### v1.26.052922（P0 无声音问题最终修复：恢复 runBlocking）

#### 问题根因重定位
- **之前结论错误**：`TtsEngineError.Engine` 改为 `data class` 并非真正原因
- **真正原因**：`SystemTtsService.onSynthesizeText()` 中移除了 `runBlocking { synthesizerJob?.join() }`
  - Android 的 `TextToSpeechService` 通过 `onSynthesizeText()` 回调驱动 TTS 合成
  - 该回调要求**同步阻塞**直到音频全部写入 `SynthesisCallback`，否则系统会立即认为合成完成并关闭音频流
  - 移除 `runBlocking` 后，`onSynthesizeText()` 立即返回，后台协程还没输出音频，音频流已被系统回收 → 无声音
- **修复**：恢复 `runBlocking { ... synthesizerJob?.join() }` 结构，重新添加 `import kotlinx.coroutines.runBlocking`

#### 保留的修改
- `ListManagerScreen.kt` 标题栏点击进入完整编辑界面
- `app/build.gradle` 版本号格式 `1.yy.MMddHH` 和 APK 文件名后缀优化
- `SysTtsForwarderService.kt` `WeakReference` 内存泄漏修复（此改动与声音无关，保留）

#### 测试结果
- ✅ **`TTS-Server-v1.26.052922-runblocking.apk` → 有声音**

#### 构建配置回退与 APK 命名优化
- `app/build.gradle`：版本号格式从 `1.yy.MMdd.n` 恢复为 **`1.yy.MMddHH`**（精确到小时），移除 `buildCounter()` 和 `.build_counter` 文件逻辑
- `app/build.gradle`：APK 文件名现在自动带上 flavor / buildType 后缀（如 `-dev`、`-debug`），避免 `appRelease` 与 `devRelease` 输出同名 APK 导致混淆装成多个应用

---

### v1.26.0529-patch2（大规则导入闪退修复）

#### 导入/恢复线程安全修复
- `ConfigImportBottomSheet.kt`：`onImport` 改为 `suspend`，`SelectImportConfigDialog` 的 `onSelectedList` 改为 `suspend`，按钮点击增加 `scope.launch + runCatching` 异常捕获
- `SpeechRuleImportBottomSheet.kt` / `PluginImportBottomSheet.kt` / `ReplaceRuleImportBottomSheet.kt` / `ListImportBottomSheet.kt`：JSON 解析与数据库插入全部移到 `Dispatchers.IO` 执行，避免主线程阻塞导致闪退
- `BackupRestoreViewModel.kt`：`restore()` 与 `importFromJsonFile()` 统一包上 `withIO`，确保备份恢复全过程在后台线程执行

#### 快捷音色交互优化
- `ListManagerScreen.kt`：系统 TTS 列表项**长按**改为设置/取消快捷音色（原来长按是切换标签）
- `ListManagerScreen.kt`：系统 TTS 列表项**轻按**保持进入快捷编辑面板（已有行为，未改动）
- `Item.kt`：DropdownMenu 中新增「切换标签」选项，保留 `switchSpeechTarget` 功能入口
- `Item.kt`：新增 `onSwitchTag` 回调参数

#### 构建配置
- `app/build.gradle`：版本号格式从 `1.yy.MMddHH` 改为 **`1.yy.MMdd.n`**，其中 `n` 为**当日构建序号**（每次构建自动 +1），方便同日多次构建时区分 APK

---

### v1.26.0529（性能优化 + Bug修复 + 快捷音色）

#### P0 严重阻塞问题修复
- `SystemTtsService.kt`：移除 `runBlocking`，`onSynthesizeText` 改为纯后台协程启动，解决最长 125 秒主线程阻塞导致的 ANR
- `HandlerUtils.kt`：`runOnIO` 中 `runBlocking` 改为 `launch(IO)`，避免后台线程被无意义阻塞
- `SystemTtsService.kt`：新增 `cachedVoices` + `cachedAllTts` 内存缓存，`onGetVoices()` / `onIsValidVoiceName()` 不再频繁查询数据库

#### P1 内存泄漏与生命周期修复
- `SysTtsForwarderService.kt`：`instance` 改为 `WeakReference`，`onDestroy` 中置空，减少内存泄漏
- `App.kt`：`GlobalScope` 改为 `CoroutineScope(SupervisorJob() + Dispatchers.IO)`，协程生命周期可控
- `KeepAliveService.kt`：保活检查间隔 `5000ms` → `30000ms`，减少主线程唤醒和电量消耗

#### P2 频繁创建对象 / 资源未复用修复
- `TtsPluginEngineV2.kt`：新增 `sharedOkHttpClient` 单例，`newBuilder()` 复用连接池，避免每次请求新建 `OkHttpClient`
- `TtsPluginEngineV2.kt`：`newCachedThreadPool()` 改为带命名 + `daemon` 的线程工厂，防止高并发时无限创建线程

#### SQLiteBlobTooBigException 修复
- `App.kt`：`SQLiteDatabase.setCursorWindowSize(10 * 1024 * 1024)`（Android 11+），全局扩大 CursorWindow
- `SpeechRuleDao.kt`：新增 `allLite` / `allEnabledLite` / `flowAllLite()` 轻量查询（不查 `code` 字段），避免大字段导致 CursorWindow 溢出
- `SpeechRuleDao.kt`：新增 `updateEnabled(id, isEnabled)` / `updateOrder(id, order)` 字段级更新，防止用轻量实体覆盖 `code`
- `SpeechRuleDao.kt`：新增 `getById(id: Long)`，编辑时按需加载完整数据
- `SpeechRuleManagerScreen.kt`：列表改用 `flowAllLite()` + 字段级更新，编辑时通过 `getById` 获取完整数据
- `BackupRestoreViewModel.kt`：备份 `speechRuleDao.all` 添加 try-catch，若触发 `SQLiteBlobTooBigException` 自动降级为逐条 `getById` 查询

#### 新功能：快捷音色
- `AppConfig.kt`：新增 `quickAccessTtsId` 持久化配置（默认 `-1L`）
- `Item.kt`：新增 `onSetQuickAccess` / `isQuickAccess` 参数；DropdownMenu 添加"设为/取消快捷音色"选项；列表项显示 ⭐ 星星标记
- `ListManagerScreen.kt`：标题栏"系统TTS"文字添加 `clickable`，点击后直接进入快捷音色的**完整编辑界面**（`TtsEditContainerScreen`）

---

---

### v1.26.0530（TTS Server 息屏停止问题修复）

#### 问题现象
- TTS Server（系统TTS转发器）工作时，界面停留在日志页面，手机自动息屏后服务停止，需手动重启

#### 根因分析
- `AbsForwarderService` 继承自 `IntentService`，默认返回 `START_NOT_STICKY`
- 息屏后系统进入 Doze 模式或内存紧张时进程被清理，`IntentService` 不会自动重启
- `ForwarderServiceManager` 使用普通 `startService()` 而非 `startForegroundService()`，在后台启动受限

#### 修复内容
- `AbsForwarderService.kt`：
  - 将基类从 `IntentService` 改为 `Service`
  - `onStartCommand` 返回 `START_STICKY`，确保进程被杀后系统自动重启服务
  - 在 `onCreate()` 中**立即**调用 `startForeground()`（使用临时通知），避免 `ForegroundServiceDidNotStartInTimeException`
  - IP 地址获取成功后通过 `updateNotification()` 更新通知内容
  - 服务器停止后自动调用 `stopSelf()` 清理
- `ForwarderServiceManager.kt`：使用 `startForegroundServiceCompat()` 替代 `startService()`
- `SystemForwarderSwitchActivity.kt`：使用 `startForegroundServiceCompat()` 替代 `startService()`

---

## 会话摘要

### 2026-05-30 本次会话（v1.26.053012 - JobCancellationException 修复）
- **当前版本**：v1.26.053014（基于 `hhh4` 分支）
- **已完成事项**：
  1. **新增「批量切换标签」功能**：
     - `ListManagerScreen.kt` AppBar 右侧新增切换标签按钮（`Icons.Default.SwapHoriz`）
     - 新建 `BatchSwitchTagDialog.kt`：复选列表选择音色，点击「切换」批量将每个条目标签切换到下一个
     - 切换逻辑：ALL→第一个标签→下一个标签…最后一个标签循环回第一个标签
     - BGM 类型条目自动排除
  2. **修复「加载配置失败：JobCancellationException」**：
     - `SystemTtsService.kt`：`mScope` 从普通 `Job` 改为 `SupervisorJob()`
     - 根因：普通 `Job` fail-fast，单个子协程异常会导致整个 Scope 被取消，后续任务全部抛出 `JobCancellationException`
  3. **生成正式版 APK**：`newapk/TTS-Server-v1.26.053014.apk`
- **注意事项**：
  - `allowMainThreadQueries` 暂时保留
  - `SystemTtsService` 的 `runBlocking` **已恢复**
  - 编译环境需使用 Android Studio 自带 JDK 21（Java 26 与 Gradle 8.10.2 不兼容）

### 2026-05-30 上次会话（v1.26.0530 - 息屏停止修复 / 已回退）
- **当前版本**：v1.26.053012
- **已完成事项**：
  1. **TTS Server 息屏停止问题修复**：
     - 将 `AbsForwarderService` 从 `IntentService` 改为 `Service`
     - `onStartCommand` 返回 `START_STICKY`，进程被杀后自动重启
     - `onCreate` 中立即调用 `startForeground()`，避免超时崩溃
     - 启动入口统一使用 `startForegroundServiceCompat()`
  2. **生成正式版 APK**：`newapk/TTS-Server-v1.26.053012.apk`
- **注意事项**：
  - `allowMainThreadQueries` 暂时保留
  - `SystemTtsService` 的 `runBlocking` **已恢复**
  - 编译环境需使用 Android Studio 自带 JDK 21（Java 26 与 Gradle 8.10.2 不兼容）

### 2026-05-30 上次会话（回退到 v1.26.052922）
- **操作**：从 v1.26.053011 **回退**到 v1.26.052922
- **回退原因**：v1.26.053011（TTS Server 息屏停止修复）验证不通过，退回上一稳定版本
- **回退内容**：
  - 代码：`git reset --hard fafafb9c`（回退至 v1.26.052922 最终状态）
  - APK：删除 `newapk/TTS-Server-v1.26.053011.apk`
- **当前有效版本**：v1.26.052922（P0 无声音问题已修复，已验证有声音）
- **注意事项**：
  - `allowMainThreadQueries` 暂时保留
  - `SystemTtsService` 的 `runBlocking` **已恢复**
  - 编译环境需使用 Android Studio 自带 JDK 21（Java 26 与 Gradle 8.10.2 不兼容）

### 2026-05-29 上次会话（v1.26.052922）
- **当前版本**：v1.26.052922（基于 `hhh4` 分支）
- **已完成事项**：
  1. P0~P2 性能优化（ANR 修复、内存泄漏修复、资源复用优化）
  2. `SQLiteBlobTooBigException` 崩溃修复（CursorWindow 扩大 + 轻量查询 + 备份降级）
  3. 新增"快捷音色"功能（列表项设为快捷音色 + 标题栏点击进入完整编辑界面）
  4. **大规则导入闪退修复**：所有导入路径（朗读规则/插件/替换规则/列表/备份恢复）的 JSON 解析与数据库插入全部移至 `Dispatchers.IO`，并增加 `runCatching` 异常捕获
  5. **P0 无声音问题最终修复**：经多轮对照 APK 测试，确认真正原因是 `SystemTtsService.onSynthesizeText()` 移除了 `runBlocking`（破坏了 Android TTS 同步契约）。已恢复 `runBlocking { synthesizerJob?.join() }`。**已验证有声音**。
- **注意事项**：
  - `allowMainThreadQueries` 暂时保留（项目中存在大量 UI 层同步数据库调用，移除需专门的数据库异步化迭代）
  - `SystemTtsService` 的 `runBlocking` **已恢复**（这是 Android `TextToSpeechService` 的同步契约要求，不能移除）
  - 编译环境需使用 Android Studio 自带 JDK 21（Java 26 与 Gradle 8.10.2 不兼容）
