# Project
cmake_minimum_required(VERSION 3.6)
project(Ramsim)


# Flags
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/bin)


# Sources
file(GLOB SOURCES "src/*.cpp")


#Binary
add_executable(Ramsim ${SOURCES})
