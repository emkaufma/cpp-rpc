/**
Copyright [2016] [Eric Kaufman]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributd on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 **/


#include "MovieDescription.hpp"
#include <jsonrpccpp/server.h>



MovieDescription::MovieDescription(string title, string rated, string released, string runtime, string plot, string filename, vector<string> genre, vector<string> actors)
:   title(title),
    rated(rated),
    released(released),
    runtime(runtime),
    plot(plot),
    filename(filename),
    genre(genre),
    actors(actors)
{
}

MovieDescription::~MovieDescription()
{
}

    string MovieDescription::getTitle(){
        return title;
    }

    string MovieDescription::getRated(){
        return rated;
    }
    
    string MovieDescription::getReleased(){
        return released;
    }
    
    string MovieDescription::getRuntime(){
        return runtime;
    }
    
    string MovieDescription::getPlot(){
        return plot;
    }   
    
    string MovieDescription::getFilename(){
        return filename;
    }
    
    vector<string> MovieDescription::getGenre(){
        return genre;
    }
    
    vector<string> MovieDescription::getActors(){
        return actors;
    }
    
    void MovieDescription::setTitle(string _title){
        title = _title;
    }
    void MovieDescription::setRated(string _rated){
        rated = _rated;
    }
    void MovieDescription::setReleased(string _released){
        released = _released;
    }
    void MovieDescription::setRuntime(string _runtime){
        runtime = _runtime;
    }
    void MovieDescription::setPlot(string _plot){
        plot = _plot;
    }
    void MovieDescription::setFilename(string _filename){
        filename = _filename;
    }
    void MovieDescription::setGenre(vector<string> _genre){
        genre = _genre;
    }
    void MovieDescription::setActors(vector<string> _actors){
        actors = _actors;
    }
    
    string MovieDescription::toString(){
        string genreString;
        string actorsString;
        
        for(size_t i = 0; i < genre.size(); i++){
            genreString += genre[i];
        }
        
        for(size_t i = 0; i < actors.size(); i++){
            actorsString += actors[i];
        }
        
        string s = "Title: " + title +
                "\n Rated: " + rated +
                "\n Release Date: " + released +
                "\n Runtime: " + runtime +
                "\n Plot: " + plot +
                "\n Filename: " + filename +
                "\n Genre: " + genreString +
                "\n Actors: " + actorsString;

        return s;
    }
	
	Json::Value MovieDescription::toJson(){
		Json::Value jsonObj;
		Json::Value jsonBody;
		jsonBody["Title"] = title;
		jsonBody["Rated"] = rated;
		jsonBody["Released"] = released;
		jsonBody["Runtime"] = runtime;
		jsonBody["Plot"] = plot;
		jsonBody["Filename"] = filename;
		Json::Value jsonGenre (Json::arrayValue);
		for(size_t i = 0; i < genre.size(); i++){
			jsonGenre.append(genre[i]);
		}
		
		Json::Value jsonActors (Json::arrayValue);
		for(size_t i = 0; i < actors.size(); i++){
			jsonActors.append(actors[i]);
		}
		jsonBody["Genre"] = jsonGenre;
		jsonBody["Actors"] = jsonActors;
		
		jsonObj[title] = jsonBody;
		return jsonObj;
		
	}
