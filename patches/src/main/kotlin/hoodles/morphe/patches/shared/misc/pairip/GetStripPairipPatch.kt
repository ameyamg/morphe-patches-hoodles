package hoodles.morphe.patches.shared.misc.pairip

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.BytecodePatch
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable
import app.morphe.patches.all.misc.hex.HexPatchBuilder
import app.morphe.patches.all.misc.hex.hexPatch
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.builder.MutableMethodImplementation
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodParameter
import hoodles.morphe.patches.shared.misc.pairip.resources.pairipResourcesPatch

fun getStripPairipPatch(
    app: String,
    replacements: (HexPatchBuilder.() -> Unit)? = null
): BytecodePatch = bytecodePatch {
    extendWith("extensions/__generated__/$app.mpe")

    dependsOn(pairipResourcesPatch)
    replacements?.also { dependsOn(hexPatch(false, it)) }

    execute {
        VMRunnerStaticCtorFingerprint.method.returnEarly()
        VMRunnerInvokeFingerprint.method.returnEarly(null)

        val applicationName = "Lcom/pairip/application/Application;"
        val applicationClass = mutableClassDefBy(applicationName)
        applicationClass.virtualMethods.removeIf { it.name == "attachBaseContext" }

        val staticCtorImpl = MutableMethodImplementation(1)
        val staticCtor = ImmutableMethod(
            applicationName,
            "<clinit>",
            emptyList<ImmutableMethodParameter>(),
            "V",
            AccessFlags.CONSTRUCTOR.value or AccessFlags.STATIC.value,
            null,
            null,
            staticCtorImpl
        ).toMutable()

        staticCtor.addInstructions(0, """
            invoke-static { }, Lcom/pairip/StartupLauncher;->launch()V
            return-void
        """.trimIndent())

        applicationClass.directMethods.add(staticCtor)

        StartupLaunchFingerprint.apply {
            val invokeIndex = instructionMatches.first().index
            method.replaceInstruction(invokeIndex, """
                invoke-static { }, Lhoodles/morphe/extension/$app/pairip/PairipHook;->inject()V
            """.trimIndent()
            )
        }
    }
}