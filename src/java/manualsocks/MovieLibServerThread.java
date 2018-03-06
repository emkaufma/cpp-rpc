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


public class MovieLibServerThread extends Thread {
    private Socket conn;
    private int id;
    private MovieLibrary lib;

    public MovieLibServerThread(Socket sock, MovieLibrary lib) {
        this.lib = lib;
        this.conn = sock;
        this.id = id;
    }


    public void run() {
        try {
            OutputStream outSock = conn.getOutputStream();
            InputStream inSock = conn.getInputStream();
            byte clientInput[] = new byte[1024]; // up to 1024 bytes in a message.
            int numr = inSock.read(clientInput, 0, 1024);

            String clientString = new String(clientInput, 0, numr);
            System.out.println("read from client: " + id + " the string: "
                    + clientString);
            int index = clientString.indexOf("{");
            
            JSONObject jObj = new JSONObject(clientString.substring(index));

            System.out.println("Calling method " + jObj.getString("method"));
            JsonRPCResponse response = createResponse(clientString);
            outSock.write(response.toJson().toString().getBytes());

            inSock.close();
            outSock.close();
            conn.close();
        } catch (IOException e) {
            System.out.println("Can't get I/O for the connection.");
        }
    }

    private JsonRPCResponse createResponse(String request){
        int index = request.indexOf("{");
        System.out.println(request.substring(index));
        JSONObject jObj = new JSONObject(request.substring(index));
        String methodName = jObj.getString("method");
        //System.out.println(methodName);
        if(methodName.equals("get")){return this.get(jObj);}
        else if(methodName.equals("add")){return this.add(jObj);}
        else if(methodName.equals("remove")){return this.remove(jObj);}
        else if(methodName.equals("getTitles")){return this.getTitles(jObj);}
        else if(methodName.equals("toJsonFile")){return this.toJsonFile(jObj);}
        else {return null;}
    }


    public JsonRPCResponse remove(JSONObject jObj) {
        JSONArray jArray = jObj.getJSONArray("params");
        return new JsonRPCResponse(lib.remove(jArray.getString(0)), null, jObj.getString("id"));
    }

    public JsonRPCResponse add(JSONObject jObj) {
        JSONArray jArray = jObj.getJSONArray("params");
        return new JsonRPCResponse(lib.add(new MovieDescription(jArray.getJSONObject(0))), null, jObj.getString("id"));
    }

    public JsonRPCResponse get(JSONObject jObj) {
        JSONArray jArray = jObj.getJSONArray("params");
        System.out.println(jArray.getString(0));
        System.out.println(lib.get(jArray.getString(0)));
        JsonRPCResponse jResponse = new JsonRPCResponse(lib.get(jArray.getString(0)).toJson(), null, jObj.getString("id"));
        System.out.println("Sending Response: " + jResponse.toJson().toString());
        return jResponse;
    }

    public JsonRPCResponse getTitles(JSONObject jObj) {
        JsonRPCResponse jResponse = new JsonRPCResponse(new JSONArray(lib.getTitles()), null, jObj.getString("id"));
        System.out.println("Sending Response: " + jResponse.toJson().toString());
        return jResponse;
    }

    public JsonRPCResponse toJsonFile(JSONObject jObj) {
        JSONArray jArray = jObj.getJSONArray("params");
        JsonRPCResponse jResponse = new JsonRPCResponse(null, null, jObj.getString("id"));
        System.out.println("Sending Response: " + jResponse.toJson().toString());
        return jResponse;
    }

}

