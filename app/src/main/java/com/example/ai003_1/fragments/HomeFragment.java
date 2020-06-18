package com.example.ai003_1.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ai003_1.CDictionary;
import com.example.ai003_1.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Button btn_logo_shopee;
    private EditText ed_question;
    private Button btn_send;
    private RecyclerView rv_home;
    private List<String> question_list;
    private String answer;
    private String question;
    private TextView text_service;
    private StringBuilder sb_message = new StringBuilder();
    private Button btn_logo_ruten;
    private Button btn_logo_yahoo;
    private Button btn_logo_store;
    private Button btn_mic;

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
                question = ed_question.getText().toString();
//                question_list.add(question);
                sb_message.append("我:" + question + "\n");

                RetrieveFeedTask task = new RetrieveFeedTask();
                task.execute();
                sb_message.append("機器人:" + answer + "\n");

                text_service.setText(sb_message.toString());
//                question_list.add("1");
//                Log.d("xiang",answer);

                ed_question.setText("");
//                rv_home.setHasFixedSize(true);
//                rv_home.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//                HomeFragment.TextViewAdapter adapter = new HomeFragment.TextViewAdapter();
//                rv_home.setAdapter(adapter);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        text_service = view.findViewById(R.id.text_service);
        ed_question = view.findViewById(R.id.ed_question);
        btn_send = view.findViewById(R.id.btn_send);
        btn_mic = view.findViewById(R.id.btn_mic);
        btn_mic.setOnClickListener(new myRecognizerIntentListener());
        btn_logo_shopee = view.findViewById(R.id.btn_logo_shopee);
        btn_logo_shopee.setHeight(btn_logo_shopee.getWidth());
        btn_logo_shopee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://shopee.tw/");
                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });
        btn_logo_ruten = view.findViewById(R.id.btn_logo_ruten);
        btn_logo_ruten.setHeight(btn_logo_ruten.getWidth());
        btn_logo_ruten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://www.ruten.com.tw/");
                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });
        btn_logo_yahoo = view.findViewById(R.id.btn_logo_yahoo);
        btn_logo_yahoo.setHeight(btn_logo_yahoo.getWidth());
        btn_logo_yahoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://tw.bid.yahoo.com/");
                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });
        btn_logo_store = view.findViewById(R.id.btn_logo_store);
        btn_logo_store.setHeight(btn_logo_store.getWidth());
        btn_logo_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri=Uri.parse("https://seller.pcstore.com.tw/");
                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });
//        rv_home = view.findViewById(R.id.rv_home);
        // Inflate the layout for this fragment
        return view;
    }


//    public class TextViewAdapter extends RecyclerView.Adapter<TextViewAdapter.TextViewHolder>{
//
//        @NonNull
//        @Override
//        public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = getLayoutInflater().inflate(R.layout.recycler_home_question, parent, false);
//            return new TextViewAdapter.TextViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
//            String[] questions = question_list.toArray(new String[question_list.size()]);
//            holder.textView.setText(questions[position]);
//        }
//
//        @Override
//        public int getItemCount() {
//            return question_list.size();
//        }
//
//        class TextViewHolder extends RecyclerView.ViewHolder {
//            TextView textView;
//        public TextViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textView = itemView.findViewById(R.id.text_question);
//        }
//    }
//    }

//聊天機器人
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
            jsonParam.put("question", question);
            Log.d("xiang", "json is " + question);
//            jsonParam.put("question", "哈囉");

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
//Log.d("xiang", "sb is " + sb.toString());
            JSONObject jsonObj = new JSONObject(sb.toString());
//            Log.d("xiang", "answers is " + jsonObj.getJSONArray("answers"));
//            Log.d("xiang", "0 is " + jsonObj.getJSONArray("answers").getJSONObject(0));
//            Log.d("xiang", "answer is " + jsonObj.getJSONArray("answers").getJSONObject(0).getString("answer"));
            answer = jsonObj.getJSONArray("answers").getJSONObject(0).getString("answer");
            Log.d("xiang","answer" + answer);

        } catch (Exception ex) {

            Log.d("xiang", "exception at last " + ex);

        } finally {

            try {

                reader.close();

            } catch (Exception ex) {

            }
        }
    }
//聊天機器人方法
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

    //語音
    public class myRecognizerIntentListener implements View.OnClickListener {
        public void onClick(View v) {
            try {
// 用Intent來傳遞語音識別的模式,並且開啟語音模式
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
// 語言模式和自由形式的語音識別
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// 提示語言開始
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請開始語音");
// 開始語音識別
                startActivityForResult(intent, CDictionary.VOICE_RECOGNITION_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getActivity(), "找不到語音裝置",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // 語音結束時的回撥函式
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CDictionary.VOICE_RECOGNITION_REQUEST_CODE
                && resultCode == CDictionary.RESULT_OK) {
// 取得語音的字元
            ArrayList<String> results = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
// 設定檢視的更新
//            mList.setAdapter(new ArrayAdapter<String>(this,
//                    android.R.layout.simple_list_item_1, results));
            String resultsString = "";
            for (int i = 0; i < results.size(); i++  ) {
                resultsString  = results.get(i);
            }
            ed_question.setText(resultsString);
//            Toast.makeText(this, resultsString, Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
