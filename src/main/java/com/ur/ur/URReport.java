package com.ur.ur;

import java.util.Arrays;

public class URReport {

    private byte[] feedBack;
    private URCommand sentCommand;

    public URReport(URCommand sentCommand, byte[] feedBack) {
        this.feedBack = feedBack;
        this.sentCommand = sentCommand;
    }


    @Override
    public String toString() {
        return "URReport{" +
                "feedBack=" + Arrays.toString(feedBack) +
                ", sentCommand=" + sentCommand +
                '}';
    }

    public URReport(URCommand sentCommand) {
        this.sentCommand = sentCommand;
    }

    public void setFeedBack(byte[] feedBack) {
        this.feedBack = feedBack;
    }

    public byte[] getFeedBack() {
        return feedBack;
    }

    public URCommand getSentCommand() {
        return sentCommand;
    }

    public void setSentCommand(URCommand sentCommand) {
        this.sentCommand = sentCommand;
    }

}
