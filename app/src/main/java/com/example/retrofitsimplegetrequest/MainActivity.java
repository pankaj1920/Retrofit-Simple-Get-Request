package com.example.retrofitsimplegetrequest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.retrofitsimplegetrequest.Jason.JsonInterface;
import com.example.retrofitsimplegetrequest.Jason.PostJson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView text_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_view = findViewById(R.id.text_view);

        //Create Instance of Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/") //or Interface Contain only Realtive URL so her we Provide base Url
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Withi this retrofit instance we can now Create our Json Post Api
        JsonInterface json_interface = retrofit.create(JsonInterface.class);

        //To exectue our Network Request We have to use the Call Object
        Call<List<PostJson>> call = json_interface.getPost();

        // now we Simply have to execute Call and get our Response back
        call.enqueue(new Callback<List<PostJson>>() {
            @Override
            public void onResponse(Call<List<PostJson>> call, Response<List<PostJson>> response) {
            // This method will be Trigger when we get back response from server but it doesnot meanour request is Sucessful
            //  bcz Server can response whith 404 code which mean the data we are looking for is not heir

                if (!response.isSuccessful()){
                    //if there is error the this code is executed
                    text_view.setText("Code : "+ response.code());
                }else {
                    // This is to get data from web service
                   List<PostJson> post =  response.body();

                   //now we want to display in our textView
                    for ( PostJson i :post){
                        // we want to append data to our text_view so create a string
                        String content = "";
                        content += "ID : "+ i.getId()+"\n";
                        content += " User Id : "+i.getUserId()+"\n";
                        content += "Title : "+i.getTitle()+"\n";
                        content += "Text : "+i.getBody();

                        text_view.append(content);

                        //Add InternetPermission in Mainifest file

                    }
                }
            }

            @Override
            public void onFailure(Call<List<PostJson>> call, Throwable t) {
                // this method onFaliure means when something went wrong with comminucation between Server
                text_view.setText(t.getMessage());

            }
        });
    }
}
