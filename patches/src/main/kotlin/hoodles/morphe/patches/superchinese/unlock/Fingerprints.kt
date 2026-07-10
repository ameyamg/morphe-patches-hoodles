package hoodles.morphe.patches.superchinese.unlock

import app.morphe.patcher.Fingerprint

fun getGetUnlockFingerprint(className: String): Fingerprint {
    return Fingerprint(
        definingClass = className,
        name = "getUnlock"
    )
}