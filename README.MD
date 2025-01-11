# 灵动通知 （For HyperOS）重构版 refactored
1. 由于忘记备份先前的代码，此次更新实现软件重构，仅实现将通知转成“灵动”的形式（需要开启应用通知权限，建议将对应软件的通知弹窗关闭）（在Xiaomi HyperOS中为系统样式的灵动额头）的功能。 
2. 取件码功能请移步轻笔记

_作者：Syutung_

_版本：v1.0.0_

## 致谢
- 感谢 [HyperOS]('https://hyperos.mi.com/')
- 感谢 [HyperCeiler]('https://github.com/ReChronoRain/HyperCeiler') 项目提供的 MiuiStringToast 调用部分的代码

## 功能
### 为了保证质量、暂未对所有的APP开启“灵动额头”显示，目前支持应用
1. 小米智能卡
2. 微信
3. 抖音
4. 电子邮箱
5. QQ
6. 微博/微博轻享版
7. 小红书
8. 学习强国
9. 知乎
10.
### 添加到任何用户的应用程序的支持， 但不完美。
有些应用程序的通知没有标题或内容，使用常用信息代替。如遇到此类应用，可在设置中关闭该应用/通知通道的通知转发。
另外，有的应用通知是常驻的，可能多次触发通知转发，可在设置中关闭该应用/通知通道的通知转发。



### 建议关闭原应用的悬浮通知
### HyperOS 似乎没有横屏的“灵动额头”
### 权限需要：
1. 后台权限、自启动、电量无限制
2. 通知读取权限、悬浮窗权限、通知权限
3. 读取应用列表权限

## Stargazers over time
[![Stargazers over time](https://starchart.cc/trueWangSyutung/Dynamic-Notification-for-HyperOS.svg?variant=adaptive)](https://starchart.cc/trueWangSyutung/Dynamic-Notification-for-HyperOS)

[![Sparkline](https://stars.medv.io/trueWangSyutung/Dynamic-Notification-for-HyperOS.svg)](https://stars.medv.io/trueWangSyutung/Dynamic-Notification-for-HyperOS)
