package org.example.web.controller;

import com.github.pagehelper.PageInfo;
import org.example.model.OjLanguages;
import org.example.model.OjProblems;
import org.example.web.service.OjLanguagesService;
import org.example.web.service.OjProblemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Cnlomou
 * @create 2020/8/12 0:03
 */
@Controller
public class ViewController {

    @Autowired
    private OjProblemsService ojProblemsService;

    @GetMapping("/edit/{id}")
    public  String editView(@PathVariable(name = "id")Integer quesId, Model model){
        OjProblems problem = ojProblemsService.findProblem(quesId);
        if(problem==null)
            return "404";
        model.addAttribute("title",problem.getProblemName())
                .addAttribute("des",problem.getProblemDescription())
                .addAttribute("inp",problem.getProblemInputFormat())
                .addAttribute("outp",problem.getProblemOutputFormat())
                .addAttribute("inps",problem.getProblemInputSample())
                .addAttribute("outps",problem.getProblemOutputSample())
                .addAttribute("proId",quesId);
        return "index";
    }

    @GetMapping("/list/{pageNo}")
    public String listView(@PathVariable(name = "pageNo")Integer pageNo, Model model){
        PageInfo<OjProblems> problems = ojProblemsService.findProblems(pageNo, 15);
        model.addAttribute("page",problems);
        return "list";
    }
}
