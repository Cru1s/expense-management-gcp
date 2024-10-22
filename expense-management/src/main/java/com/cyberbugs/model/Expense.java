/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cyberbugs.model;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Cruis
 */

@Document
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Expense {
    
    @DocumentId
    private String id;
    
    private String description;
    
    private Double amount;
    
    private Timestamp createdAt;
}
