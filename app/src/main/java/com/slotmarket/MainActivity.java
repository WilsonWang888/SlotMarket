package com.slotmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public RequestQueue queue;
    List<StockSymbol> symbols;
    String filter;
    StockInfo currentStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(getApplicationContext());
        currentStock = new StockInfo();
        String url = "https://cloud.iexapis.com/stable/ref-data/symbols?token=pk_5e6f01a62c7e43c2baac7d44cd2d5aa8";
        final TextView textView = findViewById(R.id.load_symbols_message);
        filter = "";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        symbols = new ArrayList<>();

                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject stock = response.getJSONObject(i);
                                if(stock.getBoolean("isEnabled")) {
                                    symbols.add(new StockSymbol(stock.getString("symbol"),
                                            stock.getString("exchange")));
                                }
                            }
                            textView.setText("Loaded Successfully");
                        } catch (JSONException e) {
                            textView.setText("Error Loading Symbols");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("Failed");

                    }
                });
        queue.add(jsonArrayRequest);
    }

    private int pickRandom(List<StockSymbol> list){
        Random rand = new Random();
        return rand.nextInt(list.size());
    }

    public void generateStock(View view){
        if(filter.equals("")){
            currentStock.setSymbol(symbols.get(pickRandom(symbols)).getSymbol());
        } else{
            List<StockSymbol> enabledSymbols = new ArrayList<>();
            for(int i = 0; i < symbols.size(); i++){
                if(symbols.get(i).isEnabled()){
                    enabledSymbols.add(symbols.get(i));
                }
            }
            currentStock.setSymbol(enabledSymbols.get(pickRandom(enabledSymbols)).getSymbol());
        }
        currentStock.updateInfo(queue);
        updateView();
    }

    public void updateView(){
        TextView symbolView = findViewById(R.id.stock_symbol);
        symbolView.setText(currentStock.getStock());
        TextView stockView = findViewById(R.id.stock_name);
        stockView.setText(currentStock.getStock());
        TextView priceView = findViewById(R.id.stock_price);
        String currPrice = currentStock.getValue() + "";
        priceView.setText(currPrice);
    }
}



