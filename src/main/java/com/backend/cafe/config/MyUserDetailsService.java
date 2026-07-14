package com.backend.cafe.config;

import com.backend.cafe.dao.UserDao;
import com.backend.cafe.model.User;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        User user = userDao.findByUsername(username);
        if (Objects.isNull(user)) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("user not found");
        } else {
            return user;
        }
    }

}
