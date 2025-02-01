package com.team01.realestate.service;

;

import com.team01.realestate.payload.mapper.AdvertMapper;
import com.team01.realestate.payload.mapper.FavoriteMapper;

import com.team01.realestate.repository.business.FavoriteRepository;
import com.team01.realestate.repository.user.UserRepository;
import com.team01.realestate.service.business.FavoriteServiceImpl;
import com.team01.realestate.service.helper.MethodHelper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FavoriteMapper favoriteMapper;

    @Mock
    private AdvertMapper advertMapper;

    @Mock
    private MethodHelper methodHelper;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;


    /**
     * Kullanıcı bulunduğunda ve favoriler varsa, doğru sayıda FavoriteResponse döndürüldüğünü kontrol et.
     */
//    @Test
//    void getFavoritesForAuthenticatedUser_ShouldReturnFavorites_WhenUserAndFavoritesExist() {
//        // Arrange
//        String email = "test@example.com";
//        UserDetails userDetails = Mockito.mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn(email);
//
//        User user = new User();
//        user.setId(1L);
//
//        Advert advert = new Advert();
//        advert.setId(1L);
//        advert.setTitle("Test Advert Title");
//
//        Favorite favorite = new Favorite();
//        favorite.setId(1L);
//        favorite.setAdvert(advert); // Advert set ediliyor
//
//        List<Favorite> favorites = List.of(favorite);
//
//        when(userRepository.findByEmail(email)).thenReturn(user);
//        when(favoriteRepository.findByUserId(1L)).thenReturn(favorites);
//
//        FavoriteResponse favoriteResponse = new FavoriteResponse();
//        when(favoriteMapper.toResponse(favorite)).thenReturn(favoriteResponse);
//
//        // Act
//        List<FavoriteResponse> result = favoriteService.getFavoritesForAuthenticatedUser(userDetails);
//
//        // Assert
//        Assertions.assertNotNull(result);
//        assertEquals(1, result.size());
//        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
//        Mockito.verify(favoriteRepository, Mockito.times(1)).findByUserId(1L);
//        Mockito.verify(favoriteMapper, Mockito.times(1)).toResponse(favorite);
//    }

    /**
     * Kullanıcı Bulunamadığında Hata
     */
//    @Test
//    void getFavoritesForAuthenticatedUser_ShouldThrowException_WhenUserNotFound() {
//        // Arrange
//        String email = "test@example.com";
//        UserDetails userDetails = Mockito.mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn(email);
//
//        when(userRepository.findByEmail(email)).thenReturn(null);
//
//        // Act & Assert
//        ResourceNotFoundException exception = Assertions.assertThrows(
//                ResourceNotFoundException.class,
//                () -> favoriteService.getFavoritesForAuthenticatedUser(userDetails)
//        );
//
//        assertEquals("Error: User not found with id %stest@example.com", exception.getMessage());
//        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
//    }

    /**
     * Favoriler bulunamadığında hata fırlatılmasını kontrol eder.
     */
//    @Test
//    void getFavoritesForAuthenticatedUser_ShouldThrowException_WhenFavoritesNotFound() {
//        // Arrange
//        String email = "test@example.com";
//        UserDetails userDetails = Mockito.mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn(email);
//
//        User user = new User();
//        user.setId(1L);
//
//        // Kullanıcı mevcut, ancak favoriler boş bir liste dönecek
//        when(userRepository.findByEmail(email)).thenReturn(user);
//        when(favoriteRepository.findByUserId(1L)).thenReturn(List.of());
//
//        // Act & Assert
//        ResourceNotFoundException exception = Assertions.assertThrows(
//                ResourceNotFoundException.class,
//                () -> favoriteService.getFavoritesForAuthenticatedUser(userDetails)
//        );
//
//        // Hata mesajını doğrula
//        assertEquals("Error: Favorite not found with id %s1", exception.getMessage());
//
//        // Mock'ların çağrıldığını doğrula
//        Mockito.verify(userRepository, Mockito.times(1)).findByEmail(email);
//        Mockito.verify(favoriteRepository, Mockito.times(1)).findByUserId(1L);
//    }


    /**
     * SUCCESS
     * favoriteRepository.findByUserId(userId) çağrısı ile favori listesi döndürülür.
     * Her Favorite için advertMapper.mapToAdvertResponse çağrılır.
     * AdvertResponse nesnesi kontrol edilir.
     */
