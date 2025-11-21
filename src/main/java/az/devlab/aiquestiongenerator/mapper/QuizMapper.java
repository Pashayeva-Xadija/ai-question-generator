package az.devlab.aiquestiongenerator.mapper;

import az.devlab.aiquestiongenerator.dto.QuestionResponse;
import az.devlab.aiquestiongenerator.dto.QuizResponse;
import az.devlab.aiquestiongenerator.model.Quiz;
import az.devlab.aiquestiongenerator.model.QuizQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {QuestionMapper.class})
public abstract class QuizMapper {

    @Autowired
    protected QuestionMapper questionMapper;

    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "questions", expression = "java(mapQuestions(entity))")
    public abstract QuizResponse toResponse(Quiz entity);

    protected List<QuestionResponse> mapQuestions(Quiz quiz) {
        if (quiz.getQuizQuestions() == null) {
            return List.of();
        }

        return quiz.getQuizQuestions()
                .stream()
                .sorted(Comparator.comparing(
                        qq -> qq.getOrderIndex() == null ? 0 : qq.getOrderIndex()
                ))
                .map(QuizQuestion::getQuestion)
                .map(questionMapper::toResponse)
                .collect(Collectors.toList());
    }
}
