# 待办事项（TODO）

## 版本变更记录

### v1.26.060211（批量编辑修复 + 降级为子目录候选列表修复）

#### Bug 修复：批量编辑无法识别子分组条目
- **问题**：批量编辑模式下选中包含子分组的主分组后，批量删除/批量切换插件无法识别子分组中的条目
- **根因**：`models.flatMap { it.list }` 只遍历分组的直接条目，未递归遍历子分组（`children`）
- **修复**：改为 `models.flatMap { it.allTts() }`，两处批量操作（删除、切换插件）均已修复

#### Bug 修复：子分组批量选择 Checkbox 缺失
- **问题**：批量编辑模式下，子分组（`SubGroupHeader`）没有显示批量选择 Checkbox，仍显示普通模式的"启用/禁用" Checkbox
- **修复**：在 `FlattenedCategoryItem.SubGroupHeader` 渲染处增加 `isBatchEditMode` 分支
  - 批量编辑模式：显示 **TriStateCheckbox（三态复选框）** + 展开图标 + 名称
  - 全选 ✅ / 部分选中横线（Indeterminate）/ 未选空框
  - 点击可全选/取消全选该子分组下所有条目
  - 普通模式：保持原有 `SubGroupHeader` 组件不变

#### Bug 修复：新增分组无法被选为"降级为子目录"目标
- **问题**：新增分组后，其他分组"降级为子目录"/"转为子分组"时，候选目标列表中没有新分组
- **根因**：`addGroupDialog` 取消时未重置 `addGroupParentId`，导致后续通过"添加分组"按钮创建的分组被误设为子分组（`parentGroupId != 0`），不在 `models` 中
- **修复**：
  1. `addGroupDialog` 的 `onDismissRequest` 中增加 `addGroupParentId = 0L` 重置
  2. `moveGroupDialog` 使用 `LaunchedEffect(models, targetGroup.id)` 监听 `models` 变化并强制刷新候选列表
  3. `showConvertToSubGroup` 候选列表计算移入 `AlertDialog` 的 `text` lambda 内部

---

### v1.26.060118（无限子分组树形模式）

#### 系统 TTS 列表：无限子分组树形模式
- **核心改动**：系统 TTS 分组列表从「平面分组 + categoryPath 子分组」双轨机制，升级为以 `parentGroupId` 为唯一机制的**无限层级树形分组**
- **ViewModel 层**：
  - `_list` 从 `List<GroupWithSystemTts>` 改为 `List<GroupTreeNode>`
  - 新增 `GroupTreeNode` 数据类，含 `group`/`list`/`children` 字段和 `allTts()` 方法
  - 新增 `toTree()` 从数据库平面数据构建树、`flattenNodes()`/`findNodeByGroupId()` 树遍历辅助
  - 新增 `updateGroupExpanded()`/`addGroup()`/`moveGroupToRoot()`/`moveGroupToParent()`/`wrapRootGroupsIntoParent()` 树形操作
  - `reorder()` 限制分组只能在同一 `parentGroupId` 内交换
- **UI 层**：
  - `LazyColumn` 中新增 `fun LazyListScope.groupTreeNode()` 递归渲染函数（参考拉取仓库实现方式）
  - 分组 Header 使用 `stickyHeader`，子分组通过递归调用 `groupTreeNode(child, level + 1)` 渲染
  - 缩进通过 `(level * 20).dp` 实现，支持无限层级视觉嵌套
  - `Group` 组件新增 `onAddChildGroup`/`onPromoteToRoot`/`onMoveToParent` 树形操作回调
  - 保留原有的批量编辑模式和 `categoryPath` 子分组兼容逻辑
- **数据库**：`parentGroupId` 字段和 `getGroupsByParent()` DAO 方法在 v32 升级时已就绪

#### AI 生成配置按钮图标替换
- 系统 TTS 列表 AppBar AI 按钮从 `Text("🤖")` 改为 `Icon(Icons.Default.SmartToy, ...)`
- 新增字符串资源 `ai_generate_config`（中文：AI生成配置）

---

### v1.26.060109-patch（AI 生成配置）

