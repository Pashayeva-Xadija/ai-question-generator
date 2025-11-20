package az.devlab.aiquestiongenerator.mapper;

import az.devlab.aiquestiongenerator.model.Question;
import az.devlab.aiquestiongenerator.dto.QuestionResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {QuestionOptionMapper.class})
public interface QuestionMapper {

    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "approvedById", source = "approvedBy.id")
    QuestionResponse toResponse(Question entity);
}
