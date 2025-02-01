package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.Log;
import com.team01.realestate.entity.concretes.business.TourRequest;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.entity.enums.AdvertStatus;
import com.team01.realestate.entity.enums.LogType;
import com.team01.realestate.payload.request.business.AdvertRequestForUpdate;
import com.team01.realestate.repository.business.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public void createLog(LogType logType, String description, User user, Advert advert) {
        Log log = Log.builder()
                .log(logType)
                .description(description)
                .user(user)
                .advert(advert)
                .build();

        logRepository.save(log);
    }

    public void createLogForAdminUpdate(LogType logType,
                                        String description,
                                        User user,
                                        Advert advert,
                                        AdvertStatus oldStatus,
                                        AdvertRequestForUpdate advertRequestForUpdate) {

        StringBuilder statusChangeMessage = new StringBuilder();
        StringBuilder fieldChangesMessage = new StringBuilder();

        // Statü değişimi kontrolü
        if (oldStatus != null && !oldStatus.equals(advert.getAdvertStatus())) {
            statusChangeMessage.append(String.format("Advert status changed from %s to %s. ",
                    oldStatus.getDescription(), advert.getAdvertStatus().getDescription()));
        }

        // Başlık değişimi kontrolü
        if (!advert.getTitle().equals(advertRequestForUpdate.getTitle())) {
            fieldChangesMessage.append(String.format("Advert title changed from \"%s\" to \"%s\". ",
                    advert.getTitle(), advertRequestForUpdate.getTitle()));
        }

        // Açıklama değişimi kontrolü
        if (!advert.getDesc().equals(advertRequestForUpdate.getDesc())) {
            fieldChangesMessage.append("Advert description changed. ");
        }

        // Fiyat değişimi kontrolü
        if (!advert.getPrice().equals(advertRequestForUpdate.getPrice())) {
            fieldChangesMessage.append(String.format("Advert price changed from %s to %s. ",
                    advert.getPrice(), advertRequestForUpdate.getPrice()));
        }

        // Kategori değişimi kontrolü
        if (!advert.getCategory().getId().equals(advertRequestForUpdate.getCategory_id())) {
            fieldChangesMessage.append(String.format("Advert category id is changed from %d to %d. ",
                    advert.getCategory().getId(), advertRequestForUpdate.getCategory_id()));
        }

        // Log entry'sini oluştur
        Log log = Log.builder()
                .log(logType)
                .description(statusChangeMessage.toString() + fieldChangesMessage + description)
                .user(user)
                .advert(advert)
                .build();

        logRepository.save(log); // Logu kaydet
    }




    public void createLogForTourRequest(LogType logType, String description, User guestUser, User ownerUser, Advert advert){
        Log log = Log.builder()
                .log(logType)
                .description(description)
                .user(guestUser)
                .user(ownerUser)
                .advert(advert)
                .build();

        logRepository.save(log);
    }

    public List<Log> getLogsByUserId(Long userId){

        return logRepository.findLogsByUserId(userId);
    }
}