#### 新增功能：AI 生成配置
- **来源**：从 `My-bd-tts-server-AndroidProject` 拉取仓库移植
- **入口**：系统 TTS 列表 AppBar 右侧新增「🤖 AI生成配置」按钮
- **功能**：
  - 选择已启用的 TTS 插件，支持静态解析插件代码中的声音列表，或动态调用插件接口获取声音
  - 自动识别朗读规则中的角色标签（女童/男童/少女/少年/女青年/男青年/女中年/男中年/女老年/男老年）
  - 支持 AI 大模型自动分类声音到角色类型（本地无法识别时调用 AI 接口补充）
  - 生成匹配预览：显示每个角色将匹配到的声音
  - 确认生成：自动创建插件声音组 + 各角色子分组，并写入对应的 TTS 配置
- **模型设置**：支持自定义 AI 接口（格式：`接口地址@@模型名@@API_KEY`），独立配置不影响主规则
- **适配修改**：
  - `SpeechRule` 当前版本无 `isModule`/`projectMode` 字段，筛选逻辑简化为 `allEnabled.firstOrNull()`
  - `resolveVoiceAvatarUri` 简化为只返回插件自带 icon（当前项目无 avatar drawable 资源）

---

### v1.26.060110（批量编辑 + 数据库 parentGroupId）

#### 数据库架构升级
- `SystemTtsGroup`：新增 `parentGroupId` 字段（默认 `0L`），为后续分组嵌套做准备
- `DatabaseManager`：版本号 31 → 32，添加 `AutoMigration(from = 31, to = 32)`
- `SystemTtsV2Dao`：新增 `getGroupsByParent()` / `getGroupCountByParent()` 树形查询方法

#### 新增功能：批量编辑模式
- **入口**：系统 TTS 列表 AppBar 右侧「✏️ 批量编辑」按钮
- **功能**：
  - 列表项左侧显示 Checkbox，点击勾选/取消勾选
  - 批量删除：删除所有选中的音色（含确认对话框）
  - 批量切换插件：将选中的插件音色批量替换为另一个插件（清空 locale/voice）
  - 批量编辑模式下禁用原有点击/长按操作，点击列表项变为切换选中状态
  - 返回键或「取消」按钮退出批量编辑模式

#### 图标统一
- 新增按钮使用 Material Icons（EditNote、Delete、Extension）

#### 构建配置
- `gradle.properties`：新增 `org.gradle.java.home=C:\Program Files\Android\Android Studio\jbr`，固定使用 Android Studio 自带 JDK 21，避免系统 Java 26 与 Gradle 8.10.2 不兼容

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

### 2026-06-02 本次会话（v1.26.060210 - 批量编辑 Bug 修复）
- **当前版本**：v1.26.060211（基于 `hhh4` 分支）
- **已完成事项**：
  1. **修复批量操作无法遍历子分组条目**：
     - `ListManagerScreen.kt`：批量删除和批量切换插件对话框中，`models.flatMap { it.list }` → `models.flatMap { it.allTts() }`
     - 修复后批量操作可正确遍历树形分组中的所有子分组条目
  2. **修复子分组批量选择 Checkbox 缺失**：
     - `ListManagerScreen.kt`：`FlattenedCategoryItem.SubGroupHeader` 渲染处增加 `isBatchEditMode` 分支
     - 批量编辑模式下显示 TriStateCheckbox（三态复选框），支持全选/部分选中（横线）/未选
     - 普通模式下保持原有 `SubGroupHeader` 组件不变
  3. **修复新增分组无法被选为降级目标**：
     - `ListManagerScreen.kt`：`addGroupDialog` 取消时重置 `addGroupParentId = 0L`
     - `moveGroupDialog` 使用 `LaunchedEffect` 强制刷新候选列表
     - `showConvertToSubGroup` 候选列表移入 `text` lambda
  4. **修复分组导出功能**：
     - `ListManagerScreen.kt`：`Group` 组件 `onExport` 从 `{}` 改为 `showGroupExportSheet = listOf(node)`
  5. **构建 Release APK**：`newapk/TTS-Server-v1.26.060211.apk`
