package com.example.demo.system.mapper;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonMapper {

  @Insert("insert into login_record (user_id,username,ip,login_time,type,result) values (#{userId},#{username},#{ip},#{loginTime},#{type},#{result})")
  void insertLoginRecord(JSONObject json);

  @Insert("insert into file_record (md5,file_suffix,type,user_id,create_time) values (#{md5},#{fileSuffix},#{type},#{userId},#{createTime})")
  void insertFileRecord(JSONObject record);

  @Select("select url from head_img where enable=true order by order_no")
  List<String> selectEnableHeadImg();
}