package com.team01.realestate.controller.business;

import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.AdvertTypeRequest;
import com.team01.realestate.payload.response.business.AdvertTypeResponse;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.service.business.AdvertTypeServiceIMPL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/advert-types")
public class AdvertTypeController {

    @Autowired
    private AdvertTypeServiceIMPL advertTypeService;

    /**
     * T-01  //Test edildi.
     //     * https://localhost:8080/advert-types
     * method: GET
     */
    @GetMapping
    public ResponseEntity<ResponseMessage<List<AdvertTypeResponse>>> getAllAdvertTypes() {
        List<AdvertTypeResponse> advertTypeResponses = advertTypeService.getAllAdvertType();
        ResponseMessage<List<AdvertTypeResponse>> response = ResponseMessage.<List<AdvertTypeResponse>>builder()
                .object(advertTypeResponses)
                .message(SuccessMessages.ADVERT_TYPES_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * T-02 //Test Edildi..
     * http://localhost:8080/advert-types/23
     */

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseMessage<AdvertTypeResponse>> getAdvertTypeById(@PathVariable Long id) {
        AdvertTypeResponse advertTypeResponse = advertTypeService.getAdvertTypeById(id);
        ResponseMessage<AdvertTypeResponse> response =ResponseMessage.<AdvertTypeResponse>builder()
                .message(SuccessMessages.ADVERT_TYPES_FOUND)
                .object(advertTypeResponse)
                .httpStatus(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);


    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * T-03 // Yeni AdvertType oluşturma
     */

    //https://localhost:8080/advert-types  + POST
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseMessage<AdvertTypeResponse>> createAdvertType(@RequestBody AdvertTypeRequest request) {
        AdvertTypeResponse advertTypeResponse = advertTypeService.createAdvertType(request);
        ResponseMessage<AdvertTypeResponse> response = ResponseMessage.<AdvertTypeResponse>builder()
                .message(SuccessMessages.ADVERT_TYPE_CREATED) // Başarı mesajı
                .object(advertTypeResponse)
                .httpStatus(HttpStatus.CREATED)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * T-04 // AdvertType güncelleme
     * http://localhost:8080/advert-types/id  +PUT
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseMessage<AdvertTypeResponse>> updateAdvertType(
            @PathVariable Long id,
            @RequestBody AdvertTypeRequest request) {

        AdvertTypeResponse updatedAdvertTypeResponse = advertTypeService.updateAdvertType(id, request);

        ResponseMessage<AdvertTypeResponse> response = ResponseMessage.<AdvertTypeResponse>builder()
                .message(SuccessMessages.ADVERT_TYPE_UPDATED) // Başarı mesajı
                .object(updatedAdvertTypeResponse)
                .httpStatus(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * T-05 // AdvertType silme
     *http://localhost:8080/advert-types/{id}  +DELETE
     */

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseMessage<Void>> deleteAdvertType(@PathVariable Long id) {
        advertTypeService.deleteAdvertType(id);

        ResponseMessage<Void> response = ResponseMessage.<Void>builder()
                .message(SuccessMessages.ADVERT_TYPE_DELETED) // Başarı mesajı
                .httpStatus(HttpStatus.OK) // 200 OK durumu döndür
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK ile döndür
    }

}



