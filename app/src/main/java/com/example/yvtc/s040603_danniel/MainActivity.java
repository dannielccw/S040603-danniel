package com.example.yvtc.s040603_danniel;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    InputStream inputStream;
    MyDataHandler dataHandler;
    ListView lv;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listView);
        dataHandler = new MyDataHandler();


        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL("https://udn.com/rssfeed/news/1");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    inputStream = conn.getInputStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    final StringBuilder sb = new StringBuilder();
                    String str;
                    while ((str = br.readLine()) != null)
                    {
                        sb.append(str);
                    }
                    Log.d("MYNETLog", sb.toString());

                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    xr.setContentHandler(dataHandler);
                    xr.parse(new InputSource(new StringReader(sb.toString())));


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tv.setText(sb.toString());
                            adapter = new ArrayAdapter<String>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, dataHandler.titles);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent it = new Intent(MainActivity.this, DetailActivity.class);
                                    it.putExtra("link", dataHandler.links.get(position));
                                    startActivity(it);
                                }
                            });
                        }
                    });



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }
}
