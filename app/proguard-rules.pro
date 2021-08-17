#-dontwarn rx.**
#
#-dontwarn okio.**
#
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }



-dontwarn retrofit.**
-dontwarn retrofit.appengine.UrlFetchClient


-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keep class com.android.mealpass.productdetail.model.** { *; }

#-keepattributes Signature
#-keepattributes *Annotation*