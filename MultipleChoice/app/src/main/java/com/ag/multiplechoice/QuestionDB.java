package com.ag.multiplechoice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.ag.multiplechoice.Singleton.TAG;

public class QuestionDB {
    ArrayList<Question> questionBank = new ArrayList<>(); //to contain all questions
    private String api_url = "https://opentdb.com/api.php?amount=50&type=multiple";


    public List<Question> loadQuestions(final VolleyResponse callBack){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_url, (JSONObject) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("results");
                            //Log.d("apiTing2", "getApi: " + send_api());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        assert jsonArray != null; //note to self: put in an if statement later
                        for(int i = 0; i < jsonArray.length(); i++){
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Question question = new Question();

                                //get question and correctAnswer:
                                question.setQuestionText(jsonObject.getString("question"));
                                question.setCorrectAnswer(jsonObject.getString("correct_answer"));
                                //getting the incorrect answers:
                                question.setOther1(jsonObject.getJSONArray("incorrect_answers").get(0).toString());
                                question.setOther2(jsonObject.getJSONArray("incorrect_answers").get(1).toString());
                                question.setOther3(jsonObject.getJSONArray("incorrect_answers").get(2).toString());

                                Log.d("question", "onResponse: " + question);

                                questionBank.add(question);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        if(null != callBack){
                            callBack.endResult(questionBank); //to get questionBank to MainActivity due to Volley being asynchoronous, gotta tell WHEN all the questions have been added to questionBank.
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "onErrorResponse: " + error.getMessage());

            }
        });
        Singleton.getInstance().addToRequestQueue(jsonObjectRequest);
        return questionBank;
    }


}
