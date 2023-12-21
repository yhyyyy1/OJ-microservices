package com.yhy.yhyojbackendjudgeservice.judge.strategy;


import com.yhy.yhyojbackendmodel.model.codesandbox.JudgeInfo;
import com.yhy.yhyojbackendmodel.model.dto.question.JudgeCase;
import com.yhy.yhyojbackendmodel.model.entity.Question;
import com.yhy.yhyojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 判题上下文对象
 */
@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

    private Boolean isCompileError;

}
