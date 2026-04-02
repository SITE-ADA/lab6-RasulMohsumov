package az.edu.ada.wm2.lab6.repository;

import az.edu.ada.wm2.lab6.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Product save(Product product);
    Optional<Product> findById(UUID id);
    List<Product> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);
    List<Product> findByExpirationDateBefore(LocalDate date);
    List<Product> findByPriceBetween(BigDecimal start, BigDecimal end);
}
