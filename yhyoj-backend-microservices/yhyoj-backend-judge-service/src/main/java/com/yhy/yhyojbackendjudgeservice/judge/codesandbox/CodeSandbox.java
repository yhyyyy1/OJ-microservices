package com.yhy.yhyojbackendjudgeservice.judge.codesandbox;


import com.yhy.yhyojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.yhy.yhyojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 */
public interface CodeSandbox {
    /**
     * 执行代码
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
