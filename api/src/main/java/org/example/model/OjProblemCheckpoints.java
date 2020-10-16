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
@Table(name = "oj_problem_checkpoints")
public class OjProblemCheckpoints {
    @Id
    @Column(name = "problem_checkpoint_id")
    private Integer problemCheckpointId;

    /**
     * 题目id
     */
    @Column(name = "problem_id")
    private Integer problemId;

    /**
     *  0精准匹配，1模糊匹配
     */
    @Column(name = "problem_checkpoint_match")
    private String problemCheckpointMatch;

    /**
     * 输入数据
     */
    @Column(name = "problem_checkpoint_input")
    private String problemCheckpointInput;

    /**
     * 输出数据
     */
    @Column(name = "problem_checkpoint_output")
    private String problemCheckpointOutput;

    /**
     * 该测试点分数
     */
    @Column(name = "problem_checkpoint_score")
    private Integer problemCheckpointScore;
}