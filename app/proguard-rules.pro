#-dontwarn okhttp3.**
#-dontwarn okio.**
#-dontwarn javax.annotation.**
#-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
#
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public class * extends com.bumptech.glide.AppGlideModule
#-keep public class * extends com.bumptech.glide.module.AppGlideModule
#-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#    **[] $VALUES;
#    public *;
#}
#
#-keep public class * extends com.bumptech.glide.module.AppGlideModule
#-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
#
#-dontwarn com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
#-dontwarn com.bumptech.glide.load.resource.bitmap.Downsampler
#-dontwarn com.bumptech.glide.load.resource.bitmap.HardwareConfigState
#-dontwarn com.bumptech.glide.manager.RequestManagerRetriever

-dontobfuscate
