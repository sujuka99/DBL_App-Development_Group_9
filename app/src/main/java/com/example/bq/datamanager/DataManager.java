package com.example.bq.datamanager;

import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bq.datamanager.datatypes.BookData;
import com.example.bq.datamanager.datatypes.QuestionData;
import com.example.bq.datamanager.datatypes.QuestionResponseData;
import com.example.bq.datamanager.datatypes.UserData;
import com.example.bq.datamanager.firebase.FirebaseFunction;
import com.example.bq.datamanager.firebase.FirebaseObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class DataManager {

    private static DataManager instance;

    private final String DEFAULT_IMAGE_PATH = "default/default.jpg";

    private final FirebaseFunctions functions;
    private final StorageReference ref;

    // Create a new DataManager singleton
    private DataManager() {
        functions = FirebaseFunctions.getInstance();
        ref = FirebaseStorage.getInstance().getReference();
    }

    /**
     * Get the download uri of an image from storage <br>
     * Will give a Uri object as callback to the observer object
     *
     * @param path     The location of the image to be downloaded
     * @param observer The {@link FirebaseObserver} object that will handle the callback
     */
    public void downloadImageFromStorage(String path, final FirebaseObserver observer) {
        // Get the storage reference located at path
        StorageReference storageRef = ref.child(path);

        // Tell firebase we want a Uri that we can use in Glide
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                HashMap<String, Object> callback = new HashMap<>();
                callback.put("uri", uri);
                observer.notifyOfCallback(callback);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // If we cannot find an image at that path, try to get the default image Uri
                StorageReference storageRef = ref.child(DEFAULT_IMAGE_PATH);

                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri uri) {
                        // If we are successful, return the default image
                        HashMap<String, Object> callback = new HashMap<>();
                        callback.put("uri", uri);
                        observer.notifyOfCallback(callback);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If we fail, something is wrong and we cannot do anything about it
                        // return an empty Uri
                        HashMap<String, Object> callback = new HashMap<>();
                        callback.put("uri", Uri.EMPTY);
                        observer.notifyOfCallback(callback);
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    /**
     * Upload a image to the firebase storage
     *
     * @param path   - The location of where the image has to be stored
     * @param bitmap - The bitmap to be uploaded to the server
     */
    public void uploadImageToStorage(@NonNull String path, @NonNull Bitmap bitmap) {
        StorageReference storageRef = ref.child(path);

        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getWidth() * bitmap.getHeight());
        bitmap.copyPixelsToBuffer(buffer);

        storageRef.putBytes(buffer.array());

        buffer.clear();
    }

    /**
     * Create a user reference in the database containing the minimal required data
     *
     * @param id       ID of the user, generated by FirebaseAuth
     * @param fullName The fullName that was specified when the account was registered
     */
    public void createUserInDatabase(@NonNull String id, @NonNull String fullName) {
        // First we create a new UserData object
        UserData data = new UserData();
        // Set the fields with the data we have
        data.id = id;
        data.fullName = fullName;

        // And then we call the function that will create our user reference in the database
        // using our data, which we convert into a HashMap first
        // We do not expect a callback, therefore we let the observer field be null
        FirebaseFunction.call(FirebaseFunction.FUNCTION_CREATE_USER, data.toMap(), null);
    }

    /**
     * Get userdata of the specified user from the database
     *
     * @param id       ID of the user of which we want the data
     * @param observer A {@link FirebaseObserver} object to handle the callback
     */
    public void getUserFromDatabase(String id, final FirebaseObserver observer) {
        FirebaseFunction.call(FirebaseFunction.FUNCTION_GET_USER, id, observer);
    }

    /**
     * Check if the specified user is registered as an administrator
     *
     * @param id       ID of the user of which we want the admin status
     * @param observer A {@link FirebaseObserver} object to handle the callback
     */
    public void isAdmin(String id, final FirebaseObserver observer) {
        FirebaseFunction.call(FirebaseFunction.FUNCTION_IS_ADMIN, id, observer);
    }

    /**
     * Get userdata of the specified user from the database
     *
     * @param email    Email of the user of which we want to ban the account
     * @param observer A {@link FirebaseObserver} object to handle the query callback
     */
    public void banUser(String email, final FirebaseObserver observer) {
        FirebaseFunction.call(FirebaseFunction.FUNCTION_BAN_USER, email, observer);
    }

    /**
     * Get a list of books for the specified study from the database
     *
     * @param study    Name of the study for which we want all the books from the database
     * @param loc      Location of the user browsing the books, in order to order the books by distance
     * @param observer A {@link FirebaseObserver} object to handle the callback
     */
    public void getBooks(String study, @Nullable Location loc, final FirebaseObserver observer) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("study", study);
        if (loc != null) {
            data.put("location", loc.getLatitude() + ":" + loc.getLongitude());
        }

        FirebaseFunction.call(FirebaseFunction.FUNCTION_GET_BOOKS, data, observer);
    }

    /**
     * Add a book to the database
     *
     * @param data     {@link BookData} object containing all the data of the book to be added
     * @param observer A {@link FirebaseObserver} object to handle the callback
     */
    public void addBook(BookData data, final FirebaseObserver observer) {
        FirebaseFunction.call(FirebaseFunction.FUNCTION_ADD_BOOK, data.toMap(), observer);
    }

    /**
     * Delete a book from the database
     *
     * @param study    Name of the study under which the book is listed
     * @param id       ID of the user of which we want the data
     * @param observer A {@link FirebaseObserver} object to handle the callback
     */
    public void deleteBook(String study, String id, final FirebaseObserver observer) {
        HashMap<String, String> data = new HashMap<>();

        data.put("study", study);
        data.put("id", id);

        FirebaseFunction.call(FirebaseFunction.FUNCTION_DELETE_BOOK, data, observer);
    }

    /**
     * Get questions from the database for the specified study
     *
     * @param study    Name of the study under which the question is listed
     * @param observer A {@link FirebaseObserver} object to handle the callback
     */
    public void getQuestions(String study, final FirebaseObserver observer) {
        FirebaseFunction.call(FirebaseFunction.FUNCTION_GET_QUESTIONS, study, observer);
    }

    /**
     * Add a question to the database
     *
     * @param data     {@link QuestionData} object containing all the data of the question to be added
     * @param observer A {@link FirebaseObserver} object to handle the callback
     */
    public void addQuestion(QuestionData data, final FirebaseObserver observer) {
        FirebaseFunction.call(FirebaseFunction.FUNCTION_ADD_QUESTION, data.toMap(), observer);
    }

    /**
     * Delete a question from the database
     *
     * @param id       ID of the question to be deleted
     * @param study    Name of the study under which the question is listed
     * @param observer A {@link FirebaseObserver} object to handle the callback
     */
    public void deleteQuestion(String study, String id, final FirebaseObserver observer) {
        HashMap<String, String> data = new HashMap<>();
        data.put("study", study);
        data.put("id", id);

        FirebaseFunction.call(FirebaseFunction.FUNCTION_DELETE_QUESTION, data, observer);
    }

    /**
     * Get a list of all responses to a question
     *
     * @param id       ID of the question of which we want the responses
     * @param observer A {@link FirebaseObserver} object to handle the callback
     */
    public void getQuestionResponses(String id, final FirebaseObserver observer) {
        FirebaseFunction.call(FirebaseFunction.FUNCTION_GET_RESPONSES, id, observer);
    }

    /**
     * Add a response to a question to the database
     *
     * @param data     {@link QuestionResponseData} object containing all the data of the response to be added
     * @param observer A {@link FirebaseObserver} object to handle the callback
     */
    public void respondToQuestion(QuestionResponseData data, final FirebaseObserver observer) {
        FirebaseFunction.call(FirebaseFunction.FUNCTION_RESPOND_QUESTION, data.toMap(), observer);
    }

    /**
     * Get a list of all users from the database. <br>
     * If the user is not an admin, banned accounts will not show up in that list
     *
     * @param observer A {@link FirebaseObserver} object to handle the callback
     */
    public void getUsers(final FirebaseObserver observer) {
        FirebaseFunction.call(FirebaseFunction.FUNCTION_GET_USERLIST, "", observer);
    }

    /**
     * Obtain a reference to the DataManager singleton instance
     * @return The singleton instance of DataManager
     */
    public static DataManager getInstance() {
        return instance == null ? (instance = new DataManager()) : instance;
    }

}
