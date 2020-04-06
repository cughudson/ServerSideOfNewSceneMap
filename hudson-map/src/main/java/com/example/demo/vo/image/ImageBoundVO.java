package com.example.demo.vo.image;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ImageBoundVO {
    @ApiModelProperty("用户id")
    private Long userId;//用户id

    @ApiModelProperty(required = true)
    private Bounds  bounds;

    @ApiModelProperty(value = "返回的数量",required = true)
    private Integer number;

    @ApiModelProperty(value = "del 状态")
    private Boolean del;
    @Data
    public static class Bounds{
        @ApiModelProperty(value = "ws",required = true)
        private WS ws;
        @ApiModelProperty(value = "en",required = true)
        private WS en;

    }
    @Data
    public static  class WS{
        @ApiModelProperty(value="lng",required = true)
        private String lng;
        @ApiModelProperty(value="lat",required = true)
        private String lat;
    }
}
