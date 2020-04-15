package com.example.demo.vo.image;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ImageCityVO {
  @ApiModelProperty("上传人的id")
  private Long userId;//上传人的id
  @ApiModelProperty("拍摄地所在省份")
  private String province;//拍摄地所在省份
  @ApiModelProperty("拍摄地所在地级市")
  private String city;//拍摄地所在地级市
  @ApiModelProperty("拍摄地所在县")
  private String county;//拍摄地所在县
}
