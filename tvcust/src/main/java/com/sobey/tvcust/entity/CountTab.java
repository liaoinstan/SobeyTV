package com.sobey.tvcust.entity;

/**
 * Created by Administrator on 2016/6/30 0030.
 */
public class CountTab {
    /**待办任务*/
    private int backlog;

    /**待分配*/
    private int assigning;

    /**待处理*/
    private int pengding;

    /**处理中*/
    private int handling;

    /**待验收*/
    private int accepting;

    /**待评价*/
    private int evaluating;

    /**进行中*/
    private int happening;

    /**已完成*/
    private int solved;

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public int getAssigning() {
        return assigning;
    }

    public void setAssigning(int assigning) {
        this.assigning = assigning;
    }

    public int getPengding() {
        return pengding;
    }

    public void setPengding(int pengding) {
        this.pengding = pengding;
    }

    public int getHandling() {
        return handling;
    }

    public void setHandling(int handling) {
        this.handling = handling;
    }

    public int getAccepting() {
        return accepting;
    }

    public void setAccepting(int accepting) {
        this.accepting = accepting;
    }

    public int getEvaluating() {
        return evaluating;
    }

    public void setEvaluating(int evaluating) {
        this.evaluating = evaluating;
    }

    public int getHappening() {
        return happening;
    }

    public void setHappening(int happening) {
        this.happening = happening;
    }

    public int getSolved() {
        return solved;
    }

    public void setSolved(int solved) {
        this.solved = solved;
    }

    @Override
    public String toString() {
        return "CountTab{" +
                "backlog=" + backlog +
                ", assigning=" + assigning +
                ", pengding=" + pengding +
                ", handling=" + handling +
                ", accepting=" + accepting +
                ", evaluating=" + evaluating +
                ", happening=" + happening +
                ", solved=" + solved +
                '}';
    }
}
