package xyz.pentaworks.gdc.member.todo.model;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoSearchRequestDTO {

    @Parameter(description = "메모")
    private String memo;

    @Parameter(description = "시작일자 (yyyyMMdd)", example = "20201130")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate startDueDate;

    @Parameter(description = "종료일자 (yyyyMMdd)", example = "20201231")
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate endDueDate;

}
