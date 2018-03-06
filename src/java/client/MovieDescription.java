import java.util.Vector;
import org.json.*;


/**Copyright [2016] [Eric Kaufman]

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

public class MovieDescription {

    private String title;
    private String rated;
    private String released;
    private String runtime;
    private String plot;
    private String filename;
    private Vector<String> genre;
    private Vector<String> actors;

    /**
     * Constructs a new MovieDescription object
     * @param title Title of movie
     * @param rated Rating of movie
     * @param released Release date of movie
     * @param runtime Length of movie in minutes
     * @param plot Plot of movie
     * @param filename Filename of movie file
     * @param genre List of applicable genres of movie
     * @param actors List of actors in movie
     */

    public MovieDescription(String title, String rated, String released, String runtime, String plot, String filename, Vector<String> genre, Vector<String> actors) {
        this.title = title;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.plot = plot;
        this.filename = filename;
        this.genre = genre;
        this.actors = actors;
    }

    public MovieDescription(org.json.JSONObject jsonObj){
        title = jsonObj.getString("Title");
        rated = jsonObj.getString("Rated");
        released = jsonObj.getString("Released");
        runtime = jsonObj.getString("Runtime");
        plot = jsonObj.getString("Plot");
        filename = jsonObj.getString("Filename");
        JSONArray jsonArr = jsonObj.getJSONArray("Genre");
        genre = new Vector<>();
        actors = new Vector<>();
        for(int i = 0; i < jsonArr.length(); i++){
            genre.add(jsonArr.getString(i));
        }
        jsonArr = jsonObj.getJSONArray("Actors");
        for(int i = 0; i < jsonArr.length(); i++){
            actors.add(jsonArr.getString(i));
        }

    }

    public  JSONObject toJson(){
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("Title", title);
        jsonObj.put("Rated", rated);
        jsonObj.put("Released", released);
        jsonObj.put("Runtime", runtime);
        jsonObj.put("Plot", plot);
        jsonObj.put("Filename", filename);
        jsonObj.put("Genre", genre);
        jsonObj.put("Actors", actors);
        return jsonObj;
    }

    /**
     * Returns a human readable string description of this MovieDescription object
     * @return String containing the fields of the MovieDescription object
     */
    public String toString() {
        String s = "Title: " + title +
                "\n Rated: " + rated +
                "\n Release Date: " + released +
                "\n Runtime: " + runtime +
                "\n Plot: " + plot +
                "\n Filename: " + filename +
                "\n Genre: " + genre +
                "\n Actors: " + actors;

        return s;
    }

    //The rest of the class consists of getter and setter methods.  I'm not going to write comments on them as there are
    //quite a lot and their functionality is self-explanatory.

    public String getTitle() {
        return title;
    }

    public String getRated() {
        return rated;
    }

    public String getReleased() {
        return released;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getPlot() {
        return plot;
    }

    public String getFilename() {
        return filename;
    }

    public Vector<String> getGenre() {
        return genre;
    }

    public Vector<String> getActors() {
        return actors;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setGenre(Vector<String> genre) {
        this.genre = genre;
    }

    public void setActors(Vector<String> actors) {
        this.actors = actors;
    }

}