- **注意事项**：
  - `allowMainThreadQueries` 暂时保留
  - `SystemTtsService` 的 `runBlocking` **已恢复**
  - 编译环境需使用 Android Studio 自带 JDK 21

### 2026-06-01 本次会话（v1.26.060118 - 无限子分组树形模式）
- **当前版本**：v1.26.060118（基于 `hhh4` 分支）
- **已完成事项**：
  1. **系统 TTS 列表升级为无限子分组树形模式**：
     - `ListManagerViewModel`：`GroupTreeNode` 树节点、`toTree()`/`flattenNodes()`/`findNodeByGroupId()`、树形增删改操作
     - `ListManagerScreen`：`fun LazyListScope.groupTreeNode()` 递归渲染（参考拉取仓库实现方式），`stickyHeader` + 缩进实现无限层级
     - `Group` 组件：新增 `onAddChildGroup`/`onPromoteToRoot`/`onMoveToParent` 树形操作回调
     - 拖拽限制：分组只能在同一 `parentGroupId` 内交换
  2. **AI 生成配置按钮图标替换**：`Text("🤖")` → `Icon(Icons.Default.SmartToy, ...)`，新增字符串资源
  3. **编译通过并构建 Release APK**：`newapk/TTS-Server-v1.26.060118.apk`
- **注意事项**：
  - `allowMainThreadQueries` 暂时保留
  - `SystemTtsService` 的 `runBlocking` **已恢复**
  - 编译环境需使用 Android Studio 自带 JDK 21
  - 保留原有 `categoryPath` 子分组兼容逻辑，树形分组和旧子分组机制并存

### 2026-06-01 上次会话（v1.26.060109-patch - AI 生成配置移植）
- **当前版本**：v1.26.060109-patch（基于 `hhh4` 分支）
- **已完成事项**：
  1. **移植 AI 生成配置功能**：
     - 从 `My-bd-tts-server-AndroidProject-clone` 拉取仓库完整移植 AI 生成配置到 `ListManagerScreen.kt`
     - 包含：模型设置对话框、AI 生成配置对话框、插件选择、动态获取声音、AI 声音分类、本地规则匹配、配置生成写入
     - TopAppBar 新增 🤖 按钮作为入口
  2. **编译通过**：`./gradlew :app:compileAppReleaseKotlin` BUILD SUCCESSFUL
- **注意事项**：
  - `allowMainThreadQueries` 暂时保留
  - `SystemTtsService` 的 `runBlocking` **已恢复**
  - 编译环境需使用 Android Studio 自带 JDK 21

### 2026-06-01 上次会话（v1.26.060109 - JDK 21 配置固定）
- **当前版本**：v1.26.060109（基于 `hhh4` 分支）
- **已完成事项**：
  1. **固定 Gradle 使用 Android Studio 自带 JDK 21**：
     - `gradle.properties` 新增 `org.gradle.java.home` 配置
     - 避免系统 Java 26 与 Gradle 8.10.2 不兼容导致构建失败
  2. **新增「批量切换标签」功能**：
     - `ListManagerScreen.kt` AppBar 右侧新增切换标签按钮（`Icons.Default.SwapHoriz`）
     - 新建 `BatchSwitchTagDialog.kt`：复选列表选择音色，点击「切换」批量将每个条目标签切换到下一个
     - **按分组显示**：对话框内按 Group 分组展示条目，保留分组结构
     - **只切换插件音色**：仅对 `PluginTtsSource` 类型的条目生效，本地 TTS 自动过滤
     - 切换逻辑：ALL→第一个标签→下一个标签…最后一个标签循环回第一个标签
     - BGM 类型条目自动排除
  3. **修复「加载配置失败：JobCancellationException」**：
     - `SystemTtsService.kt`：`mScope` 从普通 `Job` 改为 `SupervisorJob()`
     - 根因：普通 `Job` fail-fast，单个子协程异常会导致整个 Scope 被取消，后续任务全部抛出 `JobCancellationException`
  5. **生成正式版 APK**：`newapk/TTS-Server-v1.26.060110.apk`
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
