cmake_minimum_required(VERSION 3.0.0)
project(jni_dome VERSION 0.1.0)

# 找jni
find_package(JNI)

if(JNI_FOUND)
    message(STATUS "JNI_INCLUDE_DIRS=${JNI_INCLUDE_DIRS}")
    message(STATUS "JNI_LIBRARIES=${JNI_LIBRARIES}")
    message(STATUS "JAVA_AWT_LIBRARY=${JAVA_AWT_LIBRARY}")
    message(STATUS "JAVA_JVM_LIBRARY=${JAVA_JVM_LIBRARY}")
    message(STATUS "JAVA_INCLUDE_PATH=${JAVA_INCLUDE_PATH}")
    message(STATUS "JAVA_INCLUDE_PATH2=${JAVA_INCLUDE_PATH2}")
    message(STATUS "JAVA_AWT_INCLUDE_PATH=${JAVA_AWT_INCLUDE_PATH}")
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
