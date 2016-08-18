#include <jni.h>


extern "C" JNIEXPORT jint JNICALL Java_com_example_flickrgallery_MainActivity_sum(
		JNIEnv *env, jobject obj, jint a, jint b)
{
	return a + b;
}
