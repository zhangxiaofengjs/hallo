package com.hallo.api.response;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

/**
 * 
 * @author
 * @date
 */
public class HttpResponse extends ResponseEntity<Map<String, Object>> {
    private static final String BODY_KEY_SUCCESS = "success";
    private static final String BODY_KEY_MESSAGE = "message";
    private static final String BODY_KEY_DATA = "data";

    public HttpResponse(HttpStatusCode status) {
        super(status);
    }

    public HttpResponse(HttpStatusCode status, Map<String, Object> body) {
        super(body, status);
    }

    public static HttpResponse success(Object data) {
        return create(true, "", data);
    }

    public static HttpResponse error(String message) {
        return create(false, message, null);
    }

    private static HttpResponse create(boolean success, String message, Object data) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put(BODY_KEY_SUCCESS, success);
        bodyMap.put(BODY_KEY_MESSAGE, message);
        bodyMap.put(BODY_KEY_DATA, data);
        HttpResponse response = new HttpResponse(HttpStatus.OK, bodyMap);
        return response;
    }
}
