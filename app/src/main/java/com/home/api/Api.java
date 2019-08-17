package com.home.api;


public class Api {
    String locationName;
    String elementName;
    String startTime;
    String endTime;
    String parameterName;
    String parameterUnit;

    public Api(String startTime, String endTime, String parameterName, String parameterUnit) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.parameterName = parameterName;
        this.parameterUnit = parameterUnit;
    }

    Api(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }



    public Api() {
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterUnit() {
        return parameterUnit;
    }

    public void setParameterUnit(String parameterUnit) {
        this.parameterUnit = parameterUnit;
    }
}
