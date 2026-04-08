package com.example.quizly.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.quizly.R;
import com.example.quizly.models.Question;
import com.example.quizly.utils.QuizData;

public class QuizActivity extends AppCompatActivity {
    TextView questionText;
    Button option1, option2, option3, option4, submitBtn;
    ProgressBar progressBar;

    Question[] questions;

    int currentQuestionIndex = 0;
    int selectedOption = -1;
    int score = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.questionText);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        submitBtn = findViewById(R.id.submitBtn);
        progressBar = findViewById(R.id.progressBar);

        questions = QuizData.getQuestions();

        loadQuestion();

        option1.setOnClickListener(v -> {
            selectedOption = 0;
            option1.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.option_selected)
            );
        });
        option2.setOnClickListener(v -> {
            selectedOption = 1;
            option2.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.option_selected)
            );
        });
        option3.setOnClickListener(v -> {
            selectedOption = 2;
            option3.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.option_selected)
            );
        });
        option4.setOnClickListener(v -> {
            selectedOption = 3;
            option4.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.option_selected)
            );
        });

        submitBtn.setOnClickListener(v -> handleSubmit());
    }

    private void loadQuestion() {
        Question q = questions[currentQuestionIndex];

        questionText.setText(q.getQuestion());

        option1.setText(q.getOptions()[0]);
        option2.setText(q.getOptions()[1]);
        option3.setText(q.getOptions()[2]);
        option4.setText(q.getOptions()[3]);

        selectedOption = -1;

        resetOptions();
        updateProgress();
    }

    private void handleSubmit() {

        if (selectedOption == -1) return;

        int correctIndex = questions[currentQuestionIndex].getCorrectAnswer();

        Button[] options = {option1, option2, option3, option4};

        for (int i = 0; i < options.length; i++) {

            if (i == correctIndex) {
                options[i].setBackgroundColor(R.color.option_correct);
                options[i].setTextColor(R.color.black);
            } else if (i == selectedOption) {
                options[i].setBackgroundColor(Color.RED);
                options[i].setTextColor(R.color.white);
            }

            options[i].setEnabled(false);
        }

        if (selectedOption == correctIndex) {
            score++;
        }

        submitBtn.setText("Next");

        submitBtn.setOnClickListener(v -> nextQuestion());
    }

    private void nextQuestion() {
        currentQuestionIndex++;

        if (currentQuestionIndex < questions.length) {
            loadQuestion();
            submitBtn.setText("Submit");
            submitBtn.setOnClickListener(v -> handleSubmit());
        } else {
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
        }
    }

    private void resetOptions() {
        Button[] options = {option1, option2, option3, option4};

        for (Button btn : options) {
            btn.setEnabled(true);
            btn.setBackgroundColor(getColor(R.color.option_default));
        }
    }

    private void updateProgress() {
        int progress = (int) (((currentQuestionIndex + 1) * 100.0f) / questions.length);
        progressBar.setProgress(progress);
    }


}
