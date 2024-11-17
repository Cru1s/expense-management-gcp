/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cyberbugs.repository;

import com.cyberbugs.model.User;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;

/**
 *
 * @author Cruis
 */
public interface UserRepository extends FirestoreReactiveRepository<User>{
    
}
