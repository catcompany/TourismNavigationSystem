cmake_minimum_required(VERSION 3.10.2)

project("codeutils")

add_library(codeutils SHARED CodeUtils.cpp)

target_link_libraries(codeutils)