package com.sapient.productengineering.exception;

import com.sapient.productengineering.dto.APIResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GlobalExceptionHandlerTest{
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleGeneral(){
        ResponseEntity<APIResponse<Object>> resp=handler.handleGeneral(new Exception("xx"));
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(resp.getBody()).isNotNull();
    }
}
