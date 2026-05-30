package com.mysql.DEMO_MYSQL.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
     private String id;
     private  String userName;
     private  String passWord;
     private  String firstName;
     private String lastname;
     private LocalDate dob;

 public String getId() {
  return id;
 }

 public void setId(String id) {
  this.id = id;
 }

 public String getUserName() {
  return userName;
 }

 public void setUserName(String userName) {
  this.userName = userName;
 }

 public String getPassWord() {
  return passWord;
 }

 public void setPassWord(String passWord) {
  this.passWord = passWord;
 }

 public String getFirstName() {
  return firstName;
 }

 public void setFirstName(String firstName) {
  this.firstName = firstName;
 }

 public String getLastname() {
  return lastname;
 }

 public void setLastname(String lastname) {
  this.lastname = lastname;
 }

 public LocalDate getDob() {
  return dob;
 }

 public void setDob(LocalDate dob) {
  this.dob = dob;
 }
}
