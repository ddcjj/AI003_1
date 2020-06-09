package com.example.ai003_1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ai003_1.R;

public class HomeFragment extends Fragment {

    private String mParam1;
    private String mParam2;
    private Button btn_logo1;
    private EditText ed_question;
    private Button btn_send;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_question.setText("hihi");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ed_question = view.findViewById(R.id.ed_question);
        btn_send = view.findViewById(R.id.btn_send);
        btn_logo1 = view.findViewById(R.id.btn_logo1);
        // Inflate the layout for this fragment
        return view;
    }
}
