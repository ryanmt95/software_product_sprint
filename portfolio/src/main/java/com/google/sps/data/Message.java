
package com.google.sps.data;

public final class Message {

  private final String firstName;
  private final String lastName;
  private final String email;
  private final String mobile;
  private final String comments;

  public Message(String firstName, String lastName, String email, String mobile, String comments) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.mobile = mobile; 
    this.comments = comments;
  }
}