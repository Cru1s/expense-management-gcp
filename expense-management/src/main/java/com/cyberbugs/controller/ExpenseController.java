/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cyberbugs.controller;

import com.cyberbugs.model.Expense;
import com.cyberbugs.repository.ExpenseRepository;
import com.cyberbugs.service.ExpenseService;
import com.google.cloud.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Cruis
 */
@RestController
@RequestMapping("/")
public class ExpenseController {
    
    @Autowired
    private ExpenseRepository expenseRepository;
    
    @Autowired
    private ExpenseService expenseService;
    
    @GetMapping
    public String home(){
        return "hello";
    }
    
    @PostMapping("/save")
    public Expense createExpense(@RequestBody Expense expense){
      
        expense.setCreatedAt(Timestamp.now());
        
        Expense newExpense = this.expenseRepository.save(expense).block();
        
        return newExpense;
    }
    
    @GetMapping("/list")
    public List<Expense> findAllExpense(){
        
        List<Expense> listOfExpenses = this.expenseRepository.findAll().collectList().block();
        
        return listOfExpenses;
        
    }
    
    @DeleteMapping("/delete/{id}")
    public String deleteExpense(@PathVariable String id){
        
        this.expenseRepository.deleteById(id).block();
        
        return "Expense is deleted successfully";  
    }
    
    @PostMapping("/update/{id}")
    public Expense updateExpense(@RequestBody Expense expense, @PathVariable String id) throws Exception{
        
        Expense updateExpense = expenseService.updateExpense(id, expense);
        
        return updateExpense;
    }
    
}
