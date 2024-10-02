package com.nataland.chatapp.picker

import javax.inject.Singleton
import kotlin.random.Random

@Singleton
object CatInfo {
    val allCats = listOf(Cat.Bobby, Cat.Gisele, Cat.Catniss, Cat.Max)
    private val catMap = hashMapOf(*allCats.map { it.name to it }.toTypedArray())
    private const val CAT_NAME = "CAT_NAME"

    // Todo: move cat actions to the backend.
    private val catActions = listOf(
        "$CAT_NAME stretches lazily and lets out a soft yawn.",
        "$CAT_NAME kneads a blanket before settling down for a nap.",
        "$CAT_NAME stares out the window.",
        "$CAT_NAME slow-blinks at you.",
        "$CAT_NAME suddenly bolts across the room for no apparent reason.",
        "$CAT_NAME lazily bats at a toy.",
        "$CAT_NAME doesn't seem to notice your existence.",
        "$CAT_NAME stretches lazily and lets out a soft yawn.",
        "$CAT_NAME doesn't seem to notice your existence.",
        "$CAT_NAME tilts their head.",
        "$CAT_NAME walks up to you, sniffing your hand cautiously.",
        "$CAT_NAME sits at your feet.",
        "$CAT_NAME stares up at you with wide, curious eyes.",
        "$CAT_NAME gently pats your leg with a paw.",
        "$CAT_NAME circles around you.",
        "$CAT_NAME brushes you with their tail.",
        "$CAT_NAME leans in to sniff your face.",
        "$CAT_NAME sits beside you, watching intently as you type on your keyboard."
    )

    fun String.toCat() = catMap[this]

    fun getRandomAction(catName: String) =
        catActions[Random.nextInt(0, catActions.lastIndex)].replace(
            CAT_NAME, catName
        )
}
