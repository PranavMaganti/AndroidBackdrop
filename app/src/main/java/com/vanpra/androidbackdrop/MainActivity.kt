package com.vanpra.androidbackdrop

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.Composable
import androidx.compose.Model
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.ui.core.*
import androidx.ui.foundation.VerticalScroller
import androidx.ui.layout.*
import androidx.ui.material.*
import androidx.ui.text.ParagraphStyle
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import com.google.android.material.snackbar.Snackbar
import com.vanpra.androidbackdrop.R
import com.vanpra.backdrop.BackdropButtonGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        backdrop.apply {
            title = "Home"
            /*
            setFrontViewComposable {
                Padding(left = 16.dp, right=16.dp) {
                    Column {
                        Text(
                            context.getString(R.string.welcome_text),
                            style = TextStyle(fontSize = 18.sp),
                            paragraphStyle = ParagraphStyle(TextAlign.Center)
                        )
                        HeightSpacer(16.dp)
                        Align(Alignment.TopCenter) {
                            Button("Welcome", onClick = {
                                Snackbar.make(backdrop,"Hello World", Snackbar.LENGTH_SHORT)
                                    .setAction("Dismiss") {

                                    }.show()
                            })
                        }



                    }
                }

            }

             */

            setFrontView(R.layout.button_layout)
            val btn = frontView.findViewById<Button>(R.id.test_button)
            btn.setOnClickListener {
                Snackbar.make(backdrop,"Hello World", Snackbar.LENGTH_SHORT)
                    .setAction("Dismiss"){}.show()
            }

            val buttonGroup = backView.findViewById<BackdropButtonGroup>(R.id.button_group)

            buttonGroup.addButton("Home", ContextCompat.getDrawable(context, R.drawable.home))
            buttonGroup.addButton("Artist", ContextCompat.getDrawable(context, R.drawable.artist))
            buttonGroup.setMenuItemClickListener {
                toggleBackdrop()
                title = it
            }
        }
        
    }
}
