package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.AdvertType;
import com.team01.realestate.exception.BadRequestException;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.request.business.AdvertTypeRequest;
import com.team01.realestate.payload.response.business.AdvertTypeResponse;
import com.team01.realestate.repository.business.AdvertTypeRepository;
import com.team01.realestate.service.impl.AdvertTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdvertTypeServiceIMPL implements AdvertTypeService {

    @Autowired
    private AdvertTypeRepository advertTypeRepository;


//    public ResponseMessage<Page<AdvertType>> getAllAdvertTypes(int page, int size, String sort, String type) {
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
//        if (Objects.equals(type, "desc")) {
//            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
//        }
//        Page<AdvertType> advertTypes = advertTypeRepository.getAllAdvertTypes(pageable);
//
//        return ResponseMessage.<Page<AdvertType>>builder()
//                .message(SuccessMessages.ADVERT_TYPES_FOUND)
//                .object(advertTypes)
//                .httpStatus(HttpStatus.OK)
//                .build();
//    }

    public AdvertTypeResponse getAdvertTypeById(Long id) {

        AdvertType advertType = advertTypeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Advert type not found"));
        return AdvertTypeResponse.fromEntity(advertType);
    }

    @Override
    public List<AdvertTypeResponse> getAllAdvertType() {
        return advertTypeRepository.findAll().stream().map(AdvertTypeResponse::fromEntity).collect(Collectors.toList());
    }


    public AdvertTypeResponse createAdvertType(AdvertTypeRequest request) {
        AdvertType advertType = AdvertTypeResponse.toEntity(request); // DTO'dan entity'e dönüştür
        AdvertType savedAdvertType = advertTypeRepository.save(advertType); // Veritabanına kaydet
        return AdvertTypeResponse.fromEntity(savedAdvertType); // Yanıt DTO'sunu oluştur
    }



    public AdvertTypeResponse updateAdvertType(Long id, AdvertTypeRequest request) {
        AdvertType advertType = advertTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advert type not found"));

        if (advertType.isBuiltIn())
        {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_UPDATE);
        }
        // Güncellenen alanları ayarlayın
        advertType.setTitle(request.getTitle());
        advertType.setBuiltIn(request.isBuiltIn());

        AdvertType updatedAdvertType = advertTypeRepository.save(advertType); // Güncellenmiş nesneyi kaydet
        return AdvertTypeResponse.fromEntity(updatedAdvertType); // Yanıt DTO'sunu oluştur
    }



    public void deleteAdvertType(Long id) {
        AdvertType advertType = advertTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Advert Type not found"));

        if (advertType.isBuiltIn())
        {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_DELETE);
        }
        advertTypeRepository.delete(advertType); // AdvertType'ı sil
    }
}

