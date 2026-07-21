# Checklist

- [x] MetallumNative.swift 使用 `#if os(macOS)` / `#if os(iOS)` 条件编译，macOS 部分保持原有 AppKit 逻辑不变
- [x] MetallumNative.swift 的 iOS 分支使用 `import UIKit` 替代 `import AppKit`
- [x] iOS 上 `metallum_NSWindow_backingScaleFactor` 改为返回 `UIScreen.main.scale`
- [x] iOS 上 `metallum_NSView_setMetalLayer` 和 `metallum_NSView_clearLayer` 为空实现（stub）
- [x] MetalNativeBridge.java 根据平台选择正确的 native 库资源路径
- [x] MetalBackend.java 在 iOS 上不调用 GLFWNativeCocoa API
- [x] MetalBackend.java 在 iOS 上直接创建 CAMetalLayer 而不附加到 NSView
- [x] MetalSurface.java 的 CAMetalLayer 配置在 iOS 上正确
- [x] build.gradle 包含 `buildIosNative` task，编译目标为 `arm64-apple-ios`
- [x] `src/main/resources/natives/ios/` 目录存在
- [x] macOS 构建和运行时行为与移植前完全一致（无回归）