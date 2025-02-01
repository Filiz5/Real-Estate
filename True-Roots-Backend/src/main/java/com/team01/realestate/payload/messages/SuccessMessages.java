package com.team01.realestate.payload.messages;

public class SuccessMessages {

    private SuccessMessages() {
    }

    public static final String USER_LOGIN = "User logged in successfully";
    public static final String USER_REGISTER = "User registered successfully";
    public static final String USER_CREATE = "User created successfully";
    public static final String USER_FOUND = "User found successfully";
    public static final String USER_UPDATE = "Your information updated successfully";
    public static final String USER_UPDATE_FOR_SUPERUSER = "User updated successfully";
    public static final String USER_DELETE = "User deleted successfully";
    public static final String PASSWORD_CHANGED_RESPONSE_MESSAGE = "Password changed successfully";
    public static final String ADVERTS_USER_FOUND = "Advert's user is found successfully";
    public static final String ADMIN_CREATED = "Built-in admin created successfully";

    public static final String ADVERT_TYPES_FOUND = "Advert types found successfully";
    public static final String ADVERT_TYPE_CREATED = "Advert type created successfully";
    public static final String ADVERT_TYPE_UPDATED = "Advert type updated successfully";
    public static final String ADVERT_TYPE_DELETED = "Advert type deleted successfully";

    public static final String COUNTRIES_FOUND = "Countries retrieved successfully";
    public static final String CITIES_FOUND = "Cities retrieved successfully";
    public static final String DISTRICTS_FOUND = "Districts retrieved successfully";

    public static final String IMAGES_FOUND_SUCCESSFULLY = "Image found successfully";
    public static final String IMAGES_SAVED_SUCCESSFULLY = "Images uploaded successfully";
    public static final String IMAGES_DELETED_SUCCESSFULLY = "Image deleted successfully";
    public static final String IMAGES_UPDATED_SUCCESSFULLY = "Image updated successfully";

    public static final String FAVORITES_FOUND = "Favorites found successfully, ID: %s";
    public static final String FAVORITES_FOUND_SUCCESSFULLY = "Favorites retrieved successfully";

    public static final String FAVORITE_TOGGLED_SUCCESSFULLY = "Favorite toggled successfully";
    public static final String ADVERT_DELETED_FROM_FAVORITES_SUCCESSFULLY = "Advert removed from favorites successfully";
    public static final String ADVERT_ADDED_TO_FAVORITES_SUCCESSFULLY = "Advert added to favorites successfully";
    public static final String FAVORITES_DELETED_SUCCESSFULLY = "Favorites deleted successfully";
    public static final String FAVORITE_DELETED_SUCCESSFULLY = "Favorite deleted successfully, ID: %s";

    public static final String REPORT_CREATED_SUCCESSFULLY = "Report created successfully";
    public static final String DB_RESET_SUCCESSFULLY = "Database reset successfully except for built-in data";

    // ADVERTS
    public static final String ADVERT_CREATED = "Advert created successfully";
    public static final String ADVERT_DELETED = "Advert and its features deleted successfully";
    public static final String ADVERT_UPDATED = "Advert updated successfully";
    public static final String ADVERT_FOUND = "Advert found successfully";
    public static final String ADVERTS_FOUND = "Adverts found successfully";
    public static final String ADVERTS_FOUND_GROUPED_BY_CITIES = "Adverts grouped by city retrieved successfully";
    public static final String ADVERTS_FOUND_GROUPED_BY_CATEGORIES = "Adverts grouped by category retrieved successfully";
    public static final String ADVERTS_FOUND_BY_POPULARITY = "Popular adverts retrieved successfully";
    public static final String ADVERTS_LIST_IS_EMPTY = "No adverts found based on your filter";
    public static final String USERS_ADVERTS_FOUND = "User's adverts found successfully";

    // TOUR REQUESTS
    public static final String TOUR_REQUEST_CREATED = "Tour request created successfully";
    public static final String TOUR_REQUEST_DELETED = "Tour request deleted successfully";
    public static final String TOUR_REQUEST_UPDATED = "Tour request updated successfully";
    public static final String TOUR_REQUEST_FOUND = "Tour request found successfully";
    public static final String TOUR_REQUEST_CANCELLED = "Tour request cancelled successfully";
    public static final String TOUR_REQUEST_APPROVED = "Tour request approved successfully";
    public static final String TOUR_REQUEST_DECLINED = "Tour request declined successfully";
    public static final String ADVERT_STATUS_UPDATED = "Advert status updated successfully";
}
