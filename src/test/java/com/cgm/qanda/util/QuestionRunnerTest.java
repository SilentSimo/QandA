package com.cgm.qanda.util;

import com.cgm.qanda.QnAApplication;
import com.cgm.qanda.QuestionRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = QnAApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
/*Tried to test class Question runner but it is not designed for testing*/
public class QuestionRunnerTest {

    @Autowired
    QuestionRunner runner;

    @Mock
    Scanner scanner;
    BufferedReader reader;

    private String question1 = "question1";
    private String submitValidAnswer = "question1?\"answer1\"\"answer2\"\"answer3\"";
    private String answerToLife = "\"" + "the answer to life, universe and everything is 42" + "\"" + " according to" + "\""
                                + "The hitchhikers guide to the Galaxy" + "\"";
    private String answersQuestion1 = "answer1" + "\n" + "answer2" + "\n" + "answer3";

    private String resultValidInput = "Please enter 1 to ask a question:" + "\n"
                                    + "Please enter 2 to submit an answer: "+ "\n"
                                    + "Please enter any other number to exit: " + "\n"
                                    + "What is your question : "+ "\n"
                                    + answerToLife + "\n" + "Next input please."+ "\n"
                                    + "Lets submit an answer" + "\n"
                                    + "\n" + "Next input please."
                                    + "What is your question : "+ "\n"
                                    + answersQuestion1 + "\n" + "Next input please."+ "\n"
                                    + "Exit command received.Bye.";

    @Test
    public void testRunValidInput() throws Exception{
        Mockito.when(scanner.nextInt()).thenReturn(1,2, 1,  5);
        Mockito.when(reader.readLine()).thenReturn(question1, submitValidAnswer, question1);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo));
        runner.run(new String[]{});
        bo.flush();
        String output = new String(bo.toByteArray());
        assertTrue(output.startsWith(resultValidInput));
    }
}
