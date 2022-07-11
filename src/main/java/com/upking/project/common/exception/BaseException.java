package com.upking.project.common.exception;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author king
 * @version 1.0
 * @className BaseException
 * @description TODO
 * @date 2022/7/7
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -6853310712844466349L;

    private String returnCode = "";
    private String errorCode = "-1";
    private String errorMessage = "";
    private List<String> errorPropNames = new ArrayList();
    private Map<String, Object> errorProperties = new HashMap();

    public BaseException(String errorCode) {
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public BaseException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public BaseException(String errorCode, Throwable cause, String message) {
        super(cause);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public void setErrorMessage(String messages) {
        this.errorMessage = messages;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public String getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getErrorPropNames() {
        return this.errorPropNames;
    }

    public Map<String, Object> getErrorProperties() {
        return this.errorProperties;
    }

    public void putErrorProperty(String name, Object prop) {
        if (name != null) {
            this.errorPropNames.add(name);
            this.errorProperties.put(name, prop);
        }

    }

    public void putErrorProperty(Map<String, Object> errorProperties) {
        if (errorProperties != null) {
            Iterator var2 = errorProperties.entrySet().iterator();

            while(var2.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry)var2.next();
                this.putErrorProperty(entry.getKey(), entry.getValue());
            }
        }

    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder("[" + this.errorCode + "]");
        if (!StringUtils.isEmpty(this.errorMessage)) {
            sb.append("[").append(this.errorMessage).append("]");
        } else if (!StringUtils.isEmpty(super.getMessage())) {
            sb.append("[").append(super.getMessage()).append("]");
        }

        int errorPropSize = this.errorPropNames.size();
        if (errorPropSize > 0) {
            sb.append("[");

            for(int i = 0; i < errorPropSize; ++i) {
                String propName = (String)this.errorPropNames.get(i);
                Object propValue = this.errorProperties.get(propName);
                if (i == 0) {
                    sb.append(propName + "=" + propValue);
                } else {
                    sb.append(", " + propName + "=" + propValue);
                }
            }

            sb.append("]");
        }

        return sb.toString();
    }
}
