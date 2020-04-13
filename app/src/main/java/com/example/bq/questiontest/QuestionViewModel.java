package com.example.bq.questiontest;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bq.datamanager.DataManager;
import com.example.bq.datamanager.ViewModelCaller;
import com.example.bq.datamanager.datatypes.QuestionData;
import com.example.bq.datamanager.datatypes.QuestionResponseData;
import com.example.bq.datamanager.firebase.FirebaseFunction;
import com.example.bq.datamanager.firebase.FirebaseObserver;

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

    /**
     * Load all questions of the specified study into the ViewModel
     *
     * @param study  Name of the study of which we want all questions
     * @param caller The {@link ViewModelCaller} object that requested the VM to load the data
     */
    void loadQuestionsIntoVM(@Nullable String study, final ViewModelCaller caller) {
        // If the study is null, return as there is nothing
        if (study == null) {
            return;
        }
        // Tell the DataManager to load the questions from firebase
        DataManager.getInstance().getQuestions(study, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                // Check whether the callback was created by the getQuestions function
                if (callback.get("action").equals(FirebaseFunction.FUNCTION_GET_QUESTIONS)) {
                    // If so, we get the response
                    HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");
                    // Now we check whether the request was successful
                    if ((boolean) response.get("success")) {
                        // If so, the result is a List of HashMaps
                        List<HashMap<String, String>> result = (List<HashMap<String, String>>) response.get("result");
                        // Which we convert into a List of QuestionData
                        List<QuestionData> data = new ArrayList<>();
                        for (HashMap<String, String> question : result) {
                            data.add(new QuestionData(question));
                        }
                        // And then we set our data
                        questions.setValue(data);
                        // Finally we tell our caller that it was successful
                        caller.callback(true);
                    } else {
                        // If it was unsuccessful, we return the error to the caller
                        caller.callback(response.get("error"));
                    }
                }
            }
        });
    }

    /**
     * Load all questions of the specified study into the ViewModel
     *
     * @param questionID
     * @param caller     The {@link ViewModelCaller} object that requested the VM to load the data
     */
    void loadResponsesIntoVM(@Nullable String questionID, final ViewModelCaller caller) {
        // If there is no question id, there is no need to load anything
        if (questionID == null) {
            return;
        }
        // Tell the DataManager to load all responses to this question
        DataManager.getInstance().getQuestionResponses(questionID, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                // Check whether the callback is created by the getQuestionResponses function
                if (callback.get("action") == FirebaseFunction.FUNCTION_GET_RESPONSES) {
                    // If so, load the response
                    HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");
                    // Check if the request was successful
                    if ((boolean) response.get("success")) {
                        // If so, the result is a List of HashMaps
                        List<HashMap<String, String>> result = (List<HashMap<String, String>>) response.get("result");
                        // Which we convert into a List of QuestionResponseData
                        List<QuestionResponseData> data = new ArrayList<>();
                        for (HashMap<String, String> question : result) {
                            data.add(new QuestionResponseData(question));
                        }
                        // And set our responses to this List
                        responses.setValue(data);

                        // Finally , we message the caller that it was successful
                        caller.callback(true);
                    } else {
                        // If not successful, message our caller the error
                        caller.callback(response.get("error"));
                    }
                }
            }
        });
    }

    LiveData<List<QuestionData>> getQuestions() {
        return questions;
    }

    LiveData<List<QuestionResponseData>> getResponses() {
        return responses;
    }

    /**
     * Add a question to the ViewModel and firebase database
     *
     * @param data
     * @param caller The {@link ViewModelCaller} object that requested the VM to load the data
     */
    void addQuestion(final QuestionData data, final ViewModelCaller caller) {
        // Create a reference to our current question list
        final List<QuestionData> questionList = questions.getValue();
        // Tell the DataManager to add the question
        DataManager.getInstance().addQuestion(data, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                // Check whether the callback was created by the addQuestion function
                if (callback.get("action").equals(FirebaseFunction.FUNCTION_ADD_QUESTION)) {
                    // If so, get the response
                    HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");
                    // And check whether the request was successful
                    if ((boolean) response.get("success")) {
                        // If so, add the question to our question list, and update it
                        questionList.add(0, data);
                        questions.setValue(questionList);
                        // Let our caller know it was successful
                        caller.callback(true);
                    } else {
                        // If not successful, send the error back to our caller
                        caller.callback(response.get("error"));
                    }
                }
            }
        });
    }

    /**
     * Delete a question from the ViewModel and from the firebase database
     *
     * @param data   The {@link QuestionData} object containing the data of the question to be deleted
     * @param caller The {@link ViewModelCaller} object that requested the VM to load the data
     */
    void deleteQuestion(QuestionData data, final ViewModelCaller caller) {
        // Tell the DataManager to delete this question from the firebase database
        DataManager.getInstance().deleteQuestion(data.study, data.id, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                // Check whether the callback is created by the deleteQuestion function
                if (callback.get("action").equals(FirebaseFunction.FUNCTION_DELETE_QUESTION)) {
                    // If so, load the response
                    HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");
                    // And check if the request was successful
                    if ((boolean) response.get("success")) {
                        // If so, message the caller that it was successful
                        caller.callback(true);
                    } else {
                        // If not, message the caller the error
                        caller.callback(response.get("error"));
                    }
                }
            }
        });
    }

    /**
     * @param data
     * @param caller The {@link ViewModelCaller} object that requested the VM to load the data
     */
    public void respondToQuestion(final QuestionResponseData data, final ViewModelCaller caller) {
        // Load our current responses
        final List<QuestionResponseData> responseList = responses.getValue();
        // Tell the DataManager to add the new response to the firebase database
        DataManager.getInstance().respondToQuestion(data, new FirebaseObserver() {
            @Override
            public void notifyOfCallback(HashMap<String, Object> callback) {
                // Check whether the callback is created by the respondToQuestion function
                if (callback.get("action").equals(FirebaseFunction.FUNCTION_RESPOND_QUESTION)) {
                    // If so, load the response
                    HashMap<String, Object> response = (HashMap<String, Object>) callback.get("response");
                    // And see if the request was successful
                    if ((boolean) response.get("success")) {
                        // If so, add the response to the list
                        responseList.add(data);
                        responses.setValue(responseList);

                        // And finally tell our caller it was succesful
                        caller.callback(true);
                    } else {
                        // If not, message our caller the error
                        caller.callback(response.get("error"));
                    }
                }
            }
        });
    }
}
