package com.team01.realestate.payload.messages;

public class ErrorMessages {


    private ErrorMessages() {
    }

    public static final String ROLE_NOT_FOUND = "Role does not exist: %s";

    public static final String NOT_FOUND_USER_MESSAGE = "Error: User not found with ID %s";
    public static final String NOT_FOUND_USER_WITH_MESSAGE = "No user found with this email: %s";
    public static final String NOT_FOUND_ROLE_MESSAGE = "Error: Role not found with ID %s";
    public static final String NOT_FOUND_ADVERT_MESSAGE = "Error: Advert not found with ID %s";
    public static final String NOT_FOUND_ADVERT_SLUG = "Advert not found with slug: %s";
    public static final String NOT_FOUND_ADVERTS_MESSAGE = "Error: Could not retrieve adverts";
    public static final String NOT_FOUND_ADVERTS_GROUPED_BY_CITY_MESSAGE = "Could not retrieve adverts grouped by city";
    public static final String NOT_FOUND_ADVERTS_GROUPED_BY_CATEGORY_MESSAGE = "Could not retrieve adverts grouped by category";
    public static final String NOT_FOUND_ADVERTS_BY_POPULARITY = "Could not fetch popular adverts";
    public static final String NOT_FOUND_ADVERT_TYPE_MESSAGE = "Error: Advert type not found with ID %s";
    public static final String NOT_FOUND_CITY_MESSAGE = "Error: City not found with ID %s";
    public static final String NOT_FOUND_COUNTRY_MESSAGE = "Error: Country not found with ID %s";
    public static final String NOT_FOUND_DISTRICT_MESSAGE = "Error: District not found with ID %s";
    public static final String NOT_FOUND_CATEGORY_MESSAGE = "Error: Category not found with ID %s";
    public static final String NOT_FOUND_CATEGORY_PROPERTY_VALUE_MESSAGE = "Error: Category property value not found with ID %s";
    public static final String NOT_FOUND_CATEGORY_PROPERTY_KEY_MESSAGE = "Error: Category property key not found with ID %s";
    public static final String NOT_FOUND_IMAGE_MESSAGE = "Error: Image not found with ID %s";
    public static final String NOT_FOUND_LOG_MESSAGE = "Error: Log not found with ID %s";
    public static final String NOT_FOUND_TOUR_REQUEST_MESSAGE = "Error: Tour request not found with ID %s";
    public static final String NOT_FOUND_FAVORITE_MESSAGE = "Error: Favorite not found with ID %s";

    public static final String DONT_HAVE_AUTHORITY = "You do not have permission to perform this action";
    public static final String NOT_PERMITTED_METHOD_MESSAGE = "You do not have permission to perform this operation";
    public static final String PASSWORD_NOT_MATCHED = "Current password is incorrect";
    public static final String PASSWORD_IS_SAME = "The new password cannot be the same as the old password";
    public static final String INVALID_RESET_CODE = "The reset code is invalid";
    public static final String NOT_PERMITTED_UPDATE = "Built-in data cannot be updated";
    public static final String NOT_PERMITTED_DELETE = "Built-in data cannot be deleted";

    public static final String ALREADY_REGISTER_MESSAGE_EMAIL = "Email: %s already exists";
    public static final String ALREADY_REGISTER_MESSAGE_PHONE = "Phone number: %s already exists";

    public static final String IMAGES_NOT_FOUND_BY_ID = "Images not found with ID: %s";
    public static final String IMAGES_COULD_NOT_UPLOADED = "Images could not be uploaded";

    public static final String INTERNAL_ERROR = "An unexpected error occurred. Please try again later.";

    public static final String IMAGES_ALREADY_FEATURED = "This advert already has a featured image. Please unset the current one first.";

    public static final String FAVORITES_NOT_FOUND_BY_ID = "Favorites not found with ID: %s";

    public static final String ADVERT_NOT_DELETE_MESSAGE_FOR_BUILTIN = "Error: This advert cannot be deleted";

    // TOUR REQUEST
    public static final String STATUS_ALREADY_CANCELED = "The tour request is already canceled";
    public static final String STATUS_ALREADY_APPROVED = "The tour request is already approved";
    public static final String STATUS_ALREADY_DECLINED = "The tour request is already declined";
    public static final String STATUS_PENDING_OR_APPROVED = "The tour request is pending or approved by the customer";
    public static final String DB_RESET_FAILED = "Error: Database could not be reset";
    public static final String TIME_NOT_VALID_MESSAGE = "Error: The start date cannot be later than the end date";
    public static final String TOUR_REQUEST_ALREADY_EXIST_MESSAGE_FOR_CUSTOMER = "You already have another tour request for this date and time";
    public static final String CANNOT_CREATE_TOUR_REQUEST_FOR_OWN_ADVERT_MESSAGE = "You cannot create a tour request for your own advert";
    public static final String CANNOT_DELETE_TOUR_REQUEST = "You cannot delete the tour request";
    public static final String INVALID_STATUS = "Invalid status";

    public static final String ADVERT_IS_NOT_ACTIVATED = "Advert status is not activated";

}
