package com.yhy.yhyojbackendjudgeservice.judge.strategy;


import com.yhy.yhyojbackendmodel.model.codesandbox.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
