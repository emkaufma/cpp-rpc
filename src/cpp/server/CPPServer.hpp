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

//CPPServer class used for storing MovieDescription objects

#ifndef CPPSERVER_HPP
#define CPPSERVER_HPP

#include <iostream>
#include <vector>
#include "MovieDescription.hpp"
#include "abstractstubserver.h"
#include <jsonrpccpp/server.h>
#include <jsonrpccpp/server/connectors/httpserver.h>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>

using namespace std;
class CPPServer: public AbstractStubServer{
private:
    vector<MovieDescription> movieDesc;
	
public:
    CPPServer(jsonrpc::AbstractServerConnector &connector, int port);
	CPPServer(jsonrpc::AbstractServerConnector &connector, int port, const string& jsonFileName);
	~CPPServer();
	
	virtual bool add(const Json::Value& Title);
	virtual bool remove(const std::string& title);
	virtual void toJsonFile(const std::string& jsonFileName);
	virtual Json::Value get(const std::string& title);
	virtual Json::Value getTitles();
	virtual void loadFromJsonFile(const std::string& jsonFileName);

};

#endif // CPPSERVER_HPP
