package co.com.nequi.franchising.utils.constants;

public class BranchConstants {

    public static final String ERROR_BRANCH_NAME_NOT_NULL_OR_EMPTY = "Branch name must not be null or empty";
    public static final String ENDPOINT_CREATE_BRANCH = "/api/branch/create";
    public static final String ERROR_BRANCH_ID_MUST_BE_NUMERIC = "Branch ID must be numeric";
    public static final String ERROR_BRANCH_FRANCHISE_ID_NOT_VALID = "Branch franchise ID must not be null";
    public static final String PATH_VARIABLE_BRANCH_ID = "branchId";
    public static final String ENDPOINT_UPDATE_BRANCH_NAME = "/api/branch/{branchId}/update-name";

    private BranchConstants() {
    }

}
