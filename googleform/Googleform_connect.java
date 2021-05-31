package com.example.naveen.googleform_connect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Googleform_connect extends AppCompatActivity {




    public static final MediaType FORM_DATA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL="https://docs.google.com/forms/d/e/1FAIpQLSf31pB236ptTguEDu8Ad2uAQE-I5lnA5SWHt7T1nEAt0-QjJw/formResponse";


    public static final String Name_key="entry_199778053";
    public static final String Mobile_key="entry_315036056";
    public static final String Query_key="entry_1786765138";

    private Context context;
    private EditText name;
    private EditText mobilenumber;
    private EditText query;
   private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googleform_connect);
        context =this;
        Button sendButton = findViewById(R.id.submit_query);
        name = findViewById(R.id.name);
        mobilenumber = findViewById(R.id.mobile);
        query = findViewById(R.id.edit_query);



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if(TextUtils.isEmpty(name.getText().toString()) ||
                        TextUtils.isEmpty(mobilenumber.getText().toString()) ||
                        TextUtils.isEmpty(query.getText().toString()))
                {
                    Toast.makeText(context,"All fields are mandatory.",Toast.LENGTH_LONG).show();
                    return;
                }
else       {

                    dialog=new ProgressDialog(context);
                    dialog.setMessage("Sending Query ......");
                    dialog.show();
                }




                PostDataTask postDataTask = new PostDataTask();


                postDataTask.execute(URL,name.getText().toString(),
                        mobilenumber.getText().toString(),
                        query.getText().toString());
            }
        });
    }

    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String url = contactData[0];
            String name= contactData[1];
            String mobilenumber = contactData[2];
            String query = contactData[3];
            String postBody="";

            try {

                postBody = Name_key+"=" + URLEncoder.encode(name,"UTF-8") +
                        "&" + Mobile_key + "=" + URLEncoder.encode(mobilenumber,"UTF-8") +
                        "&" + Query_key + "=" + URLEncoder.encode(query,"UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result=false;
            }



            try{

                OkHttpClient client = new OkHttpClient();

                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
            }catch (IOException exception){
                result=false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            dialog.hide();

            Toast.makeText(context,result?"Your Query is Received Thanks":"There was some error in sending message. Please try again after some time.",Toast.LENGTH_LONG).show();
        }

    }
}
