package com.example.bookmybook;

public class Book {
    public String bookDocumentId;
    public String bookIdPerUser;
    public String bookCourse;
    public String bookDesc;
    public String bookImgUri;
    public String bookPrice;
    public String bookSem;
    public String bookSubName;
    public String bookOwnerName;
    public String bookOwnerMail;

    public Book() {

    }

    public Book(String bookDocumentId, String bookIdPerUser, String bookCourse, String bookDesc, String bookImgUri, String bookPrice, String bookSem, String bookSubName, String bookOwnerName, String bookOwnerMail) {
        this.bookDocumentId = bookDocumentId;
        this.bookIdPerUser = bookIdPerUser;
        this.bookCourse = bookCourse;
        this.bookDesc = bookDesc;
        this.bookImgUri = bookImgUri;
        this.bookPrice = bookPrice;
        this.bookSem = bookSem;
        this.bookSubName = bookSubName;
        this.bookOwnerName = bookOwnerName;
        this.bookOwnerMail = bookOwnerMail;
    }

    public String getBookCourse() {
        return bookCourse;
    }

    public void setBookCourse(String bookCourse) {
        this.bookCourse = bookCourse;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public String getBookImgUri() {
        return bookImgUri;
    }

    public void setBookImgUri(String bookImgUri) {
        this.bookImgUri = bookImgUri;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookSem() {
        return bookSem;
    }

    public void setBookSem(String bookSem) {
        this.bookSem = bookSem;
    }

    public String getBookSubName() {
        return bookSubName;
    }

    public void setBookSubName(String bookSubName) {
        this.bookSubName = bookSubName;
    }

    public String getBookOwnerName() {
        return bookOwnerName;
    }

    public void setBookOwnerName(String bookOwnerName) {
        this.bookOwnerName = bookOwnerName;
    }

    public String getBookOwnerMail() {
        return bookOwnerMail;
    }

    public void setBookOwnerMail(String bookOwnerMail) {
        this.bookOwnerMail = bookOwnerMail;
    }

    public String getBookDocumentId() {
        return bookDocumentId;
    }

    public void setBookDocumentId(String bookDocumentId) {
        this.bookDocumentId = bookDocumentId;
    }

    public String getBookIdPerUser() {
        return bookIdPerUser;
    }

    public void setBookIdPerUser(String bookIdPerUser) {
        this.bookIdPerUser = bookIdPerUser;
    }


}
