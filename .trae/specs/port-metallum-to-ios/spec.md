# Metallum iOS 移植 Spec

## Why
Metallum 目前仅支持 macOS（Apple Silicon），在 iOS 上（通过 PojavLauncher/Amethyst iOS Remastered）运行时崩溃，因为 native 库编译目标为 macOS，且使用了 AppKit（NSWindow/NSView）等 iOS 不可用的 API。需要将其移植到 iOS，使其能在 iPhone/iPad 上直接使用 Metal 渲染。

根据日志分析，当前在 iOS 上的崩溃路径为：
```
MetalNativeBridge.<clinit> → SymbolLookup.libraryLookup() 失败
→ MetalBackend.createDevice() 无法创建 Metal 设备
→ 整个游戏初始化崩溃
```

## What Changes
- **Native 库（Swift）**：新增 iOS 编译目标 (`arm64-apple-ios`)，将 AppKit 依赖替换为 UIKit 等效实现
- **Native 库加载**：支持 iOS 原生 `.dylib` 路径，适配 PojavLauncher/Amethyst 的 bundle 结构
- **MetalBackend**：移除 macOS 专用 GLFW Cocoa API 调用，改为 iOS 兼容的渲染表面获取方式
- **MetalSurface**：适配 iOS 上的 CAMetalLayer 配置差异
- **构建系统**：build.gradle 新增 iOS native 编译 task
- **资源打包**：新增 iOS native 库资源路径 `/natives/ios/`

## Impact
- Affected specs: 无（首次 iOS 移植）
- Affected code:
  - `src/main/native/MetallumNative.swift` — 核心改动，AppKit → UIKit 适配
  - `src/main/java/com/metallum/client/metal/render/bridge/MetalNativeBridge.java` — 资源路径和加载逻辑
  - `src/main/java/com/metallum/client/metal/render/MetalBackend.java` — 窗口/视图获取方式
  - `src/main/java/com/metallum/client/metal/render/MetalSurface.java` — CAMetalLayer 配置
  - `build.gradle` — 新增 iOS 编译 task
  - `src/main/resources/fabric.mod.json` — 可能更新兼容性声明

## ADDED Requirements

### Requirement: iOS Native 库编译
系统 SHALL 提供针对 iOS（arm64）的 metallum native 动态库编译能力。

#### Scenario: 在 macOS 上交叉编译 iOS native 库
- **WHEN** 开发者在 macOS 上执行 Gradle 构建
- **THEN** build.gradle 通过 `swiftc` 编译出 `arm64-apple-ios` 目标的 `libmetallum.dylib`，输出到 `src/main/resources/natives/ios/`

#### Scenario: 在非 macOS 环境构建
- **WHEN** 开发者在 Linux/Windows 上执行 Gradle 构建
- **THEN** iOS native 编译 task 跳过，不阻塞构建

### Requirement: iOS UIKit 渲染表面支持
系统 SHALL 通过 UIKit API（而非 AppKit）在 iOS 上创建和管理 CAMetalLayer。

#### Scenario: 创建 iOS Metal 渲染表面
- **WHEN** Metallum 在 iOS 上初始化
- **THEN** 使用 PojavLauncher 提供的 `egl_bridge` 或直接操作 CAMetalLayer，不依赖 NSWindow/NSView

#### Scenario: 获取 iOS 渲染目标
- **WHEN** MetalBackend.createDevice() 被调用
- **THEN** 不再调用 `GLFWNativeCocoa.glfwGetCocoaWindow()` 等 macOS 专用 API

### Requirement: iOS Native 库加载
系统 SHALL 在 iOS 上正确加载 native 动态库。

#### Scenario: 加载 iOS native 库
- **WHEN** MetalNativeBridge 静态初始化
- **THEN** 从 classpath 资源 `/natives/ios/libmetallum.dylib` 提取到临时目录，或从 PojavLauncher bundle 的 Frameworks 目录直接加载

### Requirement: iOS CAMetalLayer 配置适配
系统 SHALL 根据 iOS 平台特性调整 CAMetalLayer 配置参数。

#### Scenario: iOS 平台 CAMetalLayer 参数
- **WHEN** MetalSurface.configure() 在 iOS 上被调用
- **THEN** `displaySyncEnabled` 和 `presentsWithTransaction` 等属性使用 iOS 兼容的值

## MODIFIED Requirements

### Requirement: Native 库资源路径
**原行为**：仅支持 `/natives/macos/libmetallum.dylib`
**新行为**：MetalNativeBridge 根据运行平台自动选择 `/natives/macos/` 或 `/natives/ios/` 路径下的 native 库。平台检测通过 `System.getProperty("os.name")` 或 PojavLauncher 提供的平台标识。

#### Scenario: iOS 平台自动选择 iOS native 库
- **WHEN** 运行时检测到 iOS 平台
- **THEN** 加载 `/natives/ios/libmetallum.dylib` 而非 macOS 版本

### Requirement: MetalBackend 窗口表面获取
**原行为**：使用 `GLFWNativeCocoa.glfwGetCocoaWindow()` 和 `glfwGetCocoaView()` 获取 macOS 原生窗口和视图，然后创建 CAMetalLayer 并附加到 NSView。

**新行为**：在 iOS 上，CAMetalLayer 直接通过 PojavLauncher 的 EGL bridge 获取（`pojavCreateContext` 返回 CAMetalLayer），无需通过 NSWindow/NSView。

#### Scenario: iOS 上跳过 AppKit 窗口 API
- **WHEN** 运行平台为 iOS
- **THEN** MetalBackend.createDevice() 不调用任何 GLFWNativeCocoa 方法，直接通过 Metal API 创建 CAMetalLayer

### Requirement: MetallumNative.swift 平台适配
**原行为**：仅使用 `import AppKit`，所有窗口相关函数使用 `NSWindow`/`NSView`。

**新行为**：
- 保留 macOS 实现（`#if os(macOS)`）
- 新增 iOS 实现（`#if os(iOS)`），使用 `UIKit` 的 `UIView`/`CALayer` 替代
- 移除 `metallum_NSWindow_backingScaleFactor` 在 iOS 上的实现（iOS 使用 `UIScreen.main.scale`）
- 移除 `metallum_NSView_setMetalLayer` / `metallum_NSView_clearLayer` 在 iOS 上的实现（由 PojavLauncher 管理 surface）

## REMOVED Requirements
无移除项。