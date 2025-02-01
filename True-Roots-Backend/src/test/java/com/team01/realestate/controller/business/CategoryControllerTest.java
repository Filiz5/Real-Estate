package com.team01.realestate.controller.business;

import com.team01.realestate.payload.request.business.CategoryCreateRequest;
import com.team01.realestate.payload.request.business.CategoryRequest;
import com.team01.realestate.payload.request.business.PropertyKeyRequest;
import com.team01.realestate.payload.response.business.CategoryResponse;
import com.team01.realestate.payload.response.business.PropertyKeyResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.service.business.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;


import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCategories_ShouldReturnCategoryPage() {
        String query = "";
        int page = 0;
        int size = 10;
        String sort = "createdAt";
        String type = "desc";
        Page<CategoryResponse> categoryPage = new PageImpl<>(Collections.emptyList());
        when(categoryService.getCategories(query, page, size, sort, type)).thenReturn(categoryPage);

        ResponseMessage<Page<CategoryResponse>> response = categoryController.getCategories(query, page, size, sort, type);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(categoryPage, response.getObject());
        verify(categoryService, times(1)).getCategories(query, page, size, sort, type);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getCategoriesAdmin_ShouldReturnAdminCategoryPage() {
        String query = "";
        int page = 0;
        int size = 20;
        String sort = "category_id";
        String type = "asc";
        Page<CategoryResponse> categoryPage = new PageImpl<>(Collections.emptyList());
        when(categoryService.getCategoriesAdmin(query, page, size, sort, type)).thenReturn(categoryPage);

        ResponseMessage<Page<CategoryResponse>> response = categoryController.getCategoriesAdmin(query, page, size, sort, type);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(categoryPage, response.getObject());
        verify(categoryService, times(1)).getCategoriesAdmin(query, page, size, sort, type);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createCategory_ShouldReturnCreatedCategory() {
        CategoryCreateRequest request = new CategoryCreateRequest();
        CategoryResponse createdCategory = new CategoryResponse();
        when(categoryService.createCategory(request)).thenReturn(createdCategory);

        ResponseMessage<CategoryResponse> response = categoryController.createCategory(request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals(createdCategory, response.getObject());
        verify(categoryService, times(1)).createCategory(request);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getCategoryById_ShouldReturnCategory() {
        Long id = 1L;
        CategoryResponse categoryResponse = new CategoryResponse();
        when(categoryService.getCategoryById(id)).thenReturn(categoryResponse);

        ResponseMessage<CategoryResponse> response = categoryController.getCategoryById(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(categoryResponse, response.getObject());
        verify(categoryService, times(1)).getCategoryById(id);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void updateCategory_ShouldReturnUpdatedCategory() {
        Long id = 1L;
        CategoryRequest request = new CategoryRequest();
        CategoryResponse updatedCategory = new CategoryResponse();
        when(categoryService.updateCategory(id, request)).thenReturn(updatedCategory);

        ResponseMessage<CategoryResponse> response = categoryController.updateCategory(id, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(updatedCategory, response.getObject());
        verify(categoryService, times(1)).updateCategory(id, request);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void deleteCategory_ShouldReturnDeletedCategory() {
        Long id = 1L;
        CategoryResponse deletedCategory = new CategoryResponse();
        when(categoryService.deleteCategory(id)).thenReturn(deletedCategory);

        ResponseMessage<CategoryResponse> response = categoryController.deleteCategory(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(deletedCategory, response.getObject());
        verify(categoryService, times(1)).deleteCategory(id);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getPropertyKeysByCategoryId_ShouldReturnPropertyKeys() {
        Long id = 1L;
        List<PropertyKeyResponse> propertyKeys = Collections.emptyList();
        when(categoryService.getPropertyKeysByCategoryId(id)).thenReturn(propertyKeys);

        ResponseMessage<List<PropertyKeyResponse>> response = categoryController.getPropertyKeysByCategoryId(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(propertyKeys, response.getObject());
        verify(categoryService, times(1)).getPropertyKeysByCategoryId(id);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createPropertyKey_ShouldReturnCreatedPropertyKey() {
        Long id = 1L;
        PropertyKeyRequest request = new PropertyKeyRequest();
        PropertyKeyResponse createdPropertyKey = new PropertyKeyResponse();
        when(categoryService.createPropertyKey(id, request)).thenReturn(createdPropertyKey);

        ResponseMessage<PropertyKeyResponse> response = categoryController.createPropertyKey(id, request);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals(createdPropertyKey, response.getObject());
        verify(categoryService, times(1)).createPropertyKey(id, request);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void updatePropertyKey_ShouldReturnUpdatedPropertyKey() {
        Long id = 1L;
        PropertyKeyRequest request = new PropertyKeyRequest();
        PropertyKeyResponse updatedPropertyKey = new PropertyKeyResponse();
        when(categoryService.updatePropertyKey(id, request)).thenReturn(updatedPropertyKey);

        ResponseMessage<PropertyKeyResponse> response = categoryController.updatePropertyKey(id, request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(updatedPropertyKey, response.getObject());
        verify(categoryService, times(1)).updatePropertyKey(id, request);
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void deletePropertyKey_ShouldReturnDeletedPropertyKey() {
        Long id = 1L;
        PropertyKeyResponse deletedPropertyKey = new PropertyKeyResponse();
        when(categoryService.deletePropertyKey(id)).thenReturn(deletedPropertyKey);

        ResponseMessage<PropertyKeyResponse> response = categoryController.deletePropertyKey(id);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(deletedPropertyKey, response.getObject());
        verify(categoryService, times(1)).deletePropertyKey(id);
    }

    @Test
    void getCategoriesAsList_ShouldReturnCategoryList() {
        List<CategoryResponse> categoryList = Collections.emptyList();
        when(categoryService.getCategoriesAsList()).thenReturn(categoryList);

        ResponseMessage<List<CategoryResponse>> response = categoryController.getCategoriesAsList();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals(categoryList, response.getObject());
        verify(categoryService, times(1)).getCategoriesAsList();
    }
}
