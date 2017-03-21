# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# General Configuration
-dontskipnonpubliclibraryclasses
-forceprocessing
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

# Suppress warnings if you are NOT using IAP:
-dontwarn com.squareup.**
-dontwarn org.joda.time.**
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.**
-dontwarn retrofit2.**

# Do not minify this library
-keep class com.flurry.** { *; }
-keep class android.support.v4.** { *; }
-keep class android.support.v7.** { *; }

-keepattributes Signature
-keepattributes EnclosingMethod
-keepattributes *Annotation*
