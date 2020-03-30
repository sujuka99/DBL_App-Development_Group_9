package com.example.bq.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bq.R;

public class HomeStudies extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_studies, container, false);
        initButtons(root);
        return root;
    }

    public void initButtons(View view) {

        Button butBCS = view.findViewById(R.id.buttonBCS);
        Button butBAM = view.findViewById(R.id.buttonBAM);
        Button butBAP = view.findViewById(R.id.buttonBAP);
        Button butBBE = view.findViewById(R.id.buttonBBE);
        Button butBDS = view.findViewById(R.id.buttonBDS);
        Button butBEE = view.findViewById(R.id.buttonBEE);

        final HomeFragment parent = (HomeFragment) getParentFragment();

        if (parent == null) {
            return;
        }

        butBCS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parent.onButtonShowPopupWindowClick(v);
                parent.major = "bcs";
            }
        });

        butBAM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parent.onButtonShowPopupWindowClick(v);
                parent.major = "bam";
            }
        });

        butBAP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parent.onButtonShowPopupWindowClick(v);
                parent.major = "bap";
            }
        });

        butBBE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parent.onButtonShowPopupWindowClick(v);
                parent.major = "bbe";
            }
        });
        butBDS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parent.onButtonShowPopupWindowClick(v);
                parent.major = "bds";
            }
        });

        butBEE.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parent.onButtonShowPopupWindowClick(v);
                parent.major = "bee";
            }
        });
    }
}
