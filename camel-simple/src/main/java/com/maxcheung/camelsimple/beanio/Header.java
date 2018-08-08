package com.maxcheung.camelsimple.beanio;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.dataformat.beanio.BeanIOHeader;

public class Header implements BeanIOHeader {

    String identifier;
    String recordType;
    Date headerDate;

    public Header() {
    }

    public Header(String identifier, Date headerDate, String recordType) {
        this.identifier = identifier;
        this.headerDate = headerDate;
        this.recordType = recordType;
    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (recordType != null ? recordType.hashCode() : 0);
        result = 31 * result + (headerDate != null ? headerDate.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            Header record = (Header) obj;
            return identifier.equals(record.getIdentifier()) && recordType.equals(record.getRecordType());
        }
    }

    @Override
    public String toString() {
        return "TYPE[" + this.recordType + "], IDENTIFIER[" + this.identifier + "]";
    }

    /**
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return the headerDate
     */
    public Date getHeaderDate() {
        return headerDate;
    }

    /**
     * @param headerDate the headerDate to set
     */
    public void setHeaderDate(Date headerDate) {
        this.headerDate = headerDate;
    }

    /**
     * @return the recordType
     */
    public String getRecordType() {
        return recordType;
    }

    /**
     * @param recordType the recordType to set
     */
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @Override
    public Map<String, Object> getHeaders() {
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put(recordType + "Date", headerDate);
        return headers;
    }
}