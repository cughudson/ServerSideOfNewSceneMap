package com.cscec.util.response;

public class MyException extends RuntimeException {
    private GenericResponse response;
    public MyException(GenericResponse response){
        super();
        this.response=response;
    }

    public GenericResponse getResponse() {
        return response;
    }
}
