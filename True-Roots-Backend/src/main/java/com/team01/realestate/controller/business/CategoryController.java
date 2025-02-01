package com.team01.realestate.controller.business;
import com.team01.realestate.payload.request.business.CategoryCreateRequest;
import com.team01.realestate.payload.request.business.CategoryRequest;
import com.team01.realestate.payload.request.business.PropertyKeyRequest;
import com.team01.realestate.payload.response.business.CategoryResponse;
import com.team01.realestate.payload.response.business.PropertyKeyResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.repository.business.CategoryPropertyKeyRepository;
import com.team01.realestate.service.business.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryPropertyKeyRepository categoryPropertyKeyRepository;

    //getCategoryPageable  sadece aktifleri getirir

    @GetMapping
    public ResponseMessage<Page<CategoryResponse>> getCategories(@RequestParam(value = "q", defaultValue = "") String query,
                                                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                          @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                          @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
                                                                                          @RequestParam(value = "type", defaultValue = "desc") String type) {
        Page<CategoryResponse> categoryResponse = categoryService.getCategories(query,page,size,sort,type);

        return ResponseMessage.<Page<CategoryResponse>>builder()
                .object(categoryResponse)
                .message("Kategoriler başarıyla bulundu.")
                .httpStatus(HttpStatus.OK)
                .build();
    }
//getAdmin Categories /tüm Categorileri getirir. silineneler dahil
@GetMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
public ResponseMessage<Page<CategoryResponse>> getCategoriesAdmin(
        @RequestParam(value = "q", required = false) String query,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "20") int size,
        @RequestParam(value = "sort", defaultValue = "category_id") String sort,
        @RequestParam(value = "type", defaultValue = "asc") String type) {

    Page<CategoryResponse> categoryResponse = categoryService.getCategoriesAdmin(query, page, size, sort, type);

    return ResponseMessage.<Page<CategoryResponse>>builder()
            .object(categoryResponse)
            .message("Categories are found successfully.")
            .httpStatus(HttpStatus.OK)
            .build();
}

    //create a new category
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<CategoryResponse> createCategory(@RequestBody CategoryCreateRequest request) {
        CategoryResponse createdCategory = categoryService.createCategory(request);

        return ResponseMessage.<CategoryResponse>builder()
                .object(createdCategory)
                .message("Category is created successfully.")
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    // Get category by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<CategoryResponse> getCategoryById(@PathVariable Long id) {
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);

        return ResponseMessage.<CategoryResponse>builder()
                .object(categoryResponse)
                .message("Category is found successfully.")
                .httpStatus(HttpStatus.OK)
                .build();
    }

//Update
@PutMapping("/{id}")
@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
public ResponseMessage<CategoryResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
    CategoryResponse updatedCategory = categoryService.updateCategory(id, request);

    return ResponseMessage.<CategoryResponse>builder()
            .object(updatedCategory)
            .message("Category is updated successfully.")
            .httpStatus(HttpStatus.OK)
            .build();
}

//Delete
@DeleteMapping("/{id}")
@PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
public ResponseMessage<CategoryResponse> deleteCategory(@PathVariable Long id) {
    CategoryResponse deletedCategory = categoryService.deleteCategory(id);

    return ResponseMessage.<CategoryResponse>builder()
            .object(deletedCategory)
            .message("Category is deleted successfully.")
            .httpStatus(HttpStatus.OK)
            .build();
}

    // Kategoriye ait özellik anahtarlarını getir
    @GetMapping("/{id}/properties")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<List<PropertyKeyResponse>> getPropertyKeysByCategoryId(@PathVariable Long id) {
        List<PropertyKeyResponse> propertyKeys = categoryService.getPropertyKeysByCategoryId(id);

        return ResponseMessage.<List<PropertyKeyResponse>>builder()
                .object(propertyKeys)
                .message("Property fields are found successfully.")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Yeni özellik anahtarı ekle
    @PostMapping("/{id}/properties")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<PropertyKeyResponse> createPropertyKey(
            @PathVariable Long id,
            @RequestBody PropertyKeyRequest request) {

        PropertyKeyResponse createdPropertyKey = categoryService.createPropertyKey(id, request);

        return ResponseMessage.<PropertyKeyResponse>builder()
                .object(createdPropertyKey)
                .message("Property field is created successfully.")
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

// Yeni anahtarı güncelle
    @PutMapping("/properties/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<PropertyKeyResponse> updatePropertyKey(
            @PathVariable("id") Long propertyKeyId,
            @RequestBody PropertyKeyRequest request) {

        PropertyKeyResponse updatedPropertyKey = categoryService.updatePropertyKey(propertyKeyId, request);

        return ResponseMessage.<PropertyKeyResponse>builder()
                .object(updatedPropertyKey)
                .message("Property field is updated successfully.")
                .httpStatus(HttpStatus.OK)
                .build();
    }


    // Özellik anahtarını sil
    @DeleteMapping("/properties/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<PropertyKeyResponse> deletePropertyKey(@PathVariable("id") Long propertyKeyId) {
        PropertyKeyResponse deletedPropertyKey = categoryService.deletePropertyKey(propertyKeyId);

        return ResponseMessage.<PropertyKeyResponse>builder()
                .object(deletedPropertyKey)
                .message("Property field is deleted successfully.")
                .httpStatus(HttpStatus.OK)
                .build();
    }


    //getCategoriesAsList
    @GetMapping("/list")
    public ResponseMessage<List<CategoryResponse>> getCategoriesAsList() {
        List<CategoryResponse> categoryResponse = categoryService.getCategoriesAsList();

        return ResponseMessage.<List<CategoryResponse>>builder()
                .object(categoryResponse)
                .message("Categories are found successfully.")
                .httpStatus(HttpStatus.OK)
                .build();
    }


}