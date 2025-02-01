package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.CategoryPropertyValue;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.entity.enums.LogType;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.AdvertMapper;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.messages.SuccessMessages;
import com.team01.realestate.payload.request.business.AdvertRequest;
import com.team01.realestate.payload.request.business.AdvertRequestForUpdate;
import com.team01.realestate.payload.request.business.UpdateAdvertStatusRequest;
import com.team01.realestate.payload.response.business.*;
import com.team01.realestate.repository.business.AdvertRepository;
import com.team01.realestate.repository.business.CategoryPropertyValueRepository;
import com.team01.realestate.service.helper.MethodHelper;
import com.team01.realestate.service.helper.PageableHelper;
import com.team01.realestate.service.user.EmailService;
import com.team01.realestate.util.SlugUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import com.team01.realestate.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdvertService {
    private final AdvertRepository advertRepository;
    private final AdvertMapper advertMapper;
    private final MethodHelper methodHelper;
    private final LogService logService;
    private final PageableHelper pageableHelper;
    private final CategoryPropertyValueRepository categoryPropertyValueRepository;
    private final EmailService emailService;
    public ResponseMessage<AdvertResponse> createAdvert(AdvertRequest advertRequest, HttpServletRequest httpServletRequest) {
        User foundUser = methodHelper.findAuthenticatedUser(httpServletRequest);

        String title = advertRequest.getTitle();
        String uniqueSlug = SlugUtil.generateUniqueSlug(title, advertRepository::existsBySlug);

        // Step 1: Convert AdvertRequest to Advert
        Advert advertToSave = advertMapper.advertRequestToAdvert(advertRequest);

        // Step 2: Set additional fields and persist Advert
        advertToSave.setAdvertStatus(AdvertStatus.PENDING);
        advertToSave.setSlug(uniqueSlug);
        advertToSave.setUser(foundUser);
        // Save the Advert first
        Advert savedAdvert = advertRepository.save(advertToSave);

        // Step 3: Now that Advert is saved, persist CategoryPropertyValues
        List<CategoryPropertyValue> categoryPropertyValues = savedAdvert.getCategoryPropertyValues();
        if (categoryPropertyValues != null && !categoryPropertyValues.isEmpty()) {
            categoryPropertyValueRepository.saveAll(categoryPropertyValues);  // Save property values after Advert is persisted
        }
        // Step 4: Log creation
        logService.createLog(
                LogType.CREATED,
                "Advert is created and waits for approval: " + savedAdvert.getTitle(),
                foundUser,
                savedAdvert
        );

        return ResponseMessage.<AdvertResponse>builder()
                .object(advertMapper.advertToAdvertResponse(savedAdvert))
                .message(SuccessMessages.ADVERT_CREATED)
                .httpStatus(HttpStatus.CREATED)
                .build();
    }


    public ResponseMessage<Page<AdvertResponse>> getAdvertSearchAndParametersPage(
            String query, Long categoryId, Long advertTypeId, BigDecimal priceStart,
            BigDecimal priceEnd, AdvertStatus status, int page, int size, String sort, String order) {

        try {
            Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, order);

            Page<Advert> adverts = advertRepository.findAdvertsWithParameters(
                    query,
                    categoryId,
                    advertTypeId,
                    priceStart,
                    priceEnd,
                    status,
                    pageable
            );

            Page<AdvertResponse> advertResponses = adverts.map(advertMapper::advertToAdvertResponse);
            return ResponseMessage.<Page<AdvertResponse>>builder()
                    .message(SuccessMessages.ADVERTS_FOUND)
                    .object(advertResponses)
                    .httpStatus(HttpStatus.OK)
                    .build();

        } catch (Exception e) {
            return ResponseMessage.<Page<AdvertResponse>>builder()
                    .message(e.getMessage())
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }//A01---------------------------------------♠♠♠♠♠♠

    // Şehirlere göre gruplanmış ilanları döndüren method
    public List<CityAdvertResponse> getAdvertsGroupedByCity() {
        // Veritabanından ilanları şehir bazında gruplayan sorguyu çalıştır
        List<Object[]> results = advertRepository.getAdvertsGroupedByCity();

        // Sorgu sonucunu DTO'ya dönüştür
        return results.stream()
                .map(result -> new CityAdvertResponse((String) result[0], ((Long) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    // Kategorilere göre gruplanmış ilanları döndüren method
    public List<CategoryAdvertResponse> getAdvertsGroupedByCategory() {
        // Veritabanından ilanları kategori bazında gruplayan sorguyu çalıştır
        List<Object[]> results = advertRepository.getAdvertsGroupedByCategory();

        // Sorgu sonucunu DTO'ya dönüştür
        return results.stream()
                .map(result -> new CategoryAdvertResponse((String) result[0], ((Long) result[1]).intValue()))
                .collect(Collectors.toList());
    }

    public ResponseMessage<List<AdvertResponse>> getPopularAdverts(int amount) {
        try {
            // Popüler ilanları alıyoruz
            List<Advert> popularAdverts = advertRepository.findPopularAdverts(amount);

            // Advert'leri AdvertResponse DTO'ya dönüştürüyoruz
            List<AdvertResponse> advertResponses = popularAdverts.stream()
                    .map(advertMapper::advertToAdvertResponse)
                    .collect(Collectors.toList());

            // Başarı mesajı ile döndürüyoruz
            return ResponseMessage.<List<AdvertResponse>>builder()
                    .message(SuccessMessages.ADVERTS_FOUND_BY_POPULARITY)
                    .httpStatus(HttpStatus.OK)
                    .object(advertResponses)
                    .build();

        } catch (Exception e) {
            // Hata durumunda dönecek mesaj
            return ResponseMessage.<List<AdvertResponse>>builder()
                    .message(ErrorMessages.NOT_FOUND_ADVERTS_BY_POPULARITY)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public ResponseMessage<List<AdvertResponse>> getPopularAdvertsForAdmin(int amount) {
        try {
            // Popüler ilanları alıyoruz
            List<Advert> popularAdverts = advertRepository.findPopularAdvertsByTourRequestCount(amount);

            // Advert'leri AdvertResponse DTO'ya dönüştürüyoruz
            List<AdvertResponse> advertResponses = popularAdverts.stream()
                    .map(advertMapper::advertToAdvertResponse)
                    .collect(Collectors.toList());

            // Başarı mesajı ile döndürüyoruz
            return ResponseMessage.<List<AdvertResponse>>builder()
                    .message(SuccessMessages.ADVERTS_FOUND_BY_POPULARITY)
                    .httpStatus(HttpStatus.OK)
                    .object(advertResponses)
                    .build();

        } catch (Exception e) {
            // Hata durumunda dönecek mesaj
            return ResponseMessage.<List<AdvertResponse>>builder()
                    .message(ErrorMessages.NOT_FOUND_ADVERTS_BY_POPULARITY)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }


    public ResponseMessage<Page<AdvertResponseForList>> getAuthenticatedUsersAdverts(String q, int page, int size, String sort, String type, HttpServletRequest httpServletRequest) {
        try {
            // HttpServletRequest üzerinden kullanıcının bilgilerini alıyoruz
            User authenticatedUser = methodHelper.findAuthenticatedUser(httpServletRequest);

           Long userId = authenticatedUser.getId();

            Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

            // Kullanıcıya ait ilanları buluyoruz, query eklendi
            Page<Advert> advertsPage = advertRepository.findUserAdvertsWithParameters(userId,q, pageable);

            // İlanları DTO'ya dönüştürme
            Page<AdvertResponseForList> advertResponses = advertsPage.map(advertMapper::advertToAdvertResponseForList);

            // Başarı mesajı ile döndürme
            return ResponseMessage.<Page<AdvertResponseForList>>builder()
                    .message(SuccessMessages.ADVERTS_FOUND)
                    .httpStatus(HttpStatus.OK)
                    .object(advertResponses)
                    .build();

        } catch (Exception e) {
            // Hata durumunda dönecek mesaj
            return ResponseMessage.<Page<AdvertResponseForList>>builder()
                    .message(ErrorMessages.NOT_FOUND_ADVERTS_MESSAGE)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public ResponseMessage<Page<AdvertResponseForList>> getAdvertSearchAndParametersPageForAdmin(
            String query, Long categoryId, Long advertTypeId, BigDecimal priceStart,
            BigDecimal priceEnd, AdvertStatus status, int page, int size, String sort, String order) {

        try {
            Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, order);

            Page<Advert> adverts = advertRepository.findAdvertsWithParametersForAdmin(
                    query,
                    categoryId,
                    advertTypeId,
                    priceStart,
                    priceEnd,
                    status,
                    pageable
            );

            Page<AdvertResponseForList> advertResponses = adverts.map(advertMapper::advertToAdvertResponseForList);
            return ResponseMessage.<Page<AdvertResponseForList>>builder()
                    .message(SuccessMessages.ADVERTS_FOUND)
                    .object(advertResponses)
                    .httpStatus(HttpStatus.OK)
                    .build();

        } catch (Exception e) {
            return ResponseMessage.<Page<AdvertResponseForList>>builder()
                    .message(ErrorMessages.NOT_FOUND_ADVERT_MESSAGE)
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    public AdvertResponse getAdvertBySlug(String slug) {
        Advert advert = advertRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ADVERT_MESSAGE, slug)));

        // View count artırılıyor
        advert.setViewCount(advert.getViewCount() + 1);

        // Güncellenmiş değeri kaydediyoruz
        advertRepository.save(advert);

        return advertMapper.advertToAdvertResponse(advert);
    }


    public AdvertResponse getAuthenticatedUsersAdvertById(Long advertId, HttpServletRequest httpServletRequest) {
     // Kullanıcının email bilgisi alınır
        User authenticatedUser = methodHelper.findAuthenticatedUser(httpServletRequest);

          // Verilen id ile ilgili ilan bulunur
          Advert foundAdvert = methodHelper.findAdvertById(advertId);

          // Eğer ilan sahibi oturum açan kullanıcı değilse, hata dönebiliriz
          if (!foundAdvert.getUser().equals(authenticatedUser)) {
              throw new BadRequestException(ErrorMessages.DONT_HAVE_AUTHORITY); //bu advert giriş yapmış kullanıcıya ait değil hatası.
          }

        // İlanın map edilmesi
        return advertMapper.advertToAdvertResponse(foundAdvert);
    }

    public AdvertResponse getAdvertByIdForAdmin(Long id) {

        // Verilen id ile ilgili ilan bulunur
        Advert foundAdvert = methodHelper.findAdvertById(id);

        // İlanın map edilmesi
        return advertMapper.advertToAdvertResponse(foundAdvert);
    }

    public AdvertResponse updateAuthenticatedUsersAdvertById(Long advertId,
                                                             HttpServletRequest httpServletRequest,
                                                             AdvertRequestForUpdate advertRequestForUpdate) {
        User authenticatedUser = methodHelper.findAuthenticatedUser(httpServletRequest);

        Advert foundAdvert = methodHelper.findAdvertById(advertId);

        if (foundAdvert.isBuiltIn()){
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_UPDATE);
        }
        // Eğer ilan sahibi oturum açan kullanıcı değilse, hata dönebiliriz
        if (!foundAdvert.getUser().equals(authenticatedUser)) {
            throw new BadRequestException(ErrorMessages.DONT_HAVE_AUTHORITY); //bu advert giriş yapmış kullanıcıya ait değil hatası.
        }

        Advert updatedAdvert = advertMapper.advertRequestToUpdatedAdvert(advertRequestForUpdate, foundAdvert);

        updatedAdvert.setAdvertStatus(AdvertStatus.PENDING);
        updatedAdvert.setActive(advertRequestForUpdate.isActive());

        advertRepository.save(updatedAdvert);

        logService.createLog(
                LogType.UPDATED,
                "Advert is updated by user: "+ authenticatedUser.getFirstName() + updatedAdvert.getTitle(),
                authenticatedUser,
                updatedAdvert
        );

        return advertMapper.advertToAdvertResponse(updatedAdvert);

    }

    public AdvertResponse updateAdvertById(Long advertId,
                                           AdvertRequestForUpdate advertRequestForUpdate,
                                           HttpServletRequest httpServletRequest) {

        User authenticatedAdmin = methodHelper.findAuthenticatedUser(httpServletRequest);

        Advert foundAdvert = methodHelper.findAdvertById(advertId);

        if (foundAdvert.isBuiltIn()) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_UPDATE);
        }

        // Statü değişikliklerini kontrol et
        AdvertStatus oldStatus = foundAdvert.getAdvertStatus(); // Eski statüyü kaydet
        LogType logType = LogType.UPDATED; // Varsayılan olarak UPDATED

        // Eğer yeni bir statü sağlanmışsa, güncelle
        if (advertRequestForUpdate.getAdvertStatus() != null) {
            foundAdvert.setAdvertStatus(advertRequestForUpdate.getAdvertStatus());
            // Statü değişiklikleri için log türünü belirleyelim
            logType = getLogType(foundAdvert, oldStatus);
        }

        String title = advertRequestForUpdate.getTitle();
        // Başlık değişmişse, yeni bir slug oluştur
        if (!(foundAdvert.getTitle().equals(title))) {
            String uniqueSlug = SlugUtil.generateUniqueSlug(title, advertRepository::existsBySlug);
            foundAdvert.setSlug(uniqueSlug);
        }

        Advert updatedAdvert = advertMapper.advertRequestToUpdatedAdvert(advertRequestForUpdate, foundAdvert);

        advertRepository.save(updatedAdvert); // Güncellenmiş advertü kaydet

        // Güncellenmiş advertün kullanıcısını al
        User updatedAdvertsUser = updatedAdvert.getUser();

        // Log kaydet
        logService.createLogForAdminUpdate(
                logType,
                "Advert updated by admin: " + authenticatedAdmin.getFirstName() + " - " + updatedAdvert.getTitle(),
                updatedAdvertsUser,
                updatedAdvert,
                oldStatus, // Eski statü ile birlikte logla
                advertRequestForUpdate // Değişiklikleri kontrol etmek için
        );

        return advertMapper.advertToAdvertResponse(updatedAdvert); // Güncellenmiş advertü döndür
    }


    static LogType getLogType(Advert foundAdvert, AdvertStatus oldStatus) {
        LogType logType;

        // Sadece statü değişikliği olmuşsa log türünü belirle
        if (!oldStatus.equals(foundAdvert.getAdvertStatus())) {
            if (foundAdvert.getAdvertStatus() == AdvertStatus.REJECTED) {
                logType = LogType.DECLINED; // Statü "REJECTED" olduysa DECLINED log kaydedilir
            } else  {
                logType = LogType.UPDATED; // Statü "ACTIVATED" olduysa UPDATED log kaydedilir
            }
        } else {
            logType = LogType.UPDATED; // Eğer statü değişmemişse, UPDATE logu kullanılır
        }

        return logType;
    }


    public ResponseMessage<AdvertResponse> deleteAdvert(Long advertId) {

    //    String email = (String) httpServletRequest.getAttribute("username");
    //    User authenticatedAdmin = methodHelper.findUserByEmail(email);

        //advert'ı repositroy'de var mı
        Advert foundAdvert = advertRepository.findById(advertId).orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.NOT_FOUND_ADVERTS_MESSAGE,advertId)));
        //bunu genel bir methoda çevirebiliriz daha sonra

        //built-in kontrolü
        if (foundAdvert.isBuiltIn()) {
            throw new BadRequestException(ErrorMessages.ADVERT_NOT_DELETE_MESSAGE_FOR_BUILTIN);
        }

        //Advert ve adverta bağlı olan diğer alanları da sil
        //Advert Entityde cascade özelliği eklendi
        // tourRequest-favorites-logs-images-categoryPropertyValues-category CascadeType.All özelliği ile advert silindiğinde silinecek
        advertRepository.delete(foundAdvert);

        return ResponseMessage.<AdvertResponse>builder()
                .object(advertMapper.advertToAdvertResponse(foundAdvert))
                .message(SuccessMessages.ADVERT_DELETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<List<AdvertResponse>> getAdvertsReports(LocalDateTime date1, LocalDateTime date2, Long categoryId, Long typeId, AdvertStatus status) {

        List<Advert> adverts = advertRepository.findByFilters(date1, date2, categoryId, typeId, status);

        if(adverts.isEmpty()) {
            return ResponseMessage.<List<AdvertResponse>>builder()
                    .object(adverts.stream().map(advertMapper::advertToAdvertResponse).toList())
                    .message(SuccessMessages.ADVERTS_LIST_IS_EMPTY)
                    .httpStatus(HttpStatus.OK)
                    .build();
        }

        List<AdvertResponse> advertResponses = adverts.stream().map(advertMapper::advertToAdvertResponse).toList();

        return ResponseMessage.<List<AdvertResponse>>builder()
                .object(advertResponses)
                .message(SuccessMessages.ADVERTS_FOUND)
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<Set<AdvertResponse>> getUsersAdvertsById(Long userId) {
        User foundUser = methodHelper.findUserById(userId);
        Set<Advert> foundAdverts=foundUser.getAdverts();

        return ResponseMessage.<Set<AdvertResponse>>builder()
                .httpStatus(HttpStatus.OK)
                .message(SuccessMessages.USERS_ADVERTS_FOUND)
                .object(foundAdverts.stream()
                        .map(advertMapper::advertToAdvertResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

    public ResponseMessage<AdvertResponse> approveAdvert(Long advertId,
                                                         HttpServletRequest httpServletRequest,
                                                         UpdateAdvertStatusRequest updateAdvertStatusRequest) {
        User authenticatedAdmin = methodHelper.findAuthenticatedUser(httpServletRequest);

        Advert foundAdvert = methodHelper.findAdvertById(advertId);

        if (foundAdvert.isBuiltIn()) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_UPDATE);
        }
        System.out.println(updateAdvertStatusRequest.getStatus());
        AdvertStatus oldStatus = foundAdvert.getAdvertStatus(); // Eski statüyü kaydet
        Advert advert = setAdvertStatus(foundAdvert, updateAdvertStatusRequest);

        // Statü değişiklikleri için log türünü belirle
        LogType logType = getLogType(advert, oldStatus);


        // Güncellenmiş advertü kaydet
        Advert updatedAdvert = advertRepository.save(advert);
        sendMail(updatedAdvert, updateAdvertStatusRequest);


        return ResponseMessage.<AdvertResponse>builder()
                .object(null)
                .message(SuccessMessages.ADVERT_STATUS_UPDATED)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    Advert setAdvertStatus(Advert advert, UpdateAdvertStatusRequest status) {
        if (status.getStatus().equalsIgnoreCase("REJECTED")) {
            advert.setAdvertStatus(AdvertStatus.REJECTED);
        } else if (status.getStatus().equalsIgnoreCase("ACTIVATED")) {
            advert.setAdvertStatus(AdvertStatus.ACTIVATED);
        } else if(status.getStatus().equalsIgnoreCase("PENDING")) {
            advert.setAdvertStatus(AdvertStatus.PENDING);
        } else {
            throw new BadRequestException(ErrorMessages.INVALID_STATUS);
        }
        return  advert;
    }

    void sendMail(Advert updatedAdvert, UpdateAdvertStatusRequest updateAdvertStatusRequest) {
        if (updatedAdvert.getAdvertStatus().equals(AdvertStatus.ACTIVATED)) {
            emailService.sendEmail(updatedAdvert.getUser().getEmail(), "Advert Approved", "Your advert has been approved.");
        }else if (updatedAdvert.getAdvertStatus().equals(AdvertStatus.REJECTED)) {
            String message = "Your advert has been rejected. "+
                    "\nReason: " + updateAdvertStatusRequest.getRejectMessage();
            emailService.sendEmail(updatedAdvert.getUser().getEmail(), "Advert Rejected", message);
        }else if(updatedAdvert.getAdvertStatus().equals(AdvertStatus.PENDING)) {
            String message = "Your advert status is changed to pending."+
                    "\nReason: " + updateAdvertStatusRequest.getRejectMessage();
            emailService.sendEmail(updatedAdvert.getUser().getEmail(), "Advert Pending", message);
        }
    }

    public ResponseMessage<String> deleteAuthenticatedUsersAdvertById(Long advertId, HttpServletRequest httpServletRequest) {
        User authenticatedUser = methodHelper.findAuthenticatedUser(httpServletRequest);

        Advert foundAdvert = methodHelper.findAdvertById(advertId);

        if (foundAdvert.isBuiltIn()) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_UPDATE);
        }

        if (!foundAdvert.getUser().equals(authenticatedUser)) {
            throw new BadRequestException(ErrorMessages.DONT_HAVE_AUTHORITY);
        }

        advertRepository.delete(foundAdvert);

        return ResponseMessage.<String>builder()
                .object(null)
                .message(SuccessMessages.ADVERT_DELETED)
                .httpStatus(HttpStatus.OK)
                .build();
    }
}


