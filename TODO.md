# 待办事项（TODO）

## 版本变更记录

### v1.26.052922（TTS 引擎修改全回滚 + 问题定位）

#### 问题定位结果（对照测试）
- **upstream 原版有声音** → 原始代码没问题
- **只改 `TtsEngineError.kt`（`Engine` → `data class`）→ 没有声音**
- **只改 `AndroidTtsEngine.kt` listener + 去掉 `init()` `release()` → 没有声音**
- **结论：罪魁祸首是 `TtsEngineError.Engine` 从 `object` 改为 `data class`**
- 原因待查（仅是一个错误类型定义改动，不应影响音频逻辑，但实测可 100% 复现）

#### 回滚内容
- `TtsEngineError.kt` / `AndroidTtsEngine.kt` / `SysTtsForwarderService.kt`：全部回滚到 `v1.26.052919` 原始状态
- 保留 `ListManagerScreen.kt` 标题栏点击进入完整编辑界面的修改
- 保留 `app/build.gradle` 版本号格式和 APK 文件名后缀优化

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

## 会话摘要

### 2026-05-29 本次会话（v1.26.052922）
- **当前版本**：v1.26.052922（基于 `hhh4` 分支）
- **已完成事项**：
  1. P0~P2 性能优化（ANR 修复、内存泄漏修复、资源复用优化）
  2. `SQLiteBlobTooBigException` 崩溃修复（CursorWindow 扩大 + 轻量查询 + 备份降级）
  3. 新增"快捷音色"功能（列表项设为快捷音色 + 标题栏点击进入完整编辑界面）
  4. **大规则导入闪退修复**：所有导入路径（朗读规则/插件/替换规则/列表/备份恢复）的 JSON 解析与数据库插入全部移至 `Dispatchers.IO`，并增加 `runCatching` 异常捕获
  5. **TTS 引擎问题定位**：经多轮对照 APK 测试，确认 `TtsEngineError.Engine` 从 `object` 改为 `data class` 会导致 TTS 无输出声音，已全量回滚
- **注意事项**：
  - `allowMainThreadQueries` 暂时保留（项目中存在大量 UI 层同步数据库调用，移除需专门的数据库异步化迭代）
  - `SystemTtsService` 的 `runBlocking` 已完全移除，合成逻辑全部在后台协程执行
  - 编译环境需使用 Android Studio 自带 JDK 21（Java 26 与 Gradle 8.10.2 不兼容）
