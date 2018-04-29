package co.edu.uniandes.sdl.profiling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.newrelic.agent.android.NewRelic;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    private List<Plato> platos;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button butMemoryOverFlow = (Button) findViewById(R.id.butMemoryOverFlow);
        butMemoryOverFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Memory over flow", Toast.LENGTH_SHORT).show();
                fillMemory();
            }
        });
        Button butImageMemoryOverFlow = (Button) findViewById(R.id.butImageMemoryOverFlow);
        butImageMemoryOverFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadimage();
            }
        });
        Button httpRequestError = (Button) findViewById(R.id.httpRequestError);
        httpRequestError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpRequest();
            }
        });
        Button createInfiniteList = (Button) findViewById(R.id.createInfiniteList);
        createInfiniteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bigList();
            }
        });
        listView=(ListView)findViewById(R.id.platos_listview);
        //platos = new ArrayList<Plato>();

        NewRelic.withApplicationToken(

                "<generate app token by creating a name above>"
        ).start(this.getApplication());

    }


    public void fillMemory() {
        for(int i = 0; i <1000000;i++) {
            ArrayList a = new ArrayList();
        }
        fillMemory();
    }

    public void loadimage(){
        ImageView i = new ImageView(this);
        i.setImageResource(R.drawable.landscape);
        //i.setAdjustViewBounds(true);
        RelativeLayout r = new RelativeLayout(this);
        r.addView(i);
        setContentView(r);
    }

    private void httpRequest() {
        RestClient.get("http://demo7931028.mockable.io/platos", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                try {
                    Gson gson = new GsonBuilder().create();
                    for (int i = 0; i< array.length();i++) {
                        Plato plato = gson.fromJson(array.get(i).toString(), Plato.class);
                        platos.add(plato);
                    }
                    PlatoAdapter itemsAdapter = new PlatoAdapter(MainActivity.this, platos);
                    listView.setAdapter(itemsAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable error){
                Toast.makeText(MainActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    private void bigList() {
        RestClient.get("http://demo3573381.mockable.io/ruta", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray array) {
                try {
                    for (int i = 0; i< array.length();i++) {
                        Gson gson = new GsonBuilder().create();
                        Plato plato = gson.fromJson(array.get(i).toString(), Plato.class);
                        platos.add(plato);
                    }
                    PlatoAdapter itemsAdapter = new PlatoAdapter(MainActivity.this, platos);
                    listView.setAdapter(itemsAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable error){
                Toast.makeText(MainActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
