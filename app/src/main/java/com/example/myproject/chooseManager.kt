import android.content.Context
import com.example.myproject.Card
import com.example.myproject.dbCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object chooseManager {
    private val chosenCards = mutableListOf<Card>()
    var dbHelper: dbCard? = null
    private var currentUserId: Int? = null


    fun initialize(context: Context, userId: Int) {
        dbHelper = dbCard(context)
        CoroutineScope(Dispatchers.IO).launch {
            loadCards(userId)
        }
    }

    suspend fun loadCards(userId: Int) {
        dbHelper?.let {
            val cards = it.loadCards(userId)
            chosenCards.clear()
            chosenCards.addAll(cards)
        }
    }

    suspend fun saveCards() {
        currentUserId?.let { userId ->
            dbHelper?.let { db ->
                db.clearCards(userId)
                for (card in chosenCards) {
                    db.saveCard(card, userId)
                }
            }
        }
    }
}


