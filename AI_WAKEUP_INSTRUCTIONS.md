# AI 唤醒指令 - TTS Server Android 项目

## 快速启动（复制这段话发送给 AI）

```
请立即执行以下任务：

1. 读取并执行自动化构建脚本：
   运行 /workspace/build.sh

2. 如果构建失败，按以下步骤自动修复：
   - 检查 lib-script/build.gradle.kts 中的 J2V8 配置
   - 确保 J2V8 版本为 4.6.0 或更新版本
   - 确保 minifyEnabled 为 false
   - 执行"核弹级"清理：
     * ./gradlew --stop
     * rm -rf .gradle app/build lib-*/build
     * ./gradlew clean assembleRelease --refresh-dependencies --no-build-cache

3. 构建成功后：
   - 检查 APK 是否生成在 app/build/outputs/apk/release/
   - 将 APK 复制到 app/src/apk/ 目录
   - 删除根目录下遗留的旧 APK 文件（如果有）
   - 删除 app/src/apk/ 下旧的 APK 文件（保留最新 2 个）
   - 提交代码并推送到 J2V8 分支

不允许停止，直到构建成功并提交！
```

## 项目关键信息

### 技术选型
- **JavaScript 引擎**: J2V8 (Google V8 引擎的 Java 绑定)
  - 完全支持 ES6+ 语法
  - 原生 Android 兼容性（通过 AAR 包含原生库）
  - 稳定可靠，无兼容性问题

### 关键配置
1. **R8 混淆关闭**
   - `minifyEnabled false` - 关闭代码混淆
   - `shrinkResources false` - 关闭资源压缩
   - 确保稳定性和调试便利性

2. **依赖管理**
   - 使用 J2V8 AAR 包，无需手动管理原生库
   - 通过 Maven 仓库直接获取

### 关键配置文件

#### lib-script/build.gradle.kts (必须保持以下配置)
```kotlin
dependencies {
    // J2V8 JS 引擎（Google V8 引擎，完全支持 ES6+）
    val j2v8Version = "4.6.0"
    api("com.eclipsesource.j2v8:j2v8:$j2v8Version@aar")

    implementation(project(":lib-common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.okhttp)
    implementation(libs.logging)
    implementation(libs.slf4j.api)
    implementation(libs.logback.android)
}
```

### 构建环境

#### Docker 镜像
- **配置文件**: `.ide/Dockerfile`
- **镜像名**: `tts-server-dev`
- **基础镜像**: `eclipse-temurin:17-jdk`
- **包含**: Android SDK (API 34), Build Tools 34.0.0, NDK 25.2.9519653

#### 构建命令
```bash
# 方法 1：使用自动化脚本（推荐）
./build.sh

# 方法 2：手动构建
docker run --rm -v $(pwd):/workspace -w /workspace tts-server-dev \
    ./gradlew clean assembleRelease --refresh-dependencies
```

### 构建失败自动修复清单

| 错误类型 | 修复方法 |
|---------|---------|
| 依赖版本冲突 | 执行 `./gradlew clean --refresh-dependencies` |
| Gradle 缓存问题 | 删除 `~/.gradle/caches` 或使用 `--refresh-dependencies` |
| J2V8 原生库加载失败 | 检查 minifyEnabled 是否为 false |
| 构建超时 | 增加 Gradle 内存配置或清理缓存后重试 |

### 提交规范

构建成功后，提交信息格式：
```
feat: Build release APK with J2V8 engine [日期时间]
- J2V8: 4.6.0
- R8: Disabled
- APK: [文件名]
```

### 重要文件
- `/workspace/build.sh` - 自动化构建脚本
- `/workspace/lib-script/build.gradle.kts` - J2V8 引擎配置
- `/workspace/.ide/Dockerfile` - Docker 镜像定义
- `/workspace/.cnb.yml` - CNB 流水线配置

### APK 文件存放
- **统一存放目录**: `app/src/apk/`
- **命名规则**: `TTS-Server-J2V8-{时间戳}.apk`
- **版本保留**: 自动保留最新 2 个版本，旧版本自动清理
- **根目录**: 根目录不再存放 APK 文件，所有构建产物统一移至 `app/src/apk/`

### 注意事项
⚠️ **永远不要重新启用混淆！** 除非找到真正的 V8 兼容方案
⚠️ **构建失败必须自动修复！** 不允许询问用户，直接按清单执行修复
⚠️ **J2V8 使用原生库，首次构建可能需要较长时间下载**

### 仓库分支
- 主分支: `J2V8`
- 默认分支: `Backup`

---
## 🛑 紧急构建补丁 (Critical Build Patch) - Added on 2026-01-20

### 1. 运行环境铁律 (Environment)
* **禁止套娃**：你已在 Docker 容器内，**严禁**使用 `docker run` 或 `docker exec`。
* **正确执行**：直接在当前 Shell 运行命令 (e.g., `./gradlew ...`)。

### 2. 网络加速 (Speed Up)
* **必须配置阿里云源**：若构建卡在下载阶段，必须将 `build.gradle` 或 `~/.gradle/init.gradle` 中的仓库替换为 `maven.aliyun.com`。
* **系统源加速**：`sed -i 's/archive.ubuntu.com/mirrors.aliyun.com/g' /etc/apt/sources.list`

### 3. Android 崩溃修复方案 (Crash Fix)
**J2V8 引擎的优势：**
* 🔒 **原生兼容性**：J2V8 直接封装 V8 引擎的原生库，Android 完全兼容。
* 🔒 **ES6+ 支持**：完整支持 ES6、ES7、ES8 等现代 JavaScript 特性。
* 🔒 **混淆配置**：Release 包建议 **关闭混淆** (`minifyEnabled false`)，确保稳定性。

### 4. 标准构建命令
`./gradlew clean assembleRelease --refresh-dependencies`

### 5. 🇨🇳 极速构建仓库配置 (Fast & Robust Repositories)
**重要：在 CNB 环境下，请复制以下代码到 build.gradle 的 repositories 块中：**
```groovy
repositories {
    // 1. 阿里云极速镜像 (Aliyun - Priority High)
    maven { url "https://maven.aliyun.com/repository/public" }       // 聚合了 Central + JCenter
    maven { url "https://maven.aliyun.com/repository/google" }       // 代理 Google Maven
    maven { url "https://maven.aliyun.com/repository/gradle-plugin" }
    
    // 2. 官方源兜底 (Official Fallback)
    // 如果阿里云缺失，自动尝试官方源 (速度较慢但能保命)
    google()
    mavenCentral()
    maven { url "https://jitpack.io" }
}
```

## 🏆 最终版本与环境裁决 (FINAL VERDICT)
- **JavaScript 引擎**：使用 **J2V8** (Google V8 引擎的 Java 绑定，完全支持 ES6+)。
- **J2V8 版本**：推荐 **4.6.0** 或更高版本。
- **混淆配置**：必须保持 **minifyEnabled false** (确保稳定性和调试便利性)。
- **环境操作**：严禁使用 `docker run`，直接在 Shell 运行 `./gradlew`。
