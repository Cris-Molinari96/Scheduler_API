package com.project.scheduler.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseHttp {

    private Object dataSource;
    private List<?> userCompany;
    private String message;
    private String code;

    public ResponseHttp(String message){
        this.message = message;
    }

}
