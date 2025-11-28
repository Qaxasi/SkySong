package com.mycompany.skysong.web.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String message,
        String errorCode,
        int status,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) Map<String, String> errors)  {
}
