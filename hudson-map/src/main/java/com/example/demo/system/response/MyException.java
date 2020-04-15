
package com.example.demo.system.response;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyException extends RuntimeException {
  private GenericResponse response;

  public MyException(GenericResponse response) {
    log.info("MyException:" + JSON.toJSONString(response));
    this.response = response;
  }

  public GenericResponse getResponse() {
    return this.response;
  }
}
