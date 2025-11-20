package az.devlab.aiquestiongenerator.mapper;

import az.devlab.aiquestiongenerator.integration.HuggingFaceResponsePayload;
import az.devlab.aiquestiongenerator.model.Question;
import az.devlab.aiquestiongenerator.model.QuestionOption;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AiQuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aiModel", source = "model")
    @Mapping(target = "text", source = "questionText")
    @Mapping(target = "topic", source = "topic")
    @Mapping(target = "difficulty", source = "difficulty")
    @Mapping(target = "type", constant = "MULTIPLE_CHOICE")
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "approvedAt", ignore = true)
    @Mapping(target = "alternativeText", source = "alternativeText")
    @Mapping(target = "explanation", source = "explanation")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "options", expression = "java(mapOptions(payload))")
    Question toEntity(HuggingFaceResponsePayload payload);

    default List<QuestionOption> mapOptions(HuggingFaceResponsePayload payload) {
        if (payload.getOptions() == null)
            return List.of();

        return payload.getOptions()
                .stream()
                .map(o -> QuestionOption.builder()
                        .text(o.getText())
                        .correct(o.isCorrect())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
