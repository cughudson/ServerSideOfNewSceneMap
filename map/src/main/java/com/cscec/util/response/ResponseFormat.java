package com.cscec.util.response;


/**
 * 统一返回客户端数据格式
 * @author sxd
 * @since 2018/4/1
 */
public class ResponseFormat {

//    public static GenericResponse retParam(Integer status,Object data) {
//        GenericResponse json = new GenericResponse(status, messageMap.get(status), data);
//        return json;
//    }
//    public static GenericResponse retParam(Integer status) {
//        GenericResponse json = new GenericResponse(status, messageMap.get(status), null);
//        return json;
//    }
//	public static GenericResponse retParam(JSONObject imgResult) {
//		Integer status=imgResult.getInteger("code");
//		return new GenericResponse(status, messageMap.get(status), imgResult.get(Constant.message));
//	}

	public static GenericResponse error(int status,String message){
		return new GenericResponse(status, message);
	}
	public static GenericResponse error(int status,String message,Object data){
		return new GenericResponse(status, message,data);
	}
	public static GenericResponse success() {
		return new GenericResponse(ErrorCode.SUCCESS, "操作成功");
	}
	public static GenericResponse success(Object data) {
		return new GenericResponse(ErrorCode.SUCCESS, "操作成功",data);
	}
	public static GenericResponse success(Object data,String message) {
		return new GenericResponse(ErrorCode.SUCCESS, message,data);
	}
}