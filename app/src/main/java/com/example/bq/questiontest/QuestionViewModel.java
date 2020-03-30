package com.example.bq.questiontest;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bq.datatypes.QuestionData;
import com.example.bq.profiletest.DataManager;
import com.example.bq.profiletest.FirebaseObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuestionViewModel extends ViewModel {

    private MutableLiveData<List<QuestionData>> questions;

    public QuestionViewModel() {
        questions = new MutableLiveData<>();
        questions.setValue(new ArrayList<QuestionData>());
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

    public LiveData<List<QuestionData>> getQuestions() {
        return questions;
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

    public void respondToQuestion() {

    }
}
