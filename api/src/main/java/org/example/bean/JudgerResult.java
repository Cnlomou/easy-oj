package org.example.bean;

import lombok.Data;

/**
 * @author Cnlomou
 * @create 2020/7/12 20:33
 */

@Data
public class JudgerResult {
    private String error;
    private SubmissionMsg submission;
    private RuntimeResult runtimeResult;
    private int useTime;
    private int userMem;
    private String output;
    private String outputSample;
}
