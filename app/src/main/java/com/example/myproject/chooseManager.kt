import android.content.Context
import com.example.myproject.Card
import com.example.myproject.dbCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object chooseManager {
    private val chosenCards = mutableListOf<Card>()
    var dbHelper: dbCard? = null

    fun initialize(context: Context) {
        dbHelper = dbCard(context)
        CoroutineScope(Dispatchers.IO).launch {
            loadCards()
        }
    }
    suspend fun loadCards() {
        dbHelper?.let {
            val cards = it.loadCards()
            chosenCards.clear()
            chosenCards.addAll(cards)
        }
    }
    suspend fun saveCards() {
        dbHelper?.let { db ->
            db.clearCards()
            for (card in chosenCards) {
                db.saveCard(card)
            }
        }
    }
}

