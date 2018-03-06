

To run the server, first use the target execute.server.assign6 .  You can specify the port number by passing the command line argument: -Dport=(port#) for example: 
ant execute.server.assign6 -Dport=3030

Then, use the target execute.client.assign6 .  You can specify the port number and ip address by passing the command line arguments: -Dport=(port#) -Dipadd=(ipadd), for example:
ant execute.client.assign6 -Dport=3030 -Dipadd=127.0.0.1

The program can be run by entering: ./bin/movieLibraryServer port#
Then in another termial entering: ./bin/movieLibraryClient ipAdd

For example:  ./bin/movieLibraryServer 3030
              ./bin/movieLibraryClient http://127.0.0.1:3030


