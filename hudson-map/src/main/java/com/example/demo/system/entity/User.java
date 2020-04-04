package com.example.demo.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * (User)实体类
 *
 * @author zhangxg
 * @since 2020-03-26 21:48:44
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 713261498142616419L;
    
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;
    
    private String username;//用户名

    private String name;

    private String password;//密码
    
    private Boolean admin;//是否为管理员
    
    private Boolean del;//是否删除
    
    private Boolean enable;//能否登陆
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;//修改时间
    
    private String extra;

    private String headImg;
}