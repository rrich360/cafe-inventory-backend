package com.backend.cafe.serviceImpl;

import com.backend.cafe.config.JwtFilter;
import com.backend.cafe.constants.CafeConstants;
import com.backend.cafe.dao.UserDao;
import com.backend.cafe.model.ChangePasswordRequest;
import com.backend.cafe.model.ForgotPasswordRequest;
import com.backend.cafe.model.User;
import com.backend.cafe.config.JWTService;
import com.backend.cafe.service.UserService;
import com.backend.cafe.utils.CafeUtils;
import com.backend.cafe.wrapper.UserWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    @Autowired
    private JWTService jwtService;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserDao userDao;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Override
    public ResponseEntity<String> register(Map<String, String> requestMap){
        log.info("Inside signup {}", requestMap);
        User user = userDao.findByUsername(requestMap.get("username"));
        try {
         if (validateRegisterMap(requestMap)) {
              if (Objects.isNull(user)) {
                 userDao.save(getUserFromMap(requestMap));
                 return CafeUtils.getResponseEntity("Successfully registered.", HttpStatus.OK);
             } else {
                 return CafeUtils.getResponseEntity("Username already exists.", HttpStatus.BAD_REQUEST);
             }
         } else {
             return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
         }
     } catch (Exception ex) {
                    ex.printStackTrace();
     }
        return CafeUtils.getResponseEntity( CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateRegisterMap(Map<String, String> requestMap){
        if (requestMap.containsKey("name") && requestMap.containsKey("mobileNumber")
                && requestMap.containsKey("username") && requestMap.containsKey("password")){
                return true;
            }
                return false;
            }

private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setMobileNumber(requestMap.get("mobileNumber"));
        user.setUsername(requestMap.get("username"));
        user.setPassword(encoder.encode(requestMap.get("password")));
        user.setStatus("enabled");
        user.setRole("user");
        if (Objects.equals(requestMap.get("username"), "superAdmin@gmail.com")){
            user.setStatus("enabled");
            user.setRole("admin");
        }
        return user;
}

    @Override
    public User signUpAsRewardsCustomer(User user) {
        log.info("Inside registerAsRewardsCustomer {}",user);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("user");
        user.setStatus("rewards");
        userDao.save(user);
        return user;
    }

    @Override
    public ResponseEntity <List<UserWrapper>> getAllUsers() {
        try {
        if (jwtFilter.isAdmin()) {
            return new ResponseEntity<>(userDao.getAllUsers(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
          }
        } catch (Exception ex) {
           ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
//                User user = userDao.findByUsername(requestMap.get("username"));
                Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
                if (optional.isPresent()){
//                    optional.setStatus(requestMap.get("status"));
                        userDao.updateStatus(requestMap.get("status"), Integer.valueOf(requestMap.get("id")));
                    return CafeUtils.getResponseEntity("User status updated successfully!", HttpStatus.OK);
                }else{
                    return  CafeUtils.getResponseEntity("User id doesn't exist", HttpStatus.OK);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @Override
//    public String verify(User user) {
//        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
//                user.getPassword()));
//        if (authentication.isAuthenticated()) {
//            return jwtService.generateToken(user.getUsername());
//        } else {
//            return "fail";
//        }
//    }

    @Override
    public ResponseEntity<String> verifyAdmin(Map<String, String> requestMap, User user){
        log.info("Inside verifyAdmin method {}", requestMap);
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("username"), requestMap.get("password"))
            );
             user = userDao.findByUsername(requestMap.get("username"));
            if (auth.isAuthenticated()) {
                if (user != null && user.getStatus().equalsIgnoreCase( "enabled")){
                return new ResponseEntity<String>("{\"token\":\"" +
                            jwtService.generateToken(user.getUsername(), user.getRole()) + "\"}",
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Please wait for admin approval." + "\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }} catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity( CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> verifyUser(Map<String, String> requestMap, User user){
        log.info("Inside verifyUser method {}", requestMap);
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestMap.get("username"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                user = userDao.findByUsername(requestMap.get("username"));
                if (user != null) {
                    return new ResponseEntity<String>("{\"token\":\"" +
                        jwtService.generateToken(user.getUsername(), user.getRole()) + "\"}",
                        HttpStatus.OK);
                }
            }
        } catch (BadCredentialsException ex) {
            // FIX: BadCredentialsException is now caught specifically before the generic Exception block.
            // This ensures Spring Security's authentication failure returns a clean 401 UNAUTHORIZED
            // instead of a misleading 500 — giving the Angular frontend a reliable status code to detect
            // and display a user-friendly "Bad credentials. Please try again." snackbar banner.
            return new ResponseEntity<String>("{\"message\":\"Bad credentials\"}", HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity( CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        // check if current username matches the username that the user enters
        if (!request.getCurrentUsername().equals(user.getUsername())){
            throw new IllegalStateException("Please enter correct username.");}
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalStateException("Please ensure the passwords you typed are matching.");}
        // update the password
        user.setPassword(encoder.encode(request.getNewPassword()));
        // save the new password
        userDao.save(user);
    }

    // Interim identity check (username + mobile number) for users who aren't logged in and
    // don't know their current password. Mobile number isn't a strong secret, so this is a
    // stopgap -- planned to be replaced with a real email-based reset (AWS SES) later in the series.
    @Override
    public ResponseEntity<String> forgotPassword(ForgotPasswordRequest request) {
        try {
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return CafeUtils.getResponseEntity("New Password and Confirm Password do not match.", HttpStatus.BAD_REQUEST);
            }
            User user = userDao.findByUsername(request.getUsername());
            if (user == null || !user.getMobileNumber().equals(request.getMobileNumber())) {
                return CafeUtils.getResponseEntity("Username and mobile number do not match our records.", HttpStatus.BAD_REQUEST);
            }
            user.setPassword(encoder.encode(request.getNewPassword()));
            userDao.save(user);
            return CafeUtils.getResponseEntity("Password reset successfully. Please sign in with your new password.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//
//    @Override
//    public String verifyAdmin(User user) {
//        if (Objects.equals(user.getPassword(), "adminGivenPassword12")) {
//            user.setPassword(encoder.encode(user.getPassword()));
//            user.setName("Administrator");
//            user.setMobileNumber("1-800-553-1987");
//            user.setRole("admin");
//            user.setStatus("enabled");
//            userDao.save(user);
//            return jwtService.generateToken(user.getUsername());
//        } else{
//            return "fail";
//        }
//    }

}
