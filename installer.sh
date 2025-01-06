#!/usr/bin/sh
#please run this from the project root directory
originalDirectory="$(pwd)"

#load dependencies
sudo apt install openjdk-8 npm
sudo npm webpack babel-core babel-loader babel-preset-env babel-preset-react babel-preset-stage-0 --save-dev
sudo npm react react-dom -save

cd "jarDependencies/"
wget -O "json_java.jar" https://search.maven.org/remotecontent?filepath=org/json/json/20241224/json-20241224.jar
wget -O "commonmark.jar" https://repo1.maven.org/maven2/org/commonmark/commonmark/0.24.0/commonmark-0.24.0.jar
cd "$originalDirectory"


#build project
javac -d  "binaries/" -cp "/jarDependencies/json_java.jar" "/jarDependencies/commonmark.jar" "src/main.java"

