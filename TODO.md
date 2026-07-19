# 待办事项（TODO）

## 版本变更记录

### v1.26.071911-patch（修复配置列表 JSON 数组导入崩溃 + TTSOnline官方按分类分组）

#### Bug 修复：普通 TTS 配置列表（JSON 数组）导入时崩溃
- **需求来源**：导入生成的 `jread_voice_千问全家桶2852_四大组_快导配置列表.json` 时抛出 `IllegalArgumentException: JsonArray is not a JsonObject`
- **涉及文件**：
  - `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ListImportBottomSheet.kt`
- **实现内容**：
  1. 在 JRead 格式检测阶段，先判断解析出的 JSON 元素是否为 `JsonObject`
  2. 只有对象类型才读取 `format` 字段进行 JRead 格式匹配
  3. JSON 数组类型直接跳过 JRead 检测，进入普通 TTS 配置列表解析路径
- **验证**：
  - `./gradlew :app:compileAppDebugKotlin` 编译通过
  - `./gradlew :app:assembleAppRelease` 构建成功，生成 `newapk/TTS-Server-v1.26.071911-1815.apk` 和 `newapk/TTS-Server-latest.apk`

#### 资源更新：TTSOnline官方音色按分类标签独立分组
- **需求来源**：用户要求 `TTSOnline官方` 不再按性别/年龄分组，而是按插件内的 `tags` 分类，每个分类一个子分组
- **涉及文件**：
  - `参考/jread_voice_千问全家桶2852_四大组_快导配置列表.json`
- **实现内容**：
  1. 保留呱呱/游音/鹿/火山豆包/2次元/官方音色的原有分组结构
  2. `TTSOnline官方` 改为按 `tags` 字段分类，每个非"全部" tag 生成一个子分组
  3. 17 个分类子分组：openai、上新、二次元、动画鬼畜、多情感、广告解说、微软、推荐、方言、旧版、有声书、模仿、热门、女生、男生、童声、通用
  4. 同一音色若属于多个 tag，会分别出现在对应分类下
- **生成结果**：`参考/jread_voice_千问全家桶2852_四大组_快导配置列表.json`（74 个分组，3597 条配置项）

---

### v1.26.071909-patch4（JRead 导入保留父级分组 + 批量删除自动删除空分组 + APK 命名优化 + 批量编辑模式合并分组）

#### Bug 修复：JRead 音色配置导入保留完整树形父级分组
- **需求来源**：导入 JRead 音色配置包后，只有包含音色的子分组被导入，没有直接音色的父级分组（如 `火山豆包`）丢失，导致树形结构不完整
- **涉及文件**：
  - `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ListImportBottomSheet.kt`
- **实现内容**：
  1. 在 `ListImportBottomSheet` 解析完导入数据后，先调用 `insertGroupsPreservingTree()` 按层级顺序插入所有分组（包括没有直接条目的父级分组）
  2. 插入顺序按 `parentGroupId` 升序、再按 `id` 升序，确保父分组先于子分组插入，保留树形关系
  3. 分组插入完成后再把音色条目展示到选择对话框，用户勾选确认后插入条目
  4. 这样导入结果会保留如 `火山豆包` → `女性青年通用` / `女性中年通用` 等完整层级
- **验证**：
  - `./gradlew :app:compileAppDebugKotlin` 编译通过
  - `./gradlew :app:assembleAppRelease` 构建成功，生成 `newapk/TTS-Server-latest.apk`

#### Bug 修复：批量删除完整分组时自动删除空分组
- **需求来源**：批量编辑模式下全选某个分组并删除后，该分组变成空分组仍残留在列表中，需要一并删除
- **涉及文件**：
  - `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ListManagerViewModel.kt`
  - `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ListManagerScreen.kt`
- **实现内容**：
  1. `ListManagerViewModel` 新增 `findFullySelectedGroups(selectedTtsIds)`：识别被完整选中的分组（分组下所有 TTS 条目均被选中），并过滤掉父子重复项
  2. `deleteGroupTree(node)` 访问级别从 `private` 改为 `internal`，供 UI 层调用
  3. 批量删除确认后，先删除选中的 TTS 条目，再递归删除被完整选中的分组（含其子分组）
  4. 仅删除被完整选中的分组；部分选中的分组即使变空也不会被自动删除
