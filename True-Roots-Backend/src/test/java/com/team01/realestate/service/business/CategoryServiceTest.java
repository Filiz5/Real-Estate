package com.team01.realestate.service.business;
import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.entity.concretes.business.CategoryPropertyKey;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.CategoryMapper;
import com.team01.realestate.payload.mapper.MapCategory;
import com.team01.realestate.payload.request.business.CategoryCreateRequest;
import com.team01.realestate.payload.request.business.CategoryRequest;
import com.team01.realestate.payload.request.business.PropertyKeyRequest;
import com.team01.realestate.payload.response.business.CategoryResponse;
import com.team01.realestate.payload.response.business.PropertyKeyResponse;
import com.team01.realestate.repository.business.CategoryPropertyKeyRepository;
import com.team01.realestate.repository.business.CategoryPropertyValueRepository;
import com.team01.realestate.repository.business.CategoryRepository;
import com.team01.realestate.service.helper.PageableHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock(lenient=true)
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private PageableHelper pageableHelper;

    @Mock
    private CategoryPropertyKeyRepository categoryPropertyKeyRepository;

    @Mock
    private CategoryPropertyValueRepository categoryPropertyValueRepository;

    @Mock
    private MapCategory mapCategory;

    private Category category;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setTitle("Test Category");
        category.setSlug("test-category");

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setTitle("Test Category");
    }

    @Test
    void testGetCategories() {
        Pageable pageable = mock(Pageable.class);
        Page<Category> categoriesPage = new PageImpl<>(Collections.singletonList(category));
        when(pageableHelper.getPageableWithProperties(0, 10, "createdAt", "desc")).thenReturn(pageable);
        when(categoryRepository.findActiveCategoriesByQuery(null, pageable)).thenReturn(categoriesPage);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

        Page<CategoryResponse> result = categoryService.getCategories(null, 0, 10, "createdAt", "desc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(categoryRepository, times(1)).findActiveCategoriesByQuery(null, pageable);
    }

    @Test
    void testGetCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(mapCategory.mapCategoryToResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals("Test Category", result.getTitle());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateCategory() {
        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setTitle("New Category");
        request.setSlug("new-category");
        request.setPropertyKeyRequests(Collections.emptyList());

        when(categoryRepository.findBySlug("new-category")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(mapCategory.mapCategoryToResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.createCategory(request);

        assertNotNull(result);
        assertEquals("Test Category", result.getTitle());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdateCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setTitle("Updated Category");
        request.setSlug("updated-category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.updateCategory(1L, request);

        assertNotNull(result);
        assertEquals("Test Category", result.getTitle());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void testDeleteCategory() {
        // Mocking categoryRepository.findById to return an Optional with category
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Mocking categoryRepository.existsAdvertisementsByCategoryId to return false
        when(categoryRepository.existsAdvertisementsByCategoryId(1L)).thenReturn(false);

        // Mocking categoryMapper.toCategoryResponse to return a valid CategoryResponse
        when(categoryMapper.toCategoryResponse(category)).thenReturn(new CategoryResponse(
                category.getId(),
                "Test Category",
                "Description",
                false,
                0,
                "Image URL",
                false,
                Collections.emptyList(),
                LocalDateTime.now(),
                LocalDateTime.now()
        ));

        // Calling the method to test
        CategoryResponse result = categoryService.deleteCategory(1L);

        // Verifying the result is not null
        assertNotNull(result);

        // Verifying that delete method was called exactly once
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void testGetCategoriesAsList() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));
        when(mapCategory.mapCategoryToResponse(category)).thenReturn(categoryResponse);

        List<CategoryResponse> result = categoryService.getCategoriesAsList();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoriesAdmin() {
        Pageable pageable = mock(Pageable.class);
        Page<Category> categoriesPage = new PageImpl<>(Collections.singletonList(category));
        when(pageableHelper.getPageableWithProperties(0, 10, "createdAt", "desc")).thenReturn(pageable);
        when(categoryRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(categoriesPage);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

        Page<CategoryResponse> result = categoryService.getCategoriesAdmin(null, 0, 10, "createdAt", "desc");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(categoryRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testGetPropertyKeysByCategoryId() {
        Long categoryId = 1L;
        List<CategoryPropertyKey> propertyKeys = Collections.singletonList(new CategoryPropertyKey(1L, "Test Property Key"));
        when(categoryRepository.findPropertyKeysByCategoryId(categoryId)).thenReturn(propertyKeys);

        List<PropertyKeyResponse> result = categoryService.getPropertyKeysByCategoryId(categoryId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Property Key", result.get(0).getName());
        verify(categoryRepository, times(1)).findPropertyKeysByCategoryId(categoryId);
    }

    @Test
    void testCreatePropertyKey() {
        Long categoryId = 1L;
        PropertyKeyRequest request = new PropertyKeyRequest();
        request.setName("New Property Key");

        Category category = new Category();
        category.setId(categoryId);

        CategoryPropertyKey propertyKey = new CategoryPropertyKey();
        propertyKey.setId(1L);
        propertyKey.setName("New Property Key");
        propertyKey.setCategory(category);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryPropertyKeyRepository.save(any(CategoryPropertyKey.class))).thenReturn(propertyKey);

        PropertyKeyResponse result = categoryService.createPropertyKey(categoryId, request);

        assertNotNull(result);
        assertEquals("New Property Key", result.getName());
        verify(categoryPropertyKeyRepository, times(1)).save(any(CategoryPropertyKey.class));
    }

    @Test
    void testUpdatePropertyKey() {
        Long propertyKeyId = 1L;
        PropertyKeyRequest request = new PropertyKeyRequest();
        request.setName("Updated Property Key");

        CategoryPropertyKey propertyKey = new CategoryPropertyKey();
        propertyKey.setId(propertyKeyId);
        propertyKey.setName("Old Property Key");

        when(categoryPropertyKeyRepository.findById(propertyKeyId)).thenReturn(Optional.of(propertyKey));
        when(categoryPropertyKeyRepository.save(any(CategoryPropertyKey.class))).thenReturn(propertyKey);

        PropertyKeyResponse result = categoryService.updatePropertyKey(propertyKeyId, request);

        assertNotNull(result);
        assertEquals("Updated Property Key", result.getName());
        verify(categoryPropertyKeyRepository, times(1)).save(any(CategoryPropertyKey.class));
    }

    @Test
    void testGetCategoryByIdNotFound() {
        // Kategori bulunamadığında, Optional.empty() döndürüyoruz
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // IllegalArgumentException bekliyoruz
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.getCategoryById(1L);
        });

        // Beklenen hata mesajının doğru olduğunu doğruluyoruz
        assertEquals("Kategori bulunamadı. ID: 1", exception.getMessage());

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateCategoryNotFound() {
        CategoryRequest request = new CategoryRequest();
        request.setTitle("Updated Category");
        request.setSlug("updated-category");

        // Kategori bulunamadığında, Optional.empty() dönmelidir
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Hata durumu, exception fırlatılması bekleniyor
        assertThrows(RuntimeException.class, () -> categoryService.updateCategory(1L, request));
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteCategoryWithAdvertisements() {
        // Kategori bulundu, ancak reklamları var
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsAdvertisementsByCategoryId(1L)).thenReturn(true);

        // Hata durumu bekliyoruz (örneğin exception fırlatılabilir)
        assertThrows(RuntimeException.class, () -> categoryService.deleteCategory(1L));
        verify(categoryRepository, times(1)).existsAdvertisementsByCategoryId(1L);
    }

    @Test
    void testCreatePropertyKeyCategoryNotFound() {
        Long categoryId = 1L;
        PropertyKeyRequest request = new PropertyKeyRequest();
        request.setName("New Property Key");

        // Kategori bulunamadığında hata fırlatılabilir
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Kategori bulunamadığında exception bekliyoruz
        assertThrows(RuntimeException.class, () -> categoryService.createPropertyKey(categoryId, request));
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testUpdatePropertyKeyNotFound() {
        Long propertyKeyId = 1L;
        PropertyKeyRequest request = new PropertyKeyRequest();
        request.setName("Updated Property Key");

        // PropertyKey bulunamadığında, Optional.empty() dönecektir
        when(categoryPropertyKeyRepository.findById(propertyKeyId)).thenReturn(Optional.empty());

        // PropertyKey bulunamadığında exception bekliyoruz
        assertThrows(RuntimeException.class, () -> categoryService.updatePropertyKey(propertyKeyId, request));
        verify(categoryPropertyKeyRepository, times(1)).findById(propertyKeyId);
    }

    @Test
    void testGetCategoriesThrowsException() {
        Pageable pageable = mock(Pageable.class);
        when(pageableHelper.getPageableWithProperties(0, 10, "createdAt", "desc")).thenReturn(pageable);
        when(categoryRepository.findActiveCategoriesByQuery(null, pageable)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> categoryService.getCategories(null, 0, 10, "createdAt", "desc"));
        verify(categoryRepository, times(1)).findActiveCategoriesByQuery(null, pageable);
    }

    @Test
    void testCreateCategoryWithExistingSlug() {
        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setTitle("New Category");
        request.setSlug("new-category");
        request.setPropertyKeyRequests(Collections.emptyList());

        when(categoryRepository.findBySlug("new-category")).thenReturn(Optional.of(category));

        assertThrows(RuntimeException.class, () -> categoryService.createCategory(request));
        verify(categoryRepository, times(1)).findBySlug("new-category");
    }

    @Test
    void testDeleteCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        // ResourceNotFoundException bekliyoruz
        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(1L));
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testCreatePropertyKeyWithInvalidCategoryId() {
        Long categoryId = 1L;
        PropertyKeyRequest request = new PropertyKeyRequest();
        request.setName("New Property Key");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.createPropertyKey(categoryId, request));
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void testUpdatePropertyKeyWithInvalidId() {
        Long propertyKeyId = 1L;
        PropertyKeyRequest request = new PropertyKeyRequest();
        request.setName("Updated Property Key");

        when(categoryPropertyKeyRepository.findById(propertyKeyId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryService.updatePropertyKey(propertyKeyId, request));
        verify(categoryPropertyKeyRepository, times(1)).findById(propertyKeyId);
    }

    @Test
    void testGetCategoriesAdminThrowsException() {
        Pageable pageable = mock(Pageable.class);
        when(pageableHelper.getPageableWithProperties(0, 10, "createdAt", "desc")).thenReturn(pageable);
        when(categoryRepository.findAll(any(Specification.class), eq(pageable))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> categoryService.getCategoriesAdmin(null, 0, 10, "createdAt", "desc"));
        verify(categoryRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void testDeleteCategoryWithBuiltInCategory() {
        // Kategori bulunuyor, ancak varsayılan kategori
        category.setBuilt_in(true);

        // Mocking categoryRepository.findById to return the built-in category
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Kategoriye bağlı reklamlar yok, sadece varsayılan kategori kontrolü yapılacak
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        // Beklenen hata mesajı doğrulanıyor
        assertEquals("Varsayılan kategori silinemez. ID: 1", exception.getMessage());

        // Veritabanı işlemleri kontrol ediliyor
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(0)).delete(category);
    }

    @Test
    void testCreateCategoryWithInvalidSlug() {
        // Verilen input
        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setSlug("existing-slug");

        // Mevcut kategori durumu simüle ediliyor
        when(categoryRepository.findBySlug("existing-slug")).thenReturn(Optional.of(new Category()));

        // Exception fırlatılması bekleniyor
        try {
            categoryService.createCategory(request);
            fail("Slug zaten mevcut hatası bekleniyordu");
        } catch (IllegalArgumentException e) {
            assertEquals("Bu slug zaten mevcut. Lütfen farklı bir slug kullanın.", e.getMessage());
        }
    }

    @Test
    void testUpdateCategorySlugConflict() {
        // CategoryRequest nesnesi oluşturulur
        CategoryRequest request = new CategoryRequest();
        request.setTitle("Updated Category");
        request.setSlug("existing-category");  // Mevcut slug

        // Mevcut kategoriyi mock'la
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setSlug("existing-category");
        existingCategory.setTitle("Existing Category");

        // Yeni slug'ı kullanan başka bir kategori mock'la
        Category existingCategoryWithSlug = new Category();
        existingCategoryWithSlug.setId(2L);
        existingCategoryWithSlug.setSlug("existing-category");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.findBySlug("existing-category")).thenReturn(Optional.of(existingCategoryWithSlug));

        // Kategori güncelleme işlemi sırasında exception bekleniyor
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                categoryService.updateCategory(1L, request));

        // Beklenen hata mesajını kontrol et
        assertEquals("Bu slug zaten mevcut. Lütfen farklı bir slug kullanın.", exception.getMessage());

        // `save` metodunun çağrılmadığını doğrula
        verify(categoryRepository, times(0)).save(any(Category.class));
    }

    @Test
    void shouldDeletePropertyKeySuccessfully() {
        // Arrange
        Long propertyKeyId = 1L;
        CategoryPropertyKey mockPropertyKey = new CategoryPropertyKey();
        mockPropertyKey.setId(propertyKeyId);
        mockPropertyKey.setName("Test Key");
        mockPropertyKey.setBuiltIn(false);

        when(categoryPropertyKeyRepository.findById(propertyKeyId)).thenReturn(java.util.Optional.of(mockPropertyKey));

        // Act
        PropertyKeyResponse response = categoryService.deletePropertyKey(propertyKeyId);

        // Assert
        assertNotNull(response);
        assertEquals(propertyKeyId, response.getId());
        assertEquals("Test Key", response.getName());

        verify(categoryPropertyKeyRepository).findById(propertyKeyId);
        verify(categoryPropertyValueRepository).deleteByCategoryPropertyKeyId(propertyKeyId);
        verify(categoryPropertyKeyRepository).delete(mockPropertyKey);
    }

    @Test
    void shouldThrowExceptionWhenPropertyKeyNotFound() {
        // Arrange
        Long propertyKeyId = 1L;
        when(categoryPropertyKeyRepository.findById(propertyKeyId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                categoryService.deletePropertyKey(propertyKeyId)
        );
        assertEquals("Özellik anahtarı bulunamadı. ID: " + propertyKeyId, exception.getMessage());

        verify(categoryPropertyKeyRepository).findById(propertyKeyId);
        verifyNoInteractions(categoryPropertyValueRepository);
        verify(categoryPropertyKeyRepository, never()).delete(any());
    }

    @Test
    void shouldThrowExceptionWhenBuiltInPropertyKey() {
        // Arrange
        Long propertyKeyId = 1L;
        CategoryPropertyKey mockPropertyKey = new CategoryPropertyKey();
        mockPropertyKey.setId(propertyKeyId);
        mockPropertyKey.setBuiltIn(true);

        when(categoryPropertyKeyRepository.findById(propertyKeyId)).thenReturn(java.util.Optional.of(mockPropertyKey));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                categoryService.deletePropertyKey(propertyKeyId)
        );
        assertEquals("Built-in özellik anahtarları silinemez.", exception.getMessage());

        verify(categoryPropertyKeyRepository).findById(propertyKeyId);
        verifyNoInteractions(categoryPropertyValueRepository);
        verify(categoryPropertyKeyRepository, never()).delete(any());
    }


}
