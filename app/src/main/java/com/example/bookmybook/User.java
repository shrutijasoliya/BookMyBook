package com.example.bookmybook;

public class User {
    public String userId;
    public String fullName;
    public String mobileNumber;
    public String email;
    public String password;
    public String uriImage;
    public String collegeName;

    public User() {
    }

    public User(String fullName, String mobileNumber, String email) {
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.email = email;
    }

    public User(String userId, String fullName, String mobileNumber, String email, String password, String uriImage) {
        this.userId = userId;
        this.fullName = fullName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.password = password;
        this.uriImage = uriImage;
    }


    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getfullName() {
        return fullName;
    }

    public void setfullName(String fullName) {
        this.fullName = fullName;
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getmobileNumber() {
        return mobileNumber;
    }

    public void setmobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getpassword() {
        return password;
    }

    public void setpassword(String password) {
        this.password = password;
    }

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

}