//    @Test
//    void getFavoritesForUser_ShouldReturnAdvertResponses_WhenFavoritesExist() {
//        // Arrange
//        Long userId = 1L;
//
//        Advert advert = new Advert();
//        advert.setId(1L);
//        advert.setTitle("Test Advert Title");
//
//        Favorite favorite = new Favorite();
//        favorite.setId(1L);
//        favorite.setAdvert(advert);
//
//        List<Favorite> favorites = List.of(favorite);
//
//        when(favoriteRepository.findByUserId(userId)).thenReturn(favorites);
//
//        AdvertResponse advertResponse = new AdvertResponse();
//        advertResponse.setId(1L);
//        advertResponse.setTitle("Test Advert Title");
//
//        // Doğru nesneyi mapToAdvertResponse'a veriyoruz
//        when(advertMapper.mapToAdvertResponse(favorite)).thenReturn(advertResponse);
//
//        // Act
//       // List<AdvertResponse> result = favoriteService.getFavoritesForUser(userId);
//
//        // Assert
//        Assertions.assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("Test Advert Title", result.get(0).getTitle());
//
//        Mockito.verify(favoriteRepository, Mockito.times(1)).findByUserId(userId);
//        Mockito.verify(advertMapper, Mockito.times(1)).mapToAdvertResponse(favorite);
//    }

    /**
     * Favoriler Bulunamadığında Hata:
     * favoriteRepository.findByUserId(userId) çağrısı boş bir liste döndürür.
     * Metot ResourceNotFoundException fırlatır ve hata mesajı doğrulanır.
     */
//    @Test
//    void getFavoritesForUser_ShouldThrowException_WhenFavoritesNotFound() {
//        // Arrange
//        Long userId = 1L;
//
//        // Favoriler bulunmadığı durum
//        when(favoriteRepository.findByUserId(userId)).thenReturn(List.of());
//
//        // Act & Assert
//        ResourceNotFoundException exception = Assertions.assertThrows(
//                ResourceNotFoundException.class,
//                () -> favoriteService.getFavoritesForUser(userId)
//        );
//
//        // Hata mesajını doğrula
//        assertEquals("Error: Favorite not found with id %s1", exception.getMessage());
//
//        Mockito.verify(favoriteRepository, Mockito.times(1)).findByUserId(userId);
//    }


    /**
     * Başarılı Durum:
     * methodHelper.toggleFavoriteAddOrRemove çağrıldığında bir AdvertResponse döner.
     * Dönen yanıtın doğru olup olmadığını ve methodHelper'ın doğru şekilde çağrıldığını doğrularız.
     */
//    @Test
//    void toggleFavorite_ShouldReturnAdvertResponse_WhenMethodHelperWorksCorrectly() {
//        // Arrange
//        Long advertId = 1L;
//
//        AdvertResponse advertResponse = new AdvertResponse();
//        advertResponse.setId(advertId);
//        advertResponse.setTitle("Test Advert Title");
//
//        // methodHelper davranışını mockluyoruz
//        when(methodHelper.toggleFavoriteAddOrRemove(advertId)).thenReturn(advertResponse);
//
//        // Act
//        AdvertResponse result = favoriteService.toggleFavorite(advertId);
//
//        // Assert
//        Assertions.assertNotNull(result);
//        assertEquals(advertId, result.getId());
//        assertEquals("Test Advert Title", result.getTitle());
//
//        // methodHelper'ın çağrıldığını doğrula
//        Mockito.verify(methodHelper, Mockito.times(1)).toggleFavoriteAddOrRemove(advertId);
//    }

    /**
     * Hata Durumu:
     * Eğer methodHelper.toggleFavoriteAddOrRemove bir istisna fırlatırsa, favoriteService.toggleFavorite metodunun bu istisnayı düzgün şekilde fırlattığını kontrol ederiz.
     */
//    @Test
//    void toggleFavorite_ShouldThrowException_WhenMethodHelperFails() {
//        // Arrange
//        Long advertId = 1L;
//
//        // methodHelper istisna fırlatıyor
//        when(methodHelper.toggleFavoriteAddOrRemove(advertId))
//                .thenThrow(new RuntimeException("Unexpected error"));
//
//        // Act & Assert
//        RuntimeException exception = Assertions.assertThrows(
//                RuntimeException.class,
//                () -> favoriteService.toggleFavorite(advertId)
//        );
//
//        assertEquals("Unexpected error", exception.getMessage());
//
//        // methodHelper'ın çağrıldığını doğrula
//        Mockito.verify(methodHelper, Mockito.times(1)).toggleFavoriteAddOrRemove(advertId);
//    }

    /**
     * Başarılı Durum:
     * userRepository.findByEmailDelete(email) doğru bir User nesnesi döndürür.
     * favoriteRepository.deleteAllByUserId(user.getId()) metodu çağrılır ve favoriler silinir.
     */

