package com.mustofin.multilanguage;

import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tofin on 29/03/16.
 */
public class GetDictionaryAsyncTask extends AsyncTask<String, String, String > {

    Context context;

    public GetDictionaryAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String status = "";
        try {
            URL url = new URL(params[0]);
            String filename = params[1];

            URLConnection connection = url.openConnection();
            connection.connect();

            InputStream inputStream = new BufferedInputStream(url.openStream());

            File file = new File(context.getFilesDir(), filename);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte data[] = new byte[1024];
            int count = 0;

            while((count = inputStream.read(data)) != -1){
                outputStream.write(data, 0, count);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            status = "finish";

        } catch (Exception e){
            e.printStackTrace();
        }
        return status;
    }
}
