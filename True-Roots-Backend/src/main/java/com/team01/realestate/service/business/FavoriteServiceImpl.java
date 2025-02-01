package com.team01.realestate.service.business;

import com.team01.realestate.entity.concretes.business.Advert;
import com.team01.realestate.entity.concretes.business.Favorite;
import com.team01.realestate.entity.concretes.user.User;
import com.team01.realestate.exception.ResourceNotFoundException;
import com.team01.realestate.payload.mapper.AdvertMapper;
import com.team01.realestate.payload.mapper.FavoriteMapper;
import com.team01.realestate.payload.messages.ErrorMessages;
import com.team01.realestate.payload.response.business.AdvertResponse;
import com.team01.realestate.payload.response.business.FavoriteResponse;
import com.team01.realestate.repository.business.FavoriteRepository;
import com.team01.realestate.repository.user.UserRepository;
import com.team01.realestate.service.helper.MethodHelper;
import com.team01.realestate.service.helper.PageableHelper;
import com.team01.realestate.service.impl.FavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class
FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;
    private final UserRepository userRepository;
    private final AdvertMapper advertMapper;
    private final MethodHelper methodHelper;
    private final PageableHelper pageableHelper;

    @Override
    public List<FavoriteResponse> getFavoritesForAuthenticatedUser(HttpServletRequest httpServletRequest) {


        User user = methodHelper.findAuthenticatedUser(httpServletRequest);

        // Kullanıcının favorilerini getir
        List<Favorite> favorites = favoriteRepository.findByUserId(user.getId());

        // Favorileri `FavoriteResponse` nesnesine dönüştür
        return favorites.stream()
                .map(favorite -> {
                    Advert advert = favorite.getAdvert();
                    if (advert.getTitle() == null || advert.getTitle().isBlank()) {
                        throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_ADVERT_MESSAGE + favorite.getId());
                    }
                    return favoriteMapper.toResponse(favorite);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<FavoriteResponse> getAuthenticatedUserFavoritesPageable(String query, HttpServletRequest httpServletRequest, int page, int size, String sort, String type) {

        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        User user = methodHelper.findAuthenticatedUser(httpServletRequest);

        Long userId = user.getId();
        Page<Favorite> favorites = favoriteRepository.findByUserId(userId, pageable, query);

        return favorites.map(favorite -> {
            Advert advert = favorite.getAdvert();
            if (advert.getTitle() == null || advert.getTitle().isBlank()) {
                throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_ADVERT_MESSAGE + favorite.getId());
            }
            return favoriteMapper.toResponse(favorite);
        });
    }

    @Override
    public List<FavoriteResponse> getFavoritesForUser(Long userId) {

        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

        return favorites.stream()
                .map(favorite -> {
                    Advert advert = favorite.getAdvert();
                    if (advert.getTitle() == null || advert.getTitle().isBlank()) {
                        throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_ADVERT_MESSAGE + favorite.getId());
                    }
                    return favoriteMapper.toResponse(favorite);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<FavoriteResponse> getFavoritesByUserIdPageable(String query, Long userId, int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);

        Page<Favorite> favorites = favoriteRepository.findByUserId(userId, pageable, query);

        return favorites.map(favorite -> {
            Advert advert = favorite.getAdvert();
            if (advert.getTitle() == null || advert.getTitle().isBlank()) {
                throw new ResourceNotFoundException(ErrorMessages.NOT_FOUND_ADVERT_MESSAGE + favorite.getId());
            }
            return favoriteMapper.toResponse(favorite);
        });
    }


    /**
     * Toggles a favorite record for a given advert ID. If the advert is already in the favorites,
     * it is removed; otherwise, it is added. Returns the mapped AdvertResponse for the processed favorite.
     *
     * @param advertId the ID of the advert to toggle as favorite
     * @return AdvertResponse representing the advert details
     */
    @Override
    public AdvertResponse toggleFavorite(Long advertId, HttpServletRequest httpServletRequest) {
        // Kullanıcı bilgilerini al
        User foundUser = methodHelper.findAuthenticatedUser(httpServletRequest);

        if (foundUser == null) {
            throw new RuntimeException("Kullanıcı bulunamadı.");
        }

        // İlanı bul
        Advert advert = methodHelper.findAdvertById(advertId);
        if (advert == null) {
            throw new RuntimeException("İlan bulunamadı.");
        }

        // Kullanıcının favorisi olup olmadığını kontrol et
        Optional<Favorite> favoriteOptional = favoriteRepository.findByUserAndAdvert(foundUser, advert);

        if (favoriteOptional.isPresent()) {
            // Eğer favori zaten varsa, sil
            favoriteRepository.delete(favoriteOptional.get());
            System.out.println("İlan favorilerden çıkarıldı.");
        } else {
            // Favori yoksa, yeni bir favori ekle
            Favorite favorite = Favorite.builder()
                    .user(foundUser)
                    .advert(advert)
                    .createdAt(LocalDateTime.now())
                    .build();
            favoriteRepository.save(favorite);
            System.out.println("İlan favorilere eklendi.");
        }

        // Kullanıcının ilanı şu anda favori listesinde olup olmadığını kontrol et
        boolean isFavorited = favoriteRepository.existsByUserAndAdvert(foundUser, advert);

        // Cevap oluştur ve döndür
        return AdvertResponse.builder()
                .id(advert.getId())
                .title(advert.getTitle())
                .isFavorited(isFavorited)
                .build();
    }


    @Transactional
    @Override
    public void removeAllFavoritesCustomer(UserDetails userDetails) {

        //user validation
        String email = userDetails.getUsername();
        User user = userRepository.findByEmailOptional(email).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE + email));

        //remove all favorites
        favoriteRepository.deleteAllByUserId(user.getId());
    }

    @Transactional
    @Override
    public void removeAllFavoritesForAdmin(Long userId) {

    userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NOT_FOUND_USER_MESSAGE + userId));
        favoriteRepository.deleteAllByUserId(userId);
    }
    @Override
    public void removeFavorite(Long id) {
        //favorite validation
        Favorite favorite = favoriteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.NOT_FOUND_FAVORITE_MESSAGE + id));

        // Action - Delete
        favoriteRepository.delete(favorite);
    }


}
