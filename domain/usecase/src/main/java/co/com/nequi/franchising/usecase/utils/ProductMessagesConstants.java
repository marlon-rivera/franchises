package co.com.nequi.franchising.usecase.utils;

public class ProductMessagesConstants {

    public static final String ERROR_PRODUCT_NAME_NOT_NULL_OR_EMPTY = "The product name must not be null or empty.";
    public static final String ERROR_PRODUCT_QUANTITY_NOT_VALID = "The product quantity must be greater than zero.";
    public static final String ERROR_PRODUCT_QUANTITY_NOT_NULL = "The product quantity must not be null.";
    public static final String ERROR_PRODUCT_BRANCH_ID_NOT_VALID = "The product branch ID must not be null.";
    public static final String ERROR_PRODUCT_ID_OR_BRANCH_ID_NOT_VALID = "The product ID or branch ID must not be null.";
    public static final String ERROR_COMBINATION_PRODUCT_BRANCH_NOT_EXIST = "The combination of product and branch does not exist.";
    public static final String ERROR_PRODUCT_NOT_EXIST = "The product does not exist with ID: ";

    private ProductMessagesConstants(){}

}
