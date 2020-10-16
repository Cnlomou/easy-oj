package org.example.web.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.model.OjProblems;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.example.mapper.OjProblemsMapper;
import org.example.web.service.OjProblemsService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;

/**
 * @author Cnlomou
 * @create 2020/7/5 17:26
 */
@Service
public class OjProblemsServiceImpl implements OjProblemsService{

    @Resource
    private OjProblemsMapper ojProblemsMapper;

    @Override
    public OjProblems findProblem(Integer quesId) {
        return ojProblemsMapper.selectByPrimaryKey(quesId);
    }

    @Override
    public PageInfo<OjProblems> findProblems(int pageNo, int size) {
        PageHelper.startPage(pageNo,size);
        Example example = new Example(OjProblems.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("problemStatus","1");
        List<OjProblems> ojProblems = ojProblemsMapper.selectByExample(example);
        return new PageInfo<>(ojProblems);
    }
}
