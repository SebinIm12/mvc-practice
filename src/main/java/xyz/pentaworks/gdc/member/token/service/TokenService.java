package xyz.pentaworks.gdc.member.token.service;

import org.springframework.stereotype.Service;
import xyz.pentaworks.ssp.core.component.TokenManager;
import xyz.pentaworks.ssp.core.dto.DefaultTokenDto;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    private final TokenManager tokenManager;

    public TokenService(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public DefaultTokenDto checkinById(String primaryId) {
        // 채널 : DEFAULT
        return tokenManager.checkin(primaryId);
    }

    public DefaultTokenDto checkinByIdAndChannel(String primaryId, String channel) {
        return tokenManager.checkin(primaryId, channel);
    }

    public DefaultTokenDto checkinByIdAndChannelAndTimeout(String primaryId, String channel,
            Integer timeout) {
        return tokenManager.checkin(primaryId, channel, timeout);
    }

    public DefaultTokenDto checkinByIdAndChannelAndTimeoutAndTimeunit(String primaryId,
            String channel, Integer timeout, TimeUnit timeUnit) {
        return tokenManager.checkin(primaryId, channel, timeout, timeUnit);
    }

    public void checkout() {
        tokenManager.checkout();
    }

    public void checkout(String pid) {
        tokenManager.checkout(pid);
    }

    public void checkout(String pid, String channel) {
        tokenManager.checkout(pid, channel);
    }

    public void discard() {
        tokenManager.discard();
    }

    public DefaultTokenDto refresh() {
        return tokenManager.refresh();
    }

}



