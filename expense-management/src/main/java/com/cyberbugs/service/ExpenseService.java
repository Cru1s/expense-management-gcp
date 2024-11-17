/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cyberbugs.service;

import com.cyberbugs.model.Expense;
import com.cyberbugs.repository.ExpenseRepository;
import com.google.cloud.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cruis
 */
@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public void addExpense(String description, Double amount) {

        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setCreatedAt(Timestamp.now());
        this.expenseRepository.save(expense).block();
    }

    public String viewAllExpenses() {
        List<Expense> listOfExpenses = this.expenseRepository.findAll().collectList().block();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        StringBuilder builder = new StringBuilder("Your Expenses:\n---------------------------------\n");

        for (var expense : listOfExpenses) {

            String formattedDate = (expense.getCreatedAt()!= null)
                    ? dateFormatter.format(expense.getCreatedAt().toDate())
                    : "N/A";

            builder.append("🆔 ID: ").append(expense.getId()).append("\n")
                    .append("💵 Amount: $").append(String.format("%.2f", expense.getAmount())).append("\n")
                    .append("📄 Description: ").append(expense.getDescription()).append("\n")
                    .append("📅 Date: ").append(formattedDate).append("\n")
                    .append("---------------------------------\n");
        }

        return builder.toString();
    }

    public void updateExpense(String id, Double amount, String description) throws Exception {

        if (!expenseRepository.existsById(id).block()) {
            throw new Exception("This expense does not exist");
        }

        Expense oldExpense = expenseRepository.findById(id).block();

        if (oldExpense == null) {
            throw new Exception("This expense could not be retrieved");
        }

        if (amount != null) {
            oldExpense.setAmount(amount);
        }

        if (description != null) {
            oldExpense.setDescription(description);
        }

        this.expenseRepository.save(oldExpense).block();

    }
    
    public void deleteExpense(String expenseId){
        
        this.expenseRepository.deleteById(expenseId).block();
        
    }
    

}