- **验证**：
  - `./gradlew :app:compileAppDebugKotlin` 编译通过
  - `./gradlew :app:assembleAppRelease` 构建成功，生成 `newapk/TTS-Server-latest.apk`

#### 优化：APK 文件名加入构建时间戳，构建后自动拷贝到 newapk 并生成 latest 副本
- **需求来源**：`newapk/` 目录中多个版本号连续的 APK 难以分辨哪个是最新生成的；希望构建后的 APK 自动放到 `newapk/`
- **涉及文件**：
  - `app/build.gradle`
- **实现内容**：
  1. APK 文件名新增分钟秒时间戳后缀，例如 `TTS-Server-v1.26.071910-3628.apk`，每次构建文件名唯一
  2. 每次 `release` 构建完成后自动将原始 APK 拷贝到 `newapk/` 目录
  3. 同时额外生成 `newapk/TTS-Server-latest.apk` 副本（dev 版本为 `TTS-Server-latest-dev.apk`），方便快速识别最新包
- **验证**：
  - `./gradlew :app:assembleAppRelease` 构建成功
  - 生成 `app/build/outputs/apk/app/release/TTS-Server-v1.26.071910-3628.apk`
  - 同时拷贝到 `newapk/TTS-Server-v1.26.071910-3628.apk`
  - 同时生成 `newapk/TTS-Server-latest.apk`

---

### v1.26.071909-patch（批量编辑模式合并分组）

#### 新增功能：批量编辑模式下合并多个分组
- **需求来源**：用户需要在批量编辑模式多选分组，将多个分组合并为一个分组，且合并后不能修改排序
- **涉及文件**：
  - `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ListManagerViewModel.kt`
  - `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ListManagerScreen.kt`
  - `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/MenuMoreOptions.kt`
  - `app/src/main/res/values/strings.xml`
  - `app/src/main/res/values-en/strings.xml`
- **实现内容**：
  1. 移除之前放在右上角 `⋮` 更多选项菜单中的「合并发音人列表」入口
  2. 在批量编辑模式顶部 AppBar 新增「合并分组」按钮（`Icons.Default.CallMerge`）
  3. `ListManagerViewModel` 新增：
     - `getFullySelectedGroups(selectedTtsIds)`：识别被完整选中的分组（分组下所有 TTS 条目均被选中）
     - `mergeSelectedGroups(selectedGroupNodes, targetGroupId)`：将非目标分组的所有条目移动到目标分组，然后删除被合并的空分组
  4. **合并规则**：
     - 必须完整选中至少两个分组（通过分组 Header 的 TriStateCheckbox 全选该分组）
     - 选中的分组必须在同一层级（`parentGroupId` 相同），避免父子关系导致结构混乱
     - 移动条目时修改 `groupId` 为目标分组，并清空 `categoryPath`，使其成为目标分组的直接条目
     - 删除源分组时递归删除其所有子分组
  5. **不修改排序**：条目的 `order` 字段保持不变；合并后列表显示顺序不变
  6. 点击合并按钮后弹出对话框，从选中的分组中选择一个作为目标分组，确认后执行合并并 Toast 提示移动条数
- **验证**：
  - `./gradlew :app:compileAppDebugKotlin` 编译通过
  - `./gradlew :app:assembleAppRelease` 构建成功，APK：`newapk/TTS-Server-v1.26.071909.apk`

---

### v1.26.071908（新增 JRead 插件包/音色配置包导入支持）

#### 新增功能：支持导入 JRead 阅读器导出的插件与音色配置
- **需求来源**：用户提供了 `参考/` 目录下的两个 JRead 格式 JSON 文件，需要 TTS Server 能识别并导入
- **涉及文件**：
  - `lib-database/src/main/java/com/github/jing332/database/entities/plugin/jread/JReadPluginBundle.kt`
  - `lib-database/src/main/java/com/github/jing332/database/entities/plugin/jread/JReadPluginConverter.kt`
  - `lib-database/src/main/java/com/github/jing332/database/entities/systts/jread/JReadVoiceConfigBundle.kt`
  - `lib-database/src/main/java/com/github/jing332/database/entities/systts/jread/JReadVoiceConfigConverter.kt`
  - `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/ConfigImportBottomSheet.kt`
  - `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/plugin/PluginImportBottomSheet.kt`
  - `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ListImportBottomSheet.kt`
  - `lib-database/src/test/java/com/github/jing332/database/jread/JReadImportTest.kt`
