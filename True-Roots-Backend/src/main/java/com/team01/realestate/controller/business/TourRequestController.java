package com.team01.realestate.controller.business;

import com.team01.realestate.payload.request.business.TourRequestRequest;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.business.TourRequestResponse;
import com.team01.realestate.payload.response.user.UserResponse;
import com.team01.realestate.service.business.TourRequestService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Path;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tour-requests")
public class TourRequestController {

    private final TourRequestService tourRequestService;

    //S05
    // create a tourRequest
    //https://localhost:8080/tour-requests + POST
    @PostMapping
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<TourRequestResponse>createTourRequest(@RequestBody @Valid TourRequestRequest tourRequestRequest,
                                                                 HttpServletRequest httpServletRequest){
        return tourRequestService.createTourRequest(tourRequestRequest, httpServletRequest);

    }

    // S01
    // get authenticated user’s tour requests
    // http://localhost:8080/tour-requests/auth?q=a&page=0&size=10&sort=createdAt&type=desc + GET
    @GetMapping("/auth") // F09
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<Page<TourRequestResponse>> getTourRequestsByAuthUser(
            @RequestParam(value = "q", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type,
            HttpServletRequest httpServletRequest){

        return tourRequestService.getTourRequestsByAuthUser(query, page, size, sort, type, httpServletRequest);

    }

    // get authenticated user’s tour requests
    // http://localhost:8080/tour-requests/auth?q=a&page=0&size=10&sort=createdAt&type=desc + GET
   // @GetMapping("/{userId}/admin") // F09
   // @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
//    public List<TourRequestResponse> getTourRequestsByUserId(
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size,
//            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
//            @RequestParam(value = "type", defaultValue = "desc") String type,
//            @PathVariable Long userId){
//
//        return tourRequestService.getTourRequestsByUserId(userId);
//
//    }

    // S02
    // get all tour requests
    // http://localhost:8080/tour-requests/admin?q=a&page=0&size=10&sort=createdAt&type=desc + GET
    @GetMapping("/admin") // F09
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<Page<TourRequestResponse>> getAllTourRequestByPage(
            @RequestParam(value = "q", defaultValue = "") String query,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type){

        return tourRequestService.getAllTourRequestByPage(query, page, size, sort, type);

    }

    // S03
    // get tour request detail for auth user
    // http://localhost:8080/tour-requests/{tourRequestId}/auth + GET
    @GetMapping("/{tourRequestId}/auth") // F09
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<TourRequestResponse> getTourRequestDetailForAuthUser(@PathVariable Long tourRequestId, HttpServletRequest httpServletRequest){
        return tourRequestService.getTourRequestDetailForAuthUser(tourRequestId, httpServletRequest);
    }

    // S04
    // get tour request detail for admin
    // http://localhost:8080/tour-requests/{tourRequestId}/admin + GET
    @GetMapping("/{tourRequestId}/admin") // F09
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<TourRequestResponse> getTourRequestDetailForAdmin(@PathVariable Long tourRequestId){
        return tourRequestService.getTourRequestDetailForAdmin(tourRequestId);
    }

    // S06
    // update tour request detail for admin
    // http://localhost:8080/tour-requests/{tourRequestId}/auth + PUT
    @PutMapping("/{tourRequestId}/auth")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<TourRequestResponse> updateTourRequestForUser(@RequestBody @Valid TourRequestRequest tourRequestRequest,
                                                                         @PathVariable Long tourRequestId, HttpServletRequest httpServletRequest){
        return tourRequestService.updateTourRequestForAuthUser(tourRequestRequest, tourRequestId, httpServletRequest);

    }

    // S07
    // cancel tour request for customer
    // http://localhost:8080/tour-requests/{tourRequestId}/cancel + PATCH
    @PatchMapping("/{tourRequestId}/cancel")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<TourRequestResponse> cancelTourRequestForUser(@PathVariable Long tourRequestId, HttpServletRequest httpServletRequest){
        return tourRequestService.cancelTourRequestForUser(tourRequestId, httpServletRequest);
    }

    // S08
    // approve tour request  for customer
    // http://localhost:8080/tour-requests/{tourRequestId}/approve + PATCH
    @PatchMapping("/{tourRequestId}/approve")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<TourRequestResponse> approveTourRequestForUser(@PathVariable Long tourRequestId, HttpServletRequest httpServletRequest){
        return tourRequestService.approveTourRequestForUser(tourRequestId, httpServletRequest);
    }

    // S09
    // decline tour request  for customer
    // http://localhost:8080/tour-requests/{tourRequestId}/decline + PATCH
    @PatchMapping("/{tourRequestId}/decline")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<TourRequestResponse> declineTourRequestForUser(@PathVariable Long tourRequestId, HttpServletRequest httpServletRequest){
        return tourRequestService.declineTourRequestForUser(tourRequestId, httpServletRequest);
    }

    // S10
    // delete tour request  for admin and manager
    // http://localhost:8080/tour-requests/{tourRequestId} + DELETE
    @DeleteMapping("/{tourRequestId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    public ResponseMessage<TourRequestResponse> deleteTourRequestForAdminAndManager(@PathVariable Long tourRequestId, HttpServletRequest httpServletRequest){
        return tourRequestService.deleteTourRequestForAdminAndManager(tourRequestId, httpServletRequest);
    }

    @DeleteMapping("/{tourRequestId}/delete")
    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
    public ResponseMessage<TourRequestResponse> deleteTourRequestForGuest(@PathVariable Long tourRequestId, HttpServletRequest httpServletRequest){
        return tourRequestService.deleteTourRequestForGuest(tourRequestId, httpServletRequest);
    }


}
