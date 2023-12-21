package com.yhy.yhyojbackendjudgeservice.judge.codesandbox.impl;


import com.yhy.yhyojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.yhy.yhyojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.yhy.yhyojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.yhy.yhyojbackendmodel.model.codesandbox.JudgeInfo;
import com.yhy.yhyojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.yhy.yhyojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 实例代码沙箱（仅为了跑通业务流程）
 */
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getValue());

        // todo 现在是写死的返回情况，之后要给写活
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputsList(inputList);
        executeCodeResponse.setMessage("测试成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
