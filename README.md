# EdgeEffectScrollingContainers
Android. 滚动容器 边界动画定义,仿照IOS页面弹性动画(包含收拾触发和惯性触发)

---
theme: hydrogen
---
怎么开头呢?

在Ui过程中,每一个Android开发可能都接收到如下需求:

> 这个XXX的时候,防一下IOS的某某动画

是不是,肯定是的.

IOS中页面滑动动画确实不错, 我说的是**页面边界的弹簧动画**, 其实这是一个简单的动画,出现的场景可以是任意位置

1. 如果出现在布局的某个位置,我们直接可以使用动画处理
2. 需求中基本出现的位置是滚动性容器,或者某一个页面

## 先看下效果

效果1 页面边界弹性动画

<img src="https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/35904818735748e597704f6e16a770b6~tplv-k3u1fbpfcp-jj-mark:0:0:0:0:q75.image#?w=592&h=1280&s=2482273&e=gif&f=71&b=f5f3f8" alt="output_1.gif" width="50%" />

效果2 列表边界弹性动画


<img src="https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8c96d803333a465b9a4f8c4a2aa88987~tplv-k3u1fbpfcp-jj-mark:0:0:0:0:q75.image#?w=592&h=1280&s=2677350&e=gif&f=51&b=f9f8fc" alt="out.gif" width="50%" />

## 特性描述

1. 边界状态时手指拖动跟随手指,松开动画
2. 快速滑动,惯性弹性动画

## 相关技术调研

其实就是Google找轮子,找的过程中发现,有人使用EdgeEffect 实现了RecyclerView 列表的阻尼滑动效果(就是弹簧动画) https://juejin.cn/post/7235463575300046903 ,




可以参考这个大佬的文章:https://juejin.cn/post/7235463575300046903

## 满足需求的核心技术 (读源码 很好理解)
1. 判断容器是否已经在边界
2. 处理惯性手势


RV看完这个https://juejin.cn/post/7235463575300046903 文章就搞定了,ScrollView 爬了RV的代码 修改完成,我不想写原理 有需要找我要代码

实现效果:

<img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a55e4b4a85d14c5d8dbed505f7aeb4a7~tplv-k3u1fbpfcp-jj-mark:0:0:0:0:q75.image#?w=1080&h=2400&s=7595737&e=gif&f=325&b=fbf8fc" alt="out.gif" width="50%" />

仓库地址:

https://github.com/kongxiaoan/EdgeEffectScrollingContainers