- **实现内容**：
  1. 新增 `jread_voice_plugin_bundle` 数据模型与转换器，将 JRead 插件包转换为 TTS Server 的 `Plugin` 实体
     - `Plugin.version` 与 JRead 字符串版本兼容：优先取数字前缀，无法解析时使用字符串 `hashCode` 绝对值
  2. 新增 `jread_voice_config_bundle` 数据模型与转换器，将 JRead 音色配置包转换为 `GroupWithSystemTts` 列表
     - 按 `groupName/subGroupName/thirdGroupName` 自动分组，`categoryPath` 保留子分组路径
     - 每个配置生成 `PluginTtsSource`，关联到对应 `pluginId`
  3. `ConfigImportBottomSheet` 新增 `autoWrapJsonList` 参数，允许关闭自动 `[ ]` 包装，以支持对象格式 JSON 导入
  4. `PluginImportBottomSheet` 检测 `jread_voice_plugin_bundle` 格式并自动转换导入
  5. `ListImportBottomSheet` 检测 `jread_voice_config_bundle` 格式并自动转换导入，同时保留原有 TTS Server 配置导入兼容性
- **验证**：
  - `./gradlew :app:compileAppDebugKotlin` 编译通过
  - `./gradlew :lib-database:testDebugUnitTest --tests "com.github.jing332.database.jread.JReadImportTest"` 单元测试通过
  - `./gradlew :app:assembleAppRelease` 构建成功，APK：`newapk/TTS-Server-v1.26.071909.apk`

#### Bug 修复：JRead 插件导入后无法识别、列表导入分组不准确
- **问题 1**：插件导入后 TTS Server 无法识别变量与启用状态
  - **根因**：`JReadPlugin` 数据类遗漏了 `defVars`、`userVars`、`enabled` 等字段，导致导入后的插件缺少变量声明和启用状态
  - **修复**：
    - `JReadPluginBundle.kt`：新增 `defVars`、`userVars`、`enabled` 等字段及 `JReadPluginVar` 数据类
    - `JReadPluginConverter.kt`：把 JRead 的 `defVars.{name,hint}` 映射为 TTS Server 的 `defVars.{label,hint}`，并保留 `userVars` 和 `enabled`
- **问题 2**：音色配置导入后分组不准确
  - **根因**：旧实现按 `(groupName, subGroupName, thirdGroupName)` 平面组合创建分组，未使用 `parentGroupId` 构建树形层级，且主分组因无直接配置被过滤掉
  - **修复**：
    - `JReadVoiceConfigConverter.kt`：优先使用 `groups` 字段构建分组结构，按 `groupName → subGroupName → thirdGroupName` 创建主/子/孙分组并正确设置 `parentGroupId`
    - 导入结果保留空主分组，确保 UI 能显示完整树形结构
- **问题 3**：插件导入点击后无法打开选择列表
  - **根因**：`PluginImportBottomSheet` / `ListImportBottomSheet` 使用字符串 `contains` 判断 `format` 字段，对带空格/换行的 JSON 不健壮
  - **修复**：改用 `parseToJsonElement` 解析后读取 `format` 字段，再决定转换路径
- **验证**：单元测试覆盖 defVars 映射、树形分组 `parentGroupId`、无 groups 字段兜底逻辑

#### 新增功能：快捷分配角色分类
- **需求来源**：参考 APK `I·TTS Server [1.26.071808]` 在试听对话框中提供了"点击为该发音人分配分类"提示，用户希望在快捷编辑面板中实现类似功能
- **涉及文件**：
  - `app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ui/widgets/SpeechRuleEditScreen.kt`
  - `app/src/main/res/values/strings.xml`
  - `app/src/main/res/values-en/strings.xml`