//    @Test
//    void removeAllFavoritesCustomer_ShouldDeleteAllFavorites_WhenUserExists() {
//        // Arrange
//        String email = "test@example.com";
//        UserDetails userDetails = Mockito.mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn(email);
//
//        User user = new User();
//        user.setId(1L);
//
//        when(userRepository.findByEmailOptional(email)).thenReturn(Optional.of(user));
//
//        // Act
//        favoriteService.removeAllFavoritesCustomer(userDetails);
//
//        // Assert
//        Mockito.verify(userRepository, Mockito.times(1)).findByEmailOptional(email);
//        Mockito.verify(favoriteRepository, Mockito.times(1)).deleteAllByUserId(1L);
//    }

    /**
     * Kullanıcı Bulunamadığında Hata:
     * userRepository.findByEmailDelete(email) boş bir sonuç döndürür.
     * ResourceNotFoundException fırlatılır.
     * favoriteRepository.deleteAllByUserId çağrılmaz.
     */

//    @Test
//    void removeAllFavoritesCustomer_ShouldThrowException_WhenUserNotFound() {
//        // Arrange
//        String email = "test@example.com";
//        UserDetails userDetails = Mockito.mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn(email);
//
//        when(userRepository.findByEmailOptional(email)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        ResourceNotFoundException exception = Assertions.assertThrows(
//                ResourceNotFoundException.class,
//                () -> favoriteService.removeAllFavoritesCustomer(userDetails)
//        );
//
//        // Hata mesajını doğrula
//        assertEquals("Error: User not found with id %stest@example.com", exception.getMessage());
//
//        Mockito.verify(userRepository, Mockito.times(1)).findByEmailOptional(email);
//        Mockito.verify(favoriteRepository, Mockito.times(0)).deleteAllByUserId(Mockito.anyLong());
//    }

    /**
     * Başarılı Durum:
     * userRepository.findById(userId) doğru bir User nesnesi döndürür.
     * favoriteRepository.deleteAllByUserId(userId) çağrılır ve favoriler silinir.
     */

//    @Test
//    void removeAllFavoritesForAdmin_ShouldDeleteAllFavorites_WhenUserExists() {
//        // Arrange
//        Long userId = 1L;
//
//        User user = new User();
//        user.setId(userId);
//
//        // Kullanıcı var
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        // Act
//        favoriteService.removeAllFavoritesForAdmin(userId);
//
//        // Assert
//        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
//        Mockito.verify(favoriteRepository, Mockito.times(1)).deleteAllByUserId(userId);
//    }

    /**
     * Kullanıcı Bulunamadığında Hata:
     * userRepository.findById(userId) boş bir sonuç döndürür (Optional.empty).
     * ResourceNotFoundException fırlatılır.
     * favoriteRepository.deleteAllByUserId metodu hiç çağrılmaz.
     */
//    @Test
//    void removeAllFavoritesForAdmin_ShouldThrowException_WhenUserNotFound() {
//        // Arrange
//        Long userId = 1L;
//
//        // Kullanıcı bulunamıyor
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        ResourceNotFoundException exception = Assertions.assertThrows(
//                ResourceNotFoundException.class,
//                () -> favoriteService.removeAllFavoritesForAdmin(userId)
//        );
//
//        // Hata mesajını doğrula
//        assertEquals("Error: User not found with id %s1", exception.getMessage());
//
//        Mockito.verify(userRepository, Mockito.times(1)).findById(userId);
//        Mockito.verify(favoriteRepository, Mockito.times(0)).deleteAllByUserId(Mockito.anyLong());
//    }

    /**
     * Başarılı Durum:     *
     * Geçerli bir id sağlandığında, ilgili favori (Favorite) başarıyla silinir.
     */

//    @Test
//    void removeFavorite_ShouldDeleteFavorite_WhentFavoriteExists() {
//        Long favoriteId = 1L;
//
//        Favorite favorite = new Favorite();
//        favorite.setId(favoriteId);
//
//        //if favorite exists
//        when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.of(favorite));
//
//        //action
//        favoriteService.removeFavorite(favoriteId);
//
//        //assertion
//        Mockito.verify(favoriteRepository, Mockito.times(1)).findById(favoriteId);
//        Mockito.verify(favoriteRepository, Mockito.times(1)).delete(favorite);
//    }

    /**
     * Favori Bulunamadığında Hata:
     * Geçersiz bir id sağlandığında, ResourceNotFoundException fırlatılır.
     */

//    @Test
//    void removeFavorite_ShouldThrowException_WhenFavoriteNotFound() {
//
//        // Arrange
//        Long favoriteId = 1L;
//
//        // Favori bulunamıyor
//        Mockito.when(favoriteRepository.findById(favoriteId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        ResourceNotFoundException exception = Assertions.assertThrows(
//                ResourceNotFoundException.class,
//                () -> favoriteService.removeFavorite(favoriteId)
//        );
//
//        // Hata mesajını doğrula
//        Assertions.assertEquals("Error: Favorite not found with id %s1", exception.getMessage());
//
//        // Mock'ların doğru çağrıldığını doğrula
//        Mockito.verify(favoriteRepository, Mockito.times(1)).findById(favoriteId);
//        Mockito.verify(favoriteRepository, Mockito.times(0)).delete(Mockito.any());
//    }
}








