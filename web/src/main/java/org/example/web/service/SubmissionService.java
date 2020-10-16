package org.example.web.service;

import org.example.bean.JudgerResult;

/**
 * @author Cnlomou
 * @create 2020/8/7 15:38
 */
public interface SubmissionService {

    Integer addToMessageQueue(Integer id, String code, String type);

    void onJudgerResult(JudgerResult judgerResult);

    JudgerResult getJudgerResult(Integer subId);
}
