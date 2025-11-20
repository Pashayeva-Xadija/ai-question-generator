package az.devlab.aiquestiongenerator.mapper;

import az.devlab.aiquestiongenerator.model.QuestionOption;
import az.devlab.aiquestiongenerator.dto.QuestionOptionResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface QuestionOptionMapper {

    QuestionOptionResponse toResponse(QuestionOption option);
}
