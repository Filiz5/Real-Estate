package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.TourRequest;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.entity.enums.LogType;
import com.team01.realestate.entity.enums.TourRequestStatus;
import com.team01.realestate.exception.BadRequestException;
import com.team01.realestate.payload.mapper.TourRequestMapper;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.TourRequestRequest;
import com.team01.realestate.payload.response.business.ResponseMessage;
import com.team01.realestate.payload.response.business.TourRequestResponse;
import com.team01.realestate.repository.business.TourRequestRepository;
import com.team01.realestate.service.helper.MethodHelper;
import com.team01.realestate.service.helper.PageableHelper;
import com.team01.realestate.service.validator.DateTimeValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.team01.realestate.entity.concretes.user.Role;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourRequestService {

    private final TourRequestRepository tourRequestRepository;
    private final TourRequestMapper tourRequestMapper;
    private final MethodHelper methodHelper;
    private final LogService logService;
    private final PageableHelper pageableHelper;
    private final DateTimeValidator dateTimeValidator;

    public ResponseMessage<TourRequestResponse> createTourRequest(TourRequestRequest tourRequestRequest,
                                                                  HttpServletRequest httpServletRequest) {
        User guestUser = methodHelper.findAuthenticatedUser(httpServletRequest);
        Advert advertForTourRequest = methodHelper.findAdvertById(tourRequestRequest.getAdvertId());
        User ownerUser = methodHelper.findUserById(advertForTourRequest.getUser().getId());

        Long guestUserId = guestUser.getId();
        LocalDate tourDate = tourRequestRequest.getTourDate();
        LocalTime tourTime = tourRequestRequest.getTourTime();
        LocalTime tourTimePlusOneHour = tourTime.plusHours(1);
        LocalTime tourTimeMinusOneHour = tourTime.minusHours(1);

        if (guestUser.getId().equals(ownerUser.getId())) {
            throw new BadRequestException(ErrorMessages.CANNOT_CREATE_TOUR_REQUEST_FOR_OWN_ADVERT_MESSAGE);
        }

        if (tourRequestRepository.existsByGuestUserAndTourDateWithinHour(
                guestUserId, tourDate, tourTime, tourTimePlusOneHour, tourTimeMinusOneHour)) {
            throw new BadRequestException(ErrorMessages.TOUR_REQUEST_ALREADY_EXIST_MESSAGE_FOR_CUSTOMER);
        }
        if (!advertForTourRequest.getAdvertStatus().equals(AdvertStatus.ACTIVATED)) {
            throw new BadRequestException(ErrorMessages.ADVERT_IS_NOT_ACTIVATED);
        }


        TourRequest tourRequestToSave = tourRequestMapper.tourRequestRequestToTourRequest(tourRequestRequest);

        tourRequestToSave.setGuestUser(guestUser);
        tourRequestToSave.setOwnerUser(ownerUser);
        tourRequestToSave.setAdvert(advertForTourRequest);
        tourRequestToSave.setTourDate(tourDate);
        tourRequestToSave.setTourTime(tourTime);
        tourRequestToSave.setTourRequestStatus(TourRequestStatus.PENDING);

        TourRequest savedTourRequest = tourRequestRepository.save(tourRequestToSave);

        logService.createLogForTourRequest(
                LogType.TOUR_REQUEST_CREATED,
                "Tour Request is created on " + savedTourRequest.getTourDate() + " at " + savedTourRequest.getTourTime(),
                guestUser,
                ownerUser,
                advertForTourRequest
        );

        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestMapper.tourRequestToTourRequestResponse(savedTourRequest))
                .message(SuccessMessages.TOUR_REQUEST_CREATED)
                .httpStatus(HttpStatus.CREATED)
                .build();


    }

    public ResponseMessage<Page<TourRequestResponse>> getTourRequestsByAuthUser(String query, int page, int size, String sort, String type, HttpServletRequest httpServletRequest) {
        // Kullanıcının email adresini al
        String email = (String) httpServletRequest.getAttribute("username");

        // Pageable oluştur
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        // Kullanıcıya ait TourRequest verilerini getir
        Page<TourRequest> tourRequests = tourRequestRepository.findTourRequestsByAuthUser(email, query, pageable);

        // TourRequest verilerini TourRequestResponse'a dönüştür
        Page<TourRequestResponse> tourRequestResponses = tourRequests.map(tourRequestMapper::tourRequestToTourRequestResponse);

        // Response oluştur ve dön
        return ResponseMessage.<Page<TourRequestResponse>>builder()
                .object(tourRequestResponses)
                .message(SuccessMessages.TOUR_REQUEST_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public List<TourRequestResponse> getTourRequestsByUserId(Long userId) {

        // Kullanıcıya ait TourRequest verilerini getir
        List<TourRequest> tourRequests = tourRequestRepository.findTourRequestsByUserId(userId);

// TourRequest verilerini TourRequestResponse'a dönüştür

        // Response oluştur ve dön
        return tourRequests.stream()
                .map(tourRequestMapper::tourRequestToTourRequestResponse)
                .collect(Collectors.toList());
    }

    public ResponseMessage<Page<TourRequestResponse>> getAllTourRequestByPage(String query, int page, int size, String sort, String type) {
        // Pageable oluştur
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        // Veritabanından tüm TourRequest verilerini çek
        Page<TourRequest> tourRequests = tourRequestRepository.findAllByQuery(query, pageable);

        // TourRequest verilerini DTO'ya dönüştür
        Page<TourRequestResponse> tourRequestResponses = tourRequests.map(tourRequestMapper::tourRequestToTourRequestResponse);

        // Response oluştur ve dön
        return ResponseMessage.<Page<TourRequestResponse>>builder()
                .object(tourRequestResponses)
                .message(SuccessMessages.TOUR_REQUEST_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<TourRequestResponse> getTourRequestDetailForAuthUser(Long tourRequestId, HttpServletRequest httpServletRequest) {

        User authUser = methodHelper.findAuthenticatedUser(httpServletRequest);
        TourRequest foundTourRequest = methodHelper.findTourRequestById(tourRequestId);


        if (!foundTourRequest.getGuestUser().getId().equals(authUser.getId())) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        TourRequestResponse tourRequestResponse = tourRequestMapper.tourRequestToTourRequestResponse(foundTourRequest);

        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    public ResponseMessage<TourRequestResponse> getTourRequestDetailForAdmin(Long tourRequestId) {
        TourRequest foundTourRequest = methodHelper.findTourRequestById(tourRequestId);
        TourRequestResponse tourRequestResponse = tourRequestMapper.tourRequestToTourRequestResponse(foundTourRequest);

        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<TourRequestResponse> updateTourRequestForAuthUser(TourRequestRequest tourRequestRequest, Long tourRequestId,
                                                                             HttpServletRequest httpServletRequest) {

        User authUser = methodHelper.findAuthenticatedUser(httpServletRequest);
        TourRequest tourRequest = methodHelper.findTourRequestById(tourRequestId);
        TourRequestStatus status = tourRequest.getTourRequestStatus();

        if (!tourRequest.getGuestUser().getId().equals(authUser.getId())) {
            throw new AccessDeniedException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // Update edilmek istenen tour request status PENDING veya DECLINED degilse update islemine izin verilmiyor
        if(status == TourRequestStatus.APPROVED){
            throw new BadRequestException(ErrorMessages.STATUS_ALREADY_APPROVED);
        }
        if(status == TourRequestStatus.CANCELED){
            throw new BadRequestException(ErrorMessages.STATUS_ALREADY_CANCELED);
        }

        Long guestUserId = authUser.getId();
        LocalDate tourDate = tourRequestRequest.getTourDate();
        LocalTime tourTime = tourRequestRequest.getTourTime();
        LocalTime tourTimePlusOneHour = tourTime.plusHours(1);
        LocalTime tourTimeMinusOneHour = tourTime.minusHours(1);

        if (tourRequestRepository.existsByGuestUserAndTourDateWithinHour(
                guestUserId, tourDate, tourTime, tourTimePlusOneHour, tourTimeMinusOneHour)) {
            throw new BadRequestException(ErrorMessages.TOUR_REQUEST_ALREADY_EXIST_MESSAGE_FOR_CUSTOMER);
        }

        // Update edilen tour request in status pending olarak setleniyor
        tourRequest.setTourRequestStatus(TourRequestStatus.PENDING);

        // Diger field lar setleniyor
        tourRequest.setTourDate(tourDate);
        tourRequest.setTourTime(tourTime);

        // Kaydetmek  uzere DB ye gonderiliyor
        tourRequestRepository.save(tourRequest);

        TourRequestResponse tourRequestResponse = tourRequestMapper.tourRequestToTourRequestResponse(tourRequest);

        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<TourRequestResponse> cancelTourRequestForUser(Long tourRequestId, HttpServletRequest httpServletRequest) {

        User authUser = methodHelper.findAuthenticatedUser(httpServletRequest);

        TourRequest tourRequest = methodHelper.findTourRequestById(tourRequestId);
        TourRequestStatus status = tourRequest.getTourRequestStatus();

        if (!tourRequest.getGuestUser().getId().equals(authUser.getId())) {
            throw new AccessDeniedException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        if(status == TourRequestStatus.CANCELED){
            throw new BadRequestException(ErrorMessages.STATUS_ALREADY_CANCELED);
        }

        tourRequest.setTourRequestStatus(TourRequestStatus.CANCELED);

        logService.createLogForTourRequest(
                LogType.TOUR_REQUEST_CANCELED,
                "Tour request canceled by advert guest.",
                tourRequest.getGuestUser(),
                tourRequest.getOwnerUser(),
                tourRequest.getAdvert()
        );

        tourRequestRepository.save(tourRequest);

        TourRequestResponse tourRequestResponse = tourRequestMapper.tourRequestToTourRequestResponse(tourRequest);

        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_CANCELLED)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    public ResponseMessage<TourRequestResponse> approveTourRequestForUser(Long tourRequestId, HttpServletRequest httpServletRequest) {

        User authUser = methodHelper.findAuthenticatedUser(httpServletRequest);

        TourRequest tourRequest = methodHelper.findTourRequestById(tourRequestId);
        TourRequestStatus status = tourRequest.getTourRequestStatus();

        if (!tourRequest.getOwnerUser().getId().equals(authUser.getId())) {
            throw new AccessDeniedException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        if(status == TourRequestStatus.APPROVED){
            throw new BadRequestException(ErrorMessages.STATUS_ALREADY_APPROVED);
        }

        tourRequest.setTourRequestStatus(TourRequestStatus.APPROVED);
        logService.createLogForTourRequest(
                LogType.TOUR_REQUEST_ACCEPTED,
                "Tour request approved by advert owner.",
                tourRequest.getGuestUser(),
                tourRequest.getOwnerUser(),
                tourRequest.getAdvert()
        );


        tourRequestRepository.save(tourRequest);

        TourRequestResponse tourRequestResponse = tourRequestMapper.tourRequestToTourRequestResponse(tourRequest);

        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_APPROVED)
                .httpStatus(HttpStatus.OK)
                .build();
    }


    public ResponseMessage<TourRequestResponse> declineTourRequestForUser(Long tourRequestId, HttpServletRequest httpServletRequest) {

        User authUser = methodHelper.findAuthenticatedUser(httpServletRequest);
        TourRequest tourRequest = methodHelper.findTourRequestById(tourRequestId);
        TourRequestStatus status = tourRequest.getTourRequestStatus();

        if (!tourRequest.getOwnerUser().getId().equals(authUser.getId())) {
            throw new AccessDeniedException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        if(status == TourRequestStatus.DECLINED){
            throw new BadRequestException(ErrorMessages.STATUS_ALREADY_DECLINED);
        }

        tourRequest.setTourRequestStatus(TourRequestStatus.DECLINED);
        logService.createLogForTourRequest(
                LogType.TOUR_REQUEST_DECLINED,
                "Tour request declined by advert owner.",
                tourRequest.getGuestUser(),
                tourRequest.getOwnerUser(),
                tourRequest.getAdvert()
        );

        tourRequestRepository.save(tourRequest);

        TourRequestResponse tourRequestResponse = tourRequestMapper.tourRequestToTourRequestResponse(tourRequest);

        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_DECLINED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<TourRequestResponse> deleteTourRequestForAdminAndManager(Long tourRequestId, HttpServletRequest httpServletRequest) {

        User authUser = methodHelper.findAuthenticatedUser(httpServletRequest);
        TourRequest tourRequest = methodHelper.findTourRequestById(tourRequestId);

        // authUser ve rollerin null olup olmadığını kontrol et
        if (authUser == null || authUser.getRoles() == null) {
            throw new AccessDeniedException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // Kullanıcı rolünü belirle
        String role = authUser.getRoles().stream()
                .map(Role::getRoleName) // Role isimlerini al
                .filter(roleName -> roleName.equalsIgnoreCase("ADMIN") || roleName.equalsIgnoreCase("MANAGER"))
                .findFirst() // İlk eşleşen rolü bul
                .orElse(null); // Eşleşme yoksa null döner

        // Rol kontrolü
        if (role == null) {
            throw new AccessDeniedException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        try {


//            logService.createLogForTourRequest(
//                    LogType.TOUR_REQUEST_DELETED,
//                    "Tour request deleted by " + role + ".",
//                    tourRequest.getGuestUser(),
//                    tourRequest.getOwnerUser(),
//                    tourRequest.getAdvert()
// );

            tourRequestRepository.deleteById(tourRequestId);

            TourRequestResponse tourRequestResponse = tourRequestMapper.tourRequestToTourRequestResponse(tourRequest);

            return ResponseMessage.<TourRequestResponse>builder()
                    .object(tourRequestResponse)
                    .message(SuccessMessages.TOUR_REQUEST_DELETED)
                    .httpStatus(HttpStatus.OK)
                    .build();

        } catch (Exception e) {
            // Log generic error for unexpected issues
            logService.createLogForTourRequest(
                    LogType.ERROR,
                    "Unexpected error occurred while deleting tour request.",
                    null, null, null
            );
            throw new BadRequestException(ErrorMessages.CANNOT_DELETE_TOUR_REQUEST);
        }
    }


    public ResponseMessage<List<TourRequestResponse>> getTourRequestReports(LocalDate date1, LocalDate date2, TourRequestStatus status) {

        List<TourRequest> tourRequests = tourRequestRepository.findByFilters(date1, date2, status);

        if(tourRequests.isEmpty()) {
            return ResponseMessage.<List<TourRequestResponse>>builder()
                    .object(tourRequests.stream().map(tourRequestMapper::tourRequestToTourRequestResponse).toList())
                    .message(SuccessMessages.ADVERTS_LIST_IS_EMPTY)
                    .httpStatus(HttpStatus.OK)
                    .build();
        }

        List<TourRequestResponse> tourRequestResponses = tourRequests.stream().map(tourRequestMapper::tourRequestToTourRequestResponse).toList();

        return ResponseMessage.<List<TourRequestResponse>>builder()
                .object(tourRequestResponses)
                .message(SuccessMessages.TOUR_REQUEST_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();

    }



    public void deleteTourRequestsByUserId(Long userId){
        // Kullanıcıya ait TourRequest verilerini getir
        List<TourRequest> tourRequests = tourRequestRepository.findTourRequestsByUserId(userId);

        tourRequestRepository.deleteAll(tourRequests);
    }


    public ResponseMessage<TourRequestResponse> deleteTourRequestForGuest(Long tourRequestId, HttpServletRequest httpServletRequest) {

        User authUser = methodHelper.findAuthenticatedUser(httpServletRequest);
        TourRequest tourRequest = methodHelper.findTourRequestById(tourRequestId);

        if (!tourRequest.getGuestUser().getId().equals(authUser.getId()) || tourRequest.getOwnerUser().getId().equals(authUser.getId())) {
            throw new AccessDeniedException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        tourRequestRepository.deleteById(tourRequestId);

        TourRequestResponse tourRequestResponse = tourRequestMapper.tourRequestToTourRequestResponse(tourRequest);

        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestResponse)
                .message(SuccessMessages.TOUR_REQUEST_DELETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
