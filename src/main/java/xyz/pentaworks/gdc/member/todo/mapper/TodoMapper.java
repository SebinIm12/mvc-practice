package xyz.pentaworks.gdc.member.todo.mapper;

import xyz.pentaworks.gdc.member.config.SSPDatasource;
import xyz.pentaworks.gdc.member.todo.model.TodoRequestDTO;
import xyz.pentaworks.gdc.member.todo.model.TodoSearchRequestDTO;
import xyz.pentaworks.gdc.member.todo.model.TodoVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@SSPDatasource
public interface TodoMapper {

    List<TodoVO> findAll(TodoSearchRequestDTO params);

    TodoVO findById(String id);

    int add(TodoRequestDTO todo);

    int modify(TodoRequestDTO todo);

    int remove(String id);

}
