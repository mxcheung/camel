package com.maxcheung.camelsimple.beanio;


public class Trailer {
    int numberOfRecords;

    public Trailer() {
    }

    public Trailer(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }

    @Override
    public int hashCode() {
        return numberOfRecords;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            Trailer record = (Trailer) obj;
            return this.numberOfRecords == record.getNumberOfRecords();
        }
    }

    /**
     * @return the numberOfRecords
     */
    public int getNumberOfRecords() {
        return numberOfRecords;
    }

    /**
     * @param numberOfRecords the numberOfRecords to set
     */
    public void setNumberOfRecords(int numberOfRecords) {
        this.numberOfRecords = numberOfRecords;
    }
}
