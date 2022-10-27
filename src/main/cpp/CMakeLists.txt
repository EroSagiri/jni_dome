cmake_minimum_required(VERSION 3.0.0)
project(jni_dome VERSION 0.1.0)

# 找jni
find_package(JNI)

if(JNI_FOUND)
    message(STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message(STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
endif()

# 导入JAVA头文件目录
include_directories(${JNI_INCLUDE_DIRS})

include(CTest)
enable_testing()

add_library(jni_dome SHARED jni_dome.cpp Hello.h)
# 连接
target_link_libraries(jni_dome ${JNI_LIBRARIES})

set(CPACK_PROJECT_NAME ${PROJECT_NAME})
set(CPACK_PROJECT_VERSION ${PROJECT_VERSION})
include(CPack)
