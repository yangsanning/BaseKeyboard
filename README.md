# BaseKeyboard
[![](https://jitpack.io/v/yangsanning/BaseKeyboard.svg)](https://jitpack.io/#yangsanning/BaseKeyboard)
[![API](https://img.shields.io/badge/API-19%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=19)

## 效果预览

| [StockPriceKeyboard]                      |
| ------------------------------- |
| [<img src="https://github.com/yangsanning/BaseKeyboard/blob/master/images/image1.jpg" height="512"/>][StockPriceKeyboard]   | |


## 主要文件
| 名字             | 摘要           |
| ---------------- | -------------- |
|[KeyboardManager] | 键盘管理器 |
|[BaseKeyboard] | 键盘基类 |
|[BaseKeyboardView] | 键盘控件（进行自定义绘制） |

### 1.基本用法 

#### 1.1 继承 [BaseKeyboard]

#### 1.2 初始化 [KeyboardManager]
```
keyboardManager = new KeyboardManager(this, R.layout.layout_keyboard_view);
```

#### 1.3 绑定输入框和键盘
```
keyboardManager.bind(editText, keyboard);
```

### 2.添加方法

#### 2.1 添加仓库

在项目的 `build.gradle` 文件中配置仓库地址。

```android
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

#### 2.2 添加项目依赖

在需要添加依赖的 Module 下添加以下信息，使用方式和普通的远程仓库一样。

```android
implementation 'com.github.yangsanning:BaseKeyboard:1.0.0'
```

[StockPriceKeyboard]:https://github.com/yangsanning/BaseKeyboard/blob/master/app/src/main/java/ysn/com/demo/basekeyboard/StockPriceKeyboard.java
[BaseKeyboard]:https://github.com/yangsanning/BaseKeyboard/blob/master/BaseKeyboard/src/main/java/ysn/com/basekeyboard/base/BaseKeyboard.java
[BaseKeyboardView]:https://github.com/yangsanning/BaseKeyboard/blob/master/BaseKeyboard/src/main/java/ysn/com/basekeyboard/base/BaseKeyboardView.java
[KeyboardManager]:https://github.com/yangsanning/BaseKeyboard/blob/master/BaseKeyboard/src/main/java/ysn/com/basekeyboard/KeyboardManager.java
