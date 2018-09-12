package com.mmc.assist.result;

import lombok.Data;

@Data
public class Result {

    private String code;

    private String msg;

    private boolean success;

    private Data data;

    @lombok.Data
    static class Data{
        private Integer totalPageSize;

        private Object inner;

        public Data(){}
        public Data(Object inner,Integer totalPageSize){
            this.inner=inner;
            this.totalPageSize=totalPageSize;
        }
    }
}
