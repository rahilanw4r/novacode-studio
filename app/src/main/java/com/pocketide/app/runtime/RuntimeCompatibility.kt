package com.pocketide.app.runtime

internal fun supportsArm64Runtime(supportedAbis: Array<String>, osArchitecture: String?): Boolean {
    val abiHasArm64 = supportedAbis.any { it.equals("arm64-v8a", ignoreCase = true) }
    val arch = osArchitecture?.lowercase()?.trim() ?: ""
    val kernelIsArm64 = arch.contains("aarch64") || arch.contains("arm64") || arch.contains("armv8")
    return abiHasArm64 && (kernelIsArm64 || arch.isEmpty())
}
