package com.slotmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    ArrayList<String>symbols;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        symbols = new ArrayList<String>();
        AsyncTask.execute(new Runnable(){
            public void run(){
                try {
                    getResults("https://api.iextrading.com/1.0/ref-data/symbols");
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        });
        setContentView(R.layout.activity_main);
    }

    protected void getResults(String url) throws Exception{
        URL iexEndpoint = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection)iexEndpoint.openConnection();

        connection.setRequestProperty("User-Agent", "stockroller-v0.1");

        if(connection.getResponseCode() == 200){
            InputStream responseBody = connection.getInputStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.beginArray();
            while(jsonReader.hasNext()){
                symbols.add(readSymbol(jsonReader));
            }
            jsonReader.endArray();
            connection.disconnect();
        } else{
            System.out.println("Error");
        }

    }

    public String printRand(){
        Random rand = new Random();

        int n = rand.nextInt(symbols.size());
        return symbols.get(n);
    }

    public void changeText(View view){
        EditText symbolText = (EditText) findViewById(R.id.symbolText);
        symbolText.setText(printRand());
    }

    private String readSymbol(JsonReader jsonreader) throws IOException {
        jsonreader.beginObject();
        String symbol = null;
        while(jsonreader.hasNext()){
            String name = jsonreader.nextName();
            if (name.equals("symbol")){
                System.out.print(symbol);
                symbol = jsonreader.nextString();
            }else{
                jsonreader.skipValue();
            }
        }
        jsonreader.endObject();
        return symbol;
    }
}

