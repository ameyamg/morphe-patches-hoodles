package hoodles.morphe.patches.shared.misc.pairip

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.OpcodesFilter
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.Opcode

object VMRunnerStaticCtorFingerprint : Fingerprint (
    definingClass = "Lcom/pairip/VMRunner;",
    name = "<clinit>"
)

object VMRunnerInvokeFingerprint : Fingerprint (
    definingClass = "Lcom/pairip/VMRunner;",
    name = "invoke"
)

object StartupLaunchFingerprint : Fingerprint (
    definingClass = "Lcom/pairip/StartupLauncher;",
    name = "launch",
    filters = listOf(
        methodCall(name = "invoke")
    )
)