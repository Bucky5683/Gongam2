package com.cono.gongam.ui.register

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cono.gongam.R
import com.cono.gongam.ui.CircleTextButton
import com.cono.gongam.ui.main.MainActivity
import com.cono.gongam.ui.register.ui.theme.GongamTheme
import com.cono.gongam.utils.SharedPreferencesUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : ComponentActivity() {
//    private val userViewModel: UserViewModel by viewModels()
//    @Inject
//    lateinit var userViewModel: UserViewModel
    private lateinit var sharedPreferencesUtil : SharedPreferencesUtil
    private lateinit var imgSelectedInGalleryUrl: String

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri = handleGalleryResult(data)
            selectedImageUri?.let {
                imgSelectedInGalleryUrl = it.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferencesUtil = SharedPreferencesUtil(this)
        imgSelectedInGalleryUrl = sharedPreferencesUtil.getUser().profileImageURL ?: ""

//        val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        setContent {
            GongamTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.white)
                ) {
                    RegisterScreen(
                        nextBtnOnClick = {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        profileImgUrl = sharedPreferencesUtil.getUser().profileImageURL ?: "",
                        selectImage = { selectImage() },
                        imgSelectedInGalleryUrl = imgSelectedInGalleryUrl,
                        nickName = sharedPreferencesUtil.getUser().name ?: "",
                        email = sharedPreferencesUtil.getUser().email ?: ""
                    )
                }
            }
        }
    }

    private fun selectImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startForResult.launch(galleryIntent)
    }

    private fun handleGalleryResult(data: Intent?): Uri? {
        if (data == null || data.data == null) {
            Log.e("GalleryResult", "Invalid gallery result")
            return null
        }

        val selectedImageUri: Uri = data.data!!
        Log.d("GalleryResult", "Selected Image URI : $selectedImageUri")

//        imgSelectedInGalleryUrl = selectedImageUri.toString()

        return selectedImageUri
    }
}

@Composable
fun ProfileImageEdit(profileImgUrl: String, selectImage: () -> Unit, imgSelectedInGalleryUrl: String) {
    // TODO :: 이미지 shadow 적용
    // TODO :: 갤러리 result 이미지 반영
    var currentProfileImageUrl by remember { mutableStateOf(profileImgUrl) }


    Log.d("ProfileImage :: ", "url : $profileImgUrl")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 122.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AsyncImage(
            model = currentProfileImageUrl,
            placeholder = debugPlaceHolder(R.drawable.img_test_profile),
            contentDescription = "profile_Img",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable {
                    selectImage()
                    currentProfileImageUrl = imgSelectedInGalleryUrl
                }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        ) {
            Spacer(modifier = Modifier.weight(17f))
            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_edit_profile),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize()
                )
                Text(
                    text = "✏️",
                    fontSize = 15.sp,
                    fontWeight = FontWeight(700),
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 7.dp)
                )
            }
            Spacer(modifier = Modifier.weight(3f))
        }
    }
}

@Composable
fun debugPlaceHolder(@DrawableRes debugPreview: Int) =
    if (LocalInspectionMode.current) {
        painterResource(id = debugPreview)
    } else {
        null
    }

@Composable
fun RegisterScreen(nextBtnOnClick: () -> Unit, profileImgUrl: String = "", selectImage: () -> Unit, imgSelectedInGalleryUrl: String,
                   nickName: String, email: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            ProfileImageEdit(profileImgUrl = profileImgUrl, selectImage = selectImage, imgSelectedInGalleryUrl = imgSelectedInGalleryUrl)
            Spacer(modifier = Modifier.height(12.dp))
            InputTextTitle("닉네임")
            CustomTextField(onValueChange = {}, defaultText = nickName)
            InputTextTitle("이메일")
            CustomTextField(onValueChange = {}, defaultText = email)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Spacer(modifier = Modifier.weight(1f))
            CircleTextButton(buttonText = "Next", nextBtnOnClick = { nextBtnOnClick() }, buttonColor = colorResource(
                id = R.color.main_gray
            ))
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun InputTextTitle(title: String) {
    Text(
        text = title, fontSize = 15.sp, fontWeight = FontWeight(700), color = Color.Black,
        modifier = Modifier.padding(top = 11.dp, start = 38.dp)
    )
}

@Composable
fun CustomTextField(
    onValueChange: (String) -> Unit, defaultText: String
) {
    var text by remember { mutableStateOf(defaultText) }

    Column(
        modifier = Modifier.padding(vertical = 9.dp)
    ) {
        androidx.compose.material.TextField(
            value = text,
            onValueChange = {
                text = it
            },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 38.dp),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = colorResource(id = R.color.gray_scale1),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

// ------------------------------------ Previews ------------------------------------

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewRegisterScreen() {
    RegisterScreen( nextBtnOnClick = {}, selectImage = {}, imgSelectedInGalleryUrl = "", nickName = "nickname", email = "email")
}

@Preview
@Composable
fun PreviewNextButton() {
    CircleTextButton("Next", nextBtnOnClick = {}, buttonColor = colorResource(id = R.color.main_gray))
}