package com.example.demo.system.entity;

import java.io.Serializable;
import lombok.*;
import javax.persistence.Id;
/**
 * (SystemConfig)实体类
 *
 * @author zhangxg
 * @since 2020-03-13 20:26:08
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SystemConfig implements Serializable {
    private static final long serialVersionUID = -71804060076182978L;
    
    @Id
    private Integer id;
    
    private String code;
    
    private String value;
    
    private String remark;
    
    private Boolean onlyRead;
}