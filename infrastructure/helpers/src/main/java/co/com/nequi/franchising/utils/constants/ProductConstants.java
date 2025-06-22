package co.com.nequi.franchising.utils.constants;

public class ProductConstants {

    public static final String ERROR_PRODUCT_NAME_NOT_NULL_OR_EMPTY = "The product name must not be null or empty.";
    public static final String ERROR_PRODUCT_QUANTITY_NOT_VALID = "The product quantity must be greater than zero.";
    public static final String ERROR_PRODUCT_QUANTITY_NOT_NULL = "The product quantity must not be null.";
    public static final String ERROR_PRODUCT_BRANCH_ID_NOT_VALID = "The product branch ID must not be null.";
    public static final String ENDPOINT_CREATE_PRODUCT = "/api/product/create";

    private ProductConstants() {}

}
