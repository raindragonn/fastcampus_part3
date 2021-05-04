package com.raindragon.chapter06_usedtrading.model

// Created by raindragonn on 2021/05/01.

data class ArticleModel(
    val sellerId: String,
    val title: String,
    val createdAt: Long,
    val price: String,
    val imageUrl: String
){
    constructor() : this("","",0,"","")
}
