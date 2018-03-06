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

#include <iostream>
#include <stdlib.h>
#include <stdio.h>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include "CPPServer.hpp"
#include <jsonrpccpp/server.h>
#include <jsonrpccpp/server/connectors/httpserver.h>

using namespace jsonrpc;

CPPServer::CPPServer(jsonrpc::AbstractServerConnector &connector, int port):AbstractStubServer(connector){
	
	}
CPPServer::CPPServer(jsonrpc::AbstractServerConnector &connector, int port, const string& jsonFileName):AbstractStubServer(connector){
	
   Json::Reader reader;
   Json::Value root;
   std::ifstream json(jsonFileName.c_str(), std::ifstream::binary);
   
   bool parseSuccess = reader.parse(json,root,false);
   if(parseSuccess){
      //cout << "successful parse" << endl;
      Json::Value::Members mbr = root.getMemberNames();
      for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
		//cout << *i << " " << endl;
		Json::Value jsonUser = root[*i];
		 
		string title = jsonUser["Title"].asString();
		string rated = jsonUser["Rated"].asString();
		string released = jsonUser["Released"].asString();
		string runtime = jsonUser["Runtime"].asString();
		string plot = jsonUser["Plot"].asString();
		string filename = jsonUser["Filename"].asString();
		//string title = jsonUser["Title"].asString();
		//string title = jsonUser["Title"].asString();
		
		vector<string>* genre  = new vector<string>();
		vector<string>* actors = new vector<string>();
		
		Json::Value jGenre = jsonUser["Genre"];
		Json::Value jActors = jsonUser["Actors"];
		
		for(uint i = 0; i < jGenre.size(); i++){
			genre->push_back(jGenre[i].asString());
		}
		
		for(uint i = 0; i < jActors.size(); i++){
			actors->push_back(jActors[i].asString());
		}
		
		MovieDescription* movieDescript = new MovieDescription(title, rated, released, runtime, plot, filename, *genre, *actors);
		movieDesc.push_back(*movieDescript);

	  }
   }
   
   else{ cout << "blah blah";}
}
CPPServer::~CPPServer(){
	
}
	
bool CPPServer::add(const Json::Value& Title){
	string title = Title["Title"].asString();
		string rated = Title["Rated"].asString();
		string released = Title["Released"].asString();
		string runtime = Title["Runtime"].asString();
		string plot = Title["Plot"].asString();
		string filename = Title["Filename"].asString();
		
		vector<string>* genre  = new vector<string>();
		vector<string>* actors = new vector<string>();
		
		Json::Value jGenre = Title["Genre"];
		Json::Value jActors = Title["Actors"];
		
		for(uint i = 0; i < jGenre.size(); i++){
			genre->push_back(jGenre[i].asString());
		}
		
		for(uint i = 0; i < jActors.size(); i++){
			actors->push_back(jActors[i].asString());
		}
		
		MovieDescription* movieDescript = new MovieDescription(title, rated, released, runtime, plot, filename, *genre, *actors);
		movieDesc.push_back(*movieDescript);
		
		return true;
}
	
bool CPPServer::remove(const std::string& title){
    cout << "Received request to remove title: " << title;
	for(vector<MovieDescription>::iterator it = movieDesc.begin(); it != movieDesc.end(); it++){
		if (it->getTitle() == title) {  //compare each title in library to aTitle
			movieDesc.erase(it);  //delete MovieDescription from library if found
			return true;  //return true if deleted
		} 
}
    
	return false;  //return false if nothing deleted
}

void CPPServer::toJsonFile(const std::string& jsonFileName){
    cout << "Received request to save current library to filename: " << jsonFileName << endl;
	Json::StyledStreamWriter ssw("  ");
	std::ofstream jsonOutFile(jsonFileName.c_str(), std::ofstream::binary);
	for(size_t i = 0; i < movieDesc.size(); i++){
		ssw.write(jsonOutFile, movieDesc[i].toJson());
	}
}

Json::Value CPPServer::get(const std::string& title){
    cout << "Received request for movie title: " << title  << endl;
	for(vector<MovieDescription>::iterator it = movieDesc.begin(); it != movieDesc.end(); it++){
        if (it->getTitle() == title) {

            return it->toJson();
        } 
    }
	return Json::Value ();
}

Json::Value CPPServer::getTitles(){
    cout << "Received request for list of movie titles" << endl;
	Json::Value jsonObj;
	for(size_t i = 0; i < movieDesc.size(); i++){
		jsonObj[movieDesc[i].getTitle()];
	}
	return jsonObj;
}

void CPPServer::loadFromJsonFile(const std::string& jsonFileName){
    cout << "Received request to load library from json file: " << jsonFileName << endl;
	movieDesc = vector<MovieDescription>();
	Json::Reader reader;
	Json::Value root;
	std::ifstream json(jsonFileName.c_str(), std::ifstream::binary);
   
	bool parseSuccess = reader.parse(json,root,false);
	if(parseSuccess){
		  cout << "successful parse" << endl;
		  Json::Value::Members mbr = root.getMemberNames();
		  for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
				cout << *i << " " << endl;
				Json::Value jsonUser = root[*i];
				 
				string title = jsonUser["Title"].asString();
				string rated = jsonUser["Rated"].asString();
				string released = jsonUser["Released"].asString();
				string runtime = jsonUser["Runtime"].asString();
				string plot = jsonUser["Plot"].asString();
				string filename = jsonUser["Filename"].asString();
				//string title = jsonUser["Title"].asString();
				//string title = jsonUser["Title"].asString();
				
				vector<string>* genre  = new vector<string>();
				vector<string>* actors = new vector<string>();
				
				Json::Value jGenre = jsonUser["Genre"];
				Json::Value jActors = jsonUser["Actors"];
			
				for(uint i = 0; i < jGenre.size(); i++){
					genre->push_back(jGenre[i].asString());
				}
				
				for(uint i = 0; i < jActors.size(); i++){
					actors->push_back(jActors[i].asString());
				}
				
				MovieDescription* movieDescript = new MovieDescription(title, rated, released, runtime, plot, filename, *genre, *actors);
				movieDesc.push_back(*movieDescript);

		  }
   }
}

int main(int argc, char **argv)
{
	int port = 8080;
	if(argc > 1){
	  port = atoi(argv[1]);
   }
   //cout << port << endl;
   jsonrpc::HttpServer httpserver(port);
   CPPServer cs(httpserver, port, "movies.json");
   cs.StartListening();
   int c = getchar();
 //  cs.StopListening();
   return 0;
}
