package com.example.demo.system.response;

public class ResponseFormat {
    public ResponseFormat() {
    }

    public static GenericResponse error(int status, String message) {
        return new GenericResponse(status, message);
    }

    public static GenericResponse error(int status, String message, Object data) {
        return new GenericResponse(status, message, data);
    }

    public static GenericResponse success() {
        return new GenericResponse(0, "操作成功");
    }

    public static GenericResponse success(Object data) {
        return new GenericResponse(0, "操作成功", data);
    }

    public static GenericResponse success(Object data,Long size) {
        return new GenericResponse(0, size, data);
    }

}
