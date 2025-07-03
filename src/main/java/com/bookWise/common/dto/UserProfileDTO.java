package com.bookWise.dto;

import com.bookWise.model.BookWiseUser;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileDTO {

    private int userId;
    private String userName;
    private Integer age;
    private String gender;
    private String mainLanguage;
    private String userEmail;
    private String userPhoneNumber;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String description;

    public UserProfileDTO(BookWiseUser user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.mainLanguage = user.getMainLanguage();
        this.userEmail = user.getUserEmail();
        this.userPhoneNumber = user.getUserPhoneNumber();
        this.street = user.getStreet();
        this.city = user.getCity();
        this.state = user.getState();
        this.zipCode = user.getZipCode();
        this.description = user.getDescription();
    }
}