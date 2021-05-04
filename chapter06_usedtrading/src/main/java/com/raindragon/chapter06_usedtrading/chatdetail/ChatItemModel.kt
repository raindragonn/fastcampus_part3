package com.raindragon.chapter06_usedtrading.chatdetail

// Created by raindragonn on 2021/05/04.

data class ChatItemModel(
    val senderId: String,
    val message: String
){
    constructor() : this("","")
}
