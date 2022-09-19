package com.bridgelabz.userFundooService.model;

import com.bridgelabz.userFundooService.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "admin_service")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "Name")
    private String name;
    @Column(name = "Email")
    private String email;
    @Column(name = "Password")
    private String password;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private Boolean isActive;
    private Boolean isDeleted;
    private String dob;
    @Column(name = "Phone_Number")
    private String phoneNo;
    private String profilePic;

    public User(UserDTO userDTO) {
        this.name = userDTO.getName();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
//        this.createdAt = userDTO.getCreatedAt();
//        this.updatedAt = userDTO.getUpdatedAt();
//        this.isActive = userDTO.getIsActive();
//        this.isDeleted = userDTO.getIsDeleted();
        this.dob = userDTO.getDob();
        this.password = userDTO.getPhoneNo();

    }

    public User(long id, UserDTO userDto) {
        this.id = id;
        this.name = userDto.getName();
        this.email = userDto.getEmail();
        this.password = userDto.getPassword();
//        this.createdAt = userDto.getCreatedAt();
//        this.updatedAt = userDto.getUpdatedAt();
//        this.isActive = userDto.getIsActive();
//        this.isDeleted = userDto.getIsDeleted();
        this.dob = userDto.getDob();
        this.password = userDto.getPhoneNo();
    }
}
