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

import java.net.*;
import java.io.*;

public class MovieLibServer{

        public static void main(String[] args) throws IOException {

            if (args.length != 1) {
                System.err.println("Usage: java MovieLibServer <port number>");
                System.exit(1);
            }

            int portNumber = Integer.parseInt(args[0]);
            boolean listening = true;

            MovieLibrary lib = new MovieLibrary("moviesJava.json");

            try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
                while (listening) {
                    new MovieLibServerThread(serverSocket.accept(), lib).start();
                }
            } catch (IOException e) {
                System.err.println("Could not listen on port " + portNumber);
                System.exit(-1);
            }
        }
}
