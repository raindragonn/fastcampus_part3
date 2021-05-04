package com.raindragon.chapter06_usedtrading.chatlist

// Created by raindragonn on 2021/05/04.

data class ChatListItemModel(
    val buyerId: String,
    val sellerId: String,
    val itemTitle: String,
    val key: Long,
){
    constructor(): this("","","",0)
}
