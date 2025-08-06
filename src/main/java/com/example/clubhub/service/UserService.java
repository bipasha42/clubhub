package com.example.clubhub.service;

import com.example.clubhub.model.User;

public interface UserService {
    User getByEmail(String email);
}