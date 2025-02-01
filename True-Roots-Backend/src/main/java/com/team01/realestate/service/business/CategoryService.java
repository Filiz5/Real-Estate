package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.CategoryPropertyKey;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.CategoryMapper;
import com.team01.realestate.payload.mapper.MapCategory;
import com.team01.realestate.payload.request.business.CategoryCreateRequest;
import com.team01.realestate.payload.request.business.CategoryRequest;
import com.team01.realestate.payload.request.business.PropertyKeyRequest;
import com.team01.realestate.payload.response.business.CategoryResponse;
import com.team01.realestate.entity.concretes.business.Category;
import com.team01.realestate.payload.response.business.PropertyKeyResponse;
import com.team01.realestate.repository.business.CategoryPropertyKeyRepository;
import com.team01.realestate.repository.business.CategoryPropertyValueRepository;
import com.team01.realestate.repository.business.CategoryRepository;
import com.team01.realestate.service.helper.PageableHelper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CategoryService {


    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PageableHelper pageableHelper;
    private final CategoryPropertyKeyRepository categoryPropertyKeyRepository;
    private final CategoryPropertyValueRepository categoryPropertyValueRepository;
    private final MapCategory mapCategory;


    public Page<CategoryResponse> getCategories(String q, int page, int size, String sort, String type) {
        // Sayfalama ve sıralama ayarlarını oluştur
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        // Sorgu parametresinin boş ya da null olması durumunu ele al
        String query = (q == null || q.trim().isEmpty()) ? null : q.trim().toLowerCase();

        // Veritabanı sorgusunu çalıştır
        Page<Category> categories = categoryRepository.findActiveCategoriesByQuery(query, pageable);

        // Gelen sonuçları DTO'ya dönüştür ve döndür
        return categories.map(categoryMapper::toCategoryResponse);
    }

    public Page<CategoryResponse> getCategoriesAdmin(String q, int page, int size, String sort, String type) {
        // Sayfalama ve sıralama ayarlarını oluştur
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        // Sorgu parametresinin boş ya da null olması durumunu ele al
//        String query = (q == null || q.trim().isEmpty()) ? null : q.trim().toLowerCase();
        Specification<Category> specification = Specification.where((root, query, cb) -> cb.conjunction()); // Düzgün başlatma
        if (q != null && !q.isEmpty()) {
            specification = specification.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("title")), "%" + q.toLowerCase() + "%"));
        }


        // Veritabanı sorgusunu çalıştır
        Page<Category> categories = categoryRepository.findAll(specification,  pageable);

        // Gelen sonuçları DTO'ya dönüştür ve döndür
        return categories.map(categoryMapper::toCategoryResponse);
    }


    public CategoryResponse createCategory(CategoryCreateRequest request) {
        // Slug'un benzersiz olup olmadığını kontrol et
        if (categoryRepository.findBySlug(request.getSlug()).isPresent()) {
            throw new IllegalArgumentException("This slug is already in use. Please use a different slug.");
        }

        Category category = new Category();
        category.setTitle(request.getTitle());
        category.setIcon(request.getIcon());
        category.setBuilt_in(request.isBuiltIn()); // builtIn olarak güncellendi
        category.setSeq(request.getSeq());
        category.setSlug(request.getSlug());
        category.setCreatedAt(LocalDateTime.now());

        // Kategoriyi kaydet
        Category savedCategory = categoryRepository.save(category);


        for (PropertyKeyRequest propertyKeyRequest : request.getPropertyKeyRequests()) {
            CategoryPropertyKey propertyKey = new CategoryPropertyKey();
            propertyKey.setName(propertyKeyRequest.getName());
            propertyKey.setCategory(savedCategory);
            categoryPropertyKeyRepository.save(propertyKey);

            if (savedCategory.getCategoryPropertyKeys() == null) {
                savedCategory.setCategoryPropertyKeys(new ArrayList<>());
            }
            savedCategory.getCategoryPropertyKeys().add(propertyKey);
        }


        // Yanıt nesnesini oluştur
        return mapCategory.mapCategoryToResponse(savedCategory);
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Kategori bulunamadı. ID: " + id));

        return mapCategory.mapCategoryToResponse(category);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı. ID: " + id));
        // Slug çakışması kontrolü
        Optional<Category> existingCategoryWithSlug = categoryRepository.findBySlug(request.getSlug());
        if (existingCategoryWithSlug.isPresent() && !existingCategoryWithSlug.get().getId().equals(id)) {
            throw new IllegalArgumentException("Bu slug zaten mevcut. Lütfen farklı bir slug kullanın.");
        }
        // Varsayılan kategori kontrolünü kaldırdık
         if (category.isBuilt_in()) {
             throw new IllegalArgumentException("Varsayılan kategori güncellenemez. ID: " + id);
         }

        // Kategori alanlarını güncelle
        category.setTitle(request.getTitle());
        category.setIcon(request.getIcon());
        category.setBuilt_in(request.isBuiltIn()); // Eğer builtIn durumunu değiştirmek istiyorsanız
        category.setSeq(request.getSeq());
        category.setSlug(request.getSlug());
        category.setActive(request.isActive()); // Eğer aktif durumu değiştirmek istiyorsanız
        category.setUpdatedAt(LocalDateTime.now()); // Zaman damgasını güncelle

        // Güncellenen kategoriyi kaydet
        Category updatedCategory = categoryRepository.save(category);

        // Yanıtı döndür
        return categoryMapper.toCategoryResponse(updatedCategory);
    }

    public CategoryResponse deleteCategory(Long id) {
        // Kategoriyi veritabanından bul
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadı. ID: " + id));

        // Varsayılan kategori olup olmadığını kontrol et
        if (category.isBuilt_in()) {
            throw new IllegalArgumentException("Varsayılan kategori silinemez. ID: " + id);
        }

        // Kategorinin bağlı olduğu reklam kayıtlarını kontrol et
        if (categoryRepository.existsAdvertisementsByCategoryId(id)) {
            throw new IllegalArgumentException("Bu kategoriye bağlı reklam kayıtları var, silme işlemi yapılmaz.");
        }

        // Kategoriyi sil
        categoryRepository.delete(category);

        // Silinen kategori nesnesini döndür
        return categoryMapper.toCategoryResponse(category);
    }

    public List<PropertyKeyResponse> getPropertyKeysByCategoryId(Long categoryId) {
        // Kategoriye ait özellik anahtarlarını al
        List<CategoryPropertyKey> propertyKeys = categoryRepository.findPropertyKeysByCategoryId(categoryId);

        // Yanıt DTO'suna dönüştür
        return propertyKeys.stream()
                .map(key -> new PropertyKeyResponse(key.getId(), key.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public PropertyKeyResponse createPropertyKey(Long categoryId, PropertyKeyRequest request) {
        // Kategori bulun
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı."));

        // Yeni özellik anahtarını oluştur
        CategoryPropertyKey propertyKey = new CategoryPropertyKey();
        propertyKey.setName(request.getName());
        propertyKey.setCategory(category);

        // Özellik anahtarını kaydet
        CategoryPropertyKey savedPropertyKey = categoryPropertyKeyRepository.save(propertyKey);

        // Yanıt DTO'suna dönüştür
        return new PropertyKeyResponse(savedPropertyKey.getId(), savedPropertyKey.getName());
    }

    @Transactional
    public PropertyKeyResponse updatePropertyKey(Long propertyKeyId,PropertyKeyRequest request) {
        // Özellik anahtarını bul
        CategoryPropertyKey propertyKey = categoryPropertyKeyRepository.findById(propertyKeyId)
                .orElseThrow(() -> new RuntimeException("Özellik anahtarı bulunamadı."));

        // Eğer builtIn özelliği true ise güncelleme yapma
        if (propertyKey.isBuiltIn()) {
            throw new RuntimeException("Built-in özellik anahtarları güncellenemez.");
        }

        // Özellik anahtarını güncelle
        propertyKey.setName(request.getName());

        // Güncellenmiş özelliği kaydet
        CategoryPropertyKey updatedPropertyKey = categoryPropertyKeyRepository.save(propertyKey);

        // Yanıt DTO'suna dönüştür
        return new PropertyKeyResponse(updatedPropertyKey.getId(), updatedPropertyKey.getName());
    }

    @Transactional
    public PropertyKeyResponse deletePropertyKey(Long propertyKeyId) {
        // Özellik anahtarını bul
        CategoryPropertyKey propertyKey = categoryPropertyKeyRepository.findById(propertyKeyId)
                .orElseThrow(() -> new ResourceNotFoundException("Özellik anahtarı bulunamadı. ID: " + propertyKeyId));

        // Eğer builtIn özelliği true ise silme işlemi yapma
        if (propertyKey.isBuiltIn()) {
            throw new IllegalArgumentException("Built-in özellik anahtarları silinemez.");
        }

        // İlişkili category_property_values kayıtlarını sil
        categoryPropertyValueRepository.deleteByCategoryPropertyKeyId(propertyKeyId);

        // Özellik anahtarını sil
        categoryPropertyKeyRepository.delete(propertyKey);

        // Yanıt DTO'suna dönüştür
        return new PropertyKeyResponse(propertyKey.getId(), propertyKey.getName());
    }

    public List<CategoryResponse> getCategoriesAsList() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(mapCategory::mapCategoryToResponse)
                .collect(Collectors.toList());
    }
}