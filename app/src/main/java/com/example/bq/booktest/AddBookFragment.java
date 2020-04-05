package com.example.bq.booktest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.bq.MainActivity;
import com.example.bq.R;
import com.example.bq.datatypes.BookData;
import com.example.bq.profiletest.FirebaseObserver;
import com.example.bq.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddBookFragment extends Fragment implements FirebaseObserver {

    private final int TAKE_IMAGE = 0;
    private final int PICK_IMAGE = 1;

    private ImageView bookImage;
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookDescription;
    private EditText bookPrice;

    BookViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addbook, container, false);
        viewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        initializeComponents(root);

        return root;
    }

    private void initializeComponents(View root) {
        bookImage = root.findViewById(R.id.bookImage);
        bookAuthor = root.findViewById(R.id.bookAuthor);
        bookDescription = root.findViewById(R.id.bookDescription);
        bookPrice = root.findViewById(R.id.bookPrice);
        bookTitle = root.findViewById(R.id.bookTitle);

        Button selectImage = root.findViewById(R.id.imageSelect);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        Button registerBook = root.findViewById(R.id.registerBook);

        registerBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBook();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose an image to upload");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch (options[item].toString()) {
                    case "Take Photo":
                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, TAKE_IMAGE);
                        break;
                    case "Choose from Gallery":
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE);
                        break;
                    default:
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if (resultCode == RESULT_OK && data != null) {
                switch (requestCode) {
                    case TAKE_IMAGE:
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        bookImage.setImageBitmap(selectedImage);
                        break;
                    case PICK_IMAGE:
                        Uri selectedImage2 = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage2 != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage2,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                bookImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                        break;
                }
            }
        }
    }

    private void registerBook() {
        BookData data = new BookData();

        if (bookTitle.getText() == null || bookTitle.getText().toString().trim().length() == 0) {
            bookTitle.setError("A book must have a title!");
            return;
        }
        if (bookAuthor.getText() == null || bookAuthor.getText().toString().trim().length() == 0) {
            bookAuthor.setError("A book must have an author!");
            return;
        }
        if (bookDescription.getText() == null || bookDescription.getText().toString().trim().length() == 0) {
            bookDescription.setError("A book must have a description!");
            return;
        }
        if (bookPrice.getText() == null || bookPrice.getText().toString().trim().length() == 0) {
            bookPrice.setError("A book must have a price!");
            return;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getActivity().getApplicationContext(), "You must be logged in to do that!", Toast.LENGTH_SHORT).show();
            getActivity().startActivity(new Intent());
            getActivity().finish();
        }

        HomeFragment parent = (HomeFragment) getParentFragment();

        data.title = bookTitle.getText().toString();
        data.author = bookAuthor.getText().toString();
        data.study = parent.major;
        data.price = bookPrice.getText().toString();
        data.seller = (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + "-" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        data.id = UUID.randomUUID().toString().replaceAll("-", "");
        Location loc = MainActivity.userLocation;
        data.location = loc.getLatitude() + ":" + loc.getLongitude();
        data.timeStamp = Long.toString(Calendar.getInstance().getTimeInMillis());

        viewModel.addBook(data, this);
    }

    @Override
    public void notifyOfCallback(Object obj) {
        if ((boolean) obj) {
            Toast.makeText(getActivity().getApplicationContext(), "Successfully added a book!", Toast.LENGTH_SHORT).show();
            HomeFragment parent = (HomeFragment) getParentFragment();
            parent.getChildFragmentManager().popBackStackImmediate();
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Unsuccessful when attempting to add the book!", Toast.LENGTH_SHORT).show();
        }
    }
}
