package com.example.expense.service.impl;

import java.time.LocalDateTime;
import javax.persistence.criteria.Predicate;

import org.apache.logging.log4j.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.expense.dto.*;
import com.example.expense.entity.ExpenseTransaction;
import com.example.expense.exception.ExpenseException;
import com.example.expense.mapper.ExpenseMapper;
import com.example.expense.repository.ExpenseTransactionRepository;
import com.example.expense.service.TransactionService;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private static final Logger LOGGER = LogManager.getLogger(TransactionServiceImpl.class);
    private final ExpenseTransactionRepository repository;
    public TransactionServiceImpl(ExpenseTransactionRepository repository) {
        this.repository = repository;
    }
    public TransactionResponse create(TransactionRequest r, ExpenseActor a) {
        validate(r);
        requireActor(a);
        ExpenseTransaction t = new ExpenseTransaction();
        apply(t, r);
        t.setUserId(a.getUserId());
        t.setCreatedAt(LocalDateTime.now());
        t.setUpdatedAt(t.getCreatedAt());
        t = repository.save(t);
        LOGGER.info("Created expense transaction {}", t.getId());
        return ExpenseMapper.toResponse(t);
    }
    @Transactional(readOnly = true)
    public TransactionResponse findById(Long id, ExpenseActor a) {
        return ExpenseMapper.toResponse(owned(id, a));
    }
    @Transactional(readOnly = true)
    public Page<TransactionResponse> findAll(final TransactionFilterRequest f, Pageable p, final ExpenseActor a) {
        requireActor(a);
        if (f != null && f.getFrom() != null && f.getTo() != null && f.getFrom().isAfter(f.getTo()))
            throw new ExpenseException("Invalid date range", 400);
        Specification<ExpenseTransaction> s = (root, q, b) -> {
            Predicate x = b.equal(root.get("userId"), a.getUserId());
            if (f != null && f.getFrom() != null)
                x = b.and(x, b.greaterThanOrEqualTo(root.get("transactionDate"), f.getFrom()));
            if (f != null && f.getTo() != null)
                x = b.and(x, b.lessThanOrEqualTo(root.get("transactionDate"), f.getTo()));
            if (f != null && f.getCategory() != null)
                x = b.and(x, b.equal(root.get("category"), f.getCategory()));
            if (f != null && f.getType() != null)
                x = b.and(x, b.equal(root.get("type"), f.getType()));
            return x;
        };
        return repository.findAll(s, p).map(ExpenseMapper::toResponse);
    }
    public TransactionResponse update(Long id, TransactionRequest r, ExpenseActor a) {
        validate(r);
        ExpenseTransaction t = owned(id, a);
        apply(t, r);
        t.setUpdatedAt(LocalDateTime.now());
        return ExpenseMapper.toResponse(repository.save(t));
    }
    public void delete(Long id, ExpenseActor a) {
        ExpenseTransaction t = owned(id, a);
        repository.delete(t);
        LOGGER.info("Deleted expense transaction {}", id);
    }
    private ExpenseTransaction owned(Long id, ExpenseActor a) {
        requireActor(a);
        if (id == null)
            throw new ExpenseException("Transaction id is required", 400);
        ExpenseTransaction t =
                repository.findById(id).orElseThrow(() -> new ExpenseException("Transaction not found: " + id, 404));
        if (!a.getUserId().equals(t.getUserId()))
            throw new ExpenseException("Access to this transaction is forbidden", 403);
        return t;
    }
    private void apply(ExpenseTransaction t, TransactionRequest r) {
        t.setType(r.getType());
        t.setAmount(r.getAmount());
        t.setCategory(r.getCategory());
        t.setDescription(r.getDescription() == null ? null : r.getDescription().trim());
        t.setTransactionDate(r.getTransactionDate());
    }
    private void validate(TransactionRequest r) {
        if (r == null || r.getAmount() == null || r.getAmount().signum() <= 0)
            throw new ExpenseException("Amount must be greater than zero", 400);
        if (r.getType() == null || r.getCategory() == null || r.getTransactionDate() == null)
            throw new ExpenseException("Type, category and transaction date are required", 400);
    }
    static void requireActor(ExpenseActor a) {
        if (a == null || a.getUserId() == null || a.getUserId().trim().isEmpty())
            throw new ExpenseException("Authentication is required", 401);
    }
}
