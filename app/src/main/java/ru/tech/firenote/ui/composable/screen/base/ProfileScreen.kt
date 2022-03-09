package ru.tech.firenote.ui.composable.screen.base

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material.icons.twotone.Password
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.tech.firenote.R
import ru.tech.firenote.model.UIState
import ru.tech.firenote.ui.composable.single.Toast
import ru.tech.firenote.viewModel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateProfile(uri)
    }

    Box(Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.TwoTone.Email, null)
                Spacer(Modifier.size(20.dp))
                Text(
                    text = viewModel.email,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    modifier = Modifier.padding(start = 20.dp),
                    onClick = { viewModel.sendResetPasswordLink() }) {
                    Text(
                        text = stringResource(R.string.change),
                        color = Color.Gray
                    )
                }
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.TwoTone.Password, null)
                Spacer(Modifier.size(20.dp))
                Text(
                    text = "•••••••••••••",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                TextButton(modifier = Modifier.padding(start = 20.dp), onClick = { /*TODO*/ }) {
                    Text(
                        text = stringResource(R.string.reset),
                        color = Color.Gray
                    )
                }
            }

            Box(Modifier.size((screenWidth * 0.4).dp)) {
                val alpha = rememberSaveable { mutableStateOf(0f) }
                when (val state = viewModel.photoState.collectAsState().value) {
                    is UIState.Loading -> alpha.value = 1f
                    is UIState.Success<*> -> {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(state.data as Uri?)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(30)),
                            contentScale = ContentScale.Crop,
                            onLoading = {
                                alpha.value = 1f
                            },
                            onSuccess = {
                                alpha.value = 0f
                            },
                            onError = {
                                alpha.value = 0f
                            }
                        )
                    }
                    is UIState.Empty -> {
                        state.message?.let { Toast(it) }
                        Icon(Icons.Default.AccountCircle, null, Modifier.fillMaxSize())
                        alpha.value = 0f
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(alpha.value),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            Button(onClick = { launcher.launch("image/*") }) {
                Text(stringResource(R.string.pickImage))
            }

        }
    }

}