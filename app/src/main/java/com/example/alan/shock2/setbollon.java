package com.example.alan.shock2;

/**
 * Created by Alan on 2017/7/3.
 */

public class setbollon {
    //開關
    private boolean on;
    //模式 true:震動 false:亮光
    private boolean modle ;
    //有無密碼
    private boolean setting_b;
    //密碼
    private String passeord;

    public setbollon(boolean on, boolean modle, boolean setting_b) {
        this.on = on;
        this.modle = modle;
        this.setting_b = setting_b;
    }



    public String getPasseord() {
        return passeord;
    }

    public void setPasseord(String passeord) {
        this.passeord = passeord;
        if(passeord.equals("null")){
            setSetting_b(false);
        }else {
            setSetting_b(true);
        }
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isModle() {
        return modle;
    }

    public void setModle(boolean modle) {
        this.modle = modle;
    }

    public boolean isSetting_b() {
        return setting_b;
    }

    public void setSetting_b(boolean setting_b) {
        this.setting_b = setting_b;
    }


}
