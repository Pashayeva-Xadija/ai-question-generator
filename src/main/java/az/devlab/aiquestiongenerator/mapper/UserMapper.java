package az.devlab.aiquestiongenerator.mapper;

import az.devlab.aiquestiongenerator.model.User;
import az.devlab.aiquestiongenerator.dto.RegisterRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "enabled", constant = "true")
    User toEntity(RegisterRequest request);
}
