import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

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
public class MovieLibrary {

    private Vector<MovieDescription> movieDescriptions;

    /**
     * Construct an empty MovieLibrary
     */
    public MovieLibrary(){
        movieDescriptions = new Vector<>();
    }


    public MovieLibrary(String jsonFileName){
        FileInputStream in = null;
        try {
            in = new FileInputStream(jsonFileName);
            JSONObject obj = new JSONObject(new JSONTokener(in));
            String [] names = obj.getNames(obj);
            movieDescriptions = new Vector<>();

            for(String s: names){
                MovieDescription desc = new MovieDescription(obj.getJSONObject(s));
                movieDescriptions.add(desc);
            }
            in.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construct a MovieLibrary with an existing list of MovieDescription objects
     * @param movieDescriptions
     */
    public MovieLibrary(Vector<MovieDescription> movieDescriptions){
        this.movieDescriptions = movieDescriptions;
    }


    public  void  toJsonFile(String jsonFileName){
        try {
            PrintWriter out = new PrintWriter(jsonFileName);
            JSONObject jsonObj = new JSONObject();

            for(MovieDescription desc: movieDescriptions){
                jsonObj.put(desc.getTitle(), desc.toJson());
            }

            out.print(jsonObj.toString(3));
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Add a new MovieDescription to the MovieLibrary
     * @param aClip MovieDescription to be added
     * @return Whether the MovieDescription was successfully added
     */
    public boolean add(MovieDescription aClip){
        return movieDescriptions.add(aClip);
    }

    /**
     * Remove a MovieDescription from the MovieLibrary
     * @param aTitle Title of Movie to be removed
     * @return Whether the movie was sucessfully removed
     */
    public boolean remove(String aTitle){
        for(MovieDescription movie : movieDescriptions){
            if (movie.getTitle().equals(aTitle)) return movieDescriptions.remove(movie);
        }

        return false;
    }

    /**
     * Get a MovieDescription from the library that has the given title.
     * @param aTitle Title of MovieDescription to be retrieved
     * @return MovieDescription with given title.  Returns null if movie is not found.
     */

    public MovieDescription get(String aTitle){
        for(MovieDescription movie : movieDescriptions){
            if (movie.getTitle().equals(aTitle)) return movie;
        }

        return null;
    }

    /**
     * Returns a string array containing all the movie titles in this MovieLibrary
     * @return String array containing the title of every movie
     */

    public String[] getTitles(){
        String[] titles = new String[movieDescriptions.size()];
        for(int i = 0; i < movieDescriptions.size(); i++){
            titles[i] = movieDescriptions.elementAt(i).getTitle();
        }

        return titles;
    }

    /**
     * Driver to test out MovieDescription and MovieLibrary classes
     * @param args
     */

    public static void main(String[] args){
        //MovieLibrary lib = new MovieLibrary("boats.json");
        //lib.toJsonFile("moviesJava.json");


    }

}
