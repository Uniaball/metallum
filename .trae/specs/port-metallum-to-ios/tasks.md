# Tasks

- [x] Task 1: 修改 MetallumNative.swift — 平台条件编译适配
  - [x] 使用 `#if os(macOS)` / `#if os(iOS)` 条件编译分隔 macOS 和 iOS 实现
  - [x] macOS 分支保留现有 `import AppKit` + NSWindow/NSView 逻辑
  - [x] iOS 分支使用 `import UIKit`，将 `metallum_NSWindow_backingScaleFactor` 替换为 `UIScreen.main.scale` 实现
  - [x] iOS 分支将 `metallum_NSView_setMetalLayer` 和 `metallum_NSView_clearLayer` 改为空实现（stub），由 PojavLauncher 管理 surface
  - [x] 确保所有 Metal API 调用（Device/CommandQueue/Buffer/Texture 等）在两个平台上一致可用

- [x] Task 2: 修改 MetalNativeBridge.java — 平台感知的 native 库加载
  - [x] 新增 iOS 平台检测逻辑（通过 `System.getProperty("os.name")` 或 `pojav` 标识）
  - [x] 根据平台选择 `/natives/macos/` 或 `/natives/ios/` 资源路径
  - [x] 保持 macOS 加载逻辑不变

- [x] Task 3: 修改 MetalBackend.java — iOS 渲染表面创建
  - [x] 新增 iOS 平台检测
  - [x] iOS 分支跳过 `GLFWNativeCocoa.glfwGetCocoaWindow()` 和 `glfwGetCocoaView()` 调用
  - [x] iOS 分支直接创建 CAMetalLayer（调用 `metallum_create_metal_layer`），不附加到 NSView
  - [x] macOS 分支保持现有逻辑不变

- [x] Task 4: 修改 MetalSurface.java — iOS CAMetalLayer 配置适配
  - [x] 确认 `displaySyncEnabled` 在 iOS 上行为正确
  - [x] 调整 `metallum_configure_layer` 的 iOS 调用参数

- [x] Task 5: 修改 build.gradle — 新增 iOS native 编译 task
  - [x] 新增 `buildIosNative` task，使用 `swiftc` 编译 `arm64-apple-ios` 目标
  - [x] 链接 `UIKit`、`Metal`、`QuartzCore` framework
  - [x] 输出到 `src/main/resources/natives/ios/libmetallum.dylib`
  - [x] 仅在 macOS 构建环境执行，非 macOS 跳过
  - [x] 将 `buildIosNative` 挂载到 `processResources` 依赖

- [x] Task 6: 创建 iOS native 库资源目录
  - [x] 创建 `src/main/resources/natives/ios/` 目录
  - [x] 添加 `.gitkeep` 占位文件

# Task Dependencies
- Task 2 依赖 Task 1（MetalNativeBridge 需要知道 iOS native 库导出了哪些符号）
- Task 3 依赖 Task 1（MetalBackend 需要 iOS 版本的 native 函数）
- Task 4 可与 Task 3 并行
- Task 5 和 Task 6 可并行，均不依赖其他 Task