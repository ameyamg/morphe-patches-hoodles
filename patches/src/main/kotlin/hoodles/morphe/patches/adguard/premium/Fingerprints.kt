/**
 * Copyright 2026 Hoo-dles
 * https://github.com/hoo-dles/morphe-patches
 */

package hoodles.morphe.patches.adguard.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.InstructionLocation
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.opcode
import com.android.tools.smali.dexlib2.Opcode

object GetPlusStateFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("Failed to get state from backend. Remaining retry count: ")
    ),
    parameters = listOf(),
    returnType = "L",
    filters = OpcodesFilter.opcodesToFilters(
        Opcode.IGET_OBJECT,
        Opcode.IF_NEZ
    )
)

object PaidLicenseFingerprint : Fingerprint(
    classFingerprint = Fingerprint(
        strings = listOf("PaidLicense(licenseKey=")
    ),
    name = "<init>"
)

object LifetimeDurationFingerprint : Fingerprint(
    name = "toString",
    strings = listOf("Lifetime")
)