package com.example.bq.datamanager.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;

public class FirebaseFunction {
    private static final FirebaseFunctions functions = FirebaseFunctions.getInstance();

    // All possible firebase actions
    public static final String FUNCTION_GET_USER = "getUser";
    public static final String FUNCTION_CREATE_USER = "addUser";
    public static final String FUNCTION_GET_USERLIST = "viewUserList";
    public static final String FUNCTION_GET_BOOKS = "getBooks";
    public static final String FUNCTION_ADD_BOOK = "addBook";
    public static final String FUNCTION_DELETE_BOOK = "deleteBook";
    public static final String FUNCTION_GET_QUESTIONS = "getQuestions";
    public static final String FUNCTION_ADD_QUESTION = "addQuestion";
    public static final String FUNCTION_DELETE_QUESTION = "deleteQuestion";
    public static final String FUNCTION_RESPOND_QUESTION = "respondToQuestion";
    public static final String FUNCTION_GET_RESPONSES = "getQuestionResponses";
    public static final String FUNCTION_IS_ADMIN = "isAdmin";
    public static final String FUNCTION_BAN_USER = "banUser";

    // Make this class a static only class
    private FirebaseFunction() {
    }

    /**
     * This static function is a general implementation of the
     * {@link FirebaseFunctions#getHttpsCallable(String)} function and gives a generic callback to
     * the specified observer. <br>
     * The Firebaseobserver field may be null, as some firebase functions are commands and not
     * queries and therefore do not need to handle a callback.
     *
     * @param function The name of the firebase function on the firebase server to be called
     * @param data     Additional data to be sent with the call to the function, must be serializable
     * @param observer A {@link FirebaseObserver} object to process the function its callback result
     */
    public static void call(@NonNull final String function, @NonNull final Object data,
                            @Nullable final FirebaseObserver observer) {
        functions.getHttpsCallable(function).call(data).addOnSuccessListener(
                new OnSuccessListener<HttpsCallableResult>() {
                    @Override
                    public void onSuccess(HttpsCallableResult callResult) {
                        // If the observer is null, there is no need to process the response,
                        // as we cannot do anything with the response
                        if (observer == null) {
                            return;
                        }
                        // Result data is a HashMap with success boolean and result another object
                        HashMap<String, Object> response = (HashMap<String, Object>) callResult.getData();
                        assert (response != null);

                        // Create a callback that returns the function name and the response object of above
                        HashMap<String, Object> callback = new HashMap<>();
                        callback.put("action", function);
                        callback.put("response", response);
                        observer.notifyOfCallback(callback);
                    }
                });
    }
}