- **实现内容**：
  1. 在 `SpeechRuleEditScreen` 的朗读目标选择区域下方，当 `target != TAG` 时显示提示"点击为该发音人分配分类："
  2. 使用 `FilterChip` 横向排列常用角色分类：女童、男童、少女、少年、女青年、男青年、女中年、男中年、女老年、男老年、旁白
  3. 点击分类后自动查找已启用朗读规则中匹配的 tag，并切换 `target = TAG`，同时设置 `tagRuleId`、`tag`、`tagName`
  4. 新增 `findTagForCategory()` 辅助函数，按分类名匹配规则 tag（支持 "旁白" 等特殊处理）
- **验证**：`./gradlew :app:compileAppDebugKotlin` 编译通过

#### Bug 修复：`String.toJsonListString()` 处理带前导空白的 JSON 失败
- **问题**：`startsWith("[")` / `endsWith("]")` 直接在当前字符串上调用，而非 trim 后的字符串，导致带前导空白或后缀逗号的 JSON 被错误包装
- **修复**：`lib-common/src/main/java/com/github/jing332/common/utils/StringUtils.kt` 中改为对 `s` 调用 `startsWith` / `endsWith`
- **影响**：修复后插件/配置导入点击无反应的问题（旧格式 JSON 导入路径被错误包装导致解析失败）

#### 测试补充
- `JReadImportTest.kt` 新增 `realPluginBundle_parseAndConvert`：读取 `参考/` 目录下的真实 JRead 插件包文件，验证完整解析与转换链路
- **验证**：`./gradlew :lib-database:testDebugUnitTest --tests "com.github.jing332.database.jread.JReadImportTest"` 通过

---

### v1.26.060212（批量分配标签集成到批量编辑模式）

#### 功能优化：批量分配标签支持预选中 + 批量编辑入口
- **问题**：批量编辑模式下选中了条目后，需要到分组菜单中再选"批量分配标签"，操作冗余
- **优化**：
  1. `BatchTagDialog` 新增 `preselectedIds: Set<Long>` 参数，支持传入预选中条目 ID 集合，对话框打开时自动勾选
  2. 批量编辑模式顶部 AppBar 新增"批量分配标签"按钮（`Icons.AutoMirrored.Filled.Label`），选中条目后直接打开标签分配对话框
  3. 分组/子分组菜单的"批量分配标签"入口保持可用，传入当前分组全部条目作为预选中
- **图标修复**：`Icons.Default.Label` → `Icons.AutoMirrored.Filled.Label`（AutoMirrored 版本兼容性更好）

---

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

### 2026-07-19 本次会话（v1.26.071911-patch - 修复配置列表数组导入崩溃 + TTSOnline官方按分类分组）
- **当前版本**：v1.26.071911-patch（基于 `hhh4` 分支）
- **已完成事项**：
  1. 修复导入普通 TTS 配置列表（JSON 数组格式）时崩溃：`ListImportBottomSheet` 的 JRead 格式检测直接对数组调用 `jsonObject` 导致 `IllegalArgumentException`
  2. 在检测 `format` 字段前先判断 `element is JsonObject`，数组格式直接跳过 JRead 检测流程
  3. 重新生成 2852 配置列表：`TTSOnline官方` 改为按 `tags` 分类，生成 17 个独立子分组
  4. 编译验证：`./gradlew :app:compileAppDebugKotlin` 通过
  5. Release APK 构建：`./gradlew :app:assembleAppRelease` 生成 `newapk/TTS-Server-v1.26.071911-1815.apk` 和 `newapk/TTS-Server-latest.apk`
- **注意事项**：
  - 崩溃根因：生成的 2852 配置列表是 JSON 数组 `[{group,list},...]`，而 JRead 检测代码假设输入一定是 JsonObject
  - 修复后 JSON 数组配置列表可正常进入普通 TTS 配置导入路径
  - `TTSOnline官方` 按 tags 分类后，同一音色若属于多个 tag 会出现在多个分类下，导入后配置项总数会大于实际音色数

