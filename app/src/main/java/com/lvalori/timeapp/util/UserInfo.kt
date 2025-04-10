package com.lvalori.timeapp.util

import android.content.Context
import android.os.Process
import android.os.UserManager

object UserInfo {
    fun getCurrentUsername(context: Context): String {
        return try {
            val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager
            userManager.userName ?: "lvalori"  // Usiamo il tuo username come fallback
        } catch (e: Exception) {
            "lvalori"  // Usiamo il tuo username come fallback
        }
    }
}