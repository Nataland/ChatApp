package com.nataland.chatapp.picker

import androidx.annotation.DrawableRes
import com.nataland.chatapp.R

sealed class Cat(val name: String, @DrawableRes val pictureResId: Int) {
    data object Bobby : Cat("Bobby", R.drawable.bobby)
    data object Gisele : Cat("Gisele", R.drawable.gisele)
    data object Catniss : Cat("Catniss", R.drawable.catniss)
    data object Max : Cat("Max", R.drawable.max)
}
