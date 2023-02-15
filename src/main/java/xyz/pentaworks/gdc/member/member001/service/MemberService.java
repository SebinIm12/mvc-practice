package xyz.pentaworks.gdc.member.member001.service;

import xyz.pentaworks.gdc.member.member001.model.EmailVerifyHistVo;
import xyz.pentaworks.gdc.member.member001.mapper.MemberMapper;
import xyz.pentaworks.gdc.member.member001.model.MemberVO;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.pentaworks.ssp.core.component.TokenManager;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class MemberService {
    private final MemberMapper memberMapper;
    private final TokenManager tokenManager;

    public MemberService(MemberMapper memberMapper, TokenManager tokenManager) {

        this.memberMapper = memberMapper;
        this.tokenManager = tokenManager;
    }



    public MemberVO selectIndutyList(MemberVO memberVO) {
        MemberVO vo = null;
        //커리가 비었을 때
        if(memberVO.getIndutyTypeCd().equals("") && memberVO.getIndutyGprCd().equals("") && memberVO.getIndutyCd().equals("")) {
            vo = memberMapper.selectGroupCommonCode(memberVO);
        }

        //업종 코드가 있을 때
        if(!memberVO.getIndutyCd().equals("")){
            //업종 코드 조회
            MemberVO voo  = memberMapper.selectCommonCode(memberVO);
            //업종 대분류 조회
            return memberMapper.selectGroupCommonCode(voo);
        }

        //업종 중분류 코드가 있을 때
        if(!memberVO.getIndutyGprCd().equals("")){
            return memberMapper.selectCommonCode(memberVO);
        }

        //업종 대분류 코드가 있을 때
        if(!memberVO.getIndutyTypeCd().equals("")){
            return memberMapper.selectCommonCode(memberVO);
        }
        return  vo;
    }

    //member001
    public int selectMemberBaseInfo(MemberVO memberVO) {
        MemberVO vo = memberMapper.selectMemberBaseInfo(memberVO);
        int result;
        if ( vo.getMbrNo()!= null && vo.getMbrTypeCd().equals("법인")) {
            result = memberMapper.selectMemberCprCnt(vo);
        }
        else if (vo.getMbrNo()!= null &&  vo.getMbrTypeCd().equals("개인")) {
            result = memberMapper.selectMemberIndivCnt(vo);
        }

        else result = 0;
        return result;
    }

    public MemberVO selectMemberDetail(MemberVO memberVO){
        return memberMapper.selectMemberBaseInfo(memberVO);
    }

    public String createRanNum() {

        Random random = new Random();         //랜덤 함수 선언
        int createNum = 0;                   //1자리 난수
        String ranNum = "";                 //1자리 난수 형변환 변수
        int letter = 6;                    //난수 자릿수:6
        String resultNum = "";            //결과 난수

        for (int i = 0; i < letter; i++) {

            createNum = random.nextInt(9);        //0부터 9까지 올 수 있는 1자리 난수 생성
            ranNum = Integer.toString(createNum);       //1자리 난수를 String으로 형변환
            resultNum += ranNum;                       //생성된 난수(문자열)을 원하는 수(letter)만큼 더하며 나열
        }

        return resultNum;
    }

    public void insertVerificationEmailHist(EmailVerifyHistVo emailVerifyHistVo) {
        memberMapper.insertVerificationEmailHist(emailVerifyHistVo);
    }

    //member002
    public int checkAuthEmail(EmailVerifyHistVo emailVerifyHistVo) {
        return memberMapper.checkAuthEmail(emailVerifyHistVo);
    }

    //member003
    public String changePw(MemberVO memberVo) {
        String pw = memberVo.getMbrPw();

        if (pw.length() < 7 || pw.length() > 17) {
            return "비밀번호는 8~16자로 입력해주세요";
        }
        String pattern1 = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,16}$"; // 영문, 숫자, 특수문자 8~16자리
        Matcher match;
        match = Pattern.compile(pattern1).matcher(pw);
        if (!match.find()) {
            return "비밀번호는 영문, 특수문자, 숫자 조합이어야 한다.";
        }
        else{
            MemberVO member = memberMapper.selectMemberBaseInfo(memberVo);
            if(!member.getMbrTypeCd().equals("법인") && !member.getMbrTypeCd().equals("개인")){
                return "사용할 수 없는 기능입니다.";
            }
            else{
                String hashed = Hashing.sha256()
                        .hashString(pw, StandardCharsets.UTF_8)
                        .toString();
                try{
                    member.setMbrPw(hashed);
                    memberMapper.updateMemberBaseInfo(member);
                    tokenManager.checkout();
                }
                catch(Exception e){
                    log.error(e.getMessage());
                }
                return "정상 처리되었습니다.";
            }
        }

    }

    //member004
    public String checkId(MemberVO memberVo) {
        String id = memberVo.getLoginId();
        int result;
        if(id.length() > 15 || id.length() < 5){
            return "아이디는 5자~ 151자 길이로 사용가능합니다.";
        }
        String pattern2 = "^[a-z[0-9]]{5,15}$"; // 영문, 숫자
        Matcher match;
        match = Pattern.compile(pattern2).matcher(id);

        if(!match.find()) {
            return "아이디는 영문 소문자와 숫자만 사용가능합니다.";
        }
        else{
            result = memberMapper.selectMemberBaseCnt(memberVo);
        }

        if(result==1){
            return "사용중인 아이입니다.";
        }else{
            return "정상 처리되었습니다.";
        }
    }

    //member005
    public String checkNickName(MemberVO memberVo) {
        String nickname = memberVo.getNickName();
        int result;
        if(nickname.length() < 2 || nickname.length() > 10){
            return "닉네임은 2자~ 10자 길이로 사용가능합니다.";
        }
        String pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9][가-힣]$@$!%*#?&]{2,10}$"; // 영문, 숫자, 특수문자, 한글 2~10자리 **********????
        Matcher match;
        match = Pattern.compile(pattern).matcher(nickname);
        if (!match.find()) {
            return "비밀번호는 영문,숫자 특수문자, 한글만 사용가능합니다.";
        }
        result = memberMapper.selectMemberIndivCnt(memberVo);
        if(result==1){
            return "사용중인 닉네임입니다.";
        }
        else{
            return "정상 처리되었습니다.";
        }
    }

    //member-007
    public String changeRole(MemberVO memberVO) {
        int cnt;
        MemberVO member = memberMapper.selectMemberBaseInfo(memberVO);
        if(!member.getMbrTypeCd().equals("법인") && !member.getMbrTypeCd().equals("개인")){
            return "사용할 수 없는 기능입니다.";
        }
        else{
            cnt =memberMapper.selectPartnerAuthCnt(memberVO);
        }
        if(cnt!=1) {
            return "해당 파트너 권한이 없습니다.";
        }
        else{
            memberMapper.updatePartnerAuthN(memberVO);
            memberMapper.updatePartnerAuthY(memberVO);
        }
        return "정상 처리되었습니다.";

    }
}
