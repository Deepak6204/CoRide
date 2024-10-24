import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.myapplication.R

data class TutorialData(val title: String, val subtitle: String, val imageResId: Int)

@Composable
fun TutorialScreen(navController: NavController) {
    // List of tutorial steps
    val tutorialSteps = listOf(
        TutorialData("Seamless Navigation For Every Trip",
            "Lorem Ipsum is simply dummy text of the Lorem Ipsum has been the industry's",
            R.drawable.carpool_1),
        TutorialData("Plan Your Journey",
            "Easily plan your trip with our intuitive interface.",
            R.drawable.img4),
        TutorialData("Enjoy the Ride",
            "Sit back and relax while we handle the rest.",
            R.drawable.google)
    )

    // State to hold the current tutorial index
    var currentStep by remember { mutableStateOf(0) }

    // Get current tutorial data
    val currentData = tutorialSteps[currentStep]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Display the current tutorial image
        Image(painter = painterResource(id = currentData.imageResId), contentDescription = "Tutorial Image")

        // Title Text
        Text(
            text = currentData.title,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        // Subtitle Text
        Text(
            text = currentData.subtitle,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Bottom section
        BottomSection(currentStep, navController = navController, totalSteps = tutorialSteps.size) {
            // Update the current step when the next button is clicked
            if (currentStep < tutorialSteps.size - 1) {
                currentStep++
            } else {
                navController.navigate("home")
            }
        }
    }
}

@Composable
fun BottomSection(currentStep: Int, totalSteps: Int,navController: NavController, onNextClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Skip Button
        Button(onClick = { navController.navigate("home") },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text("Skip",color = Color.Gray,
                fontSize = 16.sp
            )
        }

        // Page Indicator
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)
        ) {
            repeat(totalSteps) { index ->
                CircleIndicator(isSelected = index == currentStep)
            }
        }

        // Next Button
        Button(
            onClick = onNextClick,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853)) // Green color
        ) {
            Text(text = "Next", color = Color.White)
        }
    }
}

@Composable
fun CircleIndicator(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(if (isSelected) 12.dp else 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = if (isSelected) Color(0xFF00C853) else Color.Gray,
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(if (isSelected) 12.dp else 8.dp)
        ) {}
    }
}
