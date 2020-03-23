package com.example.bq.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.bq.MainActivity;
import com.example.bq.R;

public class HomeFragment extends Fragment {

    public static String major;
    Button butBCS;
    Button butBAM;
    Button butBAP;
    Button butBBE;
    Button butBDS;
    Button butBEE;


    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        initButtons(root);
        return root;
    }
    public void initButtons(View view){

        butBCS = view.findViewById(R.id.buttonBCS);
        butBAM = view.findViewById(R.id.buttonBAM);
        butBAP = view.findViewById(R.id.buttonBAP);
        butBBE = view.findViewById(R.id.buttonBBE);
        butBDS = view.findViewById(R.id.buttonBDS);
        butBEE = view.findViewById(R.id.buttonBEE);

        final MainActivity main  = (MainActivity) getActivity();
        butBCS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                main.onButtonShowPopupWindowClick(v);
                main.major = "BCS";

            }
        });
        butBAM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                main.onButtonShowPopupWindowClick(v);
                main.major = "BAM";
            }
        });
        butBAP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                main.onButtonShowPopupWindowClick(v);
                main.major = "BAP";
            }
        });
        butBBE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                main.onButtonShowPopupWindowClick(v);
                main.major = "BBE";
            }
        });
        butBDS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                main.onButtonShowPopupWindowClick(v);
                main.major = "BDS";
            }
        });
        butBEE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                main.onButtonShowPopupWindowClick(v);
                main.major = "BEE";
            }
        });

    }
}