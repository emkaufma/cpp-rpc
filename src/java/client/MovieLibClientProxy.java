/**
 * Copyright [2016] [Eric Kaufman]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 * Created by Eric on 8/29/2016.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * Class designed to handle method calling between Client GUI and Server.  Methods correspond with methods in the
 * MovieLibrary class.
 */

public class MovieLibClientProxy {

    String host;
    int portNo;
    String clientID;

    public MovieLibClientProxy(String host, int portNo, String clientID){
        this.host = host;
        this.portNo = portNo;
        this.clientID = clientID;
    }

    public void toJsonFile(String jsonFileName) {
        Object[] params = new Object[1];
        params[0] = jsonFileName;
        JsonRPCRequest rqst = new JsonRPCRequest("toJsonFile", params, clientID);
        sendRequest(rqst);
    }

    public boolean add(MovieDescription aClip) {
        Object[] params = new Object[1];
        params[0] = aClip.toJson();
        JsonRPCRequest rqst = new JsonRPCRequest("add", params, clientID);
        JSONObject response = sendRequest(rqst);
        return response.getBoolean("result");

    }

    public boolean remove(String aTitle) {
        Object[] params = new Object[1];
        params[0] = aTitle;
        JsonRPCRequest rqst = new JsonRPCRequest("remove", params, clientID);
        JSONObject response = sendRequest(rqst);
        return response.getBoolean("result");
    }

    public MovieDescription get(String aTitle) {
        Object[] params = new Object[1];
        params[0] = aTitle;
        JsonRPCRequest rqst = new JsonRPCRequest("get", params, clientID);
        JSONObject response = sendRequest(rqst);
        return new MovieDescription(response.getJSONObject("result"));
    }

    public String[] getTitles() {
        JsonRPCRequest rqst = new JsonRPCRequest("getTitles", null, clientID);
        JSONObject response = sendRequest(rqst);
        JSONArray jArr = response.getJSONArray("result");
        return jArr.toList().toArray(new String[jArr.toList().size()]);
    }

    private JSONObject sendRequest(JsonRPCRequest rqst){
        System.out.println("Sending request to server: " + rqst.toJson().toString());
        byte bytesToSend[] = rqst.toJson().toString().getBytes();
        int numBytesReceived;
        String strReceived;
        byte bytesReceived[] = new byte[1024];
        int bufLen = 1024;
        try {
            Socket sock = new Socket(host, portNo);
            OutputStream os = sock.getOutputStream();
            InputStream is  = sock.getInputStream();
            os.write(bytesToSend,0,bytesToSend.length);
            numBytesReceived = is.read(bytesReceived, 0, bufLen);
            strReceived = new String(bytesReceived, 0, numBytesReceived);
            System.out.println("Received from server: " + strReceived);
          //  System.out.print("String to send>");
            JSONObject jResponse = new JSONObject(strReceived);
            sock.close();
            return jResponse;
        } catch (Exception e){e.printStackTrace(); return null;}
    }
}