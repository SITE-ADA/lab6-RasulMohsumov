package az.edu.ada.wm2.lab6.model.mapper;

import az.edu.ada.wm2.lab6.model.Category;
import az.edu.ada.wm2.lab6.model.Product;
import az.edu.ada.wm2.lab6.model.dto.ProductRequestDto;
import az.edu.ada.wm2.lab6.model.dto.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  // Entity → Response DTO
  @Mapping(target = "categoryNames", source = "categories")
  ProductResponseDto toResponseDto(Product product);

  // Request DTO → Entity (WITHOUT categories)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "categories", ignore = true)
  Product toEntity(ProductRequestDto dto);

  default List<String> mapCategoriesToNames(List<Category> categories) {
    if (categories == null) {
      return List.of(); // Fixes shouldHandleEmptyCategories test
    }
    return categories.stream()
      .map(Category::getName)
      .collect(Collectors.toList());
  }
}