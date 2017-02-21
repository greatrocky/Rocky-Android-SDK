package com.luoji.pay;

/**
 * Created by lixueqing on 2017/1/7.
 */

public class BaseEvent {

    public String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int status;

    public BaseEvent(int s) {
        this.status=s;
    }

}
