package org.example.bean;

/**
 * @author Cnlomou
 * @create 2020/7/11 23:12
 */
public enum RuntimeResult {
    AC("AC"),   //运行成功
    TLE("TLE"), //超时
    MLE("MLE"), //内存超出
    CE("CE"),   //编译错误
    RE("RE"),   //发生错误


    PS("PS");

    private RuntimeResult(String tag) {
        this.tag = tag;
    }

    private String tag;

    @Override
    public String toString() {
        return this.tag;
    }
}
