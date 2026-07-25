package com.example.expense.service.impl;

import com.example.expense.dto.*;
import com.example.expense.entity.Budget;
import com.example.expense.exception.ExpenseException;
import com.example.expense.mapper.ExpenseMapper;
import com.example.expense.repository.BudgetRepository;
import com.example.expense.service.BudgetService;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.logging.log4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BudgetServiceImpl implements BudgetService {
  private static final Logger LOGGER = LogManager.getLogger(BudgetServiceImpl.class);
  private final BudgetRepository repository;
  public BudgetServiceImpl(BudgetRepository repository) {
    this.repository = repository;
  }
  public BudgetResponse create(BudgetRequest r, ExpenseActor a) {
    validate(r);
    TransactionServiceImpl.requireActor(a);
    if (repository.existsByUserIdAndCategoryAndMonthAndYear(
            a.getUserId(), r.getCategory(), r.getMonth(), r.getYear()))
      duplicate();
    Budget b = new Budget();
    apply(b, r);
    b.setUserId(a.getUserId());
    b.setCreatedAt(LocalDateTime.now());
    b.setUpdatedAt(b.getCreatedAt());
    b = repository.save(b);
    LOGGER.info("Created monthly budget {}", b.getId());
    return ExpenseMapper.toResponse(b);
  }
  @Transactional(readOnly = true)
  public BudgetResponse findById(Long id, ExpenseActor a) {
    return ExpenseMapper.toResponse(owned(id, a));
  }
  @Transactional(readOnly = true)
  public List<BudgetResponse> findAll(Integer month, Integer year, ExpenseActor a) {
    TransactionServiceImpl.requireActor(a);
    validatePeriod(month, year);
    return repository.findByUserIdAndMonthAndYear(a.getUserId(), month, year)
        .stream()
        .map(ExpenseMapper::toResponse)
        .collect(Collectors.toList());
  }
  public BudgetResponse update(Long id, BudgetRequest r, ExpenseActor a) {
    validate(r);
    Budget b = owned(id, a);
    if (repository.existsByUserIdAndCategoryAndMonthAndYearAndIdNot(
            a.getUserId(), r.getCategory(), r.getMonth(), r.getYear(), id))
      duplicate();
    apply(b, r);
    b.setUpdatedAt(LocalDateTime.now());
    return ExpenseMapper.toResponse(repository.save(b));
  }
  public void delete(Long id, ExpenseActor a) {
    Budget b = owned(id, a);
    repository.delete(b);
    LOGGER.info("Deleted monthly budget {}", id);
  }
  private Budget owned(Long id, ExpenseActor a) {
    TransactionServiceImpl.requireActor(a);
    if (id == null)
      throw new ExpenseException("Budget id is required", 400);
    Budget b = repository.findById(id).orElseThrow(
        () -> new ExpenseException("Budget not found: " + id, 404));
    if (!a.getUserId().equals(b.getUserId()))
      throw new ExpenseException("Access to this budget is forbidden", 403);
    return b;
  }
  private void apply(Budget b, BudgetRequest r) {
    b.setCategory(r.getCategory());
    b.setMonthlyLimit(r.getMonthlyLimit());
    b.setMonth(r.getMonth());
    b.setYear(r.getYear());
  }
  private void validate(BudgetRequest r) {
    if (r == null || r.getMonthlyLimit() == null || r.getMonthlyLimit().signum() <= 0)
      throw new ExpenseException("Monthly limit must be greater than zero", 400);
    if (r.getCategory() == null)
      throw new ExpenseException("Category is required", 400);
    validatePeriod(r.getMonth(), r.getYear());
  }
  static void validatePeriod(Integer m, Integer y) {
    if (m == null || m < 1 || m > 12 || y == null || y < 1900 || y > 9999)
      throw new ExpenseException("Month must be 1-12 and year must be 1900-9999", 400);
  }
  private void duplicate() {
    throw new ExpenseException("A budget already exists for this category and month", 409);
  }
}
