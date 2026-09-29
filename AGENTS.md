# AGENTS.md

## 项目简介
本项目是一个 Android 原生 + Kotlin + Jetpack Compose 的字体/字效设计工具，目标是做一个偏“字效编辑器 + 海报设计工作台”的应用。

核心能力：
- 文字图层编辑
- 主题切换（磨砂 / 浅色 / 深色 / 图片 / 视频）
- 自定义背景
- 图层管理（前后置、删除、选择）
- 本地字体导入
- 3D 立体字支持
- PS 风格文字效果
- 本地项目保存与导出

## 技术栈
- Kotlin
- Android Native
- Jetpack Compose
- Material 3
- Hilt
- Room
- Coil
- Kotlinx Serialization

## 目录约定
- app/src/main/java/com/example/fontcraftpro/ui/editor/：编辑器核心 UI
- app/src/main/java/com/example/fontcraftpro/data/model/：模型定义
- app/src/main/java/com/example/fontcraftpro/data/repository/：数据仓库
- app/src/main/java/com/example/fontcraftpro/ui/theme/：主题与全局背景
- app/src/main/java/com/example/fontcraftpro/ui/settings/：设置与信息页

## 代码规范
- 优先保留现有 Kotlin + Compose 结构，不要大规模重写。
- 新增功能尽量保持在当前模块边界内：
  - UI 逻辑放在 ui/editor/
  - 数据模型放在 data/model/
  - 状态与业务逻辑放在 ViewModel
- 尽量保留现有的命名风格：EditorScreen、EditorViewModel、ThemeBackground、TextLayer。
- 功能迭代时优先小步提交，确保每次有明确的可回滚增量。
- 不要为了“演示效果”而加入复杂无用依赖。

## 设计原则
- 用户体验应保持“简洁、现代、可视化强”。
- 主题和编辑器背景需保持阅读清晰。
- 文字效果应以“层级可控、可调整、可预览”为主。
- 本地功能优先，避免无必要的云服务依赖。

## 代理执行要求
- 每次改动都要优先阅读最相关文件，而不是全盘扫描。
- 修改前先确认根因；不要在没有依据的情况下“堆功能”。
- 如果本地编译工具链缺失，至少使用 VS Code 诊断检查并说明限制。
- 任何功能增量建议都要保持在项目当前方向内：字效工具、字体工具、主题、海报排版。
- 每完成一个可交付阶段，应执行 git 提交并推送到远程仓库。

## 常用命令
- 运行 Android 项目：在 Android Studio 中打开并启动
- 检查诊断：使用 VS Code 问题面板 / get_errors
- 提交代码：
  - git add .
  - git commit -m "feat: ..."
  - git push origin main

## 注意事项
- 不要引入账号退出等非必要功能，除非明确需求要求。
- 当前项目的设计目标是“专业字效工具”，因此要优先增强：
  1. 字体支持
  2. 文字效果
  3. 图层编辑
  4. 主题与导出
- 对于 PSD/PLP 之类的素材导入，需要优先保留“可编辑文字层”能力，而不是只做静态展示。

## 未来扩展方向
- 更强的字体管理器
- 多层文字样式模板
- 批量模板套用与一键风格库
- 穿透/浮雕/3D/路径字设计增强
- 海报导出与动效输出
- 更强的资源库和工程管理
