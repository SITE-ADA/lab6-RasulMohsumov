package az.edu.ada.wm2.service;

import az.edu.ada.wm2.lab6.model.Category;
import az.edu.ada.wm2.lab6.model.Product;
import az.edu.ada.wm2.lab6.model.dto.ProductRequestDto;
import az.edu.ada.wm2.lab6.model.dto.ProductResponseDto;
import az.edu.ada.wm2.lab6.model.mapper.ProductMapper;
import az.edu.ada.wm2.lab6.repository.CategoryRepository;
import az.edu.ada.wm2.lab6.repository.ProductRepository;
import az.edu.ada.wm2.lab6.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductMapper productMapper;

  @Override
  public ProductResponseDto createProduct(ProductRequestDto dto) {
    if (dto.getPrice() == null || dto.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Price must be greater than zero");
    }

    Product product = productMapper.toEntity(dto);

    if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
      List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
      product.setCategories(categories);
    }

    Product saved = productRepository.save(product);
    return productMapper.toResponseDto(saved);
  }

  @Override
  public ProductResponseDto getProductById(UUID id) {
    Product product = productRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Product not found"));
    return productMapper.toResponseDto(product);
  }

  @Override
  public List<ProductResponseDto> getAllProducts() {
    return productRepository.findAll().stream()
      .map(productMapper::toResponseDto)
      .toList();
  }

  @Override
  public ProductResponseDto updateProduct(UUID id, ProductRequestDto dto) {
    if (dto.getPrice() != null && dto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Price cannot be negative");
    }

    Product existing = productRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Product not found"));

    existing.setProductName(dto.getProductName());
    existing.setPrice(dto.getPrice());
    existing.setExpirationDate(dto.getExpirationDate());

    Product saved = productRepository.save(existing);
    return productMapper.toResponseDto(saved);
  }

  @Override
  public void deleteProduct(UUID id) {
    Product product = productRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Product not found"));
    productRepository.delete(product);
  }

  @Override
  public List<ProductResponseDto> getProductsExpiringBefore(LocalDate date) {
    return productRepository.findByExpirationDateBefore(date).stream()
      .map(productMapper::toResponseDto)
      .toList();
  }

  @Override
  public List<ProductResponseDto> getProductsByPriceRange(BigDecimal min, BigDecimal max) {
    return productRepository.findByPriceBetween(min, max).stream()
      .map(productMapper::toResponseDto)
      .toList();
  }
}