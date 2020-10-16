package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Cnlomou
 * @create 2020/7/5 17:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "oj_problems")
public class OjProblems {
    @Id
    @Column(name = "problem_id")
    private Integer problemId;

    /**
     * 状态，默认为0，表示不可见
     */
    @Column(name = "problem_status")
    private String problemStatus;

    /**
     * 题目名
     */
    @Column(name = "problem_name")
    private String problemName;

    /**
     * 时间限制，ms
     */
    @Column(name = "problem_time_limit")
    private Integer problemTimeLimit;

    /**
     * 内存限制，kb
     */
    @Column(name = "problem_memory_limit")
    private Integer problemMemoryLimit;

    /**
     * 题目描述
     */
    @Column(name = "problem_description")
    private String problemDescription;

    /**
     * 输入格式
     */
    @Column(name = "problem_input_format")
    private String problemInputFormat;

    /**
     * 输出格式
     */
    @Column(name = "problem_output_format")
    private String problemOutputFormat;

    /**
     * 样式输入
     */
    @Column(name = "problem_input_sample")
    private String problemInputSample;

    /**
     * 样式输出
     */
    @Column(name = "problem_output_sample")
    private String problemOutputSample;

    /**
     * 提示
     */
    @Column(name = "problem_hint")
    private String problemHint;
}