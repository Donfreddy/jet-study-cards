package com.freddydev.studycards.deckpart3.models

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import com.freddydev.studycards.common.paddingOffset
import com.freddydev.studycards.deckpart3.CardSwipeState
import com.freddydev.studycards.deckpart3.animations.CardSwipeAnimation
import com.freddydev.studycards.deckpart3.animations.CardsInDeckAnimation
import com.freddydev.studycards.deckpart3.animations.FlipCardAnimation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class StudyCardDeckEvents(
  val cardWidth: Float,
  val cardHeight: Float,
  val model: StudyCardDeckModel,
  val peepHandler: () -> Unit,
  val playHandler: (String, String) -> Unit,
  val nextHandler: () -> Unit,
  val actionCallback: (String) -> Unit = {}
) {
  val cardSwipe: CardSwipeAnimation = CardSwipeAnimation(
    model = model,
    cardWidth = cardWidth,
    cardHeight = cardHeight
  )
  val flipCard = FlipCardAnimation(peepHandler)
  val cardsInDeck = CardsInDeckAnimation(paddingOffset, model.count)

  @SuppressLint("ModifierFactoryExtensionFunction")
  fun makeCardModifier(
    coroutineScope: CoroutineScope,
    topCardIndex: Int,
    idx: Int
  ): Modifier {
    return if (idx > topCardIndex) {
      Modifier
        .scale(cardsInDeck.scaleX(idx), 1f)
        .offset { cardsInDeck.offset(idx) }
    } else {
      Modifier
        .scale(flipCard.scaleX(), flipCard.scaleY())
        .offset { cardSwipe.toIntOffset() }
        .pointerInput(Unit) {
          detectDragGestures(
            onDragEnd = {
              cardSwipe.animateToTarget(
                coroutineScope,
                CardSwipeState.DRAGGING
              ) {
                if (it) {
                  nextHandler()
                  coroutineScope.launch {
                    flipCard.backToInitState()
                  }

                }
                cardsInDeck.backToInitState()
              }
            },
            onDrag = { change, _ ->
              cardSwipe.draggingCard(coroutineScope, change) {
                cardsInDeck.pushBackToTheFront()
              }
            }
          )
        }
    }
  }
}