/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cyberbugs.service;

import com.cyberbugs.model.User;
import com.cyberbugs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cruis
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public void registerUser(Long userId, String username){
        User user = new User();
        user.setId(userId.toString());
        user.setUsername(username);
        userRepository.save(user).block();
    }
    
    public boolean isExistId(Long userId){
        return userRepository.existsById(userId.toString()).block();
    }
    
}
