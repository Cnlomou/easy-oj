package org.example.web.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.example.mapper.OjJudgeResultMapper;
import org.example.web.service.OjJudgeResultService;
/**
 * @author Cnlomou
 * @create 2020/7/5 17:26
 */
@Service
public class OjJudgeResultServiceImpl implements OjJudgeResultService{

    @Resource
    private OjJudgeResultMapper ojJudgeResultMapper;

}
