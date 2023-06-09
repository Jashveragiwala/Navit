package com.example.a1d;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// The Distance Matrix API allows calculating travel distance and time for a matrix of origins and destinations.

// This class uses OkHttpClient to send GET requests to the Google Maps API, parse the JSON responses,
// and returns the distance matrix in a two-dimensional array or a HashMap of HashMaps.
public class DistanceMatrix {

    public int originArrLen = 0; // the length of the array of origins
    public HashMap<Integer, String> indexes = new HashMap<>(); // a HashMap to store the indexes of the origins

    public HashMap<Integer, String> getIndexes() {
        return indexes;
    }

    public double[][] getDistances(String locationsString) throws Exception {
        HashMap<String, double[]> locations = new HashMap<>(); // a HashMap to store the locations and their distances

        String origins = locationsString;
        String destinations = origins;

        String originArr[] = origins.split("%7C%");
        // split the string into an array of origins, the string from API call comes with %7C% in it
        originArrLen = originArr.length;

        // put the origins in the locations HashMap with an empty distance array and save their indexes in the indexes HashMap
        int x = 0;
        for (String s : originArr) {
            double arr[] = new double[originArrLen];
            locations.put(s, arr);
            indexes.put(x, s);
            x++;
        }

        double[][] distanceMatrix = new double[originArrLen][originArrLen];

        OkHttpClient client = new OkHttpClient().newBuilder().build(); // create a new OkHttpClient instance
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                        origins + "&destinations=" + destinations + "&mode=driving&language=en-EN&" +
                        "key=YOUR_API_KEY")
                .method("GET", null)
                .build();

        // API KEY removed for security reasons
        // create a new GET request to the Distance Matrix API with the origins and destinations as parameters and the API key


        Response response = client.newCall(request).execute();

        String output = response.body().string(); // get the response body as a string

        JSONParser parser = new JSONParser(); // create a new JSONParser instance
        try {
            Object obj = parser.parse(output); // parse the JSON response
            JSONObject jsonobj = (JSONObject) obj;

            JSONArray dist = (JSONArray) jsonobj.get("rows"); // extract the rows from the JSON response

            // loop through the rows and extract the distances between the origins and destinations
            for (int i = 0; i < originArrLen; i++) {
                JSONObject objX = (JSONObject) dist.get(i);
                double locArr[] = new double[originArrLen];

                for (int j = 0; j < originArrLen; j++) {
                    JSONArray distIngX = (JSONArray) objX.get("elements");
                    JSONObject objY = (JSONObject) distIngX.get(j);

                    JSONObject distance = (JSONObject) objY.get("distance");
                    JSONObject duration = (JSONObject) objY.get("duration");

                    Object distanceText = distance.get("text");
                    Object durationText = duration.get("text");

                    double value = Double.parseDouble(((String) distanceText).replaceAll("[^0-9.]", ""));
                    locArr[j] = value;

                    if (value == 1.0)
                        distanceMatrix[i][j] = 0.0;
                    else
                        distanceMatrix[i][j] = value;
                }
                locations.put(originArr[i], locArr);


            }
            // Close the response and evict all connections
            response.close();
            client.connectionPool().evictAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return distanceMatrix;
    }


    public HashMap<String,HashMap> getDistancesClusters(String locationString) throws Exception {
        // Initialize a HashMap to store the locations and their corresponding distances
        HashMap<String, HashMap> locations = new HashMap<>();
        // Extract the origin locations from the location string
        String origins = locationString;

        origins = origins.substring(0, origins.length()-4);
        String destinations = origins;

        String originArr[] = origins.split("%7C%");
        int originArrLen = originArr.length;

        for (String s: originArr) {
            locations.put(s, new HashMap<>());
        }

        // Initialize the OkHttpClient to make the API call to the Google Maps Distance Matrix API
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                        origins + "&destinations=" + destinations + "&mode=driving&language=en-EN&" +
                        "key=YOUR_API_KEY")
                .method("GET", null)
                .build();

        // API KEY removed for security reasons

        // Execute the API call and get the response
        Response response = client.newCall(request).execute();

        String output = response.body().string();

        // Parse the response to a JSONObject using JSONParser
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(output);
        JSONObject jsonobj = (JSONObject) obj;

        // Get the rows array from the JSONObject
        JSONArray dist = (JSONArray) jsonobj.get("rows");

        // Loop through each origin location and calculate the distance to each destination location
        // Store the distances in a 1D double array called locArr, and add this array to the locations HashMap
        // The outer loop goes through each origin location, and the inner loop goes through each destination location
        for (int i=0; i<originArrLen; i++) {
            JSONObject objX = (JSONObject) dist.get(i);
            HashMap<String, Double> loc = new HashMap<>();

            for (int j = 0; j < originArrLen; j++) {
                JSONArray distIngX = (JSONArray) objX.get("elements");
                JSONObject objY = (JSONObject) distIngX.get(j);

                // Get the distance and duration objects from the JSONObject and extract the text values
                JSONObject distance = (JSONObject) objY.get("distance");
                JSONObject duration = (JSONObject) objY.get("duration");

                Object distanceText = distance.get("text");
                Object durationText = duration.get("text");

                Double value = Double.parseDouble(((String) distanceText).replaceAll("[^0-9.]", ""));
                // If the value is 1.0, set the corresponding distance in the distanceMatrix to 0.0
                // This is because the distance from a location to itself is always 0
                if (value != 1.0) {
                    loc.put(originArr[j],value);
                }
            }
            // Add the locArr array to the locations HashMap with the corresponding origin location as the key
            locations.put(originArr[i], loc);
        }

        // Close the response and evict all connections
        response.close();
        client.connectionPool().evictAll();
        return locations;
    }

}
