package com.yhy.yhyojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.yhy.yhyojbackendcommon.common.ErrorCode;
import com.yhy.yhyojbackendcommon.exception.BusinessException;
import com.yhy.yhyojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.yhy.yhyojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.yhy.yhyojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.yhy.yhyojbackendjudgeservice.judge.strategy.JudgeContext;
import com.yhy.yhyojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.yhy.yhyojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.yhy.yhyojbackendmodel.model.codesandbox.JudgeInfo;
import com.yhy.yhyojbackendmodel.model.dto.question.JudgeCase;
import com.yhy.yhyojbackendmodel.model.entity.Question;
import com.yhy.yhyojbackendmodel.model.entity.QuestionSubmit;
import com.yhy.yhyojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.yhy.yhyojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.yhy.yhyojbackendserviceclient.service.QuestionFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题服务实现类
 */
@Service
@Slf4j
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //1. 传入题目的提交id，获取对应题目的Submit信息，code language等信息
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目提交信息不存在");
        }

        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目信息不存在");
        }

        //2. 判断题目的提交状态，如果不为“等待中”就不用重复执行了
        Integer status = questionSubmit.getStatus();
        if (!status.equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }

        //3. 获取到题目信息后，更改题目的判题状态为“判题中”
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题状态更新失败");
        }

        //4. 调用沙箱，获取执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        //分别获取code language inputList 三个参数
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        //对于JudgeCase，之前存储在String中，所以要进行转换（可以参照。。。之后找一下）
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputsList();

        //5. 根据沙箱返回的结果，设置题目的判题状态和信息


        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        if (executeCodeResponse.getStatus() == 3) {
            judgeContext.setIsCompileError(true);
        } else {
            judgeContext.setIsCompileError(false);
        }
        //log.info(String.valueOf(judgeContext));
        //使用judgeManager，避免因为对不同语言进行不同操作而导致不停的if... else if... 导致代码冗余的情况
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        //6. 需要修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        //失败要额外判断
        if (!judgeInfo.getMessage().equals(JudgeInfoMessageEnum.ACCEPTED.getValue())) {
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.FAILED.getValue());
        } else {
            questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        }
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题状态更新失败");
        }
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionId);
        return questionSubmitResult;
    }
}
