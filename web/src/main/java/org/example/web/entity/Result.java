package org.example.web.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Cnlomou
 * @create 2020/8/7 15:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private boolean flag;
    private int code;
    private T data;
    private String message;

    public static Result error(String msg){
        return new Result<>(false,400,null,msg);
    }

    public static Result excep(String msg){
        return new Result<>(false,300,null,msg);
    }
}
