#!/usr/bin/sh
#please run this from the project root directory
originalDirectory="$(pwd)"

#load dependencies
sudo apt install openjdk-8 npm

cd "jar-dependencies/"
wget -O "json-20241224.jar" https://search.maven.org/remotecontent?filepath=org/json/json/20241224/json-20241224.jar
cd "$originalDirectory"


#build project
javac -d  "binaries/" -cp "/jar-dependencies/json-20241224.jar" "backend/main.java"

