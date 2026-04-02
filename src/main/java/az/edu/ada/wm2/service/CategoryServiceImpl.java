package az.edu.ada.wm2.service;

import az.edu.ada.wm2.lab6.model.Category;
import az.edu.ada.wm2.lab6.model.Product;
import az.edu.ada.wm2.lab6.model.dto.*;
import az.edu.ada.wm2.lab6.model.mapper.CategoryMapper;
import az.edu.ada.wm2.lab6.model.mapper.ProductMapper;
import az.edu.ada.wm2.lab6.repository.CategoryRepository;
import az.edu.ada.wm2.lab6.repository.ProductRepository;
import az.edu.ada.wm2.lab6.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  public CategoryResponseDto create(CategoryRequestDto dto) {
    Category category = CategoryMapper.toEntity(dto);
    return CategoryMapper.toResponseDto(categoryRepository.save(category));
  }

  @Override
  public List<CategoryResponseDto> getAll() {
    return categoryRepository.findAll()
      .stream()
      .map(CategoryMapper::toResponseDto)
      .toList();
  }

  @Override
  public CategoryResponseDto addProduct(UUID categoryId, UUID productId) {
    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new RuntimeException("Category not found"));

    Product product = productRepository.findById(productId)
      .orElseThrow(() -> new RuntimeException("Product not found"));

    category.getProducts().add(product);
    product.getCategories().add(category);

    productRepository.save(product);
    return CategoryMapper.toResponseDto(category);
  }

  @Override
  public List<ProductResponseDto> getProducts(UUID categoryId) {
    Category category = categoryRepository.findById(categoryId)
      .orElseThrow(() -> new RuntimeException("Category not found"));

    return category.getProducts()
      .stream()
      .map(productMapper::toResponseDto)
      .toList();
  }
}