# FontCraft Pro

FontCraft Pro 是一款基于 Android 原生 + Kotlin + Jetpack Compose 的图层编辑器与字体设计应用。它以“图片/视频全局主题 + 图层操作 + 工程保存”为核心，适合做轻量版海报设计、文字排版、贴纸合成和个性化主题编辑体验。

## 功能概览

- 透明磨砂玻璃默认主题
- 支持主题切换：磨砂 / 浅色 / 深色
- 支持自定义图片作为全局背景
- 支持自定义视频作为全局背景
- 支持添加文字图层
- 支持调整文字大小、描边、阴影、颜色、透明度、对齐方式
- 支持图层前后置、删除和选择
- 支持贴纸/图片图层添加
- 支持基础缩放与旋转
- 支持本地工程保存与恢复

## 技术栈

- Kotlin
- Android Native
- Jetpack Compose
- Material 3
- Hilt
- Room
- Coil
- Kotlinx Serialization

## 项目结构

```text
字效/
├── app/
│   ├── src/main/java/com/example/fontcraftpro/
│   │   ├── MainActivity.kt
│   │   ├── FontCraftApp.kt
│   │   ├── data/
│   │   │   ├── local/
│   │   │   ├── model/
│   │   │   └── repository/
│   │   ├── di/
│   │   └── ui/
│   │       ├── editor/
│   │       └── theme/
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── .gitignore
├── README.md
└── LICENSE
```

## 主题设计说明

默认主题使用“透明磨砂玻璃”风格，整体效果包括：

- 蓝绿色渐变底色
- 柔和白色遮罩层
- 模糊玻璃效果
- 低透明度覆盖层提升视觉层次

此外，应用支持将图片或视频资源设置为全局背景，并自动叠加半透明遮罩，使界面在视觉上依然保持清晰和现代感。

## 运行方式

### 1. 环境要求

- Android Studio Hedgehog 及以上版本
- JDK 17
- Android SDK 34
- Android 虚拟机或实际设备

### 2. 打开项目

1. 使用 Android Studio 打开当前目录
2. 等待 Gradle 同步完成
3. 选择一个设备或模拟器
4. 点击 Run 按钮启动应用

### 3. 常用操作

- 在顶部按钮中切换主题
- 点击“图片”或“视频”按钮选择自定义全局背景
- 在文字输入框中输入文案并添加文字
- 选中文字图层后可调整大小、颜色、阴影、透明度和对齐方式
- 点击“保存”可将工程保存到本地数据库

## 关键模块说明

### EditorScreen

主编辑器界面，负责组合：

- 顶部主题切换 bar
- 画布区域
- 文字输入与工具栏
- 图层管理区
- 贴纸选择面板

### EditorCanvas

负责展示文字图层、图片图层以及选择框，支持基础拖动、缩放和旋转交互。

### EditorViewModel

负责维护：

- 图层列表
- 选中状态
- 背景配置
- 保存工程等业务逻辑

### ThemeBackground

用于渲染全局应用背景，支持：

- 玻璃背景
- 浅色主题
- 深色主题
- 图片背景
- 视频背景

## 已知说明

当前项目已具备清晰的 Android 应用结构，并已经实现主要的主题与图层编辑基础功能。由于当前运行环境中未安装完整 Android 构建工具链，无法在本地做完整的 Gradle 编译验证；但代码结构、依赖和功能实现已按工程化方式组织。

## 版本计划

后续可继续扩展：

- 字体库选择
- 自定义图层样式模板
- 多页工程管理
- 导出为图片/视频
- 资源库与云端同步
- 更多贴纸和特效

## 仓库

- GitHub: https://github.com/shuting52/zixiao2026.git

## License

MIT License

Copyright (c) 2026 FontCraft Pro

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
