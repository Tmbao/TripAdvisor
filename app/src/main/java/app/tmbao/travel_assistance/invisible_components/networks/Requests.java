package app.tmbao.travel_assistance.invisible_components.networks;

import android.os.StrictMode;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by tmbao on 8/24/2015.
 */
public class Requests {

    public static class FormData {
        public enum Type {
            TEXT,
            FILE
        }

        String key;
        Type contentType;
        String content;

        public FormData(String key, Type contentType, String content) {
            this.key = key;
            this.contentType = contentType;
            this.content = content;
        }

        public void writeData(DataOutputStream dataOutputStream) throws IOException {
            switch (contentType) {
                case FILE:
                    File file = new File(content);
                    if (file.exists() && file.isFile()) {
                        dataOutputStream.writeBytes(String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"%s", key, content, crlf));
                        dataOutputStream.writeBytes(crlf);

                        byte[] buffer = new byte[BUFFER_SIZE];
                        FileInputStream inputStream = new FileInputStream(file);
                        for (int readSize; (readSize = inputStream.read(buffer)) > 0; )
                            dataOutputStream.write(buffer, 0, readSize);
                    }
                    break;
                case TEXT:
                    dataOutputStream.writeBytes(String.format("Content-Disposition: form-data; name=\"%s\"%s", key, crlf));
                    dataOutputStream.writeBytes(crlf);
                    dataOutputStream.writeBytes(content);
                    break;
            }
        }
    }

    private static final int TIMEOUT_LIMIT = 10000;
    private static final int BUFFER_SIZE = 4096;
    private static String twoHyphens = "--";
    private static String boundary = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    private static String crlf = "\r\n";

    private static void grantNetworkPermission() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static HttpResponse get(String urlString, List<Pair<String, String>> parameters) throws IOException {
        grantNetworkPermission();

        if (parameters != null) {
            urlString += "?";
            for (int index = 0; index < parameters.size(); index++) {
                if (index > 0)
                    urlString += "&";
                urlString += String.format("%s=%s", parameters.get(index).first, URLEncoder.encode(parameters.get(index).second, "UTF-8"));
            }
        }

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

//        Send request and receive response
        int responseCode = connection.getResponseCode();
        String responseContent = "";
        if (responseCode == 200 || responseCode == 201) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            for (String line; (line = reader.readLine()) != null; )
                responseContent += line;
            reader.close();
        }
        connection.disconnect();
        return new HttpResponse(responseContent, responseCode);
    }

    public static void download(String urlString, String fileName) throws IOException {
        grantNetworkPermission();

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream inputStream = connection.getInputStream();
        OutputStream outputStream = new FileOutputStream(new File(fileName));

        byte[] buffer = new byte[BUFFER_SIZE];
        for (int readSize; (readSize = inputStream.read(buffer)) > 0; )
            outputStream.write(buffer, 0, readSize);

        inputStream.close();
        outputStream.close();
    }

    public static HttpResponse post(String urlString, List<FormData> parameters) throws IOException {
        grantNetworkPermission();

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");

//        Setup request data
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", String.format("multipart/form-data; boundary=%s", boundary));

        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());

        if (parameters != null)
            for (FormData parameter : parameters) {

                dataOutputStream.writeBytes(twoHyphens + boundary + crlf);
                parameter.writeData(dataOutputStream);
                dataOutputStream.writeBytes(crlf);
            }

        dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + crlf);

        dataOutputStream.flush();
        dataOutputStream.close();

//        Send request and receive response
        int responseCode = connection.getResponseCode();
        String responseContent = "";
        if (responseCode == 200 || responseCode == 201) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            for (String line; (line = reader.readLine()) != null; )
                responseContent += line;
            reader.close();
        }
        connection.disconnect();
        return new HttpResponse(responseContent, responseCode);
    }
}