### 2026-07-19 本次会话（生成 2852 音色快导配置列表）
- **当前版本**：v1.26.071909-patch4（基于 `hhh4` 分支）
- **已完成事项**：
  1. 解析 `参考/千问全家桶2852音色_精简低内存_呱呱加油包1+火山豆包98_流式版_高风险参考重做版..json` 插件包
  2. 从插件 `code` 中提取 `UNIFIED_CLONE_VOICES_JSON`、`UNIFIED_OFFICIAL_VOICES_JSON`、`TTSON_STANDARD_VOICE_DATA_JSON`、`TTSON_ACGN_VOICE_DATA_JSON` 四类音色数据
  3. 按 `参考/jread_voice_千问全家桶2709_四大组_快导配置列表.json` 格式生成新的配置列表
  4. 顶级分组：呱呱、游音、鹿、火山豆包、2次元、官方音色、TTSOnline官方（共 7 个大组，保留多层级子分组）
  5. 呱呱/游音/鹿/2次元 复用 2709 配置中的年龄分组映射，结构与 2709 完全一致
  6. 火山豆包按插件内 `category` 字段分为 7 个年龄子组
  7. 呱呱加油包1 音色合并到呱呱组，locale 保持为 `呱呱加油包1克隆`
  8. 生成文件：`参考/jread_voice_千问全家桶2852_四大组_快导配置列表.json`（2852 条音色，2.2 MB）
- **注意事项**：
  - 配置列表中 `tagRuleId` 使用 `qianwen_unified_4pool_2852`，便于与 2709 的朗读规则区分
  - 分组 ID 以 `1783200000000` 为基准，避免与 2709 配置（`1783100000000` 起）冲突
  - 如需精简为仅四大组，可删除 `官方音色`、`TTSOnline官方` 两个顶级组

### 2026-07-19 本次会话（v1.26.071909-patch4 - JRead 导入保留父级分组 + 批量删除自动删空分组 + APK 命名优化 + 批量编辑模式合并分组）
- **当前版本**：v1.26.071909-patch4（基于 `hhh4` 分支）
- **已完成事项**：
  1. JRead 音色配置导入时先插入所有分组（包括没有直接条目的父级分组），保留完整树形结构
  2. 批量删除时，若分组被完整选中（分组下所有条目均被勾选），删除条目后自动删除该空分组（含子分组）
  3. 将「合并」入口从右上角 `⋮` 更多选项菜单迁移到批量编辑模式 AppBar
  4. 批量编辑模式下新增「合并分组」按钮，点击后识别被完整选中的分组
  5. 多选至少两个同层级分组后，弹出目标分组选择对话框
  6. 将非目标分组的所有条目移动到目标分组，清空 `categoryPath`，保持 `order` 不变
  7. 删除被合并的空分组（含子分组）
  8. APK 文件名新增构建时间戳后缀，每次构建文件名唯一
  9. 每次 Release 构建后自动将原始 APK 拷贝到 `newapk/` 目录
  10. 每次 Release 构建后额外生成 `newapk/TTS-Server-latest.apk` 副本，方便识别最新包
  11. 编译验证：`./gradlew :app:compileAppDebugKotlin` 通过
  12. Release APK 构建：`./gradlew :app:assembleAppRelease` 生成 `newapk/TTS-Server-latest.apk` 和带时间戳的原始 APK
- **注意事项**：
  - JRead 导入入口：系统 TTS 列表 → 右上角 ⋮ → 导入 → 选择 JRead 音色配置包文件
  - JRead 导入后会保留如 `火山豆包` → `女性青年通用` / `女性中年通用` 等完整父级分组
  - 批量删除入口：系统 TTS 列表 → 右上角批量编辑按钮 → 勾选分组或条目 → 点击删除按钮
  - 仅当被完整选中的分组才会在删除后自动移除；部分选中导致变空的分组不会被删除
  - 合并分组入口：系统 TTS 列表 → 右上角批量编辑按钮 → 选择至少两个分组 → 点击合并分组按钮
  - 合并分组必须完整选中分组（分组 Header 的 Checkbox 全选该分组下所有条目）
  - 合并分组选中的分组必须在同一层级
  - 合并后条目的 `order` 不变，列表排序保持不变
  - 最新 APK 直接看 `newapk/TTS-Server-latest.apk`
  - 之前的 JRead 导入、分组修复、导入点击无响应等问题已在 v1.26.071909 中修复

