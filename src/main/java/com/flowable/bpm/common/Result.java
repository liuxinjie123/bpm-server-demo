package com.flowable.bpm.common;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonInclude
public class Result<T> implements Serializable {
    private int code;
    private String msg;
    private String data;

    public static Result success(){
        return new Result().setCode(1).setMsg("成功");
    }

    public static Result success(String msg){
        return new Result().setCode(1).setMsg(msg);
    }

    public static Result error(String err){
        return new Result().setCode(0).setMsg(err);
    }

    public static Result setData(Object data) {
        if (null == data) {
            return success();
        } else {
            return new Result(1, "成功", data.toString());
        }
    }
}
