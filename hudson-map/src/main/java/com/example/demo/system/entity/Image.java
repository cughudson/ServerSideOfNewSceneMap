package com.example.demo.system.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * (Image)实体类
 *
 * @author zhangxg
 * @since 2020-03-31 23:05:31
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class Image implements Serializable {
  private static final long serialVersionUID = -96526907989364121L;

  @Id
  private String id;
  @ApiModelProperty("文件名称")
  private String filename;//文件名称
  @ApiModelProperty("图片路径")
  private String url;//图片路径
  @ApiModelProperty("拍摄设备")
  private String model;//拍摄设备
  @ApiModelProperty("文件创立时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date fileCreateTime;//文件创立时间
  @ApiModelProperty("光圈")
  private String aperture;//光圈
  @ApiModelProperty("曝光时间")
  private String exposure;//曝光时间
  @ApiModelProperty("焦距")
  private String focusLen;//焦距
  @ApiModelProperty("GPS latitude")
  private String lat;//GPS latitude
  @ApiModelProperty("GPS longitude")
  private String lng;//GPS longitude
  @ApiModelProperty("GPS altitude")
  private String alt;//GPS altitude
  @ApiModelProperty("高德地图 latitude")
  private String blat;//高德地图 latitude
  @ApiModelProperty("高德地图 lngitude")
  private String blng;//高德地图 lngitude
  @ApiModelProperty("拍摄地所在省份")
  private String province;//拍摄地所在省份
  @ApiModelProperty("拍摄地所在地级市")
  private String city;//拍摄地所在地级市
  @ApiModelProperty("拍摄地所在县")
  private String county;//拍摄地所在县
  @ApiModelProperty("上传人的名字")
  private String uploader;//上传人的名字
  @ApiModelProperty("上传人的id")
  private Long userId;//上传人的id
  @ApiModelProperty("是否删除")
  @Column(name = "`delete`")
  private Boolean delete;//是否删除
  @ApiModelProperty("生成时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;//生成时间

  @ApiModelProperty("上传人")
  @Transient
  private JSONObject user;
}