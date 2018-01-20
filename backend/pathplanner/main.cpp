#include <iostream>
#include <string>
#include <stdlib.h>
#include <fstream>

int main(int argc, char** argv) {
  std::string result = "";
  if(argc < 3) {
    std::cerr << "Missing path network file name" << std::endl;
    exit(1);
  } else {
    char* location_string = *(argv + 1);

    std::cout << "Loading path network from file " << location_string << std::endl;

    char* file_name = *(argv + 2);

    std::cout << "Loading path network from file " << file_name << std::endl;

    std::ifstream file;
    file.open(file_name);

    if(!file.is_open()) {
      std::cout << "cannot open file " << file_name << std::endl;
      exit(2);
    }

    std::string line = "";
    while(!file.eof()) {
      getline(file, line);
      if(!line.empty()) {
        // populate graph
        result += line;
        result += ";";
      }
    }
  }

  std::cout << result << std::endl;
}
