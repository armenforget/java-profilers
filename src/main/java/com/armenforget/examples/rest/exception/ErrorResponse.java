package com.armenforget.examples.rest.exception;

import java.util.Date;


public class ErrorResponse {

  private Date timestamp;
  private String status;
  private String message;
  private String description;


  ErrorResponse(Date timestamp, String status, String message, String description) {
    this.timestamp = timestamp;
    this.status = status;
    this.message = message;
    this.description = description;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
