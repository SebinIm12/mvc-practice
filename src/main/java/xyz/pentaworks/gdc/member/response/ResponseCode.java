package xyz.pentaworks.gdc.member.response;



import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Response Code 열거형
 * @author SENGJOON PAIK
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "bad-request"),
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "page-not-found"),
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "not-authorized"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "method-not-allowed"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal-server-error"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "invalid-parameter"),
    MAX_COUNT_OVER(HttpStatus.BAD_REQUEST, "max-count-over"),
    SUCCESS(HttpStatus.OK, "success"),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "login-fail"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "token-expired"),
    TOKEN_NON_EXISTS(HttpStatus.BAD_REQUEST, "token-non-exists"),
    USER_NON_EXISTS(HttpStatus.BAD_REQUEST, "user-non-exists"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "no-content"),
    ;

    private final HttpStatus httpStatus;
    private final String statusId;

}

