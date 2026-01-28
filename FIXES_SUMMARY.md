# 修复总结

## 构建信息
- **构建时间**: 2026-01-28 19:50
- **版本**: TTS-Server-v1.26.012819
- **构建类型**: Dev 版本（Debug 和 Release）

## 修复的问题

### 1. ✓ 修复了在标签和插件字段输入时闪退的问题
**问题原因**: 
在 `QuickEditBottomSheet.kt` 中，`LocalSaveCallBack` 只在 `ui.showSpeechEdit` 为 true 时才被提供，但是 `SpeechRuleEditScreen` 中的 `SaveActionHandler` 总是尝试使用它，导致在某些情况下崩溃。

**修复方案**: 
将 `CompositionLocalProvider` 包裹整个内容区域，确保 `LocalSaveCallBack` 始终可用。

**修改文件**: 
- `/workspace/app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ui/widgets/QuickEditBottomSheet.kt`

### 2. ✓ 修复了插件显示英文代号而不是插件名称的问题
**问题原因**: 
`PluginDescriptor.kt` 中使用 `getEnabled()` 方法只查找启用的插件，当插件被禁用时就会显示插件ID（英文代号）。

**修复方案**: 
改用 `getByPluginId()` 方法查找插件，无论插件是否启用都能正确显示名称。

**修改文件**: 
- `/workspace/app/src/main/java/com/github/jing332/tts_server_android/compose/systts/list/ui/PluginDescriptor.kt`

### 3. ✓ 修复了编辑插件时闪退的问题
**问题原因**: 
`PluginEditorScreen.kt` 中存在两个重复的 `LaunchedEffect` 块，导致插件初始化两次，可能引发竞争条件和崩溃。

**修复方案**: 
1. 删除重复的 `LaunchedEffect` 块
2. 将 `rememberSaveable` 改为 `remember` 避免插件对象序列化问题

**修改文件**: 
- `/workspace/app/src/main/java/com/github/jing332/tts_server_android/compose/systts/plugin/PluginEditorScreen.kt`

## 生成的 APK 文件

### Debug 版本（推荐测试使用）
- **文件**: `/workspace/app/build/outputs/apk/dev/debug/TTS-Server-v1.26.012819_debug.apk`
- **大小**: 43MB
- **特点**: 包含调试信息，日志详细，适合测试和问题排查

### Release 版本
- **文件**: `/workspace/app/build/outputs/apk/dev/release/TTS-Server-v1.26.012819.apk`
- **大小**: 17MB
- **特点**: 体积小，性能优化，适合日常使用

## 测试建议

1. **测试标签输入**: 在快速编辑界面中，尝试在标签字段输入内容，确认不会闪退
2. **测试插件选择**: 创建或编辑插件TTS配置时，确认插件下拉框显示插件名称而不是英文代号
3. **测试插件管理**: 进入插件管理，点击编辑插件，确认不会闪退
4. **测试插件变量设置**: 编辑插件后，尝试设置插件变量，确认功能正常

## 注意事项
- 这是开发版本，建议先备份重要数据
- 如果发现问题，请提供详细的复现步骤和日志信息
- Debug 版本包含详细的日志输出，可以通过日志猫查看具体错误信息
