import org.json.JSONObject;

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

/**
 * JsonRPCRequest encapsulates method call in JsonRPC format
 */
public class JsonRPCRequest {
    private Object[] params; //Object array containing parameters
    private String method; //String with method name
    private String id; //ID of client

    public JsonRPCRequest(String method, Object[] params, String id){
        this.params = params;
        this.method = method;
        this.id = id;
    }

    public Object[] getParams(){
        return params;
    }

    public String getMethod(){
        return method;
    }

    public String getId(){
        return id;
    }

    /**
     * Converts JsonRPCRequest object into a JSONObject
     * @return JSONObject representation of this request
     */
    public JSONObject toJson(){
        JSONObject jObj = new JSONObject();
        jObj.put("method", method);
        jObj.put("params", params);
        jObj.put("id", id);
        return jObj;
    }
}
