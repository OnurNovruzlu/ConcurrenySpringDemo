package com.example.springdemo.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceResponse {
    private Object response;
    private String error;

    public ServiceResponse(Object response) {
        this.response = response;
    }

    public ServiceResponse(String error) {
        this.error = error;
    }

    public ServiceResponse(Object response, String error) {
        this.response = response;
        this.error = error;
    }
}
