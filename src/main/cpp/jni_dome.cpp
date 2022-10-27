#include "Hello.h"

jint count = 1;

/*
 * Class:     Hello
 * Method:    getCount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_Hello_getCount(JNIEnv *env, jclass) {
    return count;
}

/*
 * Class:     Hello
 * Method:    setCount
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_Hello_setCount(JNIEnv *, jclass, jint j) {
    count = j;
}