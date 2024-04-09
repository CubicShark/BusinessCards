package com.example.Business.cards.models;

import com.example.Business.cards.services.WorkersService;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RequestYearsReport {

    private int[] reqsDone;

    private int sum;

    private String workerName;

    public RequestYearsReport(int[] reqsDone, int sum, String workerName) {
        this.reqsDone = reqsDone;
        this.sum = sum;
        this.workerName = workerName;
    }

    public int[] getReqsDone() {
        return reqsDone;
    }

    public void setReqsDone(int[] reqsDone) {
        this.reqsDone = reqsDone;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }
}
