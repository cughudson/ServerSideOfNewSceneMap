
package com.example.demo.system.response;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
@Data
public class GenericResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String message;
    private Object data;
    private Long count;
    GenericResponse(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

    GenericResponse(int code, Long size, Object data) {
        this.code = code;
        this.count = size;
        this.data = data;
    }

    GenericResponse(int code, String msg, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
        if(data instanceof List){
            count=Long.valueOf(((List)getData()).size());
        }
    }

    public String toString() {
        if (Objects.isNull(this.data)) {
            this.setData(new Object());
        }
        return JSON.toJSONString(this);
    }
}