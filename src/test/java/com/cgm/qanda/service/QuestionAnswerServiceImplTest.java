package com.cgm.qanda.service;

import com.cgm.qanda.QnAApplication;
import com.cgm.qanda.dataaccessobject.QuestionRepository;
import com.cgm.qanda.dataobject.Answer;
import com.cgm.qanda.dataobject.Question;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = QnAApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public class QuestionAnswerServiceImplTest {

    @Autowired
    QuestionAnswerService service;

    @Mock
    QuestionRepository repo;

    @Before
    public void setup() {
        Question question = createQuestionEntity();
        repo.save(question);
    }

    private Question createQuestionEntity() {
        Question question = new Question();
        question.setQuestion("question1");
        Answer answer = new Answer();
        answer.setAnswer("answer1");
        Set<Answer> set = new HashSet<>();
        set.add(answer);
        return question;
    }

    @Test
    public void testGetAnswers() {
        Question q = createQuestionEntity();
        Mockito.when(repo.findByQuestion("question1")).thenReturn(Optional.ofNullable(q));
        List<String> answers = service.getAnswers("question1");
        assertNotNull(answers);
        assertEquals(1, answers.size());
    }

    @Test
    public void testGetAnswerUnknownQuestion() {
        Question q = createQuestionEntity();
        Mockito.when(repo.findByQuestion("Unkown question")).thenReturn(Optional.ofNullable(q));
        List<String> answers = service.getAnswers("Unkown question");
        assertNotNull(answers);
        assertEquals("\"" + "the answer to life, universe and everything is 42" + "\"" + " according to" + "\""
                + "The hitchhikers guide to the Galaxy" + "\"", answers.get(0));
    }

    @Test
    public void addQuestionTest() {
        Question q = createQuestionEntity();
        q.setQuestion("question");
        Mockito.when(repo.save(q)).thenReturn(q);
        Mockito.when(repo.findByQuestion("question")).thenReturn(Optional.ofNullable(q));
        service.addQuestion("question", "answer1");
        List<String> answers = service.getAnswers("question");
        assertNotNull(answers);
        assertEquals("answer1", answers.get(0));

    }

    @Test
    public void addQuestionMultipleAnswersTest() {
        Question q = createQuestionEntity();
        q.setQuestion("question3");
        Mockito.when(repo.save(q)).thenReturn(q);
        Mockito.when(repo.findByQuestion("question3")).thenReturn(Optional.ofNullable(q));
        String ans = "answer1\"answer2\"answer3\"answer4";
        service.addQuestion("question3", ans);
        String[] answArr = ans.split("\"");
        List<String> answers = service.getAnswers("question3");
        assertNotNull(answers);
        for(String answer : answArr){
            assertTrue(answers.contains(answer));
        }

    }
    /*This test fails because there is no check on question lenght, throws error on size of varchar in the db*/
    @Test
    public void addQuestionTooLongTest() {
        Question q = createQuestionEntity();
        String question = StringUtils.repeat('*',257);
        q.setQuestion(question);
        Mockito.when(repo.save(q)).thenReturn(q);
        Mockito.when(repo.findByQuestion(question)).thenReturn(Optional.ofNullable(q));
        service.addQuestion(question, "answer1");
        List<String> answers = service.getAnswers(question);
        assertNotNull(answers);
        assertNotEquals("answer1", answers.get(0));

    }

}