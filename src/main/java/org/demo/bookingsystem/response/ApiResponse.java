package org.demo.bookingsystem.response;

/**
 * Author: sthk
 * Created AT: June 7, 2025
 * <p>
 * A generic response class to represent the structure of API responses.
 * It includes status code, message, and data (of generic type T).
 */
public class ApiResponse<T> {

    private int statusCode;
    private String message;
    private T data;

    /**
     * Constructor to initialize ApiResponse.
     *
     * @param statusCode HTTP status code
     * @param message    Message describing the response
     * @param data       The actual data returned in the response
     */
    public ApiResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    /**
     * Gets the status code of the response.
     *
     * @return the HTTP status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the status code of the response.
     *
     * @param statusCode the HTTP status code
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets the message of the response.
     *
     * @return the message describing the response
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of the response.
     *
     * @param message the message describing the response
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the data of the response.
     *
     * @param <T> the type of the data
     * @return the data returned in the response
     */
    public T getData() {
        return data;
    }

    /**
     * Sets the data of the response.
     *
     * @param data the data to be returned in the response
     */
    public void setData(T data) {
        this.data = data;
    }
}
