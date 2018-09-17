
package com.sensor.test.linker.common;

import com.google.gson.JsonObject;
import com.sensor.test.util.DataAccess;
import com.sensor.test.util.DataInterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class IOManager {


    private DataInterface da;
    private Map<String, Object> responseMap;
    private JsonObject requestObject;
    private HttpServletRequest request;
    private HttpServletResponse response;


    public IOManager(HttpServletResponse response) throws IOException {
        this.da = new DataAccess();
        this.responseMap = new HashMap<String, Object>();
        this.response = response;
    }

    public IOManager(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.da = new DataAccess();
        this.responseMap = new HashMap<String, Object>();
        this.request = request;
        this.response = response;
        this.requestObject = ServerUtils.getJsonObjectFromRequest(request);
    }


    public DataInterface getDataAccess() {
        return this.da;
    }

    public Map<String, Object> getResponseMap() {
        return this.responseMap;
    }

    public void setResponseMessage(ErrorModel em) {
        responseMap.put("code", em.getCode());
        responseMap.put("message", em.getMessage());
    }

    public boolean addResponseParameter(String key, Object value) {
        if (key != null && value != null) {
            responseMap.put(key, value);
            return true;
        } else {
            return false;
        }
    }

    public JsonObject getJsonRequest() {
        return this.requestObject;
    }


    /**
     * Remove Key and value
     */
    public void removeResponseParameter(String key, Object value) {
        responseMap.remove(key, value);
    }


    public void SendJsonResponse() throws IOException {
//        System.out.println("Closing connection");
        if (da != null) {
            try {
                da.closeConnection();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        ServerUtils.respondJsonMap(response, this.responseMap);
    }

    public void closeConnection() {
        if (da != null) {
            try {
                da.closeConnection();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
