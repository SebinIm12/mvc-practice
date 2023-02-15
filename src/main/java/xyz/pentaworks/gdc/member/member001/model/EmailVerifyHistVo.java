package xyz.pentaworks.gdc.member.member001.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class EmailVerifyHistVo {

    @ApiModelProperty(value = "")
    private String no;
    @ApiModelProperty(value = "")
    private String email;
    @ApiModelProperty(value = "")
    private String vrifyNo;
    @ApiModelProperty(value = "")
    private String dt;
    @ApiModelProperty(value = "")
    private String useYn;
    @ApiModelProperty(value = "회원 번호")
    private String mbrNo;
}
