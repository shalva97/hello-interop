#include <jni.h>
#include <libnativeinterop_api.h>

JNIEXPORT jint JNICALL Java_somelib_jvm_JNI_customFunction (JNIEnv* env, jclass jclss,
  jint arg
) {
  return libnativeinterop_symbols()->kotlin.root.somelib.native.customFunction(arg);
}