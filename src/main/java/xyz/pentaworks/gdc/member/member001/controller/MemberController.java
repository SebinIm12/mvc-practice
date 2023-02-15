package xyz.pentaworks.gdc.member.member001.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import xyz.pentaworks.gdc.member.member001.model.EmailVerifyHistVo;
import xyz.pentaworks.gdc.member.member001.model.MemberVO;
import xyz.pentaworks.gdc.member.member001.service.EmailApiService;
import xyz.pentaworks.gdc.member.member001.service.MemberService;
import xyz.pentaworks.gdc.member.response.ResponseCode;
import xyz.pentaworks.gdc.member.response.ResponseVO;
import xyz.pentaworks.ssp.core.annotation.SSPRestController;

@SSPRestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final EmailApiService emailApiService;

    public MemberController(MemberService memberService, EmailApiService emailApiService) {

        this.memberService = memberService;
        this.emailApiService = emailApiService;
    }


    /**
     * ToDo 이메일 발송 API 호출
     * */
    @ApiOperation(value = "member_001 인증번호 이메일 발송")
    @PostMapping("/member/common/send-auth-email")
    public ResponseEntity<ResponseVO> sendAuthEmail(@PathVariable String email, @RequestBody MemberVO memberVo) {
        ResponseCode code = ResponseCode.SUCCESS;
         String msg = null;
         int result = memberService.selectMemberBaseInfo(memberVo);
         if(result==0)
             msg = "지원하지 않은 기능입니다.";
         else if(result != 1)
             msg = "아이디와 이메일이 동일한 회원정보가 아닙니다. 회원가입 시 입력한 아이디/이메일을 입력해주세요.";
         else {
             //인증번호 생성
             String ranNum = memberService.createRanNum();
             //이메일 발송 API 호출*****
             emailApiService.sendConfirmEmail(email , ranNum);
             //이메일 인증 이력 생성
             EmailVerifyHistVo emailVerifyHistVo = null;
             emailVerifyHistVo.setEmail(email);
             emailVerifyHistVo.setVrifyNo(ranNum);
             memberService.insertVerificationEmailHist(emailVerifyHistVo);

          }
        //return msg;
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ResponseVO.builder(code, msg).build());
    }


    @ApiOperation(value = "member_002 인증번호 확인")
    @PostMapping("/member/common/check-auth-email")
    public ResponseEntity<ResponseVO> checkAuthEmail( @RequestParam String email, @RequestParam String vrifyNo, EmailVerifyHistVo emailVerifyHistVo) {

        System.out.println("****************okayyyy*******************");

        ResponseCode code = ResponseCode.SUCCESS;
        String msg = null;
        emailVerifyHistVo.setEmail(email);
        emailVerifyHistVo.setVrifyNo(vrifyNo);
        int result = memberService.checkAuthEmail(emailVerifyHistVo);
        if(result!=1) msg = "일치하는 인증 정보가 없습니다.";
        else msg = "정상 처리되었습니다.";
        //return msg;
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ResponseVO.builder(code, msg).build());
    }

    @ApiOperation(value = "member_003 회원 비밀번호 변경")
    @PostMapping("/member/common/change-password")
    public ResponseEntity<ResponseVO> changePw(@RequestParam String token, @RequestParam String pw, MemberVO memberVo) {

        System.out.println("****************okayyyy*******************");

        ResponseCode code = ResponseCode.SUCCESS;
        String result = null;
        memberVo.setMbrPw(pw);
        result = memberService.changePw(memberVo);
        //return result;
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ResponseVO.builder(code, result).build());
    }


    @ApiOperation(value = "member_004 아이디 중복여부 체크")
    @PostMapping("/member/common/check-id")
    public ResponseEntity<ResponseVO> checkId(@RequestParam String id, MemberVO memberVo) {

        System.out.println("****************okayyyy*******************");

        ResponseCode code = ResponseCode.SUCCESS;
        String result = null;
        memberVo.setLoginId(id);
        result = memberService.checkId(memberVo);
        //return result;
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ResponseVO.builder(code, result).build());
    }


    @ApiOperation(value = "member_005 닉네임 중복여부 체크 (개인회원)")
    @PostMapping("/member/common/check-nickname/personal")
    public ResponseEntity<ResponseVO> checkNickName(@RequestParam String nickName, MemberVO memberVO) {

        System.out.println("***************okayyyy*******************");
        ResponseCode code = ResponseCode.SUCCESS;
        String result = null;

        memberVO.setNickName(nickName);
        result = memberService.checkNickName(memberVO);
        //return result;
        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ResponseVO.builder(code, result).build());
    }


    @ApiOperation(value = "member_006 업태/업종 목록 조회")
    @GetMapping("/member/common/induty")
    public ResponseEntity<ResponseVO> induty(MemberVO memberVO) {

        System.out.println("***************okayyyy******************");
        ResponseCode code = ResponseCode.SUCCESS;
        String result = null;

        //MemberVO vo = MemberService.selectIndutyList(memberVO);
        //return result;

        return null;
//        return ResponseEntity
//                .status(code.getHttpStatus())
//                .body(ResponseVO.builder(memberService.selectIndutyList(memberVO)).build());
    }

//    @ApiOperation(value = "member_007 사용중인 파트너 권한 변경")
//    @PostMapping("/member/common/change-role")
//    public ResponseEntity<ResponseVO> changeRole(Token token, MemberVO memberVO) {
//
//        System.out.println("***************okayyyy******************");
//        ResponseCode code = ResponseCode.SUCCESS;
//        String result = null;
//
//         result = memberService.changeRole(memberVO);
//
//        //return result;
//        return ResponseEntity
//                .status(code.getHttpStatus())
//                .body(ResponseVO.builder(code, result).build());
//    }

}
