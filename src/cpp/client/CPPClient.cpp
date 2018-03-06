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

#include "MovieLibraryGUI.cpp"
#include <iostream>
#include <stdlib.h>
#include <stdio.h>
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <stdlib.h>
#include <string>
#include <sstream>
#include <thread>
#include <jsonrpccpp/client/connectors/httpclient.h>
#include "stubclient.h"

using namespace jsonrpc;
using namespace std;


class CPPClient: public MovieLibraryGUI{
	

	
public:  
	std::string appAuthor;
    std::thread * playThread;
	std::string host;

	CPPClient(string _host, string name): MovieLibraryGUI(){
		host = _host;
		/**
		HttpClient httpclient(host);
		StubClient cc(httpclient);
		string inLine;
		cout << "Connected to: "  << cc.serviceInfo() << endl;
		Json::Value::Members mbr = cc.getTitles().getMemberNames();
			for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
				cout << *i << endl;
			}

		  
		   } 
			catch (JsonRpcException e) {
				cerr << e.what() << endl;
			} */
			
			  callback(ClickedX, (void*)this);
			  playThread = NULL;
			  menubar->callback(Menu_ClickedS, (void*)this);
			  appAuthor = name;
			  buildTree();
			  tree->callback(TreeCallbackS, (void*)this);
			
		
		}
		
		static void ClickedX(Fl_Widget * w, void * userdata) {
			  std::cout << "You clicked Exit" << std::endl;
			  CPPClient *o = (CPPClient*)userdata;
			  if(o->playThread != NULL && (o->playThread)->joinable()){
				 (o->playThread)->join();
			  }
			  exit(1);
		}
		
		 // Static tree callback method
		static void TreeCallbackS(Fl_Widget*w, void*data) {
			CPPClient *o = (CPPClient*)data;
			o->TreeCallback(); //call the instance callback method
		}
			
		void TreeCallback() {
			
			  HttpClient httpclient(host);
			  StubClient cc(httpclient);
			  // Find item that was clicked
			  Fl_Tree_Item *item = (Fl_Tree_Item*)tree->item_clicked();
			  std::cout << "Tree callback. Current selection is: ";
			  if ( item ) {
				 std::cout << item->label();
			  } else {
				 std::cout << "none";
			  }
			  std::cout << endl;
			  std::string aStr("unknown");
			  std::string aTitle(item->label());
			  switch ( tree->callback_reason() ) {  // reason callback was invoked
				case       FL_TREE_REASON_NONE: {aStr = "none"; break;}
				case     FL_TREE_REASON_OPENED: {aStr = "opened";break;}
				case     FL_TREE_REASON_CLOSED: {aStr = "closed"; break;}
				case   FL_TREE_REASON_SELECTED: {
				   aStr = "selection";
				   try{
                   cout << "Sending request to server for movie title: " << aTitle << endl;
				   Json::Value jMovieDesc = cc.get(aTitle);
				   Json::Value::Members mbr = jMovieDesc.getMemberNames();
				   vector<string>* title = new vector<string>();
				   for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
					   title->push_back(*i);
				   }
				   
				    titleInput->value(jMovieDesc[title->at(0)]["Title"].asCString());
				    ratedInput->value(jMovieDesc[title->at(0)]["Rated"].asCString());
				    runtimeInput->value(jMovieDesc[title->at(0)]["Runtime"].asCString());
				    plotMLIn->value(jMovieDesc[title->at(0)]["Plot"].asCString());
				    releasedInput->value(jMovieDesc[title->at(0)]["Released"].asCString());
					filenameInput->value(jMovieDesc[title->at(0)]["Filename"].asCString());
				   
					Json::Value jGenre = jMovieDesc[title->at(0)]["Genre"];
					Json::Value jActors = jMovieDesc[title->at(0)]["Actors"];
					
					actorsChoice->clear();
				    genreChoice->clear();
				    
					
					for(uint i = 0; i < jGenre.size(); i++){
						genreChoice->add(jGenre[i].asCString());
					}
					
					for(uint i = 0; i < jActors.size(); i++){
						actorsChoice->add(jActors[i].asCString());
					}
					
					actorsChoice->value(0);
					genreChoice->value(0);
					
					} catch (JsonRpcException e) {
						
					}
					
				   break;
				}
				case FL_TREE_REASON_DESELECTED: {aStr = "deselected"; break;}
			  default: {break;}
			  }
		   std::cout << "Callback reason: " << aStr.c_str() << endl;
	}
	   
	   // Static menu callback method
   static void Menu_ClickedS(Fl_Widget*w, void*data) {
      CPPClient *o = (CPPClient*)data;
      o->Menu_Clicked(); //call the instance callback method
   }
   
    // Menu selection instance method that has ccess to instance vars.
   void Menu_Clicked() {
	   
	    HttpClient httpclient(host);
		StubClient cc(httpclient);
		
      char picked[80];
      menubar->item_pathname(picked, sizeof(picked)-1);
      string selectPath(picked);
      cout << "Selected Menu Path: " << selectPath << endl;
      // get a stub to the library server
      // Handle menu selections calling methods on the stub where necessary.
      if(selectPath.compare("File/Save")==0){
         //cc.toJsonFile("movies.json");
      }else if(selectPath.compare("File/Restore")==0){
        // cc.loadFromJsonFile("movies.json");
      }else if(selectPath.compare("File/Exit")==0){
         cout << "Menu item File/Exit selected." << endl;
         if(playThread != NULL && playThread->joinable()){
            playThread->join();
         }
         exit(0);
      }else if(selectPath.compare("Movie/Remove")==0){
         cout << "Sending request to server to remove movie title: " << titleInput->value() << endl;
         cc.remove(titleInput->value());
		 buildTree();
      }
	  else if(selectPath.compare("File/Tree Refresh")==0){
		 buildTree();
      }
   }
   
   // local method to build the tree in the GUI left panel.
   void buildTree(){
        cout << "Sending request to server for list of movie titles" << endl;
		HttpClient httpclient(host);
		StubClient cc(httpclient);
		tree->clear();
		string inLine;
		Json::Value::Members mbr = cc.getTitles().getMemberNames();
			for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
				cout << *i << endl;
			}

		  
			
		Json::Value jsonTitles = cc.getTitles();
		vector<string> titles;
		mbr = jsonTitles.getMemberNames();
		for(vector<string>::const_iterator i = mbr.begin(); i!= mbr.end(); i++){
			std::stringstream stream;
			stream << "Video"
                << "/" << *i;
			tree->add(stream.str().c_str());
		}
		       
		tree->root_label(appAuthor.c_str());
		tree->redraw();
		
   }

};

int main(int argc, char*argv[]) {
   string host = "http://127.0.0.1:8080";
   if(argc>1){
      host = string(argv[1]);
   }
   CPPClient client(host, "Eric's Library");
   return (Fl::run());
}
