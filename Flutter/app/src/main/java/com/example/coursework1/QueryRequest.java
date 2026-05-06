package com.example.coursework1;

import java.util.Map;

public class QueryRequest {
    private boolean isStoredProcedure;
    private String queryString;
    private String procedureName;
    private Map<String, Object> parameters;

    public QueryRequest() {
    }

    public boolean isStoredProcedure() {
        return isStoredProcedure;
    }

    public void setStoredProcedure(boolean storedProcedure) {
        isStoredProcedure = storedProcedure;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
