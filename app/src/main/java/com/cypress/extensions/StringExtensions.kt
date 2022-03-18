package com.cypress.extensions

import android.content.Context

fun Context.getStringResources(stringId: Int?) =

    if (stringId != null) {

        try {

            resources.getString(stringId)

        } catch (e: Exception) {

            e.printStackTrace()

            ""

        }

    } else ""

