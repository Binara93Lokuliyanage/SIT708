package com.example.quizly.utils;

import com.example.quizly.models.Question;

public class QuizData {
    public static Question[] getQuestions() {
        return new Question[]{
                new Question("450 + 300", new String[]{"500","750","150","1000"},1),
                new Question("450 - 300", new String[]{"500","750","150","1000"},2)
        };
    }
}