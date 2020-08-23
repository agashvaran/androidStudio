package com.ag.multiplechoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //initialize all buttons,textviews, and cardviews:
    private TextView questionTextView;
    private static final String MESSAGE_ID = "message";
    private RadioGroup radioGroup;
    private RadioButton choiceTextView1;
    private RadioButton choiceTextView2;
    private RadioButton choiceTextView3;
    private RadioButton choiceTextView4;
    private ImageButton closeQuiz;
    private CardView colorCard;

    private TextView scoreCard;
    private int scoreNow = 0;
    private int topScore = 0;
    private TextView topScoreInGameTextView;

    private TextView questionCounter;
    private int questionNumber = 0;

    private List<Question> questionList;
    ArrayList<String> choices = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionList = new QuestionDB().loadQuestions(new VolleyResponse() {
            @SuppressLint("SetTextI18n")
            @Override
            public void endResult(ArrayList<Question> questionBank) {
                //displaying question number for the first time
                questionCounter.setText(questionNumber + " / " + questionList.size());
                //getting the question into the textview for the first time
                questionTextView.setText(questionBank.get(questionNumber).getQuestionText());

                //fetching and adding all possible answers to an array and shuffling the array to get the answer in a random textview each time-
                //-so the answer is not always in the same radiobutton.
                choices.add(questionBank.get(questionNumber).getOther1());
                choices.add(questionBank.get(questionNumber).getOther2());
                choices.add(questionBank.get(questionNumber).getOther3());
                choices.add(questionBank.get(questionNumber).getCorrectAnswer());
                Collections.shuffle(choices);
                //Log.d("array", "endResult: " + choices);
                choiceTextView1.setText(choices.get(0));
                choiceTextView2.setText(choices.get(1));
                choiceTextView3.setText(choices.get(2));
                choiceTextView4.setText(choices.get(3));

            }
        });

        //connecting the textViews,buttons etc with the mainActivity.xml
        questionTextView = findViewById(R.id.questions_text_view);
        choiceTextView1 = findViewById(R.id.choice_text1);
        choiceTextView2 = findViewById(R.id.choice_text2);
        choiceTextView3 = findViewById(R.id.choice_text3);
        choiceTextView4 = findViewById(R.id.choice_text4);
        radioGroup = findViewById(R.id.radioGroup);
        questionCounter = findViewById(R.id.question_counter_text_view);
        closeQuiz = findViewById(R.id.close_quiz);
        scoreCard = findViewById(R.id.score_text_view);
        colorCard = findViewById(R.id.cardView);
        topScoreInGameTextView = findViewById(R.id.top_score_in_game_text_view);

        //setting onclicklistener
        questionTextView.setOnClickListener(this);
        choiceTextView1.setOnClickListener(this);
        choiceTextView2.setOnClickListener(this);
        choiceTextView3.setOnClickListener(this);
        choiceTextView4.setOnClickListener(this);
        questionCounter.setOnClickListener(this);
        closeQuiz.setOnClickListener(this);

        //load top score.
        SharedPreferences sharedPreferences = this.getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        topScore = sharedPreferences.getInt("topScore", 0);
        topScoreInGameTextView.setText("Top Score:" + topScore);



    }

    //defining onclick for each button,textview etc.
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.choice_text1:
                checkAnswer(choices.get(0), choiceTextView1);
                updateView();
                break;
            case R.id.choice_text2:
                checkAnswer(choices.get(1), choiceTextView2);
                updateView();
                break;
            case R.id.choice_text3:
                checkAnswer(choices.get(2), choiceTextView3);
                updateView();
                break;
            case R.id.choice_text4:
                checkAnswer(choices.get(3), choiceTextView4);
                updateView();
                break;
            case R.id.close_quiz:
                moveToMenu();
                break;
            default:
                break;
        }
    }

    //check answers and run the appropriate methods
    @SuppressLint("SetTextI18n")
    private void checkAnswer(String userAnswer, Button userClicked){
        String correctAnswer = questionList.get(questionNumber).getCorrectAnswer();
        boolean check = userAnswer.equals(correctAnswer);
        if(check){ //if answer is right
            answerIndication("correct"); //play correct choice animation
            //add score
            scoreNow++;

            //saving score
            SharedPreferences sharedPreferences = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("recentScore", scoreNow);
            if(scoreNow > topScore){
                editor.putInt("topScore", scoreNow);
                editor.apply();
            }

            //change textView to show updated score
            topScoreInGameTextView.setText("Top Score:" + topScore);
            //move to next question if there are more questions
            if (questionNumber < questionList.size()-1){
                questionNumber += 1;
            }
            //no more questions, then go to menu.
            else{
                moveToMenu();
            }
        }
        else{ //if answer is wrong.
            answerIndication("wrong"); //play wrong answer animation
            Toast.makeText(MainActivity.this, "Wrong! it was " + correctAnswer , Toast.LENGTH_SHORT).show(); //show the correct answer
            //move to next question or menu
            if (questionNumber < questionList.size()-1){ questionNumber += 1; }
            else{
                moveToMenu();
            }
        }
    }

    //to change the card colour briefly when the user presses the right/wrong answer
    private void answerIndication(String testResult){
        AlphaAnimation colorChange = new AlphaAnimation(0, 2);
        colorChange.setDuration(100);
        colorChange.setRepeatCount(1);
        colorChange.setRepeatMode(Animation.ZORDER_TOP);
        colorCard.setAnimation(colorChange);

        if(testResult.equals("correct")){
            colorChange.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    colorCard.setCardBackgroundColor(Color.GREEN); //green for correct answer
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    colorCard.setCardBackgroundColor(Color.WHITE); //change back to white, so it doesn't stay green
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        }
        else{
            colorChange.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    colorCard.setCardBackgroundColor(Color.RED); //red for wrong answer
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    colorCard.setCardBackgroundColor(Color.WHITE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        }
    }

    //change question, choices and clear radio group check. This run each time the user picks an answer
    @SuppressLint("SetTextI18n")
    private void updateView() {
        questionTextView.setText(questionList.get(questionNumber).getQuestionText()); //change question
        questionCounter.setText(questionNumber + " / " + questionList.size()); //update question counter
        scoreCard.setText(scoreNow + " / " + questionList.size()); //update scorecard
        radioGroup.clearCheck(); //clear last selected button

        //update choices
        choices.clear(); //remove old question's answers
        choices.add(questionList.get(questionNumber).getOther1());
        choices.add(questionList.get(questionNumber).getOther2());
        choices.add(questionList.get(questionNumber).getOther3());
        choices.add(questionList.get(questionNumber).getCorrectAnswer());

        //shuffle array so the correct answer is not always in the same box
        Collections.shuffle(choices);

        //Log.d("array", "endResult: " + choices);
        //assigning choices to radio buttons.
        choiceTextView1.setText(choices.get(0));
        choiceTextView2.setText(choices.get(1));
        choiceTextView3.setText(choices.get(2));
        choiceTextView4.setText(choices.get(3));
    }

    //move back to the menu screen
    private void moveToMenu(){
        //go back to menu screen and show the score the user got
        Intent intent = getIntent();
        intent.putExtra("score", String.valueOf(topScore)); //sending the top score to menu
        intent.putExtra("recent_score", String.valueOf(scoreNow)); //sending the recent score to menu
        setResult(RESULT_OK, intent);
        finish();
    }
}




