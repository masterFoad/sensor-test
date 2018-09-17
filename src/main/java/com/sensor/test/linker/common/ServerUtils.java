package com.sensor.test.linker.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sensor.test.NeuralNetwork.src.model.NeuralNetwork;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;


public class ServerUtils {


    private ServerUtils() {
    }


    /**
     * -- Session related methods:
     */

    public static String generateToken() {
        int length = 128;
        String candidateChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(candidateChars.charAt(random.nextInt(candidateChars
                    .length())));
        }

        return sb.toString();
    }


//	/**Android related methods*/
//
//	/**
//	 * getJsonFromRequest
//	 * @param request
//	 * @param toJsonClass
//	 * @return
//	 * @throws IOException
//	 */
//	public static <T> T getJsonFromRequest(HttpServletRequest request,Class<T> toJsonClass) throws IOException {
//
//	    Gson gson = new Gson();
//	    return gson.fromJson(requestToJsonString(request),toJsonClass);
//	}


    /**Android related methods*/

    /**
     * get json object from request stream
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static JsonObject getJsonObjectFromRequest(HttpServletRequest request) throws IOException {

        // System.out.println(requestToJsonString(request));
        // return   Json.parse(requestToJsonString(request));
        Gson gson = new Gson();
        JsonObject jsonObj = gson.fromJson(requestToJsonString(request), JsonObject.class);
//        System.out.println(jsonObj);
        return jsonObj;


    }


    /**
     * parse request stream to json string
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String requestToJsonString(HttpServletRequest request) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            sb.append(s).append("\n");
        }
        br.close();
        //  System.out.println(sb.toString());
        return sb.toString();
    }


    /**
     * returns json response from map parameters
     *
     * @param response //	 * @param jsonUser
     * @throws IOException
     */
    public static void respondJsonMap(HttpServletResponse response, Map<String, Object> jsonParam) throws IOException {


        Gson gson = new Gson();


        response.setContentType("application/json");
        // Get the printwriter object from response to write the required json object to the output stream
        PrintWriter out = response.getWriter();
        // Assuming your json object is **jsonObject**, perform the following, it will return your json object
        out.print(gson.toJson(mapToJson(jsonParam)));
        //System.out.println(gson.toJson(jsonUser).toString());
        out.flush();
        out.close();

    }


    /**
     * returns jsonUser as a response -- jsonUser contains status
     *
     * @param response //	 * @param jsonUser
     * @throws IOException
     */
    public static <T> void respondJsonObject(HttpServletResponse response, T jsonObjectClass) throws IOException {


        Gson gson = new Gson();

        response.setContentType("application/json");
        // Get the printwriter object from response to write the required json object to the output stream
        PrintWriter out = response.getWriter();
        // Assuming your json object is **jsonObject**, perform the following, it will return your json object
        out.print(gson.toJson(jsonObjectClass));
        //System.out.println(gson.toJson(jsonUser).toString());
        out.flush();
        out.close();

    }

    /**
     * check if the given token is valid for the given user
     * @param userId
     * @param token
     * @param da
     * @return
     * @throws SQLException
     */
//	public static boolean validateUserSession(int userId,String token,DataInterface da) throws SQLException {
//
//		//ArrayList<Session> userSessions=null;
//
//		//get method from DA
//		ArrayList<Session> userSessions = da.getUserSessions(userId);
//
//		for(int i=0;i<userSessions.size();i++) {
//			Session s=userSessions.get(i);
//
//			if(s.getToken().equals(token) && s.getExpirationDate()>new Date().getTime()) {
//				return true;
//			}
//		}
//
//		return false;//must be false
//
//
//
//	}

    /**
     * return map as jsonObject
     *
     * @param inputMap
     * @return
     */
    public static JsonObject mapToJson(Map<String, Object> inputMap) {

        return new JsonParser().parse(new Gson().toJson(inputMap)).getAsJsonObject();

    }


    public static int search(double value, ArrayList<AccelerometerData> a) {

        if (value < a.get(0).submitDateDouble) {
            return 0;
        }
        if (value > a.get(a.size() - 1).submitDateDouble) {
            return a.size() - 1;
        }

        int lo = 0;
        int hi = a.size() - 1;

        while (lo <= hi) {
            int mid = (hi + lo) / 2;

            if (value < a.get(mid).submitDateDouble) {
                hi = mid - 1;
            } else if (value > a.get(mid).submitDateDouble) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        // lo == hi + 1
        return (a.get(lo).submitDateDouble - value) < (value - a.get(hi).submitDateDouble) ? lo : hi;
    }


    public static <T extends AccelerometerData> int getClosestK(T[] a, T x) {
        int idx = java.util.Arrays.binarySearch(a, x, Comparator.comparing(e -> e.submitDateDouble));
        if (idx < 0) {
            idx = -idx - 1;
        }

        if (idx == 0) { // littler than any
            return idx;
        } else if (idx == a.length) { // greater than any
            return idx - 1;
        }

        return d(x, a[idx - 1]) < d(x, a[idx]) ? idx - 1 : idx;
    }

    private static <T extends AccelerometerData> double d(T lhs, T rhs) {
        return Math.abs(lhs.submitDateDouble - rhs.submitDateDouble);
    }


    //TODO -- add request validation methods for WEB
    /**Web related methods*/
}
