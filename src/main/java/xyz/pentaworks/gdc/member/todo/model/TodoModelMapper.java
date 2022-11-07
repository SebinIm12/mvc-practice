package xyz.pentaworks.gdc.member.todo.model;

import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface TodoModelMapper {

    TodoVO requestDtoToEntity(TodoRequestDTO todoRequestDTO, LocalDateTime modifiedDateTime);

}
