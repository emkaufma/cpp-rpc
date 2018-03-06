/**
Copyright [2016] [Eric Kaufman]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 **/

//MovieDescription class contains several fields which are used to describe various attributes of a particular movie

#ifndef MOVIEDESCRIPTION_HPP
#define MOVIEDESCRIPTION_HPP
#include <iostream>
#include <vector>
#include <jsonrpccpp/server.h>


using namespace std;

class MovieDescription
{
private:
    string title;
    string rated;
    string released;
    string runtime;
    string plot;
    string filename;
    vector<string> genre;
    vector<string> actors;
    
public:
    MovieDescription(string title, string rated, string released, string runtime, string plot, string filename, vector<string> genre, vector<string> actors);
    ~MovieDescription();
    string getTitle();
    string getRated();
    string getReleased();
    string getRuntime();
    string getPlot();
    string getFilename();
    vector<string> getGenre();
    vector<string> getActors();
	Json::Value toJson();
    void setTitle(string _title);
    void setRated(string _rated);
    void setReleased(string _released);
    void setRuntime(string _runtime);
    void setPlot(string _plot);
    void setFilename(string _filename);
    void setGenre(vector<string> _genre);
    void setActors(vector<string> _actors);
    string toString();
};

#endif // MOVIEDESCRIPTION_HPP
