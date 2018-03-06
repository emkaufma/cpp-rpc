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
public class JsonRPCResponse {
    Object result; //return value of method
    Object error; //error value (null if no error)
    String id; //id of Client

    public JsonRPCResponse(Object result, Object error, String id){
        this.result = result;
        this.error = error;
        this.id = id;
    }

    public Object getResult(){
        return result;
    }

    public Object getError(){
        return error;
    }

    public String getID(){
        return id;
    }

    /**
     * Converts this object into a JSONObject
     * @return JSONObject represntation of this object
     */
    public JSONObject toJson(){
        JSONObject jObj = new JSONObject();
        jObj.put("result", result);
        jObj.put("error", error);
        jObj.put("id", id);
        return jObj;
    }
}
