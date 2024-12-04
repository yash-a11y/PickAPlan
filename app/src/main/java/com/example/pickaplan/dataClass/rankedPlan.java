package com.example.pickaplan.dataClass;

import java.util.List;

public class rankedPlan {

    private int freq;
    private int operator;
    private List<planData> plans;


    public rankedPlan(int freq, List<planData> plans,int operator)
    {
        this.freq = freq;
        this.plans = plans;
        this.operator = operator;
    }

    public void setOperator(int operator) {
        this.operator = operator;
    }

    public int getOperator() {
        return operator;
    }

    public int getFreq() {
        return freq;
    }

    public List<planData> getPlans() {
        return plans;

    }

    public void setPlans(List<planData> plans) {
        this.plans = plans;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    @Override
    public String toString() {
        return "rankedPlan{" +
                "freq=" + freq +
                ", operator=" + operator +
                ", plans=" + plans +
                '}';
    }
}
