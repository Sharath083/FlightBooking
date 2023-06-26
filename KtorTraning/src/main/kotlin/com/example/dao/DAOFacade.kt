package com.example.dao

import com.example.model.Article
import com.example.model.ArticleReq

interface DAOFacade {

    suspend fun allArticles(): List<Article>
    suspend fun addNewArticle(d:ArticleReq): Article?
}