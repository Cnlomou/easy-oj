package org.example.web.service.impl;

import org.example.model.OjLanguages;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.example.mapper.OjLanguagesMapper;
import org.example.web.service.OjLanguagesService;
/**
 * @author Cnlomou
 * @create 2020/7/5 17:26
 */
@Service
public class OjLanguagesServiceImpl implements OjLanguagesService{

    @Resource
    private OjLanguagesMapper ojLanguagesMapper;

    @Override
    public OjLanguages findLanguage(Integer id) {
        OjLanguages ojLanguages = ojLanguagesMapper.selectByPrimaryKey(id);
        return ojLanguages;
    }
}
