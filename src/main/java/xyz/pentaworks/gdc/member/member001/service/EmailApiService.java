package xyz.pentaworks.gdc.member.member001.service;

import xyz.pentaworks.gdc.member.member001.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailApiService {
    private final MemberMapper memberMapper;

    public EmailApiService(MemberMapper memberMapper) {

        this.memberMapper = memberMapper;
    }


    public void sendConfirmEmail(String email, String ranNum) {
    }
}
