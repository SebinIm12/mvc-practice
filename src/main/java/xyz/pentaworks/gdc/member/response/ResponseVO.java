package xyz.pentaworks.gdc.member.response;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

/**
 * Common Response VO
 */
@Data
@Builder(builderMethodName = "responseVOBuilder")
public class ResponseVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String serviceCode;
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String jsonwebtoken;

    public static ResponseVOBuilder builder(ResponseCode responseCode) {
        MessageSource messageSource = BeanUtil.getBean(MessageSource.class);
        return responseVOBuilder().timestamp(LocalDateTime.now())
                .status(responseCode.getHttpStatus().value())
                .message(messageSource.getMessage(responseCode.getStatusId() + ".message",  null, "", LocaleContextHolder.getLocale()))
                .serviceCode(messageSource.getMessage(responseCode.getStatusId() + ".code",  null, "", LocaleContextHolder.getLocale()));
    }

    public static ResponseVOBuilder builder(ResponseCode responseCode, String[] args) {
        MessageSource messageSource = BeanUtil.getBean(MessageSource.class);
        return responseVOBuilder().timestamp(LocalDateTime.now())
                .status(responseCode.getHttpStatus().value())
                .message(messageSource.getMessage(responseCode.getStatusId() + ".message",  args, "", LocaleContextHolder.getLocale()))
                .serviceCode(messageSource.getMessage(responseCode.getStatusId() + ".code",  args, "", LocaleContextHolder.getLocale()));
    }

    public static ResponseVOBuilder builder(ResponseCode responseCode, String jToken) {
        MessageSource messageSource = BeanUtil.getBean(MessageSource.class);
        return responseVOBuilder().timestamp(LocalDateTime.now())
                .jsonwebtoken(jToken)
                .status(responseCode.getHttpStatus().value())
                .message(messageSource.getMessage(responseCode.getStatusId() + ".message",  null, "", LocaleContextHolder.getLocale()))
                .serviceCode(messageSource.getMessage(responseCode.getStatusId() + ".code",  null, "", LocaleContextHolder.getLocale()));
    }
}
