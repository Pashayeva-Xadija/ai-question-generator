package az.devlab.aiquestiongenerator.mapper;

import az.devlab.aiquestiongenerator.model.Quiz;
import az.devlab.aiquestiongenerator.model.QuizQuestion;
import az.devlab.aiquestiongenerator.dto.QuestionResponse;
import az.devlab.aiquestiongenerator.dto.QuizResponse;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public interface QuizMapper {

    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "questions", expression = "java(mapQuestions(entity))")
    QuizResponse toResponse(Quiz entity);

    default List<QuestionResponse> mapQuestions(Quiz quiz) {
        return quiz.getQuizQuestions()
                .stream()
                .sorted((a, b) -> Integer.compare(
                        a.getOrderIndex() == null ? 0 : a.getOrderIndex(),
                        b.getOrderIndex() == null ? 0 : b.getOrderIndex()
                ))
                .map(qq -> qq.getQuestion())
                .map(q -> questionMapper().toResponse(q))
                .collect(Collectors.toList());
    }

    QuestionMapper questionMapper();
}


