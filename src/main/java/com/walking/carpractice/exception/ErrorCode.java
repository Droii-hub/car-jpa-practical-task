package com.walking.carpractice.exception;

public enum ErrorCode {
    NOT_FOUND(100, "Object not found", 404),
    WRONG_REQUEST(400, "Server can not handle this request", 400),
    EMPTY_REQUEST(401, "Request has empty body, please send required data",400),
    WRONG_PASSWORD(402, "Password does not match",400),
    DELETING_OWNERS_CAR(403, "This car owned some user and can not be deleted", 400),
    CAR_NOT_FOUND(101, "Car not found", 404),
    OWNER_NOT_FOUND(102,"This email not found", 404),
    DUPLICATE(200, "Object has duplicate", 500),
    DUPLICATE_OWNER(201, "This email already in use", 400),
    CONSTRAINT_FAIL(300, "Check constraint failed", 400),
    WRONG_EMAIL_FORMAT(301, "Wrong email format", 400),
    TRANSACTION_ERROR(500, "Error during transaction",500),
    SERIALIZATION_ERROR(501, "Serialization error", 500),
    // Коды для иных видов ошибки
    UNKNOWN(900, "Unknown error", 500);

    private final int internalCode;
    private final String message;
    private final int httpCode;

    ErrorCode(int internalCode, String message, int httpCode){
        this.internalCode=internalCode;
        this.message=message;
        this.httpCode=httpCode;
    }

    public int getInternalCode() {
        return internalCode;
    }

    public String getMessage() {
        return message;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
