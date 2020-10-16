package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Cnlomou
 * @create 2020/7/5 17:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "oj_submissions")
public class OjSubmissions {
    @Id
    @Column(name = "submission_id")
    @GeneratedValue(generator = "JDBC")//配置自动生成主键
    private Integer submissionId;

    /**
     * 题目id
     */
    @Column(name = "problem_id")
    private Integer problemId;

    /**
     * 用户id
     */
    @Column(name = "`uid`")
    private Integer uid;

    /**
     * 所用语言id
     */
    @Column(name = "language_id")
    private Integer languageId;

    /**
     * 提交时间
     */
    @Column(name = "submission_commit_time")
    private Date submissionCommitTime;

    /**
     * 运行结束时间
     */
    @Column(name = "submission_exe_time")
    private Date submissionExeTime;

    /**
     * 运行耗时
     */
    @Column(name = "submission_use_time")
    private Integer submissionUseTime;

    /**
     * 运行消耗内存（最大）
     */
    @Column(name = "submission_use_memory")
    private Integer submissionUseMemory;

    /**
     * 运行结果
     */
    @Column(name = "submission_result")
    private String submissionResult;

    /**
     * 所得分数
     */
    @Column(name = "submission_score")
    private Integer submissionScore;

    /**
     * 日志信息
     */
    @Column(name = "submission_log")
    private String submissionLog;

    /**
     * 提交的代码
     */
    @Column(name = "submission_code")
    private String submissionCode;
}