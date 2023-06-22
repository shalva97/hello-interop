object JNI {
    init {
        System.loadLibrary("nativeinterop")
    }

    external fun customFunction(arg: Int): Int

}
