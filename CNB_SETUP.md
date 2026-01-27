# CNB云原生开发配置说明

本项目已配置CNB云原生开发环境，支持在云端构建Android应用。

## 配置文件说明

### 1. `.ide/Dockerfile`
- 基于Ubuntu 22.04构建
- 预装Java 17
- 预装Android SDK (API 34)
- 预装code-server和常用插件
- 支持中文环境

### 2. `.cnb.yml`
- 定义开发环境规格：8核CPU
- 定义启动流程：环境检查、签名检查
- 支持VSCode远程开发

## 使用方法

### 本地使用

1. **配置签名**
   在项目根目录创建 `local.properties` 文件：
   ```properties
   KEY_PATH=/workspace/release.jks
   KEY_PASSWORD=你的密钥库密码
   ALIAS_NAME=TTSServer
   ALIAS_PASSWORD=你的密钥密码
   ```

2. **启动开发环境**
   - 访问仓库分支页面
   - 点击右上角"云原生开发"按钮
   - 等待环境启动（首次构建Docker镜像约需5-10分钟）

3. **构建APK**
   进入开发环境后，在终端执行：
   ```bash
   ./gradlew assembleAppRelease
   ```

4. **下载APK**
   构建产物位于：`/workspace/app/build/outputs/apk/app/release/`

### GitHub Actions自动构建

项目已配置GitHub Actions工作流，支持自动构建：

#### 触发条件
- `test.yml`：推送到master或compose分支时
- `release.yml`：推送CHANGELOG.md到compose分支时

#### 配置GitHub Secrets
在仓库Settings > Secrets and variables > Actions中添加：

1. `ALIAS_NAME` = TTSServer
2. `ALIAS_PASSWORD` = 你的密钥密码
3. `KEY_PASSWORD` = 你的密钥库密码
4. `KEY_STORE` = Base64编码的签名文件内容

生成Base64编码：
```bash
# 在本地执行
openssl base64 < release.jks | tr -d '\r\n' > release.jks.base64.txt
# 将release.jks.base64.txt的内容复制到KEY_STORE
```

## 注意事项

1. **首次启动较慢**
   - 首次启动需要构建Docker镜像，预计需要5-10分钟
   - 后续启动会复用缓存，速度更快

2. **资源限制**
   - 开发环境配置为8核CPU（内存自动分配16GB）
   - 如需调整，可修改 `.cnb.yml` 中的 `runner.cpus` 值

3. **签名安全**
   - `local.properties` 文件不会提交到Git仓库
   - GitHub Secrets用于CI/CD环境
   - 请勿将签名信息泄露到公开仓库

4. **构建优化**
   - 使用 `--build-cache` 参数利用Gradle缓存加速构建
   - 使用 `--parallel` 参数并行构建加快速度

## 故障排除

### 签名错误
- 检查 `local.properties` 文件路径是否正确
- 确认签名文件 `release.jks` 存在于指定路径
- 验证密码和别名是否正确

### 构建超时
- 检查网络连接，Android SDK下载需要网络
- 考虑使用国内镜像源加速下载
- 增加GitHub Actions超时时间

### 环境启动失败
- 查看 `.ide/Dockerfile` 构建日志
- 检查是否有依赖安装错误
- 确认Dockerfile语法正确

## 相关文档

- [CNB云原生开发介绍](https://docs.cnb.cool/zh/workspaces/intro.html)
- [自定义开发环境](https://docs.cnb.cool/zh/workspaces/custom-dev-env.html)
- [自定义构建流水线](https://docs.cnb.cool/zh/workspaces/custom-dev-pipeline.html)
