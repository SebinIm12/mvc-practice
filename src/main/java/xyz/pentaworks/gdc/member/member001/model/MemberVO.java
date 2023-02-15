package xyz.pentaworks.gdc.member.member001.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
//import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class MemberVO {


    //공통
    @ApiModelProperty(value = "회원 번호")
    private String mbrNo;
    @ApiModelProperty(value = "로그인 아이디")
    private String loginId;
    @ApiModelProperty(value = "회원유형코드[개인,법인,부계정]")
    private String mbrTypeCd;
    @ApiModelProperty(value = "회원상태코드[활성,휴면,탈퇴]")
    private String mbrStatCd;
    @ApiModelProperty(value = "회원비밀번호")
    private String mbrPw;
    @ApiModelProperty(value = "적립포인트")
    private String accmlPoint;
    @ApiModelProperty(value = "지급포인트")
    private String pymntPoint;
    @ApiModelProperty(value = "탈퇴일시")
    private String leaveDt;
    @ApiModelProperty(value = "랭크 코드")
    private String rankCd;
    @ApiModelProperty(value = "랭크 점수")
    private String rankScore;

    @ApiModelProperty(value = "")
    private String indutyTypeCdNm;
    @ApiModelProperty(value = "")
    private String indutyGrpCdNm;
    @ApiModelProperty(value = "")
    private String indutyCdNm;

    //개인 회원
    @ApiModelProperty(value = "이메일")
    private String email;
    @ApiModelProperty(value = " 닉네임")
    private String nickName;
    @ApiModelProperty(value = "이미지 URL")
    private String proflImgUrl;
    @ApiModelProperty(value = "깃허브 URL")
    private String githubUrl;
    @ApiModelProperty(value = "이름")
    private String name;
    @ApiModelProperty(value = "전화번호")
    private String telNo;
    @ApiModelProperty(value = " 가입 일시")
    private String jointDt;


    //법인 회원
    @ApiModelProperty(value = "법인 이름")
    private String bsnNm;
    @ApiModelProperty(value = " 법인 전화번호")
    private String bsnTelno;
    @ApiModelProperty(value = " 법인 주소")
    private String bsnAddr;
    @ApiModelProperty(value = " 업종 대분류 코드")
    private String indutyTypeCd;
    @ApiModelProperty(value = " 업종 중분류 코드")
    private String indutyGprCd;
    @ApiModelProperty(value = " 업종 코드")
    private String indutyCd;
    @ApiModelProperty(value = " 사업자 번호")
    private String bsnNo;
    @ApiModelProperty(value = " 대표자 이름")
    private String ceoNm;
    @ApiModelProperty(value = "대표자 이메일")
    private String ceoEmail;
    @ApiModelProperty(value = "담당자 이름")
    private String chargerNm;
    @ApiModelProperty(value = "담당자 전화번호")
    private String chargetTelno;
    @ApiModelProperty(value = "프로필 이미지 URL")
    private String profImgUrl;
    @ApiModelProperty(value = "담당자 이메일")
    private String chargetEmail;
    @ApiModelProperty(value = "회사 홈페이지 URL")
    private String comHpUrl;
    @ApiModelProperty(value = "회사 소개")
    private String comIntrcn;
    @ApiModelProperty(value = "가입 일시")
    private String joinDt;

}
