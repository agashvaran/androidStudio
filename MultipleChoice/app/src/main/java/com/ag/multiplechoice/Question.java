package com.ag.multiplechoice;

public class Question {
    public String questionText;
    public String correctAnswer;
    public String other1;
    public String other2;
    public String other3;

    //just generated constructors and getters and setters
    public Question(){}

    public Question(String questionText, String correctAnswer, String other1, String other2, String other3) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.other1 = other1;
        this.other2 = other2;
        this.other3 = other3;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getOther1() {
        return other1;
    }

    public void setOther1(String other1) {
        this.other1 = other1;
    }

    public String getOther2() {
        return other2;
    }

    public void setOther2(String other2) {
        this.other2 = other2;
    }

    public String getOther3() {
        return other3;
    }

    public void setOther3(String other3) {
        this.other3 = other3;
    }
}
