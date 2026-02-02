package com.sutonglabs.tracestore.common.category

fun categoryKey(name: String): String {
    return name
        .trim()
        .lowercase()
        .replace("&", "and")
        .replace(Regex("\\s+"), "_")
}
