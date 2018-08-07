package com.maxcheung.camelsimple.beanio;


public abstract class Record {
    String sedol;
    String source;

    public Record() {
    }

    public Record(String sedol, String source) {
        this.sedol = sedol;
        this.source = source;
    }

    public String getSedol() {
        return sedol;
    }

    public void setSedol(String sedol) {
        this.sedol = sedol;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int hashCode() {
        int result = sedol != null ? sedol.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            Record record = (Record) obj;
            return sedol.equals(record.getSedol()) && source.equals(record.getSource());
        }
    }
}
