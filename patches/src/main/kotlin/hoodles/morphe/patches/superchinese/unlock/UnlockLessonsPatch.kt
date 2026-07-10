package hoodles.morphe.patches.superchinese.unlock

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import hoodles.morphe.patches.superchinese.misc.signature.spoofSignaturePatch
import hoodles.morphe.patches.superchinese.shared.Constants
import hoodles.morphe.util.returnBoxedIntegerEarly

enum class SmaliType {
    INT, INTEGER, STRING
}

@Suppress("unused")
val unlockLessonsPatch = bytecodePatch(
    name = "Unlock all lessons",
    description = "Only unlocks lessons on the client UI! This is useful for pre-downloading content during free trial periods."
) {
    compatibleWith(Constants.Compatibility)

    dependsOn(spoofSignaturePatch)

    execute {
        val unlockClasses = mapOf(
            "Lcom/superchinese/model/BaseLesson;" to SmaliType.INTEGER,
            "Lcom/superchinese/model/LessonStart;" to SmaliType.INTEGER,
            "Lcom/superchinese/model/LessonWordGrammarEntity;" to SmaliType.STRING,
            "Lcom/superchinese/model/LessonCollection;" to SmaliType.INTEGER,
            "Lcom/superchinese/model/LessonViewUnit;" to SmaliType.INTEGER,
            "Lcom/superchinese/model/LevelIndexItem;" to SmaliType.INTEGER,
            "Lcom/superchinese/model/HomeLevelTest;" to SmaliType.INT
        )

        unlockClasses.forEach { (className, type) ->
            getGetUnlockFingerprint(className).method.apply {
                when (type) {
                    SmaliType.INT -> returnEarly(1)
                    SmaliType.INTEGER -> returnBoxedIntegerEarly(1)
                    SmaliType.STRING -> returnEarly("1")
                }
            }
        }
    }
}