package org.example.web.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.example.mapper.OjProblemCheckpointsMapper;
import org.example.web.service.OjProblemCheckpointsService;
/**
 * @author Cnlomou
 * @create 2020/7/5 17:26
 */
@Service
public class OjProblemCheckpointsServiceImpl implements OjProblemCheckpointsService{

    @Resource
    private OjProblemCheckpointsMapper ojProblemCheckpointsMapper;

}
