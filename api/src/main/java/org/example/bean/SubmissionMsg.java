package org.example.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.model.OjLanguages;

/**
 * @author Cnlomou
 * @create 2020/7/11 14:09
 */
@Data
public class SubmissionMsg {
    private int submissionId;
    private int problemId;
    private OjLanguages language;
    private String code;
}
