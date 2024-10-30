package com.jacobcastillam.cursofirebaselite.presentation.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import com.jacobcastillam.cursofirebaselite.R
import com.jacobcastillam.cursofirebaselite.presentation.model.Artist
import com.jacobcastillam.cursofirebaselite.presentation.model.Player
import com.jacobcastillam.cursofirebaselite.ui.theme.Black
import com.jacobcastillam.cursofirebaselite.ui.theme.Purple40


@Composable
fun HomeScreen(viewModel: HomeViewModel = HomeViewModel()) {
    val artist: State<List<Artist>> = viewModel.artist.collectAsState()
    val player by viewModel.player.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        Text(
            text = "PiPija's favourites",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            modifier = Modifier.padding(16.dp)
        )

        LazyRow {
            items(artist.value) {
                ArtistItem(artist = it, onItemSelected = { viewModel.addPlayer(it) })
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        player?.let {
            PlayerComponent(
                player = it,
                onCancelSelected = { viewModel.onCancelSelected() },
                onPLaySelected = { viewModel.onPlaySelected() })
        }
    }

}

@Composable
fun PlayerComponent(player: Player, onPLaySelected: () -> Unit, onCancelSelected: () -> Unit) {
    val icon = if (player.play == true) R.drawable.ic_pause else R.drawable.ic_play
    Row(
        Modifier
            .height(50.dp)
            .fillMaxWidth()
            .background(Purple40), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = player.artist?.name.orEmpty(),
            modifier = Modifier.padding(horizontal = 12.dp),
            color = Color.White
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = icon),
            contentDescription = "play/pause",
            modifier = Modifier
                .size(40.dp)
                .clickable { onPLaySelected() }
        )
        Image(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "Close",
            modifier = Modifier
                .size(40.dp)
                .clickable { onCancelSelected() }
        )
    }
}

@Composable
fun ArtistItem(artist: Artist, onItemSelected: (Artist) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(10.dp)
            .clickable { onItemSelected(artist) }) {
        Spacer(modifier = Modifier.height(4.dp))
        AsyncImage(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            model = artist.image,
            contentDescription = "Artists image",
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = artist.name.orEmpty(), color = Color.White)

    }
}

/*
data class Artist(val name: String, val numberOfSongs: Int)

fun createArtist(db: FirebaseFirestore) {
    val random = (1..10000).random()
    val artist = Artist(name = "Random $random", numberOfSongs = random)
    db.collection("artists").add(artist)
        .addOnSuccessListener { Log.i("Jacob", "SUCCESS") }
        .addOnFailureListener { Log.i("Jacob", "FAILURE") }
        .addOnCompleteListener { Log.i("Jacob", "COMPLETE") }
}*/