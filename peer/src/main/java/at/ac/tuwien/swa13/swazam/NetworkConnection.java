/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package at.ac.tuwien.swa13.swazam;

import at.ac.tuwien.swa13.swazam.library.ISong;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author mmuehlberger
 */
public class NetworkConnection {
    private final String server;
    
    public NetworkConnection(String server)
    {
        this.server = server;
    }
    
    public void makeRequest(String taskId, String username, ISong song)
    {
        System.out.println("Telling server about " + taskId + ": Found " + song.toString());
        
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://" + server + ":8080/swazam-server/result");
            postRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
            postRequest.addHeader("Cache-Control", "no-cache");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("id", taskId));
            nvps.add(new BasicNameValuePair("user", username));
            nvps.add(new BasicNameValuePair("result", buildMetadataBody(song)));
            UrlEncodedFormEntity songEntity = new UrlEncodedFormEntity(nvps);
            postRequest.setEntity(songEntity);
            HttpResponse response = client.execute(postRequest);
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                Logger.getLogger(NetworkConnection.class.getName()).log(Level.INFO, line);
            }
        } catch (IOException ex) {
            Logger.getLogger(NetworkConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static String buildMetadataBody(ISong song)
    {   
        return "{ \"artist\":\"" + song.getArtist() + "\", " +
                "\"album\": \"" + song.getAlbum() + "\", " + 
                "\"title\": \"" + song.getTitle() + "\" }";
    }
}
