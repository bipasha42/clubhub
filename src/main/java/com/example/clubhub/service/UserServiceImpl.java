package com.example.clubhub.service;

import com.example.clubhub.model.User;
import com.example.clubhub.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public User getByEmail(String email) {
        return loginRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }
}