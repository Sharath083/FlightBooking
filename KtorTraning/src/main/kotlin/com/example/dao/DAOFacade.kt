package com.example.dao

import com.example.model.UserDetails
import com.example.model.UserReq

interface DAOFacade {

    suspend fun allArticles(): List<UserDetails>
    suspend fun addNewArticle(d:UserReq): UserDetails?
    suspend fun Article(id:Int):UserDetails?
    suspend fun editArticle(details:UserDetails): Boolean
}