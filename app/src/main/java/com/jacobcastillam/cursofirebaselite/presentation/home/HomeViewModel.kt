package com.jacobcastillam.cursofirebaselite.presentation.home

import android.util.Log
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jacobcastillam.cursofirebaselite.presentation.model.Artist
import com.jacobcastillam.cursofirebaselite.presentation.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val database = Firebase.database
    private var db: FirebaseFirestore = com.google.firebase.ktx.Firebase.firestore

    private val _artist: MutableStateFlow<List<Artist>> =
        MutableStateFlow<List<Artist>>(emptyList())
    val artist: StateFlow<List<Artist>> = _artist

    private val _player = MutableStateFlow<Player?>(null)
    val player: StateFlow<Player?> = _player

    init {
        getArtists()
        getPlayer()
    }

    private fun getPlayer() {
        viewModelScope.launch {
            collectPLayer().collect {
                var player = it.getValue(Player::class.java)
                _player.value = player

            }
        }
    }

    private fun getArtists() {
        viewModelScope.launch() {
            val result: List<Artist> = withContext(Dispatchers.IO) {
                getAllArtists()
            }
            _artist.value = result

        }
    }

    suspend fun getAllArtists(): List<Artist> {
        return try {
            db.collection("artists")
                .get()
                .await()
                .documents
                .mapNotNull { snapshot ->
                    snapshot.toObject(Artist::class.java)
                }

        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun collectPLayer(): Flow<DataSnapshot> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Jacob log", "Error: ${error.message}")
                close(error.toException())
            }
        }

        val ref = database.reference.child("player")
        ref.addValueEventListener(listener)

        awaitClose {
            ref.removeEventListener(listener)
        }
    }

    fun onPlaySelected() {
        if (player.value != null) {
            val currentPLayer = _player.value?.copy(play = !player.value?.play!!)
            val ref = database.reference.child("player")
            ref.setValue(currentPLayer)
        }
    }

    fun onCancelSelected() {
        val ref = database.reference.child("player")
        ref.setValue(null)
    }

    fun addPlayer(artist: Artist) {
        val ref = database.reference.child("player")
        val player = Player(artist = artist, play = true)
        ref.setValue(player)
    }
}