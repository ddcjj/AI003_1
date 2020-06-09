package com.example.ai003_1.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai003_1.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Button btn_logo1;
    private EditText ed_question;
    private Button btn_send;
    private RecyclerView rv_home;
    private List<String> question_list;
    private String answer;

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



        question_list = new ArrayList<>();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = ed_question.getText().toString();
                question_list.add(question);

                RetrieveFeedTask task = new RetrieveFeedTask();
                task.execute();
                question_list.add(answer);

                ed_question.setText("");
                rv_home.setHasFixedSize(true);
                rv_home.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                HomeFragment.TextViewAdapter adapter = new HomeFragment.TextViewAdapter();
                rv_home.setAdapter(adapter);
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
        rv_home = view.findViewById(R.id.rv_home);
        // Inflate the layout for this fragment
        return view;
    }


    public class TextViewAdapter extends RecyclerView.Adapter<TextViewAdapter.TextViewHolder>{

        @NonNull
        @Override
        public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.recycler_home_question, parent, false);
            return new TextViewAdapter.TextViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
            String[] questions = question_list.toArray(new String[question_list.size()]);
            holder.textView.setText(questions[position]);
        }

        @Override
        public int getItemCount() {
            return question_list.size();
        }

        class TextViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_question);
        }
    }
    }


    public void GetText() throws UnsupportedEncodingException {


        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL("https://xxtestbot.azurewebsites.net/qnamaker/knowledgebases/d3cf57b4-8022-460d-bfb7-32e6498d5806/generateAnswer");

            // Send POST data request
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //conn.setRequestProperty("Ocp-Apim-Subscription-Key", "");
            conn.setRequestProperty("Authorization", "fae85c04-66dc-457b-be5b-880d2cddc870");
            conn.setRequestProperty("Content-Type", "application/json");

            //Create JSONObject here
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("question", ed_question.getText().toString());
            //jsonParam.put("question", "哈囉");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            //wr.write(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            wr.write(jsonParam.toString());
            wr.flush();
            Log.d("xiang", "json is " + jsonParam);

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {

                // Append server response in string
                sb.append(line + "\n");

            }

            answer += sb.toString()+"1";
//            txtView.setText(text+"\n");
            Log.d("xiang", "response is " + answer);

        } catch (Exception ex) {

            Log.d("xiang", "exception at last " + ex);

        } finally {

            try {

                reader.close();

            } catch (Exception ex) {

            }
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                Log.d("xiang", "called");
                GetText();
                Log.d("xiang", "after called");

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
                Log.d("xiang", "Exception occurred " + e);

            }

            return null;
        }

    }
}
