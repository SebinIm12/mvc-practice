package xyz.pentaworks.gdc.member.todo.service;

import xyz.pentaworks.gdc.member.todo.mapper.TodoMapper;
import xyz.pentaworks.gdc.member.todo.model.TodoModelMapper;
import xyz.pentaworks.gdc.member.todo.model.TodoRequestDTO;
import xyz.pentaworks.gdc.member.todo.model.TodoSearchRequestDTO;
import xyz.pentaworks.gdc.member.todo.model.TodoVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import xyz.pentaworks.ssp.core.exception.SSPExceptionBuilder;
import xyz.pentaworks.ssp.core.model.RestTemplateUtilsRequest;
import xyz.pentaworks.ssp.core.util.RestTemplateUtils;
import xyz.pentaworks.ssp.core.util.WebClientUtils;
import xyz.pentaworks.ssp.core.web.SSPRestResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TodoService {
    private final TodoMapper todoMapper;

    private final TodoModelMapper todoModelMapper;

    private final ObjectMapper objectMapper;

    public TodoService(TodoMapper todoMapper, TodoModelMapper todoModelMapper,
            ObjectMapper objectMapper) {
        this.todoMapper = todoMapper;
        this.todoModelMapper = todoModelMapper;
        this.objectMapper = objectMapper;
    }

    public List<TodoVO> findAll(TodoSearchRequestDTO params) {
        return todoMapper.findAll(params);
    }

    public TodoVO findById(String id) {

        TodoVO result = todoMapper.findById(id);

        if (result == null) {
            throw new SSPExceptionBuilder().id("BOOTSTRAP.TODO.MODIFY.400")
                                           .build();
        }

        return result;
    }

    public TodoVO add(TodoRequestDTO todo) {

        TodoVO test = todoModelMapper.requestDtoToEntity(todo, LocalDateTime.now());
        log.info("test : {}", test);

        todoMapper.add(todo);
        return findById(String.valueOf(todo.getId()));
    }

    public TodoVO modify(TodoRequestDTO todo) {
        TodoVO result = findById(String.valueOf(todo.getId()));

        if (result != null) {
            todoMapper.modify(todo);
            return findById(String.valueOf(todo.getId()));
        } else {
            // SSP_ERROR_CODE 테이블의 BOOTSTRAP.TODO.MODIFY.400 정보로 exception 발생
            throw new SSPExceptionBuilder().id("BOOTSTRAP.TODO.MODIFY.400")
                                           .build();
        }

    }

    public int remove(String id) {
        return todoMapper.remove(id);
    }

    public TodoVO findExternalWebClient(String id) {
        // SSP_API_INFO 테이블의 A00004 (TODO 조회) API를 호출
        SSPRestResult restResult = WebClientUtils.uri("A00004", id)
                                                 .exchangeToMono(response -> response.bodyToMono(
                                                         SSPRestResult.class))
                                                 .block();

        TodoVO result = null;
        if (restResult != null) {
            result = objectMapper.convertValue(restResult.getData(), TodoVO.class);
        }
        return result;
    }


    public TodoVO findExternalRestTemplate(String id) {
        Map<String, String> uriVariablesMap = new HashMap<>();
        uriVariablesMap.put("id", id);

        // SSP_API_INFO 테이블의 A00004 (TODO 조회) API를 호출
        SSPRestResult restResult = RestTemplateUtils.callSSP(RestTemplateUtilsRequest.builder()
                                                                                     .apiId("A00004")
                                                                                     .uriVariablesMap(
                                                                                             uriVariablesMap)
                                                                                     .build());

        return objectMapper.convertValue(restResult.getData(), TodoVO.class);

    }


    public ByteArrayOutputStream excelDownload(TodoSearchRequestDTO params) {

        List<String> headers = Arrays.asList("아이디", "메모", "일자", "수정일시");
        int rowNo = 0;
        int cellNum = 0;

        try (Workbook workbook = new SXSSFWorkbook(100);
                ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("todos");
            Row row = sheet.createRow(rowNo++);

            // header row 생성
            for (String key : headers) {
                // header cell 생성
                row.createCell(cellNum++)
                   .setCellValue(key);
            }

            // list 조회
            List<TodoVO> list = todoMapper.findAll(params);

            CreationHelper createHelper = workbook.getCreationHelper();

            // cell style 설정
            CellStyle cs1 = workbook.createCellStyle();
            cs1.setDataFormat(createHelper.createDataFormat()
                                          .getFormat("yyyy-mm-dd"));

            CellStyle cs2 = workbook.createCellStyle();
            cs2.setDataFormat(createHelper.createDataFormat()
                                          .getFormat("yyyy-mm-dd h:mm"));

            Cell cell = null;

            // list row 생성
            for (TodoVO todo : list) {
                row = sheet.createRow(rowNo++);

                cellNum = 0;
                row.createCell(cellNum++)
                   .setCellValue(todo.getId());
                row.createCell(cellNum++)
                   .setCellValue(todo.getMemo());
                cell = row.createCell(cellNum++);
                cell.setCellValue(todo.getDueDate());
                cell.setCellStyle(cs1);
                cell = row.createCell(cellNum++);
                cell.setCellValue(todo.getModifiedDateTime());
                cell.setCellStyle(cs2);

            }

            workbook.write(os);

            return os;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}
