package com.walking.carpractice.exception;

public class ApplicationException extends RuntimeException{
    private final ErrorCode code;

    public ApplicationException(ErrorCode code){
        this.code=code;
    }

    public ApplicationException(ErrorCode code, Throwable cause) {
        super(cause);

        this.code = code;
    }

    public ErrorCode getErrorCode(){
        return code;
    }
}
