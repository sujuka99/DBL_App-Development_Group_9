package com.example.bq.booktest;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bq.MainActivity;
import com.example.bq.R;
import com.example.bq.datamanager.ViewModelCaller;
import com.example.bq.datamanager.datatypes.BookData;
import com.example.bq.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddBookFragment extends Fragment {

    private final int TAKE_IMAGE = 0;
    private final int PICK_IMAGE = 1;

    private ImageView bookImage;
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookDescription;
    private EditText bookPrice;

    private BookViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_addbook, container, false);
        viewModel = new ViewModelProvider(this).get(BookViewModel.class);

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
        final CharSequence[] options = {getString(R.string.SelectImageDialogOpt1),
                getString(R.string.SelectImageDialogOpt2),
                getString(R.string.SelectImageDialogCancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.SelectImageDialogTitle);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case TAKE_IMAGE:
                        Intent takePicture =
                                new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, TAKE_IMAGE);
                        break;
                    case PICK_IMAGE:
                        if (ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) !=
                                PackageManager.PERMISSION_GRANTED) {
                            String[] perms = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            ActivityCompat.requestPermissions(getActivity(), perms, PICK_IMAGE);
                        }
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                    default:
                        break;
                }
            }
        }
    }

    private void registerBook() {
        BookData data = new BookData();

        if (TextUtils.isEmpty(bookTitle.getText().toString())) {
            bookTitle.setError(getString(R.string.ErrorBookTitle));
            return;
        }
        if (TextUtils.isEmpty(bookAuthor.getText().toString())) {
            bookAuthor.setError(getString(R.string.ErrorBookAuthor));
            return;
        }
        if (TextUtils.isEmpty(bookDescription.getText().toString())) {
            bookDescription.setError(getString(R.string.ErrorBookDescription));
            return;
        }
        if (TextUtils.isEmpty(bookPrice.getText().toString())) {
            bookPrice.setError(getString(R.string.ErrorBookPrice));
            return;
        }

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.ErrorNotLoggedIn,
                    Toast.LENGTH_SHORT).show();
            getActivity().startActivity(new Intent());
            getActivity().finish();
        }

        HomeFragment parent = (HomeFragment) getParentFragment();
        assert parent != null;

        data.title = bookTitle.getText().toString();
        data.author = bookAuthor.getText().toString();
        data.study = parent.major;
        data.price = bookPrice.getText().toString();
        data.seller = (FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                + "-"
                + FirebaseAuth.getInstance().getCurrentUser().getUid());
        data.id = UUID.randomUUID().toString().replaceAll("-", "");
        Location loc = MainActivity.userLocation;
        data.location = loc.getLatitude() + ":" + loc.getLongitude();
        data.timeStamp = Long.toString(Calendar.getInstance().getTimeInMillis());
        data.description = bookDescription.getText().toString();

        viewModel.addBook(data, new ViewModelCaller() {
            @Override
            public void callback(Object obj) {
                assert (getActivity() != null);
                Context appContext = getActivity().getApplicationContext();
                assert (appContext != null);

                if (obj instanceof String) {
                    Toast.makeText(appContext, (String) obj, Toast.LENGTH_SHORT).show();
                } else if ((boolean) obj) {
                    Toast.makeText(appContext, R.string.MessageSuccessAddBook, Toast.LENGTH_SHORT).show();
                    HomeFragment parent = (HomeFragment) getParentFragment();
                    assert (parent != null);
                    parent.getChildFragmentManager().popBackStackImmediate();
                }
            }
        });
    }
}
