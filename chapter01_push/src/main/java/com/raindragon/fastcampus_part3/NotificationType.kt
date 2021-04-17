package com.raindragon.fastcampus_part3

// Created by raindragonn on 2021/04/17.

enum class NotificationType(
    val title: String,
    val id: Int
) {
    NORMAL("일반 알림", 0),
    EXPANDABLE("확장형 알림", 1),
    CUSTOM("커스텀 알림", 2)
}