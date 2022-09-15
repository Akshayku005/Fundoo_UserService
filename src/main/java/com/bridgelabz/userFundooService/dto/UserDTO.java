package com.bridgelabz.userFundooService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    @Pattern(regexp = "^[A-Z]{1}[a-zA-Z\\s]{2,}$", message = "User Name is Not valid")
    private String name;
    @Email(message = "Insert valid email")
    private String email;
    @NotEmpty(message = "Enter your password")
    private String password;
    private LocalDateTime createdAt= LocalDateTime.now();
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private Boolean isDeleted;
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "date of birth must be in DD-MM-YYYY format")
    private String dob;
    @Pattern(regexp = "[8 9][0-9]{9}", message = "Invalid number, phone number should contain 10 digits")
    private String phoneNo;
}
