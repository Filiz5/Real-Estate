package com.team01.realestate.controller.business;

import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.AdvertRequest;
import com.team01.realestate.payload.request.business.AdvertRequestForUpdate;
import com.team01.realestate.payload.request.business.UpdateAdvertStatusRequest;
import com.team01.realestate.payload.response.business.*;
import com.team01.realestate.service.business.AdvertService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/adverts")
public class AdvertController {

    private final AdvertService advertService;

    //A10
    // create an advert
    //http://localhost:8080/adverts/ + POST
    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<AdvertResponse> createAdvert(@RequestBody @Valid AdvertRequest advertsRequest,
                                                        HttpServletRequest httpServletRequest) {
        return advertService.createAdvert(advertsRequest, httpServletRequest);
    }

    //A01
    //http://localhost:8080/adverts?q=Beautiful&categoryId=1&advertType=1&priceStart=750000&priceEnd=750000.50&status=0&page=0&size=10&sort=createdAt&type=asc
    @GetMapping
    public ResponseMessage<Page<AdvertResponse>> getAdvertsSearchAndParametersPage(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "advertType", required = false) Long advertTypeId,
            @RequestParam(value = "priceStart", required = false) BigDecimal priceStart,
            @RequestParam(value = "priceEnd", required = false) BigDecimal priceEnd,
            @RequestParam(value = "status", required = false) Integer statusValue,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "type", defaultValue = "asc") String type
    ) {
        // AdvertStatus enum'a dönüştürme işlemi
        AdvertStatus status = (statusValue != null) ? AdvertStatus.fromValue(statusValue) : AdvertStatus.ACTIVATED;

        // Parametrelerle servis metodunu çağırma
        return advertService.getAdvertSearchAndParametersPage(
                q, categoryId, advertTypeId, priceStart, priceEnd, status, page, size, sort, type
        );
    }

    //A02
    //http://localhost:8080/adverts/cities
    //şehir bazında ilanları döndüren method
    @GetMapping("/cities")
    public ResponseMessage<List<CityAdvertResponse>> getAdvertsGroupedByCity() {
        try {
            List<CityAdvertResponse> cityAdverts = advertService.getAdvertsGroupedByCity();
            return ResponseMessage.<List<CityAdvertResponse>>builder()
                    .message(SuccessMessages.ADVERTS_FOUND_GROUPED_BY_CITIES)
                    .httpStatus(HttpStatus.OK)
                    .object(cityAdverts)
                    .build();
        } catch (Exception e) {
            return ResponseMessage.<List<CityAdvertResponse>>builder()
                    .message(ErrorMessages.NOT_FOUND_ADVERTS_GROUPED_BY_CITY_MESSAGE)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    //A03
    //http://localhost:8080/adverts/categories
    //kategori bazında ilanları döndüren method
    @GetMapping("/categories")
    public ResponseMessage<List<CategoryAdvertResponse>> getAdvertsGroupedByCategory() {
        try {
            List<CategoryAdvertResponse> categoryAdverts = advertService.getAdvertsGroupedByCategory();
            return ResponseMessage.<List<CategoryAdvertResponse>>builder()
                    .message(SuccessMessages.ADVERTS_FOUND_GROUPED_BY_CATEGORIES)
                    .httpStatus(HttpStatus.OK)
                    .object(categoryAdverts)
                    .build();
        } catch (Exception e) {
            return ResponseMessage.<List<CategoryAdvertResponse>>builder()
                    .message(ErrorMessages.NOT_FOUND_ADVERTS_GROUPED_BY_CATEGORY_MESSAGE)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    //A04
    //http://localhost:8080/adverts/popular
    @GetMapping("/popular")
    public ResponseMessage<List<AdvertResponse>> getPopularAdverts(
            @RequestParam(value = "amount", defaultValue = "10") Integer amount,
            @PathVariable(required = false) Integer pathAmount) {

        // Eğer path parametresi varsa, onu kullan
        int finalAmount = (pathAmount != null) ? pathAmount : amount;

        return advertService.getPopularAdverts(finalAmount);
    }

    //A05
    //http://localhost:8080/adverts/auth
    @GetMapping("/auth")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<Page<AdvertResponseForList>> getUserAdverts(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "category_id") String sort,
            @RequestParam(value = "type", defaultValue = "asc") String type,
            HttpServletRequest httpServletRequest) {

            // Başarı ile döndürüyoruz
            return advertService.getAuthenticatedUsersAdverts(q,page, size,sort,type, httpServletRequest);
        }

    @DeleteMapping("/auth/{advertId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<String> deleteAuthenticatedUsersAdvertById(@PathVariable Long advertId, HttpServletRequest httpServletRequest) {
        return advertService.deleteAuthenticatedUsersAdvertById(advertId, httpServletRequest);
    }

    //A06
    //https://localhost:8080/adverts/admin?q=ev&categoryId=2&advertType=1&priceStart=10000&priceEnd=50000&status=1&page=2&size=20&sort=createdAt&type=desc
    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<Page<AdvertResponseForList>> getAdvertsSearchAndParametersPageForAdmin(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "advertType", required = false) Long advertType,
            @RequestParam(value = "priceStart", required = false) BigDecimal priceStart,
            @RequestParam(value = "priceEnd", required = false) BigDecimal priceEnd,
            @RequestParam(value = "status", required = false) Integer statusValue,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "type", defaultValue = "asc") String type
    ) {
        AdvertStatus status = (statusValue != null) ? AdvertStatus.fromValue(statusValue) : null;
        return advertService.getAdvertSearchAndParametersPageForAdmin(
                q, categoryId, advertType, priceStart, priceEnd, status, page, size, sort, type
        );
    }

    //A07
    //http://localhost:8080/adverts/{slug}
    @GetMapping("/{slug}")
    public ResponseMessage<AdvertResponse> getAdvertBySlug(@PathVariable String slug) {
        AdvertResponse advertResponse = advertService.getAdvertBySlug(slug);

        return ResponseMessage.<AdvertResponse>builder()
                        .object(advertResponse)
                        .message(SuccessMessages.ADVERT_FOUND)
                        .httpStatus(HttpStatus.OK)
                        .build();
    }

    //A08
    //http://localhost:8080/adverts/1/auth
    @GetMapping("/{advertId}/auth")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<AdvertResponse> getAuthenticatedUsersAdvertById(@PathVariable Long advertId, HttpServletRequest httpServletRequest) {
        AdvertResponse advertResponse = advertService.getAuthenticatedUsersAdvertById(advertId, httpServletRequest);

        return ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(SuccessMessages.ADVERT_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //A09
    //http://localhost:8080/adverts/1/admin
    @GetMapping("/{advertId}/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<AdvertResponse> getAdvertById(@PathVariable Long advertId) {
        AdvertResponse advertResponse = advertService.getAdvertByIdForAdmin(advertId);

        return ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(SuccessMessages.ADVERT_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // A11 - Update Advert for Authenticated Users
    @PutMapping("/auth/{advertId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<AdvertResponse> updateAuthenticatedUsersAdvertById(
            @PathVariable Long advertId,
            @RequestBody @Valid AdvertRequestForUpdate advertRequestForUpdate,
            HttpServletRequest httpServletRequest) {
        System.out.println("In advert update controller method");
        AdvertResponse advertResponse = advertService.updateAuthenticatedUsersAdvertById(advertId, httpServletRequest, advertRequestForUpdate);

        return ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(SuccessMessages.ADVERT_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // A12 - Update Advert for Admins
    @PutMapping("/admin/{advertId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<AdvertResponse> updateAdvertById(
            @PathVariable Long advertId,
            @RequestBody @Valid AdvertRequestForUpdate advertRequestForUpdate,
            HttpServletRequest httpServletRequest) {

        AdvertResponse advertResponse = advertService.updateAdvertById(advertId, advertRequestForUpdate, httpServletRequest);

        return ResponseMessage.<AdvertResponse>builder()
                .object(advertResponse)
                .message(SuccessMessages.ADVERT_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //A13
    //http://localhost:8080/adverts/admin/5
    @DeleteMapping("admin/{advertId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<AdvertResponse> deleteAdvert(@PathVariable Long advertId){

return advertService.deleteAdvert(advertId);


    }


    //
    //http://localhost:8080/adverts/getUsersAdvertsById:userId
    @GetMapping("getUsersAdvertsById/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<Set<AdvertResponse>> getUsersAdvertsById(@PathVariable Long userId){

        return advertService.getUsersAdvertsById(userId);


    }

    @PutMapping("admin/{advertId}/statusUpdate")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<AdvertResponse> approveAdvert(@PathVariable Long advertId, HttpServletRequest httpServletRequest, @RequestBody @Valid UpdateAdvertStatusRequest updateAdvertStatusRequest) {
        return advertService.approveAdvert(advertId, httpServletRequest, updateAdvertStatusRequest);
    }



}


















