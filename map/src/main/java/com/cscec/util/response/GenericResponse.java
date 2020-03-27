package com.cscec.util.response;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * 统一JSON返回类
 */
@Getter
@Setter
@ApiModel
public class GenericResponse implements Serializable{

    GenericResponse(int code,String message){
        this.code=code;
        this.message=message;
    }
    GenericResponse(int code,String message,Object data){
        this.code=code;
        this.message=message;
        this.data=data;
    }
	private static final long serialVersionUID = 1L;
	/**
     * 程序定义状态码
     */
	@ApiModelProperty(value = "返回编码 0成功")
    private int code;
    @ApiModelProperty(value = "必要的提示信息")
    private String message;
    @ApiModelProperty("业务数据")
    private Object data;

    /**
     * 对业务数据单独处理
     * @return
     */
    @Override
    public String toString() {
        if(Objects.isNull(this.data)){
            this.setData(new Object());
        }
        return JSON.toJSONString(this);
    }

}