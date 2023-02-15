package xyz.pentaworks.gdc.member.member001.mapper;

import xyz.pentaworks.gdc.member.member001.model.EmailVerifyHistVo;
import xyz.pentaworks.gdc.member.member001.model.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    MemberVO selectMemberBaseInfo(MemberVO memberVO);

    int selectMemberIndivCnt(MemberVO memberVO);

    int selectMemberCprCnt(MemberVO memberVO);

    int checkAuthEmail(EmailVerifyHistVo emailVerifyHistVo);

    void insertVerificationEmailHist(EmailVerifyHistVo emailVerifyHistVo);

    void updateMemberBaseInfo(MemberVO m);

    int selectMemberBaseCnt(MemberVO memberVo);

    MemberVO selectGroupCommonCode(MemberVO memberVO);

    MemberVO selectCommonCode(MemberVO memberVO);

    int selectPartnerAuthCnt(MemberVO memberVO);

    void updatePartnerAuthY(MemberVO memberVO);

    void updatePartnerAuthN(MemberVO memberVO);

//    MemberVO selectMemberIndivInfo(MemberVO memberVO);
//    MemberVO selectMemberCprInfo(MemberVO memberVO);
}
