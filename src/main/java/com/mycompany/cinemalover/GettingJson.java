/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.cinemalover;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class GettingJson implements Serializable {

    private List<Movie> movies;
    private Movie movieSelected;

    @PostConstruct
    public void init() {
        this.movieSelected = new Movie();
    }

    // http://localhost:8080/cinema/webresources/movies
    //public static void main(String[] args) throws JSONException {

    public List<Movie> getMovies() throws MalformedURLException, IOException {
        String json = "";
        try {

            URL url = new URL("http://localhost:8080/cinema/webresources/movies");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            //Gson gson = new Gson();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;

            while ((output = br.readLine()) != null) {

                json += output;
                //System.out.println("output" + json);
            }

            Gson gson = new Gson();
            java.lang.reflect.Type listType = new TypeToken<List<Movie>>() {
            }.getType();
            List<Movie> movies = (List<Movie>) gson.fromJson(json, listType);
            /*
             for (int i = 0; i < movies.size(); i++) {
             System.out.println("descripcion movie: " + movies.get(i).descriptionMovie);
             System.out.println("name movie " + movies.get(i).nameMovie);
             System.out.println("id movie " + movies.get(i).idMovie);
             }*/

            conn.disconnect();
            return movies;

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return null;

    }

    public void editMovie() throws MalformedURLException, IOException {
        //public static void main(String[] args) {

        try {

            Gson json = new Gson();
            String movieStr = json.toJson(movieSelected);
            System.out.println(movieStr);

            URL url = new URL("http://localhost:8080/cinema/webresources/movies" + "/" + movieSelected.getIdMovie());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(movieStr.getBytes());
            os.flush();

            /*if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
             throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
             }*/
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void deleteMovie() throws MalformedURLException, IOException {
        //public static void main(String[] args) {

        try {

            Gson json = new Gson();
            String movieStr = json.toJson(movieSelected);
            System.out.println(movieStr);

            URL url = new URL("http://localhost:8080/cinema/webresources/movies" + "/" + movieSelected.getIdMovie());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();

            if (conn.getResponseCode() != 200 && conn.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT) {
                throw new IOException(conn.getResponseMessage());
            }

            /*
             OutputStream os = conn.getOutputStream();
             os.write(movieStr.getBytes());
             os.flush();*/

            /*if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
             throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
             }*/
            /*BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
             String output;
             System.out.println("Output from Server .... \n");
             while ((output = br.readLine()) != null) {
             System.out.println(output);
             }*/
            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public Movie getMovieSelected() {
        return movieSelected;
    }

    public void setMovieSelected(Movie movieSelected) {
        this.movieSelected = movieSelected;
    }

}
