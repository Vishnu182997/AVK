package com.example.commerce.product;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class InventoryTest {
  @Test
  void preventsNegativeStock() {
    Inventory i = new Inventory();
    i.setAvailableQuantity(2);
    assertThrows(IllegalStateException.class, () -> i.deduct(3));
    assertEquals(2, i.getAvailableQuantity());
  }
  @Test
  void deductsAvailableStock() {
    Inventory i = new Inventory();
    i.setAvailableQuantity(3);
    i.deduct(2);
    assertEquals(1, i.getAvailableQuantity());
  }
}
