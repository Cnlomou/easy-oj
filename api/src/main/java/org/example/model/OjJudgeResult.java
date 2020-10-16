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
@Table(name = "oj_judge_result")
public class OjJudgeResult {
    @Id
    @Column(name = "judge_result_id")
    private Integer judgeResultId;

    /**
     * 结果标志
     */
    @Column(name = "judge_result_name")
    private String judgeResultName;

    /**
     * 结果信息
     */
    @Column(name = "judge_result_description")
    private String judgeResultDescription;
}