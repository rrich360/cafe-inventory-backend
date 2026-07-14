package com.backend.cafe.rest;

import com.backend.cafe.model.ChangePasswordRequest;
import com.backend.cafe.model.User;
import com.backend.cafe.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200/")
@RequestMapping(path = "/user")
public interface UserRest {


    @PostMapping(path = "/api/register")
    ResponseEntity<String> register(@RequestBody(required = true) Map<String, String> requestMap);


    @PostMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String,String> requestMap);


    @PostMapping(path = "/api/admin")
    ResponseEntity<String> adminSignIn(@RequestBody(required = true) Map<String, String> requestMap, User user);


    @PostMapping(path = "/api/sign_in")
    ResponseEntity<String> userSignIn(@RequestBody(required = true) Map<String, String> requestMap, User user);

    @PatchMapping
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser);//at this point, since we are already on a secured endpoint with authenticated user, we can ask Spring to INJECT an object of the Principal Class library...


    @PostMapping(path = "/register_rewards")
    public User registerAsRewards(@RequestBody User user);


    @GetMapping(path = "/get")
    public ResponseEntity<List<UserWrapper>> getAllUsers();



//    @PostMapping(path = "/admin")
//    public String adminSignIn(@RequestBody User user);
//    {//when someone sends admin login request, this method should be executed.
//        return userServiceImpl.verifyAdmin(user); //ask the method created in my service class to verify the admin... get a response to return 'valid' or 'not valid' admin.
//    }



}
