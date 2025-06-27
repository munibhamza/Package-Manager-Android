//package com.obittech.applocker.ui.components
//
//import android.annotation.SuppressLint
//import android.app.ProgressDialog
//import android.content.Context
//import android.graphics.ImageDecoder
//import android.graphics.drawable.Animatable2
//import android.graphics.drawable.AnimatedImageDrawable
//import android.graphics.drawable.Drawable
//import android.graphics.drawable.Icon
//import android.net.Uri
//import android.os.Build
//import android.os.CountDownTimer
//import android.util.Log
//import androidx.annotation.DrawableRes
//import androidx.annotation.RequiresApi
//import androidx.compose.animation.animateColorAsState
//import androidx.compose.animation.core.FastOutLinearInEasing
//import androidx.compose.animation.core.LinearOutSlowInEasing
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.animateDp
//import androidx.compose.animation.core.keyframes
//import androidx.compose.animation.core.spring
//import androidx.compose.animation.core.updateTransition
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.detectTransformGestures
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.interaction.collectIsFocusedAsState
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.wrapContentSize
//import androidx.compose.foundation.layout.wrapContentWidth
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.ClickableText
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material.icons.filled.ArrowDropUp
//import androidx.compose.material.icons.filled.KeyboardArrowDown
//import androidx.compose.material.icons.filled.KeyboardArrowUp
//import androidx.compose.material.icons.rounded.Cancel
//import androidx.compose.material.icons.rounded.Visibility
//import androidx.compose.material.icons.rounded.VisibilityOff
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.IconToggleButton
//import androidx.compose.material3.LocalTextStyle
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.focus.FocusState
//import androidx.compose.ui.focus.focusRequester
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.graphics.SolidColor
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.layout.onGloballyPositioned
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextRange
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.text.input.VisualTransformation
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import coil.ImageLoader
//import coil.compose.AsyncImage
//import coil.compose.rememberAsyncImagePainter
//import coil.compose.rememberImagePainter
//import coil.decode.ImageDecoderDecoder
//import coil.request.ImageRequest
//import coil.request.ImageResult
//import com.muzammil.avr_agent_android.R
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import coil.size.Size
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//
//import androidx.compose.runtime.*
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.input.pointer.pointerInput
//
//import androidx.compose.ui.platform.LocalContext
//import com.google.accompanist.drawablepainter.rememberDrawablePainter
//
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.pluralStringResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.core.content.ContextCompat
//import com.google.accompanist.drawablepainter.rememberDrawablePainter
//import com.muzammil.avr_agent_android.preferences.UserPreferences
//import com.muzammil.avr_agent_android.ui.theme.colorGray1
//import com.muzammil.avr_agent_android.ui.theme.colorGray2
//import com.muzammil.avr_agent_android.ui.theme.colorGray9
//import com.muzammil.avr_agent_android.ui.theme.colorPrimary
//import com.muzammil.avr_agent_android.ui.theme.colorWhite
//import com.muzammil.avr_agent_android.ui.theme.colorYellow1
//import com.muzammil.avr_agent_android.utils.Constants
//import com.muzammil.avr_agent_android.utils.ProgressDialog
//
//
//object Components {
//      @Composable
//      fun ApiProgressAndDialog(){
//            ProgressDialog(Constants.isProgressDialogShow.value)
//            if(Constants.isDialogShow.value){
//                  CustomDialog.CustomPopupDialog(
//                        dismissText = Constants.dialogDismissText,
//                        text = Constants.dialogText,
//                        onDismiss = {
//                              Constants.isDialogShow.value=false
//                        }
//                  )
//            }
//      }
//      @Composable
//      fun twoButtonDialog(onDoneClick:()->Unit={}){
//            if(Constants.isTwoButtonDialogShow.value){
//                  CustomDialog.CustomPopupWithTwoButtonDialog(
//                        doneText = Constants.dialogDoneText,
//                        dismissText =Constants.dialogDismissText,
//                        title=Constants.dialogTitle,
//                        text = Constants.twoButtonDialogText,
//                        onDismiss = {
//                              Constants.isTwoButtonDialogShow.value=false
//                        },
//                        onDoneClick={onDoneClick.invoke()}
//                  )
//            }
//      }
//      @Composable
//      fun HeaderView(
//            userPreferences: UserPreferences,
//            onNotificationClick:()->Unit,
//            onSettingClick:()->Unit,
//
//
//      ){
//            Column {
//                  Row(
//                        modifier = Modifier.padding(
//                             top = 20.dp, bottom = 15.dp, start = 15.dp, end = 15.dp
//                        ),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                        verticalAlignment = Alignment.CenterVertically
//                  ) {
//                        Components.CircularImageWithStroke(
//                              modifier = Modifier
//                                    .size(50.dp)
//                                    .clip(CircleShape)
//                                    .background(color = colorGray9)
//                                    .border(2.dp, colorGray9, CircleShape),
//                              imgModifier = Modifier,
//                              imageUrl = null,
//                              placeholderResId = R.drawable.ic_profile_placeholder
//                        )
//
//                        Column(
//                              modifier = Modifier.weight(2f).padding(start = 5.dp),
//                              verticalArrangement = Arrangement.Center
//                        ) {
//                              Components.TextView(
//                                    text = userPreferences.userDetail.name,
//                                    style = TextStyle(
//                                          fontSize = 18.sp,
//                                          fontFamily = FontFamily.Default,
//                                          fontWeight = FontWeight.Medium,
//                                          color = colorWhite
//                                    ),
//                                    maxLines = 1,
//                                    modifier = Modifier.fillMaxWidth(),
//                                    textModifier = Modifier.fillMaxWidth(),
//                                    textAlign = TextAlign.Start,
//                                    subtitle = userPreferences.userDetail.companyName,
//                                    subtitleModifier = Modifier
//                                          .fillMaxWidth()
//                                          .padding(top = 5.dp),
//                                    subtitleStyle = TextStyle(
//                                          fontSize = 15.sp,
//                                          fontFamily = FontFamily.Default,
//                                          color = colorWhite
//                                    ),
//                              )
//                        }
//                        NotificationIcon(
//                              modifier = Modifier
//                                    .padding(horizontal = 5.dp)
//                                    .size(35.dp)
//                                    .clip(CircleShape)
//                                    .background(color = colorGray9),
//                              imgModifier = Modifier.size(25.dp),
//                              placeholderResId = R.drawable.ic_notification,
//                        )
//                        Components.CircularImageWithStroke(
//                              modifier = Modifier
//                                    .clickable { onSettingClick.invoke() }
//                                    .size(35.dp)
//                                    .clip(CircleShape)
//                                    .background(color = colorGray9)
//                                    .border(2.dp, colorGray9, CircleShape),
//                              imageUrl = null,
//                              imgModifier = Modifier.padding(4.dp),
//                              placeholderResId = R.drawable.ic_setting
//                        )
//                  }
//
//
//
//                  Components.TextView(
//                        text = stringResource(id = R.string.string_AVR_TRAVEL_AND_TOURISM_LLC),
//                        style = TextStyle(
//                              fontSize = 18.sp,
//                              fontFamily = FontFamily.Default,
//                              fontWeight = FontWeight.SemiBold,
//                              color = colorWhite
//                        ),
//                        maxLines = 1,
//                        modifier = Modifier
//                              .fillMaxWidth()
//                              .padding(horizontal = 5.dp),
//                        textModifier = Modifier.fillMaxWidth(),
//
//                        )
//
//                        Spacer(
//                              modifier = Modifier
//                                    .fillMaxWidth().padding(horizontal = 10.dp, vertical = 5.dp)
//                                    .height(1.5.dp)
//                                    .background(colorWhite)
//                        )
//            }
//      }
//      fun getTintedDrawable(context: Context,drawableResId: Int, color: Color): Drawable? {
//            val drawable = ContextCompat.getDrawable(context, drawableResId)
//            drawable?.mutate()?.setTint(color.toArgb()) // Convert Compose Color to Android Color
//            return drawable
//      }
//      @Composable
//      fun ToolBar(
//            modifier: Modifier=Modifier,
//            textStyle: TextStyle= TextStyle(
//                  color = colorWhite,
//                  fontSize = 20.sp,
//                  fontFamily = FontFamily.Default,
//                  fontWeight = FontWeight.Medium,
//            ),
//            backTextStyle: TextStyle=TextStyle(
//                  color = colorWhite,
//                  fontSize = 18.sp,
//                  fontFamily = FontFamily.Default,
//                  fontWeight = FontWeight.Normal,
//            ),
//            backIconColor: Color= colorWhite,
//            isBackButtonShow:Boolean=true,
//            isOnlyBackButton:Boolean=false,
//            backButtonString:String,
//            screenTitleString:String,
//            onBackClicked:()->Unit
//      ){
//            val context= LocalContext.current
//
//            // Function to get tinted drawable
//
//            Box(
//                  modifier = modifier.fillMaxWidth()
//            ){
//                  if(isBackButtonShow) {
//                        if(isOnlyBackButton){
//                              AsyncImage(
//                                    model = ImageRequest.Builder(context)
//                                          .data("")
//                                          .crossfade(true)
//                                          .placeholder(getTintedDrawable(context,R.drawable.ic_round_back, backIconColor))
//                                          .error(getTintedDrawable(context,R.drawable.ic_round_back, backIconColor))
//                                          .build(),
//                                    modifier = Modifier
//                                          .size(30.dp).clickable { onBackClicked.invoke() },
//
//                                    contentScale = ContentScale.Fit,
//                                    contentDescription = "Circular image",
//                              )
//                        }
//                        else{
//                              Components.TextView(
//                                    text = backButtonString,
//                                    modifier = Modifier
//                                          .wrapContentWidth(),
//                                    textModifier = Modifier.wrapContentSize().padding(vertical = 10.dp),
//                                    style =backTextStyle,
//                                    startIconResId = R.drawable.ic_arrow_left,
//                                    startIconTint = backIconColor,
//                                    startIconModifierSize = 25.dp,
//                                    isClickable = true,
//                                    isStartIconClickable = true,
//                                    onStartIconClick = { onBackClicked.invoke() },
//                                    onClick = { onBackClicked.invoke() }
//                              )
//                        }
//
//
//                  }
//                  Components.TextView(
//                        text = screenTitleString,
//                        modifier = Modifier
//                              .fillMaxWidth().padding(vertical = 2.5.dp),
//                        textModifier = Modifier.fillMaxWidth(),
//                        style =textStyle,
//                        textAlign = TextAlign.Center
//
//                  )
//
//            }
//      }
//      @Composable
//      fun SpannableText(
//            fullText: String,
//            spanWords: List<String>,
//            normalStyle: TextStyle = TextStyle.Default,
//            clickableStyle: TextStyle = TextStyle.Default.copy(color = Color.Blue),
//            modifier: Modifier = Modifier,
//            onWordClick: (String) -> Unit
//      ) {
//            val annotatedString = buildAnnotatedString {
//                  append(fullText)
//
//                  spanWords.forEach { word ->
//                        val startIndex = fullText.indexOf(word)
//                        if (startIndex >= 0) {
//                              val endIndex = startIndex + word.length
//                              addStyle(
//                                    style = SpanStyle(
//                                          color = clickableStyle.color,
//                                          fontSize = clickableStyle.fontSize,
//                                          fontFamily = clickableStyle.fontFamily,
//                                          fontWeight =clickableStyle.fontWeight,
//                                          textDecoration = clickableStyle.textDecoration),
//                                    start = startIndex,
//                                    end = endIndex
//                              )
//                              addStringAnnotation(
//                                    tag = "CLICKABLE",
//                                    annotation = word,
//                                    start = startIndex,
//                                    end = endIndex
//                              )
//                        }
//                  }
//            }
//
//            ClickableText(
//                  text = annotatedString,
//                  style = normalStyle,
//                  modifier = modifier,
//                  onClick = { offset ->
//                        annotatedString.getStringAnnotations(tag = "CLICKABLE", start = offset, end = offset)
//                              .firstOrNull()?.let { annotation ->
//                                    onWordClick(annotation.item)
//                              }
//                  }
//            )
//      }
//
//
//      @SuppressLint("UnrememberedMutableInteractionSource")
//      @Composable
//      fun TextView(
//            text: String,
//            style: TextStyle = TextStyle.Default,
//            endTextStyle: TextStyle = TextStyle.Default,
//            modifier: Modifier = Modifier,
//            textAlign: TextAlign = TextAlign.Start,
//            textModifier: Modifier = Modifier,
//            iconModifier: Modifier = Modifier,
//            startIconModifierSize: Dp = 35.dp,
//            startIconModifierPaddingEnd: Dp = 1.dp,
//            endIconModifierPaddingStart: Dp = 1.dp,
//            endIconModifierSize: Dp = 35.dp,
//            backgroundColor: Color = Color.Transparent,
//            cornerRadius: Dp = 0.dp,
//            startIconResId: Int? = null,
//            endIconResId: Int? = null,
//            endText: String? = null,
//            startIconTint: Color = Color.Unspecified,
//            endIconTint: Color = Color.Unspecified,
//            endTextModifier: Modifier = Modifier,
//            isClickable: Boolean = false,
//            isStartIconClickable: Boolean = false,
//            subtitle: String? = null,
//            subtitleStyle: TextStyle = TextStyle.Default,
//            subtitleModifier: Modifier = Modifier,
//            isMaxWidth: Boolean = false,
//            onClick: () -> Unit = {},
//            onStartIconClick: () -> Unit = {},
//            isEndIconClickable: Boolean = false,
//            onEndIconClick: () -> Unit = {},
//            hasError: Boolean = false,
//            maxLines: Int = Int.MAX_VALUE,
//            errorBorderColor: Color = Color.Red,
//            borderColor: Color = Color.Transparent,
//            borderWidth: Dp = 1.dp,
//            isRequire: Boolean = false,
//      ) {
//            val borderModifier = if (hasError) {
//                  modifier.border(borderWidth, errorBorderColor, RoundedCornerShape(cornerRadius))
//            } else {
//                  modifier.border(borderWidth, borderColor, RoundedCornerShape(cornerRadius))
//            }
//
//            // Box should not fill the max width
//            Box(
//                        modifier = if (isClickable) {
//                              borderModifier
//                              .background(
//                                    color = backgroundColor,
//                                    shape = RoundedCornerShape(cornerRadius)
//                              )
//                              .clip(RoundedCornerShape(cornerRadius))
//                              .clickable(enabled = isClickable, onClick = onClick, indication = null, interactionSource = androidx.compose.foundation.interaction.MutableInteractionSource())
//                  }
//                  else{
//                              borderModifier
//                              .background(
//                                    color = backgroundColor,
//                                    shape = RoundedCornerShape(cornerRadius)
//                              )
//                              .clip(RoundedCornerShape(cornerRadius))
//                  }
//
//            ) {
//                  Row(
//                        verticalAlignment = Alignment.CenterVertically
//                  ) {
//                        // Start Icon
//                        if (startIconResId != null) {
//                              Icon(
//                                    painter = painterResource(id = startIconResId),
//                                    contentDescription = null,
//                                    modifier = iconModifier
//                                          .size(startIconModifierSize)
//                                          .padding(end = startIconModifierPaddingEnd)
//                                          .clickable(enabled = isStartIconClickable, onClick = onStartIconClick),
//                                    tint = if (startIconTint != Color.Unspecified) startIconTint else Color.Unspecified
//                              )
//                        }
//
//                        // Text Column, remove weight to wrap content
//                        Column(
//                              verticalArrangement = Arrangement.Center,
//                              modifier = if (isMaxWidth) {
//                                    modifier
//                                          .weight(1f)
//                                          .align(Alignment.CenterVertically) // Use weight dynamically based on flag
//                              } else {
//                                    modifier.align(Alignment.CenterVertically)
//                              }
//                        ) {
//                              var textWidth by remember { mutableStateOf(0) }
//                              Row(
//                                    verticalAlignment = Alignment.CenterVertically
//                              ) {
//                              Text(
//                                    text = text,
//                                    style = style,
//                                    textAlign = textAlign,
//                                    modifier = textModifier
//                                          .onGloballyPositioned { coordinates ->
//                                                textWidth = coordinates.size.width
//                                          },
//                                    maxLines = maxLines,
//                                    overflow = TextOverflow.Ellipsis,
//                              )
//                                    if (isRequire) {
//                                          Text(
//                                                text = "*",
//                                                style = style.copy(color = Color(0xFFFF0000)),
//                                                textAlign = textAlign,
//                                                modifier = Modifier.padding(start = 2.dp)
//                                          )
//                                    }
//                              }
//
//                              if (subtitle != null) {
//                                    Text(
//                                          text = subtitle,
//                                          style = subtitleStyle,
//                                          modifier = subtitleModifier,
//                                          maxLines = maxLines,
//                                          overflow = TextOverflow.Ellipsis,
//                                    )
//                              }
//                        }
//
//                        // End Icon or Text, no width filling
//                        if (endIconResId != null) {
//                              Icon(
//                                    painter = painterResource(id = endIconResId),
//                                    contentDescription = null,
//                                    modifier = iconModifier
//                                          .size(endIconModifierSize)
//                                          .padding(start = endIconModifierPaddingStart)
//                                          .clickable(enabled = isEndIconClickable, onClick = onEndIconClick),
//                                    tint = if (endIconTint != Color.Unspecified) endIconTint else Color.Unspecified
//                              )
//                        } else if (endText != null) {
//                              Text(
//                                    text = endText,
//                                    style = endTextStyle,
//                                    textAlign = textAlign,
//                                    modifier = endTextModifier
//                                          .padding(start = 8.dp)
//                                          .clickable(enabled = isEndIconClickable, onClick = onEndIconClick),
//                              )
//                        }
//                  }
//            }
//      }
//
//
//
//
//      @Composable
//      fun TextField(
//            value: MutableState<String>,
//            onValueChange: (String) -> Unit = {},
//            modifier: Modifier = Modifier,
//            label: String? = null,
//            placeholder: String? = null,
//            isError: Boolean = false,
//            isErrorRequire: Boolean = true,
//            errorMessage: String? = null,
//            visualTransformation: VisualTransformation = VisualTransformation.None,
//            singleLine: Boolean = true,
//            backgroundColor: Color = Color.Transparent,
//            shape: Shape = MaterialTheme.shapes.small,
//            focusedBorderColor: Color = Color.Unspecified,
//            unfocusedBorderColor: Color = Color.Gray,
//            errorBorderColor: Color = Color.Red,
//            borderWidth: Dp = 1.dp,
//            textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
//            placeholderColor: Color = Color.Gray,
//            maxLength: Int = Int.MAX_VALUE,
//            keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//            cursorBrush: Color = colorPrimary,
//            isIcon: Boolean = false,
//            keyboardActions: KeyboardActions = KeyboardActions.Default,
//            focusRequester: FocusRequester = FocusRequester(),
//            onFocusChanged: (FocusState) -> Unit = {}
//
//      ) {
//            var passwordVisibility by remember { mutableStateOf(false) }
//            val isPasswordType = keyboardOptions.keyboardType == KeyboardType.Password
//            val effectiveVisualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
//
//            val interactionSource = remember { MutableInteractionSource() }
//            val isFocused by interactionSource.collectIsFocusedAsState()
//
//            val borderColor = when {
//                  isError -> errorBorderColor
//                  isFocused -> focusedBorderColor
//                  else -> unfocusedBorderColor
//            }
//
//            Column(modifier = modifier) {
//                  Box(
//                        modifier = Modifier
//                              .background(color = backgroundColor, shape = shape)
//                              .border(
//                                    BorderStroke(
//                                          width = borderWidth,
//                                          brush = SolidColor(borderColor)
//                                    ),
//                                    shape = shape
//                              )
//                              .padding(start = 5.dp, top = 5.dp, bottom = 5.dp)
//                  ) {
//                        if (label != null && !isFocused && value.value.isEmpty()) {
//                              Text(
//                                    text = label,
//                                    maxLines = 1,
//                                    style = textStyle.copy(color = placeholderColor),
//                                    modifier = Modifier.padding(start = 6.dp, top = 8.dp)
//                              )
//                        }
//                        if ( !isFocused) {
//                              placeholder?.let {
//                                    Text(
//                                          text = it,
//                                          style = textStyle.copy(color = placeholderColor)
//                                    )
//                              }
//                        }
//
//                        BasicTextField(
//                              value = value.value,
//                              onValueChange = {
//                                    if (it.length <= maxLength) {
//                                          value.value = it
//                                          onValueChange.invoke(it)
//                                    }
//                              },
//                              modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = if(isPasswordType){70.dp}else{40.dp})
//                                    .focusRequester(focusRequester)
//                                    .onFocusChanged { focusState ->
//                                          onFocusChanged(focusState)
//                                    },
//                              textStyle = textStyle,
//                              singleLine = singleLine,
//                              visualTransformation = if (isPasswordType) effectiveVisualTransformation else visualTransformation,
//                              keyboardOptions = keyboardOptions,
//                              keyboardActions = keyboardActions,
//                              interactionSource = interactionSource,
//                              cursorBrush = SolidColor(cursorBrush)
//                        )
//
//                        if (isIcon && isFocused) {
//                              Box(
//                                    modifier = Modifier
//                                          .height(20.dp).padding(end = 5.dp)
//                                          .align(Alignment.CenterEnd)
//                              ) {
//                                    Row( modifier = Modifier.align(Alignment.Center)){
//                                          IconButton(
//                                                onClick = {
//                                                      value.value = ""
//                                                },
//                                                modifier = Modifier.width(25.dp)
//                                          ) {
//                                                Icon(
//                                                      imageVector =
//                                                      Icons.Rounded.Cancel
//                                                      ,
//                                                      contentDescription = "Toggle Password Visibility",
//                                                      tint = cursorBrush
//                                                )
//                                          }
//                                          if (isPasswordType) {
//
//                                                      IconButton(
//                                                            onClick = {
//
//                                                                  passwordVisibility = !passwordVisibility
//
//                                                            },
//                                                            modifier = Modifier.width(30.dp).padding(start = 5.dp)
//                                                      ) {
//                                                            Icon(
//                                                                  imageVector =
//                                                                  if (passwordVisibility) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff
//                                                                  ,
//                                                                  contentDescription = "Toggle Password Visibility",
//                                                                  tint = cursorBrush
//                                                            )
//                                                      }
//                                          }
//                              }
//
//
//                              }
//
//                        }
//
//
//                  }
//                  if(isErrorRequire) {
//
//                        if (isError && errorMessage != null) {
//                              Text(
//                                    text = errorMessage,
//                                    style = textStyle.copy(
//                                          color = errorBorderColor,
//                                          fontSize = 15.sp
//                                    ),
//                                    modifier = Modifier.padding(end = 8.dp, top = 4.dp)
//                                          .align(Alignment.End)
//                              )
//                        } else {
//                              Text(
//                                    text = "  ",
//                                    style = textStyle.copy(
//                                          color = errorBorderColor,
//                                          fontSize = 15.sp
//                                    ),
//                                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
//                              )
//                        }
//                  }
//            }
//      }
//
//
//
//
//
//      @Composable
//      fun TextField1(
//            value: MutableState<String>,
//            onValueChange: (String) -> Unit = {},
//            modifier: Modifier = Modifier,
//            label: String? = null,
//            placeholder: String? = null,
//            isError: Boolean = false,
//            errorMessage: String? = null,
//            visualTransformation: VisualTransformation = VisualTransformation.None,
//            singleLine: Boolean = true,
//            backgroundColor: Color = Color.Transparent,
//            shape: Shape = MaterialTheme.shapes.small,
//            focusedBorderColor: Color = Color.Black,
//            unfocusedBorderColor: Color = Color.Gray,
//            errorBorderColor: Color = Color.Red,
//            borderWidth: Dp = 1.dp,
//            textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
//            placeholderColor: Color = Color.Gray,
//            maxLength: Int = Int.MAX_VALUE,
//            keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//            cursorBrush: Color = Color.Black,
//            isIcon: Boolean = false,
//            keyboardActions: KeyboardActions = KeyboardActions.Default,
//            focusRequester: FocusRequester = FocusRequester(),
//            onFocusChanged: (FocusState) -> Unit = {}
//
//      ) {
//            var passwordVisibility by remember { mutableStateOf(false) }
//            val isPasswordType = keyboardOptions.keyboardType == KeyboardType.Password
//            val effectiveVisualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
//
//            val interactionSource = remember { MutableInteractionSource() }
//            val isFocused by interactionSource.collectIsFocusedAsState()
//
//            val borderColor = when {
//                  isError -> errorBorderColor
//                  isFocused -> focusedBorderColor
//                  else -> unfocusedBorderColor
//            }
//
//            Column(modifier = modifier) {
//                  Box(
//                        modifier = Modifier
//                              .background(color = backgroundColor, shape = shape)
//                              .border(
//                                    BorderStroke(
//                                          width = borderWidth,
//                                          brush = SolidColor(borderColor)
//                                    ),
//                                    shape = shape
//                              )
//                              .padding(horizontal = 5.dp, vertical = 13.dp)
//                  ) {
//                        if (value.value.isEmpty() && !isFocused) {
//                              placeholder?.let {
//                                    Text(
//                                          text = it,
//                                          style = textStyle.copy(color = placeholderColor)
//                                    )
//                              }
//                        }
//
//                        BasicTextField(
//                              value = value.value,
//                              onValueChange = {
//                                    if (it.length <= maxLength) {
//                                          value.value = it
//                                          onValueChange.invoke(it)
//                                    }
//                              },
//                              modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 40.dp)
//                                    .focusRequester(focusRequester)
//                                    .onFocusChanged { focusState ->
//                                          onFocusChanged(focusState)
//                                    },
//                              textStyle = textStyle,
//                              singleLine = singleLine,
//                              visualTransformation = if (isPasswordType) effectiveVisualTransformation else visualTransformation,
//                              keyboardOptions = keyboardOptions,
//                              keyboardActions = keyboardActions,
//                              interactionSource = interactionSource,
//                              cursorBrush = SolidColor(cursorBrush)
//                        )
//
//                        if (isIcon && isFocused) {
//                              Box(
//                                    modifier = Modifier
//                                          .height(20.dp)
//                                          .align(Alignment.CenterEnd)
//                              ) {
//                                    IconButton(
//                                          onClick = {
//                                                if (isPasswordType) {
//                                                      passwordVisibility = !passwordVisibility
//                                                } else {
//                                                      value.value = ""
//                                                }
//                                          },
//                                          modifier = Modifier.align(Alignment.Center)
//                                    ) {
//                                          Icon(
//                                                imageVector = if (isPasswordType) {
//                                                      if (passwordVisibility) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff
//                                                } else {
//                                                      Icons.Rounded.Cancel
//                                                },
//                                                contentDescription = "Toggle Password Visibility",
//                                                tint = cursorBrush
//                                          )
//                                    }
//                              }
//                        }
//
//                        if (label != null && value.value.isEmpty()) {
//                              Text(
//                                    text = label,
//                                    style = textStyle.copy(color = placeholderColor),
//                                    modifier = Modifier.padding(start = 6.dp, top = 8.dp)
//                              )
//                        }
//                  }
//
//                  if (isError && errorMessage != null) {
//                        Text(
//                              text = errorMessage,
//                              style = textStyle.copy(color = errorBorderColor, fontSize = 12.sp),
//                              modifier = Modifier.padding(start = 8.dp, top = 4.dp)
//                        )
//                  }else{
//                        Text(
//                              text = "  ",
//                              style = textStyle.copy(color = errorBorderColor, fontSize = 12.sp),
//                              modifier = Modifier.padding(start = 8.dp, top = 4.dp)
//                        )
//                  }
//            }
//      }
//
//      @Composable
//      fun TextFieldWithIcons(
//            value: MutableState<String>,
//            modifier: Modifier = Modifier,
//            textModifier: Modifier = Modifier,
//            label: String? = null,
//            labelsize: TextUnit = 14.sp,
//            placeholder: String? = null,
//            isError: Boolean = false,
//            visualTransformation: VisualTransformation = VisualTransformation.None,
//            singleLine: Boolean = true,
//            boxlength: Dp = 13.dp,
//            backgroundColor: Color = Color.Transparent,
//            shape: Shape = MaterialTheme.shapes.small,
//            focusedBorderColor: Color = Color.Black,
//            unfocusedBorderColor: Color = Color.Gray,
//            borderWidth: Dp = 1.dp,
//            textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
//            placeholderColor: Color = Color.Gray,
//            startIconResId: Int? = null,
//            endIconResId: Int? = null,
//            onEndIconClick: () -> Unit = {},
//            startIconTint: Color = Color.Unspecified, // Default to no tint
//            endIconTint: Color = Color.Unspecified, // Default to no tint
//            maxLength: Int = Int.MAX_VALUE, // Default to no limit
//            keyboardOptions: KeyboardOptions = KeyboardOptions.Default, // Default keyboard options
//            cursorBrush: Color = Color.Black,
//            isIcon: Boolean = false // New parameter to control icon visibility
//      ) {
//            val interactionSource = remember { MutableInteractionSource() }
//            val isFocused by interactionSource.collectIsFocusedAsState()
//
//            Box(
//                  modifier = modifier
//                        .background(color = backgroundColor, shape = shape)
//                        .border(
//                              BorderStroke(
//                                    width = borderWidth,
//                                    brush = SolidColor(if (isFocused) focusedBorderColor else unfocusedBorderColor)
//                              ), shape = shape
//                        )
//                        .padding(horizontal = 8.dp, vertical = boxlength)
//            ) {
//                  if (value.value.isEmpty() && !isFocused) {
//                        placeholder?.let {
//                              Text(
//                                    text = it, style = textStyle.copy(color = placeholderColor)
//                              )
//                        }
//                  }  // Start Icon
//                  var labelPadding = 12.dp
//                  if (startIconResId != null) {
//                        labelPadding = 35.dp
//                  }
//                  if (label != null && value.value.isEmpty()) {
//                        Text(
//                              text = label,
//                              style = textStyle.copy(color = placeholderColor),
//                              modifier = Modifier.padding(horizontal = labelPadding),
//                              fontSize = labelsize
//                        )
//                  }
//
//                  Row(
//                        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
//                  ) {
//                        if (startIconResId != null) {
//
//                                    Box(
//                                          modifier = Modifier.size(20.dp)
//
//                                    ) {
//                                          Icon(
//                                                painter = painterResource(id = startIconResId),
//                                                contentDescription = null,
//                                                modifier = Modifier.fillMaxSize(),
//                                                tint = if (startIconTint != Color.Unspecified) startIconTint else Color.Unspecified
//                                          )
//                                    }
//
//                        }
//
//
//                        BasicTextField(
//                              value = value.value,
//                              onValueChange = {
//                                    if (it.length <= maxLength) {
//                                          value.value = it
//                                    }
//                              },
//                              modifier = textModifier
//                                    .fillMaxWidth()
//                                    .align(Alignment.CenterVertically)
//                                    .padding(horizontal = 15.dp),
//                              textStyle = textStyle,
//                              singleLine = singleLine,
//                              visualTransformation = visualTransformation,
//                              keyboardOptions = keyboardOptions,
//                              interactionSource = interactionSource,
//                              cursorBrush = SolidColor(cursorBrush)
//                        )
//                        if (isIcon && value.value.isNotEmpty()&& isFocused) {
//                              Box(
//                                    modifier = Modifier.size(20.dp)
//                              ) {
//                                    IconButton(
//                                          onClick = { value.value = "" }, // Clear the text when icon is clicked
////                                    modifier = Modifier.align(Alignment.Center)
//                                    ) {
//                                          Icon(
//                                                painterResource(id = R.drawable.ic_clear),
//                                                contentDescription = "Clear",
////                                          tint = cursorBrush
//                                                tint = Color.Gray
//                                          )
//                                    }
//                              }
//                        }
//                        // End Icon
//                        if (endIconResId != null  && isFocused) {
//                              Spacer(modifier = Modifier.width(5.dp))
//                              Box(
//                                    modifier = Modifier.size(20.dp)
//
//                              ) {
//                                    Icon(
//                                          painter = painterResource(id = endIconResId),
//                                          contentDescription = null,
//                                          modifier = Modifier.clickable {
//                                                onEndIconClick.invoke()
//                                          },
//                                          tint = if (endIconTint != Color.Unspecified) endIconTint else Color.Unspecified
//                                    )
//                              }
//                        }
//                  }
////                  if (startIconResId != null) {
////                        Box(
////                              modifier = Modifier
////                                    .size(40.dp)
////                                    .padding(start = 12.dp)
//////                                    .align(Alignment.CenterVertically)
////                        ) {
////                              Icon(
////                                    painter = painterResource(id = startIconResId),
////                                    contentDescription = null,
////                                    modifier = Modifier
////                                          .padding(end = 10.dp)
////                                          .fillMaxSize(),
////                                    tint = if (startIconTint != Color.Unspecified) startIconTint else Color.Unspecified
////                              )
////                        }
////                  }
////                  BasicTextField(
////                        value = value.value,
////                        onValueChange = {
////                              if (it.length <= maxLength) {
////                                    value.value = it
////                              }
////                        },
////                        modifier = Modifier
////                              .fillMaxWidth()
////                              .padding(horizontal = 8.dp),
////                        textStyle = textStyle,
////                        singleLine = singleLine,
////                        visualTransformation = visualTransformation,
////                        keyboardOptions = keyboardOptions,
////                        interactionSource = interactionSource,
////                        cursorBrush = SolidColor(cursorBrush)
////                  )
////                  if (isIcon && value.value.isNotEmpty()) {
////                        Box(
////                              modifier = Modifier
////                                    .height(20.dp)
////                                    .align(Alignment.CenterEnd)
////                        ) {
////                              IconButton(
////                                    onClick = { value.value = "" }, // Clear the text when icon is clicked
////                                    modifier = Modifier.align(Alignment.Center)
////                              ) {
////                                    Icon(
////                                          painterResource(id =  R.drawable.baseline_clear_24) ,
////                                          contentDescription = "Clear",
//////                                          tint = cursorBrush
////                                          tint = colorGray2
////                                    )
////                              }
////                        }
////                  }
////                  if (label != null && value.value.isEmpty()) {
////                        Text(
////                              text = label,
////                              style = textStyle.copy(color = placeholderColor),
////                              modifier = Modifier.padding(start = 6.dp), fontSize = labelsize
////                        )
////                  }
//            }
//      }
//
//
//
//
//
//
//      @OptIn(ExperimentalMaterial3Api::class)
//      @Composable
//      fun TextFieldWithLabel(
//            value: MutableState<String>,
//            modifier: Modifier = Modifier,
//            label: String? = null,
//            placeholder: String? = null,
//            isError: Boolean = false,
//            visualTransformation: VisualTransformation = VisualTransformation.None,
//            singleLine: Boolean = true,
//            backgroundColor: Color = Color.Transparent,
//            shape: Shape = MaterialTheme.shapes.small,
//            focusedBorderColor: Color = Color.Black,
//            unfocusedBorderColor: Color = Color.Gray,
//            borderWidth: Dp = 1.dp,
//            textStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
//            placeholderColor: Color = Color.Black,
//            maxLength: Int = Int.MAX_VALUE, // Default to no limit
//            keyboardOptions: KeyboardOptions = KeyboardOptions.Default, // Default keyboard options
//            cursorBrush: Color = Color.Black,
//            isIcon: Boolean = false,
//            labelSize: TextUnit = 15.sp,
//            labelFontFamily: FontFamily= FontFamily.Default,
//            labelColor: Color = Color.Gray,
//            containerColor: Color = Color.Gray,
//            focusedLabelColor: Color = Color.Black
//      ) {
//            val interactionSource = remember { MutableInteractionSource() }
//            val isFocused by interactionSource.collectIsFocusedAsState()
//
//            Box(
//                  modifier = modifier
//                        .background(color = backgroundColor, shape = shape)
//                        .border(
//                              BorderStroke(
//                                    width = borderWidth,
//                                    brush = SolidColor(if (isFocused) focusedBorderColor else unfocusedBorderColor)
//                              ),
//                              shape = shape
//                        )
//                        .padding(horizontal = 5.dp,)
//            ) {
//                  androidx.compose.material3.TextField(
//                        value = value.value,
//                        onValueChange = {
//                              if (it.length <= maxLength) {
//                              value.value = it
//                              }
//                                        },
//                        textStyle = textStyle,
//                        singleLine = singleLine,
//                        label = { if (label != null) { Text(text = label, fontSize = labelSize, fontWeight = FontWeight.Normal, fontFamily = labelFontFamily, color = if (isFocused) focusedLabelColor else labelColor) } },
//                        colors = TextFieldDefaults.textFieldColors(
//                              containerColor = containerColor,
//                              focusedIndicatorColor = Color.Transparent, // Line color when focused
//                              unfocusedIndicatorColor = Color.Transparent,// Line color when not focused
//                              focusedLabelColor = Color.Gray,
//                              unfocusedLabelColor = Color.Gray,
//                              cursorColor = cursorBrush
//                        ),
//                        visualTransformation = visualTransformation,
//                        keyboardOptions = keyboardOptions,
//                        interactionSource = interactionSource,
//                        modifier = Modifier
//                              .fillMaxWidth(),                  )
//                  if (isIcon && value.value.isNotEmpty() && isFocused) {
//                        Box(
//                              modifier = Modifier
//                                    .height(20.dp)
//                                    .align(Alignment.CenterEnd)
//                        ) {
//                              IconButton(
//                                    onClick = { value.value = "" }, // Clear the text when icon is clicked
//                                    modifier = Modifier.align(Alignment.Center)
//                              ) {
//                                    Icon(
//                                          imageVector = Icons.Rounded.Cancel,
//                                          contentDescription = "Clear",
//                                          tint = cursorBrush
//                                    )
//                              }
//                        }
//                  }
//            }
//      }
//
//
//
//
//      @Composable
//      fun Button(
//            modifier: Modifier = Modifier,
//            textModifier: Modifier = Modifier,
//            text: String,
//            onClick: () -> Unit,
//            style: TextStyle=LocalTextStyle.current.copy(fontSize = 16.sp),
//            shape: RoundedCornerShape = RoundedCornerShape(8.dp),
//            backgroundBrush: Brush = SolidColor(Color.Black),
//      ) {
//            Box(
//                  modifier = modifier
//                        .clickable(onClick = onClick),
//                  contentAlignment = Alignment.Center
//            ) {
//                  Text(
//                        modifier = textModifier,
//                        text = text,
//                        style =style
//                  )
//            }
//      }
//
//
//
//
//
//
//
//
//
//      @Composable
//      fun NotificationIcon( placeholderResId: Int,modifier: Modifier,imgModifier: Modifier) {
//            Box(
//                  contentAlignment = Alignment.TopEnd,
//                  modifier = Modifier.padding(end=5.dp) // Adjust the size as needed
//            ) {
//                  Box(
//                        contentAlignment = Alignment.TopEnd,
//                        modifier = modifier // Adjust the size as needed
//                  ) {
//                        // Bell Icon
//                        Icon(
//                              painter = painterResource(placeholderResId), // Replace with your bell icon resource
//                              contentDescription = "Notification",
//                              modifier = imgModifier // Size for the bell icon
//                                    .align(Alignment.Center),
//                              tint = Color.Black
//                        )
//
//
//
//                  }
//                  // Red Badge
//                  Box(
//                        modifier = Modifier.padding(top = 2.dp, end = 3.dp)
//                              .size(10.dp) // Size for the red badge
//                              .background(Color.Red, shape = CircleShape)
//                              .align(Alignment.TopEnd)
//                  )
//            }
//
//      }
//
//      @Composable
//      fun CircularImageWithStroke(
//            modifier: Modifier,imgModifier: Modifier,imageUrl: String?, placeholderResId: Int) {
//            Box(
//                  modifier = modifier,
//                        contentAlignment = Alignment.Center
//
//
//            ) {
//                  if (imageUrl != null) {
//                        Image(
//                              painter = rememberImagePainter(data = imageUrl),
//                              contentDescription = null,
//                              modifier = imgModifier.clip(CircleShape),
//                        )
//                  } else {
//                        Image(
//                              painter = painterResource(id = placeholderResId),
//                              contentDescription = null,
//                              modifier = imgModifier.clip(CircleShape)
//                        )
//                  }
//            }
//      }
//
//
//
//
//
//
//      @Composable
//      fun OtpTextField(
//            modifier: Modifier = Modifier,
//            otpText: String,
//            otpCount: Int = 6,
//            onOtpTextChange: (String, Boolean) -> Unit,
//            focusRequester: FocusRequester
//      ) {
//            LaunchedEffect(Unit) {
//                  if (otpText.length > otpCount) {
//                        throw IllegalArgumentException("Otp text value must not have more than otpCount: $otpCount characters")
//                  }
//            }
//
//            BasicTextField(
//                  modifier = modifier.focusRequester(focusRequester),
//                  value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
//                  onValueChange = {
//                        if (it.text.length <= otpCount) {
//                              onOtpTextChange.invoke(it.text, it.text.length == otpCount)
//                        }
//                  },
//                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
//                  decorationBox = {
//                        Row(
//                              horizontalArrangement = Arrangement.Center,
//                              verticalAlignment = Alignment.CenterVertically,
//                              modifier = Modifier.fillMaxWidth()
//                        ) {
//                              repeat(otpCount) { index ->
//                                    val isFocused = otpText.length == index
//                                    val tint = if (isFocused) {
//                                          colorYellow1
//                                    } else {
//                                         colorWhite
//                                    }
//                                    val char = when {
//                                          index == otpText.length -> ""
//                                          index > otpText.length -> ""
//                                          else -> otpText[index].toString()
//                                    }
//
//                                    Card(
//                                          modifier = Modifier
//                                                .padding( 8.dp)
//                                                .size(50.dp)
//                                                .weight(1f),
//                                          shape = RoundedCornerShape(8.dp),
//                                          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
//                                          colors = CardDefaults.cardColors(containerColor = colorWhite),
//                                          border = BorderStroke(1.5.dp, color = tint)
//                                    ) {
//                                          Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
//                                                Text(
//                                                      text = char,
//                                                      style = MaterialTheme.typography.titleLarge,
//                                                      color = Color.Black,
//                                                      textAlign = TextAlign.Center,
//                                                      fontWeight = FontWeight.Bold,
//                                                      fontSize = 18.sp
//                                                )
//                                          }
//                                    }
//                              }
//                        }
//                  }
//            )
//      }
//
//
//      @Composable
//      fun ResendOtpTimer(
//            initialCount: Long = 180000L, // Initial countdown time in milliseconds (3 minutes)
//            interval: Long = 1000L, // Interval for countdown updates in milliseconds (1 second)
//            onTimeUpdate: (String) -> Unit, // Callback for formatted time updates
//            onFinish: () -> Unit // Callback when the timer finishes
//      ) {
//            LaunchedEffect(Unit) {
//                  val countDownTimer = object : CountDownTimer(initialCount, interval) {
//                        override fun onTick(millisUntilFinished: Long) {
//                              val minutes = (millisUntilFinished / 1000) / 60
//                              val seconds = (millisUntilFinished / 1000) % 60
//                              val formattedTime = String.format("%02d:%02d", minutes, seconds)
//                              onTimeUpdate(formattedTime) // Return formatted time
//                        }
//
//                        override fun onFinish() {
//                              onFinish() // Notify when the timer finishes
//                        }
//                  }
//                  countDownTimer.start()
//            }
//      }
//      fun ResendOtpTimerSec(
//            initialCount: Long = 180000L, // Initial countdown time in milliseconds (3 minutes)
//            interval: Long = 1000L, // Interval for countdown updates in milliseconds (1 second)
//            onTimeUpdate: (String) -> Unit, // Callback for formatted time updates
//            onFinish: () -> Unit // Callback when the timer finishes
//      ) {
//                  val countDownTimer = object : CountDownTimer(initialCount, interval) {
//                        override fun onTick(millisUntilFinished: Long) {
//                              val minutes = (millisUntilFinished / 1000) / 60
//                              val seconds = (millisUntilFinished / 1000) % 60
//                              val formattedTime = String.format("%02d", seconds)
//                              onTimeUpdate(formattedTime) // Return formatted time
//                        }
//
//                        override fun onFinish() {
//                              onFinish() // Notify when the timer finishes
//                        }
//                  }
//                  countDownTimer.start()
//
//      }
//
//
//
//      @Composable
//      fun ToggleIconButton(
//            modifier: Modifier = Modifier,
//            enableTint: Color = Color.Red,
//            disableTint: Color = MaterialTheme.colorScheme.onBackground,
//            enableIcon: Painter,
//            disableIconRes: Int, // Accept a drawable resource ID for the disable icon
//            initialState: Boolean,
//            onCheckedChange: (Boolean) -> Unit,
//      ) {
//            IconToggleButton(
//                  checked = initialState,
//                  onCheckedChange = onCheckedChange,
//                  modifier = modifier
//            ) {
//                  val transition = updateTransition(initialState, label = "favorite")
//                  val tint by animateColorAsState(
//                        targetValue = if (initialState) enableTint else disableTint,
//                        label = "tint",
//                  )
//
//                  // Animation for size
//                  val size by transition.animateDp(
//                        transitionSpec = {
//                              if (false isTransitioningTo true) {
//                                    keyframes {
//                                          durationMillis = 1000
//                                          33.dp at 0 with LinearOutSlowInEasing
//                                          32.dp at 15 with FastOutLinearInEasing
//                                          33.dp at 75
//                                          32.dp at 150
//                                    }
//                              } else {
//                                    spring(stiffness = Spring.StiffnessVeryLow)
//                              }
//                        },
//                        label = "Size"
//                  ) {
//                        if (it) 33.dp else 33.dp
//                  }
//
//                  Icon(
//                        tint = tint,
//                        painter = if (initialState) enableIcon else painterResource(id = disableIconRes),
//                        contentDescription = null,
//                        modifier = modifier.size(size)
//                  )
//            }
//      }
//      @Composable
//      fun ToggleIconButtonForComment(
//            modifier: Modifier = Modifier,
//            enableTint: Color = Color.Red,
//            disableTint: Color = MaterialTheme.colorScheme.onBackground,
//            enableIcon: Painter,
//            disableIconRes: Int, // Accept a drawable resource ID for the disable icon
//            initialState: Boolean,
//            onCheckedChange: (Boolean) -> Unit,
//      ) {
//            IconToggleButton(
//                  checked = initialState,
//                  onCheckedChange = onCheckedChange,
//                  modifier = modifier
//            ) {
//                  val transition = updateTransition(initialState, label = "favorite")
//                  val tint by animateColorAsState(
//                        targetValue = if (initialState) enableTint else disableTint,
//                        label = "tint",
//                  )
//
//                  // Animation for size
//                  val size by transition.animateDp(
//                        transitionSpec = {
//                              if (false isTransitioningTo true) {
//                                    keyframes {
//                                          durationMillis = 1000
//                                          23.dp at 0 with LinearOutSlowInEasing // for 0-15 ms
//                                          25.dp at 15 with FastOutLinearInEasing // for 15-75 ms
//                                          30.dp at 75 // ms
//                                          25.dp at 150 // ms
//                                    }
//                              } else {
//                                    spring(stiffness = Spring.StiffnessVeryLow)
//                              }
//                        },
//                        label = "Size"
//                  ) {
//                        if (it) 23.dp else 23.dp
//                  }
//
//                  Icon(
//                        tint = tint,
//                        painter = if (initialState) enableIcon else painterResource(id = disableIconRes),
//                        contentDescription = null,
//                        modifier = modifier.size(size)
//                  )
//            }
//      }
//
//      @Composable
//      fun ZoomableImage(
//            context: Context,
//            url: String // or Uri
//      ) {
//            var scale by remember { mutableStateOf(1f) }
//            var offsetX by remember { mutableStateOf(0f) }
//            var offsetY by remember { mutableStateOf(0f) }
//
//            Box(modifier = Modifier.pointerInput(Unit) {
//                  detectTransformGestures { _, pan, zoom, _ ->
//                        scale *= zoom // Apply zoom
//                        offsetX += pan.x // Apply horizontal pan (drag)
//                        offsetY += pan.y // Apply vertical pan (drag)
//                  }
//            }) {
//                  AsyncImage(
//                        model = url,
//                        contentDescription = null,
//                        modifier = Modifier
//                              .graphicsLayer(
//                                    scaleX = scale,
//                                    scaleY = scale,
//                                    translationX = offsetX,
//                                    translationY = offsetY
//                              )
//                              .fillMaxSize()
//                  )
//            }
//      }
//      @Composable
//      fun ZoomableImageWithUri(
//            context: Context,
//            fileUri: Uri // or Uri
//      ) {
//            var scale by remember { mutableStateOf(1f) }
//            var offsetX by remember { mutableStateOf(0f) }
//            var offsetY by remember { mutableStateOf(0f) }
//
//            Box(modifier = Modifier.pointerInput(Unit) {
//                  detectTransformGestures { _, pan, zoom, _ ->
//                        scale *= zoom // Apply zoom
//                        offsetX += pan.x // Apply horizontal pan (drag)
//                        offsetY += pan.y // Apply vertical pan (drag)
//                  }
//            }) {
//                  AsyncImage(
//                        model = fileUri,
//                        contentDescription = null,
//                        modifier = Modifier
//                              .graphicsLayer(
//                                    scaleX = scale,
//                                    scaleY = scale,
//                                    translationX = offsetX,
//                                    translationY = offsetY
//                              )
//                              .fillMaxSize()
//                  )
//            }
//      }
//
//
//
//
//
//}