package com.example.bq.questiontest;

import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bq.datatypes.QuestionData;
import com.example.bq.datatypes.QuestionResponseData;
import com.example.bq.profiletest.DataManager;
import com.example.bq.profiletest.FirebaseObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionViewModel extends ViewModel {

    private MutableLiveData<List<QuestionData>> questions;
    private MutableLiveData<List<QuestionResponseData>> responses;

    public QuestionViewModel() {
        questions = new MutableLiveData<>();
        responses = new MutableLiveData<>();
        questions.setValue(new ArrayList<QuestionData>());
        responses.setValue(new ArrayList<QuestionResponseData>());
    }

    public void loadQuestionsIntoVM(String study) {
        if (study == null) {
            return;
        }
        DataManager.getInstance().getQuestions(study, 0, "", new FirebaseObserver() {
            @Override
            public void notifyOfCallback(Object obj) {
                if (obj instanceof ArrayList) {
                    ArrayList<QuestionData> data = (ArrayList<QuestionData>) obj;
                    questions.setValue(data);
                }
            }
        });
    }

    public void loadResponsesIntoVM(String study, String questionID){
        if(questionID == null || study == null){
            return;
        }
        DataManager.getInstance().getQuestionResponses(questionID, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(Object obj) {
                if(obj instanceof  ArrayList){
                    ArrayList<QuestionResponseData> data = (ArrayList<QuestionResponseData>) obj;
                    responses.setValue(data);
                }
            }
        });
    }

    public LiveData<List<QuestionData>> getQuestions() {
        return questions;
    }

    public LiveData<List<QuestionResponseData>> getResponses(){
        return responses;
    }

    public void addQuestion(final QuestionData data, final FirebaseObserver observer) {
        final List<QuestionData> questionList = questions.getValue();
        DataManager.getInstance().addQuestion(data, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(Object obj) {
                if ((boolean) obj) {
                    questionList.add(data);
                    questions.setValue(questionList);
                }
                observer.notifyOfCallback(obj);
            }
        });
    }

    public void deleteQuestion(QuestionData data, final FirebaseObserver observer) {
        DataManager.getInstance().deleteQuestion(data.study, data.id, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(Object obj) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("action", "removeQuestion");
                result.put("result", obj);
                observer.notifyOfCallback(result);
            }
        });
    }

    public void respondToQuestion(QuestionResponseData data, final FirebaseObserver observer) {
        DataManager.getInstance().respondToQuestion(data, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(Object obj) {
                if(obj instanceof Boolean){
                    HashMap<String, Object> response = new HashMap<>();
                    response.put("action", "respondToQuestion");
                    if((boolean) obj){
                        response.put("result", true);

                    }else{
                        response.put("result", false);
                    }
                    observer.notifyOfCallback(response);
                }
            }
        });
    }
}
