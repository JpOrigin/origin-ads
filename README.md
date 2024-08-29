[![](https://jitpack.io/v/jporigin/origin-ads.svg)](https://jitpack.io/#jporigin/origin-ads)
# Gradle

> 1. Add the JitPack repository to your build file

Add it in your root `build.gradle` at end of repositories:<br/>
```groovy
allprojects {
	repositories {
		...
 		maven { url 'https://jitpack.io' }
	}
}
```

> 2. Add the dependency

Add it in your app `build.gradle` at end of dependency:<br/>
```groovy
dependencies {
	implementation("com.github.jporigin:origin-ads:$version")
}
```


# Usage

> Initialize Ads SDK

Add it in your `Application` at start of `onCreate()`:<br/>
#### Java
```java
@Override
public void onCreate() {
	super.onCreate();
        // Initialize the Mobile Ads SDK
        initializeMobileAds(MyApplication.this);
    }
```
#### Kotlin
```kotlin
 override fun onCreate() {
	super.onCreate()
        // Initialize the Mobile Ads SDK
        this@MyApplication.initializeMobileAds()
 }
```
## Interstitial Ads
> How do I use Interstitial ads without any listener/callback

Use interstitial ads without any listener/callback:<br/>
#### Java
```java
import static com.origin.ads.GoogleInterstitialAdsKt.showGoogleInterstitialAd;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(...);
        showGoogleInterstitialAd(activity);
    }
```
#### Kotlin
```kotlin
import com.origin.ads.showGoogleInterstitialAd

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(...)
        activity.showGoogleInterstitialAd()
    }
```
> How do I use Interstitial ads with listener/callback

Use interstitial ads with listener/callback:<br/>
#### Java
```java
import static com.origin.ads.GoogleInterstitialAdsKt.showGoogleInterstitialAd;

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
        setContentView(...);
        view.setOnClickListener(v -> {
            showGoogleInterstitialAd(activity, () -> {
                // intent to next activity
                return null;
            });
        });
    }
```
#### Kotlin
```kotlin
import com.origin.ads.showGoogleInterstitialAd

override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(...)
        view.setOnClickListener {
            activity.showGoogleInterstitialAd {
                // intent to next activity
            }
        }
    }
```
