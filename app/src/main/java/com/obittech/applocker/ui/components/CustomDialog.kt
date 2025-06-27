//package com.obittech.applocker.ui.components
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.AlertDialog
//import androidx.compose.material.Button
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Text
//import androidx.compose.material3.AlertDialog
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.SolidColor
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.muzammil.avr_agent_android.R
//import com.muzammil.avr_agent_android.ui.theme.colorBlack1
//import com.muzammil.avr_agent_android.ui.theme.colorGray2
//import com.muzammil.avr_agent_android.ui.theme.colorWhite
//import com.muzammil.avr_agent_android.ui.theme.colorYellow2
//import com.muzammil.avr_agent_android.ui.theme.colorYellowGradient1
//import com.muzammil.avr_agent_android.ui.view.update.screen.GifImage
//
//object CustomDialog {
//      @Composable
//      fun CustomPopupDialog(
//            dismissText:String=stringResource(id = R.string.string_OK),
//            text: String="",
//            onDismiss:()->Unit={}
//      ) {
//            AlertDialog(
//                  onDismissRequest = {  },  // Dismiss on background click
//                  title = null,  // No title
//                  text = {
//                        Column(
//                              horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                              Components.TextView(
//                                    text = text,
//                                    modifier = Modifier
//                                          .fillMaxWidth().padding(top = 5.dp),
//                                    textModifier = Modifier.fillMaxWidth(),
//                                    style = TextStyle(
//                                          color = colorBlack1,
//                                          fontSize = 18.sp,
//                                          fontFamily = FontFamily.Default,
//                                          fontWeight = FontWeight.Medium,
//                                    ),
//                                    textAlign = TextAlign.Center
//                              )
//                              Spacer(modifier = Modifier.height(10.dp))
//                              Components.Button(
//                                    modifier = Modifier.padding(top = 15.dp).fillMaxWidth()
//                                          .clip(RoundedCornerShape(10.dp))
//                                          .background(
//                                                brush = colorYellowGradient1,
//                                                shape = RoundedCornerShape(10.dp)
//                                          ),
//                                    textModifier = Modifier.padding(vertical = 12.dp),
//                                    text = dismissText,
//                                    onClick = {
//                                         onDismiss.invoke()
//                                    },
//                                    shape = RoundedCornerShape(10.dp),
//                                    backgroundBrush = colorYellowGradient1,
//                                    style = TextStyle(
//                                          color = colorWhite,
//                                          fontFamily = FontFamily.Default,
//                                          fontSize = 20.sp,
//                                          fontWeight = FontWeight.SemiBold
//                                    ),
//                              )
//                        }
//                  },
//                  confirmButton = {},
//                  dismissButton = {},
//                  modifier = Modifier
//                        .fillMaxWidth()
//            )
//      }
//      @Composable
//      fun CustomPopupWithTwoButtonDialog(
//            doneText:String="",
//            dismissText:String="",
//            title: String="",
//            text: String="",
//            onDismiss:()->Unit={},
//            onDoneClick:()->Unit={}
//      ) {
//            AlertDialog(
//                  onDismissRequest = {  },  // Dismiss on background click
//                  title = null,
//                  text = {
//                        Column(
//                              horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                              Components.TextView(
//                                    text = title,
//                                    modifier = Modifier
//                                          .fillMaxWidth().padding(top = 5.dp),
//                                    textModifier = Modifier.fillMaxWidth(),
//                                    style = TextStyle(
//                                          color = colorBlack1,
//                                          fontSize = 18.sp,
//                                          fontFamily = FontFamily.Default,
//                                          fontWeight = FontWeight.Bold,
//                                    ),
//                                    textAlign = TextAlign.Center
//                              )
//                              Components.TextView(
//                                    text = text,
//                                    modifier = Modifier
//                                          .fillMaxWidth().padding(top = 5.dp),
//                                    textModifier = Modifier.fillMaxWidth(),
//                                    style = TextStyle(
//                                          color = colorBlack1,
//                                          fontSize = 18.sp,
//                                          fontFamily = FontFamily.Default,
//                                          fontWeight = FontWeight.Normal,
//                                    ),
//                                    textAlign = TextAlign.Center
//                              )
//                              Spacer(modifier = Modifier.height(10.dp))
//                              Row{
//                                    Components.Button(
//                                          modifier = Modifier.padding(top = 5.dp, end = 5.dp).fillMaxWidth()
//                                                .clip(RoundedCornerShape(10.dp))
//                                                .background(
//                                                     color=colorGray2,
//                                                      shape = RoundedCornerShape(10.dp)
//                                                ).weight(1f),
//                                          textModifier = Modifier.padding(vertical = 12.dp),
//                                          text = dismissText,
//                                          onClick = {
//                                                onDismiss.invoke()
//                                          },
//                                          shape = RoundedCornerShape(10.dp),
//                                          style = TextStyle(
//                                                color = colorWhite,
//                                                fontFamily = FontFamily.Default,
//                                                fontSize = 20.sp,
//                                                fontWeight = FontWeight.SemiBold
//                                          ),
//                                    )
//                                    Components.Button(
//                                          modifier = Modifier.padding(top = 5.dp, start = 5.dp).fillMaxWidth()
//                                                .clip(RoundedCornerShape(10.dp))
//                                                .background(
//                                                      brush = colorYellowGradient1,
//                                                      shape = RoundedCornerShape(10.dp)
//                                                ).weight(1f),
//                                          textModifier = Modifier.padding(vertical = 12.dp),
//                                          text = doneText,
//                                          onClick = {
//                                                onDoneClick.invoke()
//                                          },
//                                          shape = RoundedCornerShape(10.dp),
//                                          backgroundBrush = colorYellowGradient1,
//                                          style = TextStyle(
//                                                color = colorWhite,
//                                                fontFamily = FontFamily.Default,
//                                                fontSize = 20.sp,
//                                                fontWeight = FontWeight.SemiBold
//                                          ),
//                                    )
//                              }
//
//                        }
//                  },
//                  confirmButton = {},
//                  dismissButton = {},
//                  modifier = Modifier
//                        .fillMaxWidth()
//            )
//      }
//
//
//      @Composable
//      fun CustomUpdatePopupDialog(
//            doneText:String="",
//            dismissText:String="",
//            title: String="",
//            text: String="",
//            isGifShown:Boolean=true,
//            onDismiss:()->Unit={},
//            onDoneClick:()->Unit={}
//      ) {
//            AlertDialog(
//                  onDismissRequest = {  },  // Dismiss on background click
//                  title = null,
//                  text = {
//                        Column(
//
//
//                              horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                              if(isGifShown) {
//                              Components.TextView(
//                                    text = "",
//                                    modifier = Modifier
//                                          .fillMaxWidth().padding(top = 5.dp),
//                                    textModifier = Modifier.fillMaxWidth(),
//                                    style = TextStyle(
//                                          color = colorYellow2,
//                                          fontSize = 18.sp,
//                                          fontFamily = FontFamily.Default,
//                                          fontWeight = FontWeight.Bold,
//                                    ),
//                                    textAlign = TextAlign.Center
//                              )
//
////                                    GifImage(
////                                          modifier = Modifier.size(100.dp),
////                                          drawable = R.drawable.downloading_anim
////                                    )
//                              }
//                              Components.TextView(
//                                    text = title,
//                                    modifier = Modifier
//                                          .fillMaxWidth().padding(top = 5.dp),
//                                    textModifier = Modifier.fillMaxWidth(),
//                                    style = TextStyle(
//                                          color = colorYellow2,
//                                          fontSize = 18.sp,
//                                          fontFamily = FontFamily.Default,
//                                          fontWeight = FontWeight.Bold,
//                                    ),
//                                    textAlign = TextAlign.Center
//                              )
//                              Components.TextView(
//                                    text = text,
//                                    modifier = Modifier
//                                          .fillMaxWidth().padding(top = 5.dp),
//                                    textModifier = Modifier.fillMaxWidth(),
//                                    style = TextStyle(
//                                          color = colorBlack1,
//                                          fontSize = 16.sp,
//                                          fontFamily = FontFamily.Default,
//                                          fontWeight = FontWeight.Normal,
//                                    ),
//                                    textAlign = TextAlign.Center
//                              )
//                              Spacer(modifier = Modifier.height(10.dp))
//                              Row{
//                                    Components.TextView(
//                                          text = dismissText,
//                                          modifier = Modifier.padding(top = 5.dp, start = 5.dp).fillMaxWidth()
//                                                .weight(1f),
//                                          textModifier = Modifier.padding(vertical =5.dp).fillMaxWidth(),
//                                          style = TextStyle(
//                                                color = colorBlack1,
//                                                fontFamily = FontFamily.Default,
//                                                fontSize = 18.sp,
//                                                fontWeight = FontWeight.Medium
//                                          ),
//                                          textAlign = TextAlign.Start,
//                                          isClickable = true,
//                                          onClick = {
//                                                onDismiss.invoke()
//                                          }
//                                    )
//
//                                    Components.TextView(
//                                          text = doneText,
//                                          modifier = Modifier.padding(top = 5.dp, start = 5.dp).fillMaxWidth()
//                                                .weight(2f),
//                                          textModifier = Modifier.padding(vertical =5.dp).fillMaxWidth(),
//                                          style = TextStyle(
//                                                color = colorYellow2,
//                                                fontFamily = FontFamily.Default,
//                                                fontSize = 20.sp,
//                                                fontWeight = FontWeight.SemiBold
//                                          ),
//                                          textAlign = TextAlign.End,
//                                          isClickable = true,
//                                          onClick = {
//                                                onDoneClick.invoke()
//                                          }
//                                    )
//                              }
//
//                        }
//                  },
//                  confirmButton = {},
//                  dismissButton = {},
//                  modifier = Modifier
//                        .fillMaxWidth()
//            )
//      }  @Composable
//      fun CustomUpdateMandatoryPopupDialog(
//            doneText:String="",
//            dismissText:String="",
//            title: String="",
//            text: String="",
//            onDismiss:()->Unit={},
//            onDoneClick:()->Unit={}
//      ) {
//            AlertDialog(
//                  onDismissRequest = {  },  // Dismiss on background click
//                  title = null,
//                  text = {
//                        Column(
//
//
//                              horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                              Components.TextView(
//                                    text = "",
//                                    modifier = Modifier
//                                          .fillMaxWidth().padding(top = 5.dp),
//                                    textModifier = Modifier.fillMaxWidth(),
//                                    style = TextStyle(
//                                          color = colorYellow2,
//                                          fontSize = 18.sp,
//                                          fontFamily = FontFamily.Default,
//                                          fontWeight = FontWeight.Bold,
//                                    ),
//                                    textAlign = TextAlign.Center
//                              )
//                              GifImage(modifier = Modifier.size(100.dp), drawable = R.drawable.downloading_anim)
//                              Components.TextView(
//                                    text = title,
//                                    modifier = Modifier
//                                          .fillMaxWidth().padding(top = 5.dp),
//                                    textModifier = Modifier.fillMaxWidth(),
//                                    style = TextStyle(
//                                          color = colorYellow2,
//                                          fontSize = 18.sp,
//                                          fontFamily = FontFamily.Default,
//                                          fontWeight = FontWeight.Bold,
//                                    ),
//                                    textAlign = TextAlign.Center
//                              )
//                              Components.TextView(
//                                    text = text,
//                                    modifier = Modifier
//                                          .fillMaxWidth().padding(top = 5.dp),
//                                    textModifier = Modifier.fillMaxWidth(),
//                                    style = TextStyle(
//                                          color = colorBlack1,
//                                          fontSize = 16.sp,
//                                          fontFamily = FontFamily.Default,
//                                          fontWeight = FontWeight.Normal,
//                                    ),
//                                    textAlign = TextAlign.Center
//                              )
//                              Spacer(modifier = Modifier.height(10.dp))
//                              Row{
//                                    Components.TextView(
//                                          text = "",
//                                          modifier = Modifier.padding(top = 5.dp, start = 5.dp).fillMaxWidth()
//                                                .weight(1f),
//                                          textModifier = Modifier.padding(vertical =5.dp).fillMaxWidth(),
//                                          style = TextStyle(
//                                                color = colorBlack1,
//                                                fontFamily = FontFamily.Default,
//                                                fontSize = 18.sp,
//                                                fontWeight = FontWeight.Medium
//                                          ),
//                                          textAlign = TextAlign.Start,
//                                          isClickable = true,
//                                          onClick = {
//                                                onDismiss.invoke()
//                                          }
//                                    )
//
//                                    Components.TextView(
//                                          text = doneText,
//                                          modifier = Modifier.padding(top = 5.dp, start = 5.dp).fillMaxWidth()
//                                                .weight(2f),
//                                          textModifier = Modifier.padding(vertical =5.dp).fillMaxWidth(),
//                                          style = TextStyle(
//                                                color = colorYellow2,
//                                                fontFamily = FontFamily.Default,
//                                                fontSize = 20.sp,
//                                                fontWeight = FontWeight.SemiBold
//                                          ),
//                                          textAlign = TextAlign.End,
//                                          isClickable = true,
//                                          onClick = {
//                                                onDoneClick.invoke()
//                                          }
//                                    )
//                              }
//
//                        }
//                  },
//                  confirmButton = {},
//                  dismissButton = {},
//                  modifier = Modifier
//                        .fillMaxWidth()
//            )
//      }
//
//
//
//}