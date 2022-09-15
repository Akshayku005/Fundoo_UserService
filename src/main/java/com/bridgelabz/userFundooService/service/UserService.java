package com.bridgelabz.userFundooService.service;

import com.bridgelabz.userFundooService.Util.EmailSenderService;
import com.bridgelabz.userFundooService.Util.TokenUtility;
import com.bridgelabz.userFundooService.dto.AdminLoginDTO;
import com.bridgelabz.userFundooService.dto.ResponseDTO;
import com.bridgelabz.userFundooService.dto.UserDTO;
import com.bridgelabz.userFundooService.exception.AdminException;
import com.bridgelabz.userFundooService.model.User;
import com.bridgelabz.userFundooService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private EmailSenderService mailSender;
    @Autowired
    private TokenUtility util;

    @Override
    public String addUser(UserDTO userDto) {
        User newUser = new User(userDto);
        Optional<User> userEmail = repository.findByEmail(newUser.getEmail());
        if (userEmail.isPresent()) {
            throw new AdminException(HttpStatus.BAD_REQUEST, "Email already exists, Please enter other email!!");
        } else {
            repository.save(newUser);
            String token = util.createToken((int) newUser.getId());
            mailSender.sendEmail(newUser.getEmail(), "Test Email", "Registered SuccessFully, hii: "
                    + newUser.getName() + " Please Click here to get data-> "
                    + "http://localhost:8081/user/findById/" + token);
            return token;
        }
    }

    @Override
    public User updateUser(UserDTO userDto, Long id, String token) {
        Long decode = util.decodeToken(token);
        if (id.equals(decode)) {
            Optional<User> updateUser = repository.findById(decode);
            if (updateUser.isPresent()) {
                updateUser.get().setName(userDto.getName());
                updateUser.get().setEmail(userDto.getEmail());
                updateUser.get().setPassword(userDto.getPassword());
                updateUser.get().setUpdatedAt(userDto.getUpdatedAt().now());
                updateUser.get().setDob(userDto.getDob());
                updateUser.get().setPhoneNo(userDto.getPhoneNo());
                repository.save(updateUser.get());
                mailSender.sendEmail(updateUser.get().getEmail(), "Test Email", "User updated Successfully, hii: "
                        + updateUser.get().getName() + " Please Click here to get data-> "
                        + "http://localhost:8081/user/findById/" + token);
                return updateUser.get();
            } else
                throw new AdminException(HttpStatus.NOT_FOUND, "User Details using id not found");
        } else
            throw new AdminException(HttpStatus.NOT_FOUND, "Id and token mismatch !!");
    }


    @Override
    public List<User> getAllUsers(String token) {
        Long decode = util.decodeToken(token);
        Optional<User> user = repository.findById(decode);
        if (user.isPresent()) {
            List<User> getUsers = repository.findAll();
            if (getUsers.isEmpty()) {
                throw new AdminException(HttpStatus.NOT_FOUND, "There is no User added yet");
            } else return getUsers;
        } else throw new AdminException(HttpStatus.NOT_FOUND, "data is not found on this token");
    }

    @Override
    public Optional<User> getUserById(String token) {
        Long decode = util.decodeToken(token);
        Optional<User> user = repository.findById(decode);
        if (user.isPresent()) {
            return repository.findById(decode);
        }
        throw new AdminException(HttpStatus.NOT_FOUND, "Record for provided userId is not found");
    }


    @Override
    public ResponseDTO login(AdminLoginDTO adminLoginDTO) {
        Optional<User> userData = repository.findByEmail(adminLoginDTO.getEmail());
        if (userData.isPresent()) {
            if (userData.get().getPassword().equals(adminLoginDTO.getPassword())) {
                String token = util.createToken((int) userData.get().getId());
                mailSender.sendEmail(userData.get().getEmail(), "Test Email", "Login successfull , hii: "
                        + userData.get().getName() + " Welcome back !!");
                return new ResponseDTO("User login succesfull", token);
            }
            throw new AdminException(HttpStatus.NOT_ACCEPTABLE, "Invalid credentials, password is incorrect !!");
        }
        throw new AdminException(HttpStatus.NOT_FOUND, "invalid emailId");
    }

    @Override
    public ResponseDTO resetPassword(String emailId) {
        Optional<User> userData = repository.findByEmail(emailId);
        if (userData.isPresent()) {
            String token = util.createToken((int) userData.get().getId());
            String url = "http://localhost:8081/admin/resetpassword " + token;
            String subject = "reset password Successfully";
            mailSender.sendEmail(userData.get().getEmail(), subject, url);
            return new ResponseDTO("Reset password successfully completed ", token);
        }
        throw new AdminException(HttpStatus.NOT_FOUND, "EmailNOtFound");
    }


    @Override
    public User changePassword(String token, String password) {
        Long decode = util.decodeToken(token);
        Optional<User> userData = repository.findById(decode);
        if (userData.isPresent()) {
            userData.get().setPassword(password);
            repository.save(userData.get());
            String body = "Password changed successfully with userId" + userData.get().getId();
            String subject = "Password changed Successfully";
            mailSender.sendEmail(userData.get().getEmail(), subject, body);
            return userData.get();
        }
        throw new AdminException(HttpStatus.NOT_FOUND, "Token not find");
    }


    @Override
    public ResponseDTO deleteUser(Long id, String token) {
        Long decode = util.decodeToken(token);
        Optional<User> userData = repository.findById(decode);
        if (userData.isPresent()) {
            Optional<User> user = repository.findById(id);
            if (user.isPresent()) {
                user.get().setIsActive(false);
                user.get().setIsDeleted(true);
                repository.save(user.get());
                return new ResponseDTO("success", user.get());
            } else {
                throw new AdminException(HttpStatus.NOT_FOUND, "User not present");
            }
        }
        throw new AdminException(HttpStatus.NOT_FOUND, "Token not found");
    }


    @Override
    public Boolean validateUser(String token) {
        Long decode = util.decodeToken(token);
        Optional<User> isTokenPresent = repository.findById(decode);
        if (isTokenPresent.isPresent())
            return true;
        throw new AdminException(HttpStatus.NOT_FOUND, "Token not found");
    }


    @Override
    public ResponseDTO restoreUser(Long id, String token) {
        Long decode = util.decodeToken(token);
        Optional<User> isTokenPresent = repository.findById(decode);
        if (isTokenPresent.isPresent()) {
            Optional<User> isIdPresent = repository.findById(id);
            if (isIdPresent.isPresent()) {
                isIdPresent.get().setIsActive(true);
                isIdPresent.get().setIsDeleted(false);
                repository.save(isIdPresent.get());
                return new ResponseDTO("successfully recovered ", isIdPresent.get());
            } else {
                throw new AdminException(HttpStatus.NOT_FOUND, "User not present");
            }
        }
        throw new AdminException(HttpStatus.NOT_FOUND, "Token not found");
    }


    @Override
    public ResponseDTO permanentDelete(Long id, String token) {
        Long userId = util.decodeToken(token);
        Optional<User> isUserPresent = repository.findById(userId);
        if (isUserPresent.isPresent()) {
            Optional<User> isIdPresent = repository.findById(id);
            if (isIdPresent.isPresent()) {
                repository.delete(isIdPresent.get());
                String body = "User deleted successfully with userId" + isUserPresent.get().getId();
                String subject = "User deleted Successfull";
                mailSender.sendEmail(isUserPresent.get().getEmail(), subject, body);
                return new ResponseDTO("Permanently deleted ", isIdPresent.get());
            } else {
                throw new AdminException(HttpStatus.NOT_FOUND, "User not found");
            }
        }
        throw new AdminException(HttpStatus.NOT_FOUND, "Invalid token");
    }
    @Override
    public ResponseDTO addProfilePic(Long id, MultipartFile  profilePic) throws IOException {
        Optional<User> isIdPresent = repository.findById(id);
        if(isIdPresent.isPresent()) {
            isIdPresent.get().setProfilePic(String.valueOf(profilePic.getOriginalFilename()));
            repository.save(isIdPresent.get());
            return new ResponseDTO("Success",  isIdPresent.get());
        }
        throw new AdminException(HttpStatus.NOT_FOUND, "User not found");
    }
}

