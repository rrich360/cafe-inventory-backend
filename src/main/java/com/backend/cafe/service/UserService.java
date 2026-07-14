package com.backend.cafe.service;

import com.backend.cafe.model.ChangePasswordRequest;
import com.backend.cafe.model.User;
import com.backend.cafe.wrapper.InventoryItemsWrapper;
import com.backend.cafe.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface UserService {
//
//    User register(User user);

    ResponseEntity<String> register(Map<String, String> requestMap);

    User signUpAsRewardsCustomer(User user);

    ResponseEntity<List<UserWrapper>> getAllUsers();

    ResponseEntity<String> update(Map<String, String> requestMap);

//    String verify(User user);

    void changePassword(ChangePasswordRequest request, Principal connectedUser);

    ResponseEntity<String> verifyAdmin(Map<String, String> requestMap, User user);

    ResponseEntity<String> verifyUser(Map<String, String> requestMap, User user);


//    String verifyAdmin(User user);

}

