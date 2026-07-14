package com.backend.cafe.restImpl;

import com.backend.cafe.constants.CafeConstants;
import com.backend.cafe.model.ChangePasswordRequest;
import com.backend.cafe.model.User;
import com.backend.cafe.rest.UserRest;
import com.backend.cafe.serviceImpl.UserServiceImpl;
import com.backend.cafe.utils.CafeUtils;
import com.backend.cafe.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    private UserServiceImpl userServiceImpl;


@Override
public ResponseEntity<String> register(Map<String, String> requestMap){
    try {
        return userServiceImpl.register(requestMap);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
}

    @Override
    public ResponseEntity<String> userSignIn(Map<String, String> requestMap, User user){
        try {
            return userServiceImpl.verifyUser(requestMap, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public User registerAsRewards(User user) {
       return userServiceImpl.signUpAsRewardsCustomer(user);
    }

//    @Override
//    public String signIn(User user) {//when someone sends login request, this method should be executed.
//        return userServiceImpl.verify(user); //ask the method created in my service class to verify the user... get a response to return 'valid' or 'not valid' user.
//
//    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers()  {
       return userServiceImpl.getAllUsers();
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
    return userServiceImpl.update(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> changePassword(ChangePasswordRequest request, Principal connectedUser) {//at this point, since we are already on a secured endpoint with authenticated user, we can ask Spring to INJECT an object of the Principal Class library...
        userServiceImpl.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
  }

    @Override
    public ResponseEntity<String> adminSignIn(Map<String, String> requestMap, User user){
        try {
            return userServiceImpl.verifyAdmin(requestMap, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



//    @Override
//    public String adminSignIn(User user) {//when someone sends admin login request, this method should be executed.
//        return userServiceImpl.verifyAdmin(user); //ask the method created in my service class to verify the admin... get a response to return 'valid' or 'not valid' admin.
//   }

//    @PostMapping(path = "/signup_rewards")
//    public User registerAsRewards(@RequestBody User user) {
//        return userServiceImpl.signUpAsRewardsCustomer(user);
//    }
//
//    @PostMapping(path = "/login")
//    public String login(@RequestBody User user) {//when someone sends login request, this method should be executed.
//        return userServiceImpl.verify(user); //ask the method created in my service class to verify the user... get a response to return 'valid' or 'not valid' user.}
//
//   @GetMapping(path = "/get")
//   public ResponseEntity<List<UserWrapper>> getAllUsers(){
//        return null;
//   }
//
//    @PatchMapping
//    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser) {//at this point, since we are already on a secured endpoint with authenticated user, we can ask Spring to INJECT an object of the Principal Class library...
//        userServiceImpl.changePassword(request, connectedUser);
//        return ResponseEntity.ok().build();
//       }
//
//    @PostMapping(path = "/admin")
//    public String adminLogin(@RequestBody User user) {//when someone sends admin login request, this method should be executed.
//        return userServiceImpl.verifyAdmin(user); //ask the method created in my service class to verify the admin... get a response to return 'valid' or 'not valid' admin.
//    }


}
