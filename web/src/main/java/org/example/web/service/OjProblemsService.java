package org.example.web.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.example.model.OjLanguages;
import org.example.model.OjProblems;

/**
 * @author Cnlomou
 * @create 2020/7/5 17:26
 */
public interface OjProblemsService{

        OjProblems findProblem(Integer quesId);

        PageInfo<OjProblems> findProblems(int pageNo, int size);

}
