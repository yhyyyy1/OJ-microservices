package com.yhy.yhyojbackendjudgeservice.judge.codesandbox.impl;


import com.yhy.yhyojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.yhy.yhyojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.yhy.yhyojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用非自己开发的代码沙箱）
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {

        System.out.println("第三方代码沙箱");
        return null;
    }
}
