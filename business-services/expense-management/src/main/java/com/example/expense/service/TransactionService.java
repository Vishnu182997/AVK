package com.example.expense.service;

import com.example.expense.dto.*;
import org.springframework.data.domain.*;

public interface TransactionService {
  TransactionResponse create(TransactionRequest request, ExpenseActor actor);
  TransactionResponse findById(Long id, ExpenseActor actor);
  Page<TransactionResponse> findAll(
      TransactionFilterRequest filter, Pageable pageable, ExpenseActor actor);
  TransactionResponse update(Long id, TransactionRequest request, ExpenseActor actor);
  void delete(Long id, ExpenseActor actor);
}
