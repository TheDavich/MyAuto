package com.good.cars.app.ui.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.good.cars.app.R
import com.good.cars.app.database.Car

@Composable
fun CarInfoBottomSheetContent(
    car: Car,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val carImageResource = when (car.carIcon) {
        1 -> painterResource(id = R.drawable.white_car_icon)
        2 -> painterResource(id = R.drawable.black_car_icon)
        else -> painterResource(id = R.drawable.white_car_icon)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .fillMaxWidth()
                .height(400.dp)
                .background(Color(0xff3f1700))
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp)
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${car.name}, ${car.year}",
                        fontWeight = FontWeight.W600,
                        fontSize = 24.sp,
                        color = Color.White
                    )
                    Image(
                        painter = painterResource(id = R.drawable.close_buttonm),
                        contentDescription = "close",
                        modifier = modifier.clickable { onClose() }
                    )
                }

                Spacer(modifier = modifier.height(16.dp))

                Column(
                    modifier = modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.gps_icon),
                            contentDescription = "",
                            tint = Color(0xffdbdbdb),
                            modifier = modifier
                                .padding(end = 6.dp)
                        )
                        Text(
                            text = "Пробег: ${car.mileage} км",
                            fontWeight = FontWeight.W400,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = modifier.padding(bottom = 4.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.fuel_icon),
                            contentDescription = "",
                            tint = Color(0xffdbdbdb),
                            modifier = modifier
                                .padding(end = 6.dp)
                        )
                        Text(
                            text = "На бензин: ${car.fuelExpense}р / м",
                            fontWeight = FontWeight.W400,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(Color(0xffff5c00))
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Обслуживание",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W700,
                    modifier = modifier
                        .padding(bottom = 14.dp)
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Card(
                        modifier = modifier
                            .weight(1f)
                            .border(1.dp, Color.White, RoundedCornerShape(16.dp))
                            .padding(top = 14.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xffff5c00)
                        )
                    ) {
                        Column(
                            modifier = modifier
                                .padding(start = 16.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.repair_icon),
                                contentDescription = "",
                                modifier = modifier.size(40.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = modifier.height(4.dp))
                            Text(
                                text = "Ремонт",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W700
                            )
                            Spacer(modifier = modifier.height(2.dp))
                            Text(
                                text = "${car.repairExpense}р / мес",
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.W400,
                                modifier = modifier
                                    .padding(bottom = 12.dp)
                            )
                        }
                    }

                    Card(
                        modifier = modifier
                            .weight(1f)
                            .border(1.dp, Color.White, RoundedCornerShape(16.dp))
                            .padding(top = 14.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xffff5c00)
                        )
                    ) {
                        Column(
                            modifier = modifier
                                .padding(start = 16.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.list_icon),
                                contentDescription = "",
                                modifier = modifier.size(40.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Последнее ТО",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W700
                            )
                            Spacer(modifier = modifier.height(2.dp))
                            Text(
                                text = car.repairDate,
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.W400,
                                modifier = modifier
                                    .padding(bottom = 12.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "${car.fuelExpense + car.repairExpense}₽",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.W700,
                        modifier = modifier
                            .padding(top = 32.dp)
                    )
                    Text(
                        text = "/месяц расход на машину",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W700,
                        modifier = modifier
                            .padding(top = 32.dp)
                    )
                }
            }
        }

        Image(
            painter = carImageResource,
            contentDescription = "Car",
            modifier = modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(y = (-180).dp)
        )
    }
}



@Composable
fun CustomModalSheet(
    visible: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val sheetOffsetY = remember { Animatable(screenHeight.value) }

    LaunchedEffect(visible) {
        if (visible) {
            sheetOffsetY.animateTo(0f)
        } else {
            sheetOffsetY.animateTo(screenHeight.value)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = if (visible) 0.5f else 0f))
            .clickable(onClick = { onDismiss() })
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.BottomCenter)
                .offset { IntOffset(0, sheetOffsetY.value.toInt()) }
                .background(Color.White, shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {

                }
        ) {
            content()
        }
    }
}
