package org.example.web.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.example.mapper.OjSubmissionsMapper;
import org.example.web.service.OjSubmissionsService;
/**
 * @author Cnlomou
 * @create 2020/7/5 17:26
 */
@Service
public class OjSubmissionsServiceImpl implements OjSubmissionsService{

    @Resource
    private OjSubmissionsMapper ojSubmissionsMapper;

}
