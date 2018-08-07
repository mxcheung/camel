package com.maxcheung.camelsimple.beanio;

public class A1Record extends Record {
    Double currentPrice;

    public A1Record() {
    }

    public A1Record(String sedol, String source, Double currentPrice) {
        super(sedol, source);
        this.currentPrice = currentPrice;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public int hashCode() {
        return currentPrice != null ? currentPrice.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            A1Record record = (A1Record) obj;
            return super.equals(record) && this.currentPrice.doubleValue() == record.getCurrentPrice().doubleValue();
        }
    }

    @Override
    public String toString() {
        return "SEDOL[" + this.sedol + "], SOURCE[" + this.source + "], PRICE[" + this.currentPrice + "]";
    }
}