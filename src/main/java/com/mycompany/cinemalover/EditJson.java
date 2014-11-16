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
public class EditJson implements Serializable {

    private Movie movieSelected;

    @PostConstruct
    public void init() {
        this.movieSelected = new Movie();
    }

    public void editMovie() throws MalformedURLException, IOException {
    //public static void main(String[] args) {

        try {

            Gson json = new Gson();
            String movieStr = json.toJson(movieSelected);
            System.out.println(movieStr);

            URL url = new URL("http://localhost:8080/cinema/webresources/movies");
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

    public Movie getMovieSelected() {
        return movieSelected;
    }

    public void setMovieSelected(Movie movieSelected) {
        this.movieSelected = movieSelected;
    }

 

}
