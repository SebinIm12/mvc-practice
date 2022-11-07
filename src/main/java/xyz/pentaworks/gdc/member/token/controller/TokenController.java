package xyz.pentaworks.gdc.member.token.controller;

import xyz.pentaworks.gdc.member.token.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.pentaworks.ssp.core.annotation.Channel;
import xyz.pentaworks.ssp.core.annotation.IgnoreAuthenticationFilter;
import xyz.pentaworks.ssp.core.annotation.IgnoreAuthorityFilter;
import xyz.pentaworks.ssp.core.annotation.PrimaryID;
import xyz.pentaworks.ssp.core.annotation.SSPRestController;
import xyz.pentaworks.ssp.core.dto.DefaultTokenDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Tag(name = "TOKENs", description = "토큰")
@SSPRestController
@RequestMapping("/tokens")
@Validated
public class TokenController {
    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    // check-in by id
    @GetMapping("/check-in-by-id")
    @IgnoreAuthenticationFilter
    @Operation(summary = "ID 로 로그인")
    public DefaultTokenDto checkinById(
            @Parameter(description = "토큰 생성에 사용하는 id 값") @RequestParam @NotBlank String primaryId) {
        return tokenService.checkinById(primaryId);
    }

    // check-in by id and channel
    @GetMapping("/check-in-by-id-channel")
    @IgnoreAuthenticationFilter
    @Operation(summary = "ID, Channel 로 로그인")
    public DefaultTokenDto checkinByIdAndChannel(
            @Parameter(description = "토큰 생성에 사용하는 id 값") @RequestParam @NotBlank String primaryId,
            @Parameter(hidden = true) @Channel @NotBlank String channel) {
        return tokenService.checkinByIdAndChannel(primaryId, channel);
    }

    // check-in by id and channel and timeout
    @GetMapping("/check-in-by-id-channel-timeout")
    @IgnoreAuthenticationFilter
    @Operation(summary = "ID, Channel, timeout 로 로그인")
    public DefaultTokenDto checkinByIdAndChannelAndTimeout(
            @Parameter(description = "토큰 생성에 사용하는 id 값") @RequestParam @NotBlank String primaryId,
            @Parameter(description = "토큰의 timeout 시간(초)") @RequestParam @Positive Integer timeout,
            @Parameter(hidden = true) @Channel @NotBlank String channel) {
        return tokenService.checkinByIdAndChannelAndTimeout(primaryId, channel, timeout);
    }

    // check-in by id and channel and timeout and timeunit
    @GetMapping("/check-in-by-id-channel-timeout-timeunit")
    @IgnoreAuthenticationFilter
    @Operation(summary = "ID, Channel, timeout, timeunit 로 로그인")
    public DefaultTokenDto checkinByIdAndChannelAndTimeoutAndTimeunit(
            @Parameter(description = "토큰 생성에 사용하는 id 값") @RequestParam @NotBlank String primaryId,
            @Parameter(description = "토큰의 timeout 시간") @RequestParam @Positive Integer timeout,
            @Parameter(description = "토큰의 timeout 시간 단위", schema = @Schema(defaultValue = "SECONDS",
                                                                           allowableValues = {
                                                                                   "SECONDS",
                                                                                   "MINUTES",
                                                                                   "HOURS",
                                                                                   "DAYS"}))
            @RequestParam @NotNull TimeUnit timeUnit,
            @Parameter(hidden = true) @Channel @NotBlank String channel) {
        return tokenService.checkinByIdAndChannelAndTimeoutAndTimeunit(primaryId, channel, timeout,
                timeUnit);
    }

    // primary id 확인
    @GetMapping("/find-primaryid-by-string")
    @Operation(summary = "Primary ID 받기- String")
    public Map<String, Object> findPrimaryIdByString(
            @Parameter(hidden = true) @PrimaryID String pid) {
        log.debug("primary id : {}", pid);

        Map<String, Object> result = new HashMap<>();
        result.put("pid", pid);

        return result;
    }

    @GetMapping("/find-primaryid-by-map")
    @Operation(summary = "Primary ID 받기- HashMap")
    public Map<String, Object> findPrimaryIdByMap(
            @Parameter(hidden = true) @PrimaryID HashMap pid) {
        log.debug("primary id : {}", pid);
        return pid;
    }

    @GetMapping("/find-primaryid-by-map-using-other-name")
    @Operation(summary = "Primary ID, Channel 받기- HashMap - 다른 이름 사용")
    public Map<String, Object> findPrimaryIdByMapUsingOtherName(
            @Parameter(hidden = true) @PrimaryID(fields = "userId") @Channel(fields = "ch")
            HashMap pid) {
        log.debug("primary id : {}", pid);
        return pid;
    }

    // check out
    @GetMapping("/checkout")
    @Operation(summary = "Checkout - default by id")
    public Map<String, Object> checkout(@Parameter(hidden = true) @PrimaryID String pid) {
        log.debug("primary id : {}", pid);
        tokenService.checkout();

        Map<String, Object> result = new HashMap<>();
        result.put("pid", pid);

        return result;
    }

    // check out by id
    @GetMapping("/checkout-by-id")
    @Operation(summary = "Checkout by id")
    public Map<String, Object> checkoutById(@Parameter(hidden = true) @PrimaryID String pid) {
        log.debug("primary id : {}", pid);
        tokenService.checkout(pid);

        Map<String, Object> result = new HashMap<>();
        result.put("pid", pid);

        return result;
    }

    // check out by id and channel
    @GetMapping("/checkout-by-id-channel")
    @Operation(summary = "Checkout by id and channel")
    public Map<String, Object> checkoutByIdAndChannel(
            @Parameter(hidden = true) @PrimaryID String pid,
            @Parameter(hidden = true) @Channel @NotBlank String channel) {
        log.debug("primary id : {}", pid);
        tokenService.checkout(pid, channel);

        Map<String, Object> result = new HashMap<>();
        result.put("pid", pid);

        return result;
    }

    // discard
    @GetMapping("/discard")
    @Operation(summary = "discard - default by id")
    public Map<String, Object> discard(@Parameter(hidden = true) @PrimaryID String pid) {
        log.debug("primary id : {}", pid);
        tokenService.discard();

        Map<String, Object> result = new HashMap<>();
        result.put("pid", pid);

        return result;
    }

    // token 확인 회피
    @GetMapping("/ignore-authentication-filter")
    @Operation(summary = "토큰 확인 회피")
    @IgnoreAuthenticationFilter
    public Map<String, Object> ignoreAuthenticationFilter(
            @Parameter(hidden = true) @PrimaryID String pid) {
        log.debug("primary id : {}", pid);

        Map<String, Object> result = new HashMap<>();
        result.put("pid", pid);

        return result;
    }

    // 권한 확인 회피
    @GetMapping("/ignore-authority-filter")
    @Operation(summary = "권한 확인 회피")
    @IgnoreAuthorityFilter
    public Map<String, Object> ignoreAuthorityFilter(
            @Parameter(hidden = true) @PrimaryID String pid) {
        log.debug("primary id : {}", pid);

        Map<String, Object> result = new HashMap<>();
        result.put("pid", pid);

        return result;
    }


    // 토큰 갱신
    @GetMapping("/token-refresh")
    @Operation(summary = "토큰 갱신 (토큰 발급 타입이 REFRESH 일 경우)")
    @IgnoreAuthenticationFilter
    @Parameter(in = ParameterIn.HEADER, name = "${ssp.props.header-name.refresh-token}",
               description = "리프레시 토큰", required = true)
    public DefaultTokenDto refreshToken() {
        return tokenService.refresh();
    }
}
