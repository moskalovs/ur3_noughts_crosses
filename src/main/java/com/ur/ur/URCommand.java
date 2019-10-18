package com.ur.ur;

public class URCommand {

    private String name;
    private Integer code;
    private String type;
    private boolean hasFeedback;

    public URCommand() {
    }

    public URCommand(String name, Integer code) {
        this.name = name;
        this.code = code;
    }


    public boolean hasFeedback() {
        return hasFeedback;
    }

    @Override
    public String toString() {
        return "URCommand{" +
                "name='" + name + '\'' +
                ", code=" + code +
                ", type='" + type + '\'' +
                ", hasFeedback=" + hasFeedback +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isHasFeedback() {
        return hasFeedback;
    }

    public void setHasFeedback(boolean hasFeedback) {
        this.hasFeedback = hasFeedback;
    }
}
