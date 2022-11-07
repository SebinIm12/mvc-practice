package xyz.pentaworks.gdc.member.todo.controller;

import xyz.pentaworks.gdc.member.todo.model.TodoRequestDTO;
import xyz.pentaworks.gdc.member.todo.model.TodoSearchRequestDTO;
import xyz.pentaworks.gdc.member.todo.model.TodoVO;
import xyz.pentaworks.gdc.member.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.pentaworks.ssp.core.annotation.SSPRestController;

import javax.validation.Valid;
import java.util.List;

@SSPRestController
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "TODO 전체 조회")
    @GetMapping
    public List<TodoVO> findAll(@ParameterObject @ModelAttribute TodoSearchRequestDTO params) {
        return todoService.findAll(params);
    }

    @Operation(summary = "TODO 조회")
    @GetMapping("/{id}")
    public TodoVO find(@Parameter(description = "아이디") @PathVariable String id) {
        return todoService.findById(id);
    }

    @Operation(summary = "TODO 등록")
    @PostMapping
    public TodoVO add(@RequestBody @Valid TodoRequestDTO todo) {
        return todoService.add(todo);
    }

    @Operation(summary = "TODO 수정")
    @PutMapping("/{id}")
    public TodoVO modify(@PathVariable String id, @RequestBody @Valid TodoRequestDTO todo) {
        todo.setId(Long.valueOf(id));
        return todoService.modify(todo);
    }



    @Operation(summary = "TODO 삭제")
    @DeleteMapping("/{id}")
    public int remove(@PathVariable String id) {
        return todoService.remove(id);
    }

    @Operation(summary = "TODO 조회 - 외부 API 호출 (WebClientUtils)")
    @GetMapping("/external/webclient/{id}")
    public TodoVO findExternal(@PathVariable String id) {
        return todoService.findExternalWebClient(id);
    }

    @Operation(summary = "TODO 조회 - 외부 API 호출 (RestTemplateUtils)")
    @GetMapping("/external/resttemplate/{id}")
    public TodoVO findExternalRest(@PathVariable String id) {
        return todoService.findExternalRestTemplate(id);
    }


    @GetMapping(value = "/excel")
    @Operation(summary = "엑셀 다운로드")
    public ResponseEntity<byte[]> excelDownload(
            @ParameterObject @ModelAttribute TodoSearchRequestDTO params) {

        String downFileNm = "todos.xlsx";

        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(
                                     "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                             .header(HttpHeaders.CONTENT_DISPOSITION,
                                     "attachment; filename=\"" + downFileNm + "\"")
                             .body(todoService.excelDownload(params)
                                              .toByteArray());
    }

}
