package com.example.demo.vo.image;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class IdVO {
    @ApiModelProperty("id")
    private String id;


}
