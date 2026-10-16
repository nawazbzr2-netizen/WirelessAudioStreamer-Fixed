package com.wireless.audiostreamer.util

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    fun wrap(ctx: Context): Context {
        val lang = AppPrefs.lang(ctx)
        if (lang.isEmpty() || lang == "system") return ctx
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(ctx.resources.configuration)
        config.setLocale(locale)
        return ctx.createConfigurationContext(config)
    }
}
