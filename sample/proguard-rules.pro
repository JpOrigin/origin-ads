-keep class androidx.appcompat.widget.** { *; }
-keep class com.google.android.** { *; }
-keep public class com.google.android.gms.ads.**{
   public *;
}
-flattenpackagehierarchy
-ignorewarnings
# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# Lifecycle
-keepattributes *Annotation*
-keepclassmembers enum androidx.lifecycle.Lifecycle$Event {
    <fields>;
}
-keep !interface * implements androidx.lifecycle.LifecycleObserver {
}
-keep class * implements androidx.lifecycle.GeneratedAdapter {
    <init>(...);
}
-keepclassmembers class ** {
    @androidx.lifecycle.OnLifecycleEvent *;
}
-keepclassmembers class androidx.lifecycle.ReportFragment$LifecycleCallbacks { *; }


# My Customize
-keep class com.origin.ads.sample.clone.** { *; }

-obfuscationdictionary "D:\android_studio\sdk\tools\class_encode_dictionary.txt"
-classobfuscationdictionary "D:\android_studio\sdk\tools\class_encode_dictionary.txt"
-packageobfuscationdictionary "D:\android_studio\sdk\tools\class_encode_dictionary.txt"

-mergeinterfacesaggressively
#-overloadaggressively
-repackageclasses "com.origin.ads.sample"

