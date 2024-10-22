/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cyberbugs.service;

import com.cyberbugs.model.Expense;
import com.cyberbugs.repository.ExpenseRepository;
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

    public Expense updateExpense(String id, Expense expense) throws Exception {

        if (!expenseRepository.existsById(id).block()) {
            throw new Exception("This expense does not exist");
        }

        Expense oldExpense = expenseRepository.findById(id).block();

        if (oldExpense == null) {
            throw new Exception("This expense could not be retrieved");
        }

        if (expense.getAmount() != null) {
            oldExpense.setAmount(expense.getAmount());
        }

        if (expense.getDescription() != null) {
            oldExpense.setDescription(expense.getDescription());
        }

        Expense newExpense = expenseRepository.save(oldExpense).block();

        return newExpense;
    }

}
