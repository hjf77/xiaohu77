package com.fhs.influxdb.core.model;


public class DeleteModel extends BaseModel {

    public DeleteModel(){

    }

    public DeleteModel(String measurement){
        super(measurement);
    }

}
