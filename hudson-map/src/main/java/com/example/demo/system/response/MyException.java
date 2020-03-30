
package com.example.demo.system.response;

public class MyException extends RuntimeException {
    private GenericResponse response;

    public MyException(GenericResponse response) {
        this.response = response;
    }

    public GenericResponse getResponse() {
        return this.response;
    }
}
