package com.project.healthy_life_was.healthy_life.common.constant;

import java.util.function.Supplier;

public class ResponseMessage {
    public static final String SUCCESS = "Success";
    public static final String VALIDATION_FAIL = "Validation failed.";
    public static final String DATABASE_ERROR = "Database error.";

    public static final String EXIST_DATA = "Data already exists.";
    public static final String EXIST_USER_NAME = "UserName already exists.";
    public static final String EXIST_USER_NICK_NAME = "UserNickName already exists.";
    public static final String EXIST_USER_EMAIL = "UserEmail already exists.";
    public static final String NOT_EXIST_DATA = "Data does not exist.";
    public static final String NOT_EXIST_USER = "User does not exist.";

    public static final String NOT_EXIST_POST = "Post does not exist.";
    public static final String NOT_EXIST_WORD = "Word does not exist.";

    public static final String DUPLICATED_USER_NAME = "Duplicated userName.";
    public static final String DUPLICATED_TEL_NICKNAME = "Duplicated NickName.";
    public static final String DUPLICATED_REVIEW = "Duplicated review.";

    public static final String SIGN_IN_FAIL = "Sign in failed.";
    public static final String AUTHENTICATION_FAIL = "Authentication failed.";
    public static final String NO_PERMISSION = "No permission.";
    public static final String EMAIL_AUTH_FAIL = "Email authentication failed.";
    public static final String NOT_MATCH_PASSWORD = "Password does not match.";
    public static final String  UNAUTHORIZED_USER = "Unauthorized User";

    public static final String TOKEN_CREATE_FAIL = "Token creation failed.";
    public static final String MESSAGE_SEND_FAIL = "Failed to send authentication number.";
    public static final String MESSAGE_TOKEN_SUCCESS = "Success.";

    public static final String PASSWORD_MISMATCH = "Password does not match with confirm password.";
    public static final String INCORRECT_CURRENT_PASSWORD = "Current password is incorrect.";
    public static final String NOT_EXIST_PHYSIQUE = "This physique tag does not exist";
    public static final String FAIL_TO_CONVERT = "Fail to convert";

    public static final String NOT_EXIST_PRODUCT = "Not Exist Product";
    public static final String NOT_EXIST_WISH_lIST_ITEM = "Not exist Wish list item";
    public static final String NOT_EXIST_WISH_LIST = "Not exist Wish list";
    public static final String ALREADY_EXIST_IN_WISHLIST = "This Item is already Exist";
    public static final String NOT_EXIST_PRODUCT_IN_WISH_LIST = "This item is not exist in wishlist";
    public static final String PURCHASE_INVENTORY = "Purchase volume exceeds inventory";
    public static final String NOT_EXIST_ORDER = "Not exist order";
    public static final String NOT_EXIST_SHIPPING = "Not exist shipping";;
    public static final String INVALID_METHOD = "Invalid method";;
    public static final String ALREADY_EXIST_SHIPPING = "Shipping is already exist";
    public static final String CAN_NOT_CANCEL = "This order cannot be canceled";
    public static final String CAN_NOT_EXCHANGE = "Exchange is not available during shipping.";
    public static final String CAN_NOT_RETURN = "Return is not available during shipping.";
    public static final String CAN_NOT_CHANGE_STATUS = "Only products purchased within 7 days can have their order status changed.";
    public static final String NOT_RETURN_EXCHANGE = "The return and Exchange are not available during shipping";
}
