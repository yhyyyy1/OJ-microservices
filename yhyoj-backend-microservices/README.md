# 从yhyoj-backend中同步代码和依赖
common 和 model 引入到其余各个模块
## common
annotation——权限校验
common——通用文件
constant——常量
exception——全局异常
utils——工具类
## model
model——模块类

## service-client
公用接口模块：只存放接口(service)，不存放实现

## user-service & judge-service & question-service
直接copy之前的就行
为了处理服务的内部调用（经过划分之后代码分到不同的包中，找不到对应的Bean），
使用OpenFeign组件实现跨服务的远程调用（Http调用客户端，提供更方便的方式远程调用其他服务——Nacos）


## gateway