package com.bridgelabz.userFundooService.service;

import com.bridgelabz.userFundooService.dto.AdminLoginDTO;
import com.bridgelabz.userFundooService.dto.ResponseDTO;
import com.bridgelabz.userFundooService.dto.UserDTO;
import com.bridgelabz.userFundooService.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    String addUser(UserDTO userDto);

    User updateUser(UserDTO userDto,Long id,String token);

    List<User> getAllUsers(String token);

    Optional<User> getUserById(String token);

    ResponseDTO login(AdminLoginDTO adminLoginDTO);

    ResponseDTO resetPassword(String emailId);

    User changePassword(String token, String password);

    ResponseDTO deleteUser(Long id, String token);

    Boolean validateUser(String token);

    ResponseDTO restoreUser(Long id, String token);

    ResponseDTO permanentDelete(Long id, String token);

    ResponseDTO addProfilePic(Long id, MultipartFile profilePic) throws IOException;

////	UserModel setprofilepic( File profilefile,Long id,String token);
//	Response uploadProfilePic(MultipartFile multipartFile, String token);

}
