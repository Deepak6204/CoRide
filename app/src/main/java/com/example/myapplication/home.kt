package com.example.myapplication

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.myapplication.utils.LocationUtils
import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.example.myapplication.helpers.getRides
import com.example.myapplication.models.Rides
import com.example.myapplication.viewmodel.LocationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.myapplication.utils.PostLocation

@Composable
fun home(navController: NavController, user: String, viewModel: LocationViewModel) {
    // Remember the state for the destination input
    var destination by remember { mutableStateOf("") }

//    LaunchedEffect(destination) {
//
//    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.light_gray)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // SearchField Composable
        Column(modifier = Modifier.background(colorResource(R.color.light_green))) {
            SearchField(destination = destination, onValueChange = { destination = it })
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.run {
                    height(200.dp)
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        .background(colorResource(R.color.light_green))
                }
            ){
                Text(text = "${stringResource(R.string.app_name)}", fontSize = 30.sp, color = colorResource(R.color.black), fontWeight = FontWeight.Bold)
                Text(text = "An Eco-Friendly and Economical solution to travel")
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation))
                val progress by animateLottieCompositionAsState(composition = composition, isPlaying = true)
                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier
                        .size(400.dp)
                        .padding(8.dp)
                )
            }
            if(destination == ""){
                Spacer(modifier = Modifier.height(30.dp))
                Text("Please enter your destination. ", fontSize = 20.sp)
            }
            else {
                Spacer(modifier = Modifier.height(20.dp))
                val rides = getRides()
                Log.i("Rides Deepak", rides.toString())
                if(rides.isNotEmpty()) {
                    Column {
                        for ((index, item) in rides.withIndex()) {
                            CustomCard(item, navController)
                            // Render each ride item here
                        }
                    }
                    Location(viewModel, destination)
                    Spacer(modifier = Modifier.height(20.dp))
                }
                else{
                    val context = LocalContext.current
                    val locationUtils = LocationUtils(context)

                    PostLocationButton(
                        context = context,
                        locationUtils = locationUtils,
                        viewModel = viewModel,
                        destination = destination,
                    ){
                        println("posted successfully")
                    }
                }
            }

        }
    }
}

@Composable
fun Location(viewModel: LocationViewModel, destination: String){
    val context = LocalContext.current
    val locationUtils = LocationUtils(context)
    DisplayLocation(locationUtils, viewModel, context, destination)
}

@Composable
fun DisplayLocation(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context: Context,
    destination: String
){
    val location = viewModel.location.value
    Column (modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if (location != null){
            Text("location, lat: ${location.latitude}, long: ${location.longitude}")
        }
        else {
            Text("Didn't found a perfect match? Post yourself.")
        }
        PostLocationButton(
            context = context,
            locationUtils = locationUtils,
            viewModel = viewModel,
            destination = destination,
        ){
            println("posted successfully")
        }
//        Button(onClick = {
//            if (locationUtils.hasLocationPermission(context)){
//                locationUtils.requestLocationUpdates(viewModel)
//            }
//            else{
//                reqPermissionLauncher.launch(
//                    arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    )
//                )
//            }
//            var rides = Rides(
//                latitude = location?.latitude,
//                longitude = location?.longitude,
//                destination = destination,
//                user = FirebaseAuth.getInstance().currentUser?.email,
//                uid = FirebaseAuth.getInstance().currentUser?.uid
//            )
//            PostLocation.postLocation(rides)
//
//        }) {
//            Text("Post")
//        }
    }
}

@Composable
fun PostLocationButton(
    context: Context,
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    destination: String,
    onPostSuccess: () -> Unit
) {
    val reqPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {permission ->
            if(permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true && permission[Manifest.permission.ACCESS_FINE_LOCATION] == true){
                locationUtils.requestLocationUpdates(viewModel=viewModel)
            }
            else{
                val rationalRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                if(rationalRequired){
                    Toast.makeText(context, "Location permission required", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(context, "Please enable Location permissions", Toast.LENGTH_SHORT).show()
                }
            }
        }
    )
    val location = viewModel.location.value
    Button(onClick = {
        // Check location permission
        if (locationUtils.hasLocationPermission(context)) {
            locationUtils.requestLocationUpdates(viewModel)
        } else {
            reqPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        // Create the Rides object and post location
        val rides = Rides(
            latitude = location?.latitude ?: 0.0,
            longitude = location?.longitude ?: 0.0,
            destination = destination,
            user = FirebaseAuth.getInstance().currentUser?.email ?: "",
            uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        )

        PostLocation.postLocation(rides)

        // Perform additional logic or callback on success
        onPostSuccess()
    }) {
        Text("Post Location")
    }
}


@Composable
fun SearchField(destination: String, onValueChange: (String) -> Unit) {


        TextField(
            value = destination,
            onValueChange = onValueChange,
            placeholder = {
                Text("Destination")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                focusedPlaceholderColor = colorResource(R.color.light_gray),
                unfocusedPlaceholderColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = colorResource(R.color.black),
                unfocusedTextColor = Color.Black,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .border(
                    1.dp,
                    color = colorResource(R.color.white),
                    shape = RoundedCornerShape(10.dp)
                ),

            textStyle = TextStyle(color = colorResource(R.color.black)),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = colorResource(R.color.dark_blue)
                )
            }
        )
}

@Composable
fun CustomCard(item: Rides, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(1.dp, colorResource(R.color.light_gray), shape = RoundedCornerShape(20.dp))
            .shadow(shape = RoundedCornerShape(20.dp), elevation = 13.dp,)
            .background(colorResource(R.color.forest_green)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.white)
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 13.dp)

    ){
        Log.e("Rides Deepak", item.toString())
        cardDetails(item, navController)
    }
}

@Composable
fun cardDetails(item : Rides, navController: NavController){
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White), // Placeholder for image background
                contentAlignment = Alignment.Center
            ) {
                // Use a placeholder image
                // Replace with actual Image when available
                Image(
                    painter = painterResource(id = R.drawable.defaultpfp), // Replace with image resource
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Name and Age
            Column {
                Text(
                    text = "email: ${item.user}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Text(
                    text = "Age",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Expected Time, Proposed Cost, Destination
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .background(colorResource(R.color.gray))
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
//                Text(
//                    text = "Expected Time",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.White,
//                    fontSize = 14.sp
//                )
//                Text(
//                    text = "Proposed Cost",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = Color.White,
//                    fontSize = 14.sp
//                )
                Text(
                    text = "Destination : ${item.destination}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Request Pool Button
        val userid = item.uid
        Button(
            onClick = {
                navController.navigate("chat/$userid")
            },
            modifier = Modifier
                .align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.light_green),
                contentColor = Color.White
            )
        ) {
            Text("Req Pool")
        }
    }
}
