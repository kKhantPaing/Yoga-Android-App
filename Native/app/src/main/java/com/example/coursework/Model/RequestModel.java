package com.example.coursework.Model;

public class RequestModel {

    // For api request
    private boolean isStoredProcedure;
    private String queryString;
    private String procedureName;
    private Object parameters;

    // Constructor
    public RequestModel() {
    }

    // Getters and Setters
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

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
    }
}
