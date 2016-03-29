package com.mustofin.multilanguage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Spinner spinner_language;
    TextView textview_judul;
    Button button;
    Button button_download_dictionary_en;
    Button button_download_dictionary_id;

    String string_id = "B. Indonesia";
    String string_en = "English US";

    String url_id = "https://raw.githubusercontent.com/mustofin/Support-Multilanguage-Android-with-Yaml/master/lang-id.yml";
    String url_en = "https://raw.githubusercontent.com/mustofin/Support-Multilanguage-Android-with-Yaml/master/lang-en.yml";

    String file_id = "lang-id.yml";
    String file_en = "lang-en.yml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner_language = (Spinner) findViewById(R.id.spinner_language);
        textview_judul = (TextView) findViewById(R.id.textview_judul);
        button = (Button) findViewById(R.id.button);
        button_download_dictionary_en = (Button) findViewById(R.id.button_download_dictionary_en);
        button_download_dictionary_id = (Button) findViewById(R.id.button_download_dictionary_id);

//        SET SPINNER LANGUAGE
        final String languages[] = {string_id, string_en};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, languages);
        spinner_language.setAdapter(adapter);

//        SET ACTION SPINNER
        spinner_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filename = file_id;
                if(languages[position].equals(string_en)){
                    filename = file_en;
                }

                File fileDictionary = new File(MainActivity.this.getFilesDir(), filename);
                if (fileDictionary.exists()) {
                    try {
                        YamlReader reader = new YamlReader(new FileReader(fileDictionary));
                        Map data = (Map) reader.read();
                        textview_judul.setText(data.get("title").toString());
                        button.setText(data.get("button").toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (YamlException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Tidak Ditemukan File Dictionary "+filename,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_download_dictionary_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String status = new GetDictionaryAsyncTask(MainActivity.this).execute(url_id, file_id).get();
                    if (status.equals("finish")) {
                        Toast.makeText(MainActivity.this, "Download Indonesia Dictionary Complete.", Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        button_download_dictionary_en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String status = new GetDictionaryAsyncTask(MainActivity.this).execute(url_en, file_en).get();
                    if(status.equals("finish")){
                        Toast.makeText(MainActivity.this, "Download English Dictionary Complete.", Toast.LENGTH_SHORT).show();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
