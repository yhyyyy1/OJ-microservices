package com.yhy.yhyojbackendjudgeservice.judge;

import com.yhy.yhyojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.yhy.yhyojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.yhy.yhyojbackendjudgeservice.judge.strategy.JudgeContext;
import com.yhy.yhyojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.yhy.yhyojbackendmodel.model.codesandbox.JudgeInfo;
import com.yhy.yhyojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
