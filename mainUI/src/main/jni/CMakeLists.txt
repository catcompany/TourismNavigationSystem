cmake_minimum_required(VERSION 3.10.2)

project("tns")

add_library(main SHARED Jni.cpp)

find_library(LogLib log)

target_link_libraries(main ${LogLib})