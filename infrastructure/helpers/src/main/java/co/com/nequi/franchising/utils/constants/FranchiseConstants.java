package co.com.nequi.franchising.utils.constants;

public class FranchiseConstants {

    public static final String ERROR_FRANCHISE_NAME_NOT_NULL_OR_EMPTY = "Franchise name must not be null or empty";
    public static final String ERROR_ID_MUST_BE_NUMERIC = "The franchise ID must be numeric.";
    public static final String ENDPOINT_CREATE_FRANCHISE = "/api/franchise/create";
    public static final String ENDPOINT_GET_TOP_PRODUCTS_BY_BRANCH = "/api/franchise/{franchiseId}/top-products";
    public static final String PATH_VARIABLE_FRANCHISE_ID = "franchiseId";

    private FranchiseConstants() {
    }

}