### 2026-07-19 本次会话（v1.26.071908 - 新增 JRead 插件包/音色配置包导入支持 + 导入问题修复）
- **当前版本**：v1.26.071908（基于 `hhh4` 分支）
- **已完成事项**：
  1. 新增 JRead 格式数据模型（`JReadPluginBundle`、`JReadVoiceConfigBundle`）与转换器
  2. `PluginImportBottomSheet` 支持导入 `jread_voice_plugin_bundle`（插件代码包）
  3. `ListImportBottomSheet` 支持导入 `jread_voice_config_bundle`（音色配置包）
  4. `ConfigImportBottomSheet` 新增 `autoWrapJsonList` 参数，关闭自动 `[ ]` 包装以兼容对象格式 JSON
  5. 新增单元测试 `JReadImportTest`，覆盖插件包和配置包转换逻辑
  6. 编译验证：`./gradlew :app:compileAppDebugKotlin` 通过
  7. 单元测试验证：`./gradlew :lib-database:testDebugUnitTest --tests "com.github.jing332.database.jread.JReadImportTest"` 通过
  8. Release APK 构建：`./gradlew :app:assembleAppRelease` 生成 `newapk/TTS-Server-v1.26.071909.apk`
  9. **修复插件导入后无法识别**：补全 `defVars`、`userVars`、`enabled` 字段映射
  10. **修复列表导入分组不准确**：按 `groups` 字段构建主/子/孙分组树形结构，正确设置 `parentGroupId`
  11. **修复插件导入点击无响应**：改用 `parseToJsonElement` 读取 `format` 字段，避免字符串匹配失败
  12. **修复 `toJsonListString()` bug**：带前导空白的 JSON 被错误包装导致解析失败
  13. **新增快捷分配角色分类**：在 `SpeechRuleEditScreen` 中提供分类 Chip，一键设置朗读规则标签
  14. **反编译参考 APK**：分析 `I·TTS Server [1.26.071808]`，提取可借鉴功能（混元太极代理、超时看门狗、重试追加字符、分类分配提示）
- **注意事项**：
  - JRead 插件代码（`code` 字段）理论上与 TTS Server 插件引擎兼容，但实际运行时仍需用户自行验证网络/鉴权等逻辑
  - 音色配置包导入后按 `groupName/subGroupName/thirdGroupName` 自动分组，`categoryPath` 保留子分组路径
  - `allowMainThreadQueries` 暂时保留
  - `SystemTtsService` 的 `runBlocking` **已恢复**
  - 编译环境需使用 Android Studio 自带 JDK 21

### 2026-06-02 本次会话（v1.26.060212 - 批量分配标签集成 + 批量编辑 Bug 修复）
- **当前版本**：v1.26.060212（基于 `hhh4` 分支）
- **已完成事项**：
  1. **批量分配标签集成到批量编辑模式**：
     - `BatchTagDialog`：新增 `preselectedIds: Set<Long>` 参数，支持传入预选中条目 ID 集合
     - `ListManagerScreen.kt`：批量编辑模式顶部 AppBar 新增"批量分配标签"按钮（`Icons.AutoMirrored.Filled.Label`）
     - 选中条目后点击按钮，自动将选中条目传入 `BatchTagDialog` 作为预选中
     - 分组/子分组菜单的"批量分配标签"入口保持可用，传入当前分组全部条目作为预选中
  2. **修复批量操作无法遍历子分组条目**：
     - `ListManagerScreen.kt`：批量删除和批量切换插件对话框中，`models.flatMap { it.list }` → `models.flatMap { it.allTts() }`
  3. **修复子分组批量选择 Checkbox 缺失**：
     - `ListManagerScreen.kt`：`FlattenedCategoryItem.SubGroupHeader` 渲染处增加 `isBatchEditMode` 分支，显示 TriStateCheckbox
  4. **修复新增分组无法被选为降级目标**：
     - `ListManagerScreen.kt`：`addGroupDialog` 取消时重置 `addGroupParentId = 0L`
     - `moveGroupDialog` 候选列表直接基于 `models` 计算（不用 remember/LaunchedEffect）
  5. **修复分组导出功能**：
     - `ListManagerScreen.kt`：`Group` 组件 `onExport` 从 `{}` 改为 `showGroupExportSheet = listOf(node)`
  6. **构建 Release APK**：`newapk/TTS-Server-v1.26.060212.apk`
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
