package edu.stanford.bmir.protege.web.shared.termbuilder.websearch.bing;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Yuhao Zhang
 */
public class BingSearchConnection {

    private String response;
    private int TIMEOUT = 3000;

    private String accountKey = "4aFYxs0H9LiXEy60u4OcODz9FU4bEpnS7DKSZMTVc5s";
    byte[] accountKeyBytes = Base64.encodeBase64((accountKey + ":" + accountKey).getBytes());
    String accountKeyEnc = new String(accountKeyBytes);

    public BingSearchConnection() {
        response = "";
    }

    public void sendGet(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
        con.setRequestMethod("GET");
        con.setConnectTimeout(TIMEOUT);

        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer sb = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        in.close();

        response = sb.toString();
    }

    public String getResponseString() {
        return response;
    }

}
