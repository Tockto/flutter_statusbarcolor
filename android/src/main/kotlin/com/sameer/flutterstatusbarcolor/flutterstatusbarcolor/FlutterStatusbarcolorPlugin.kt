package com.sameer.flutterstatusbarcolor.flutterstatusbarcolor

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Build
import android.view.View
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel


class FlutterStatusbarcolorPlugin : FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {

    private var mActivity: Activity? = null
    private var channel: MethodChannel? = null


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "plugins.sameer.com/statusbar")
        channel?.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: MethodChannel.Result) {
        if (mActivity == null) return result.success(null)

        when (call.method) {
            "getstatusbarcolor" -> {
                var statusBarColor = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    statusBarColor = mActivity!!.window.statusBarColor
                }
                result.success(statusBarColor)
            }
            "setstatusbarcolor" -> {
                val statusBarColor: Int = call.argument("color")!!
                val animate: Boolean = call.argument("animate")!!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animate) {
                        val colorAnim =
                            ValueAnimator.ofArgb(mActivity!!.window.statusBarColor, statusBarColor)
                        colorAnim.addUpdateListener { anim ->
                            mActivity!!.window.statusBarColor = anim.animatedValue as Int
                        }
                        colorAnim.duration = 300
                        colorAnim.start()
                    } else {
                        mActivity!!.window.statusBarColor = statusBarColor
                    }
                }
                result.success(null)
            }
            "setstatusbarwhiteforeground" -> {
                val useWhiteForeground: Boolean = call.argument("whiteForeground")!!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (useWhiteForeground) {
                        mActivity!!.window.decorView.systemUiVisibility =
                            mActivity!!.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                    } else {
                        mActivity!!.window.decorView.systemUiVisibility =
                            mActivity!!.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    }
                }
                result.success(null)
            }
            "getnavigationbarcolor" -> {
                var navigationBarColor = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    navigationBarColor = mActivity!!.window.navigationBarColor
                }
                result.success(navigationBarColor)
            }
            "setnavigationbarcolor" -> {
                val navigationBarColor: Int = call.argument("color")!!
                val animate: Boolean = call.argument("animate")!!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (animate) {
                        val colorAnim = ValueAnimator.ofArgb(
                            mActivity!!.window.navigationBarColor,
                            navigationBarColor
                        )
                        colorAnim.addUpdateListener { anim ->
                            mActivity!!.window.navigationBarColor = anim.animatedValue as Int
                        }
                        colorAnim.duration = 300
                        colorAnim.start()
                    } else {
                        mActivity!!.window.navigationBarColor = navigationBarColor
                    }
                }
                result.success(null)
            }
            "setnavigationbarwhiteforeground" -> {
                val useWhiteForeground: Boolean = call.argument("whiteForeground")!!
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (useWhiteForeground) {
                        mActivity!!.window.decorView.systemUiVisibility =
                            mActivity!!.window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
                    } else {
                        mActivity!!.window.decorView.systemUiVisibility =
                            mActivity!!.window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    }
                }
                result.success(null)
            }
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel?.setMethodCallHandler(null)
        channel = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        mActivity = binding.activity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        channel?.setMethodCallHandler(null)
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        channel?.setMethodCallHandler(FlutterStatusbarcolorPlugin())
    }

    override fun onDetachedFromActivity() {
        channel?.setMethodCallHandler(null)
    }
}
