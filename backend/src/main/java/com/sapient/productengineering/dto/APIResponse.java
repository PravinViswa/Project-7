package com.sapient.productengineering.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T>{
    private int status;
    private String message;
    private T data;

    public static <T> APIResponse<T> success(T data, String message, int status){
        return new APIResponse<>(status, message, data);
    }

    public static <T> APIResponse<T> error(String message,int status){
        return new APIResponse<>(status,message,null);
    }
}
