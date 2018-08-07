package com.maxcheung.camelsimple.beanio;

public class B1Record extends Record {
    String securityName;

    public B1Record() {
    }

    public B1Record(String sedol, String source, String securityName) {
        super(sedol, source);
        this.securityName = securityName;
    }

    public String getSecurityName() {
        return securityName;
    }

    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }

    @Override
    public int hashCode() {
        return securityName != null ? securityName.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            B1Record record = (B1Record) obj;
            return super.equals(record) && this.securityName.equals(record.getSecurityName());
        }
    }

    @Override
    public String toString() {
        return "SEDOL[" + this.sedol + "], SOURCE[" + this.source + "], NAME[" + this.securityName + "]";
    }
}