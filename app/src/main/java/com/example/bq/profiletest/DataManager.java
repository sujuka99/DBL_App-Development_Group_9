package com.example.bq.profiletest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bq.datatypes.BookData;
import com.example.bq.datatypes.QuestionData;
import com.example.bq.datatypes.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataManager {

    private static DataManager instance;

    private FirebaseFunctions functions;

    private DataManager() {
        functions = FirebaseFunctions.getInstance();
    }

    /**
     * Attempt ton download the profile picture data of the desired user and send it to the observer
     *
     * @param id       - ID of the desired user
     * @param observer - The {@link FirebaseObserver} object that will handle the callback
     */
    public void loadProfilePicture(final String id, final FirebaseObserver observer) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();

        // Get the storage reference of the user it's profile picture
        // Which is stored as "users/<ID>/profile.jpg"
        StorageReference storageRef = ref.child("users/" + id + "/profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        // Attempt to download the image and get the bytes
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // If the image was found, convert the result into a Bitmap
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Send the Bitmap to the observer
                observer.notifyOfCallback(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // If the image was not found/something was wrong, we attempt to load the default
                // profile picture
                loadDefaultPicture(observer);
            }
        });
    }

    /**
     * Download the default profile picture and send the data to the observer
     *
     * @param observer - The {@link FirebaseObserver} object that will handle the callback
     */
    private void loadDefaultPicture(final FirebaseObserver observer) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();

        // Get the storage reference of the default profile picture
        // Which is stored as "default/profile.jpg"
        StorageReference storageRef = ref.child("default/profile.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        // Attempt to download the image and get the bytes
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // If the image was found, convert the result into a Bitmap
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Send the Bitmap to the observer
                observer.notifyOfCallback(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // If failed, something went wrong and there isn't really much
                // we can do...
                Log.d("getDefaultProfilePicture: ", "Failed due to an: " +
                        exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    /**
     * Upload a profile picture to the firebase storage for a user
     *
     * @param id   - The ID of the desired user
     * @param path - The Uri pointer to the picture to be uploaded
     */
    public void setProfilePicture(@NonNull String id, @NonNull Uri path) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        StorageReference storageRef = ref.child("users/" + id + "/profile.jpg");

        storageRef.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("setProfilePicture: ", "Failed due to an: " +
                        exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    public void createUserInDatabase(String id, String fullName) {
        UserData data = new UserData();

        data.id = id;
        data.fullName = fullName;

        FirebaseFunctions functions = FirebaseFunctions.getInstance();
        functions.getHttpsCallable("addUser").call(data.toMap()).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                Object resultData = task.getResult().getData();
            }
        });
    }

    public void getUserFromDatabase(String id, final FirebaseObserver observer) {
        functions.getHttpsCallable("getUser").call(id).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                // Result data is a HashMap with success boolean and result a new HashMap<String, Object>
                HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                if ((boolean) response.get("success")) {
                    HashMap<String, Object> result = (HashMap<String, Object>) response.get("result");
                    UserData data;
                    if (result == null) {
                        data = new UserData();
                    } else {
                        data = new UserData(result);
                    }
                    observer.notifyOfCallback(data);
                }
            }
        });
    }

    public void isAdmin(String id, final FirebaseObserver observer) {
        functions.getHttpsCallable("isAdmin").call(id).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                if ((boolean) response.get("success")) {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("action", "isAdmin");
                    result.put("result", response.get("admin"));
                    observer.notifyOfCallback(result);
                } else {
                    Log.d("isAdmin", response.get("error") + "");
                }
            }
        });
    }

    public void isBanned(String id, final FirebaseObserver observer) {
        functions.getHttpsCallable("isBanned").call(id).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                if ((boolean) response.get("success")) {
                    boolean result = (boolean) response.get("admin");
                    observer.notifyOfCallback(result);
                }
            }
        });
    }

    public void banUser(String banID, final FirebaseObserver observer) {
        functions.getHttpsCallable("banUser").call(banID).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                if ((boolean) response.get("success")) {
                    observer.notifyOfCallback(true);
                } else {
                    observer.notifyOfCallback(false);
                }
            }
        });
    }

    public void unbanUser(String banID, final FirebaseObserver observer) {
        functions.getHttpsCallable("unbanUser").call(banID).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                if ((boolean) response.get("success")) {
                    observer.notifyOfCallback(true);
                } else {
                    observer.notifyOfCallback(false);
                }
            }
        });
    }

    public void viewBannedUsers(int page, final FirebaseObserver observer) {
        functions.getHttpsCallable("viewBannedUsers").call(page).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                if ((boolean) response.get("success")) {
                    Log.d("ViewBannedUsers result", response.get("result").getClass().toString());
                    List<Object> result = (List<Object>) response.get("result");
                    Log.d("ViewBannedUsers listResult", result.toString());
                }
            }
        });
    }

    public void getBooks(String study, String ordering, int page, String query, final FirebaseObserver observer) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("study", study);
        data.put("order", ordering);
        data.put("page", page);
        data.put("query", query);

        functions.getHttpsCallable("getBooks").call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.getResult().getData() != null) {
                    HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                    if ((boolean) response.get("success")) {
                        List<HashMap<String, String>> result = (List<HashMap<String, String>>) response.get("result");
                        List<BookData> callBack = new ArrayList<>();
                        for (HashMap<String, String> book : result) {
                            callBack.add(new BookData(book));
                        }
                        observer.notifyOfCallback(callBack);
                    }
                }
            }
        });
    }

    public void addBook(BookData data, final FirebaseObserver observer) {
        functions.getHttpsCallable("addBook").call(data.toMap()).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                if ((boolean) response.get("success")) {
                    observer.notifyOfCallback(true);
                } else {
                    observer.notifyOfCallback(false);
                }
            }
        });
    }

    public void deleteBook(String study, String id, final FirebaseObserver observer) {
        HashMap<String, String> data = new HashMap<>();

        data.put("study", study);
        data.put("id", id);

        functions.getHttpsCallable("deleteBook").call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                if ((boolean) response.get("success")) {
                    observer.notifyOfCallback(true);
                } else {
                    observer.notifyOfCallback(false);
                }
            }
        });
    }

    public void getQuestions(String study, int page, String query, final FirebaseObserver observer) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("study", study);
        data.put("page", page);
        data.put("query", query);

        functions.getHttpsCallable("getQuestions").call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                if (task.getResult().getData() != null) {
                    HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                    if ((boolean) response.get("success")) {
                        List<HashMap<String, String>> result = (List<HashMap<String, String>>) response.get("result");
                        List<QuestionData> callBack = new ArrayList<>();
                        for (HashMap<String, String> question : result) {
                            callBack.add(new QuestionData(question));
                        }
                        observer.notifyOfCallback(callBack);
                    }
                }
            }
        });
    }

    public void addQuestion(QuestionData data, final FirebaseObserver observer) {
        functions.getHttpsCallable("addQuestion").call(data.toMap()).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                if ((boolean) response.get("success")) {
                    observer.notifyOfCallback(true);
                } else {
                    observer.notifyOfCallback(false);
                }
            }
        });
    }

    public void deleteQuestion(String study, String id, final FirebaseObserver observer) {
        HashMap<String, String> data = new HashMap<>();
        data.put("study", study);
        data.put("id", id);

        functions.getHttpsCallable("deleteQuestion").call(data).addOnCompleteListener(new OnCompleteListener<HttpsCallableResult>() {
            @Override
            public void onComplete(@NonNull Task<HttpsCallableResult> task) {
                HashMap<String, Object> response = (HashMap<String, Object>) task.getResult().getData();
                if ((boolean) response.get("success")) {
                    observer.notifyOfCallback(true);
                } else {
                    observer.notifyOfCallback(false);
                }
            }
        });
    }

    public static DataManager getInstance() {
        return instance == null ? (instance = new DataManager()) : instance;
    }

}
