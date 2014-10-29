gwynt
----

Asynchronous client and server networking library. Supports both tcp and udp with multicasting server and clients.

#### Usage:

Project requires jdk8 for compilation.
Refer to example project for usage showcase.

#### Running examples:

* cd main project directory
* mvn clean install
* cd gwynt-examples
* mvn exec:java
* Open [3000 port](http://localhost:3000) and [3001 port](http://localhost:3001) with your browser
* port 3000 - old thread-per-channel model
* port 3001 - reactor-based model
