package com.example.githubsample.infra.remote

import com.squareup.moshi.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface RepositorySearch {
    data class Response(
        @field:Json(name = "total_count") val totalCount: Int = -1,
    )

    @GET("/search/repositories")
    suspend fun searchRepositories(@Query("q") query: String): Response
}

interface CreateIssue {
    data class Response(
        val id: String,
        val title: String,
    )

    data class Request(
        val title: String,
        val body: String,
    )

    @POST("/repos/{owner}/{repo}/issues")
    fun createIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: Request
    ): Response
}

interface GitHubApiService : RepositorySearch, CreateIssue {
}

object ApiModule {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    fun provideRepositorySearch() = retrofit.create(RepositorySearch::class.java)

    fun provideGitHubApiService(): GitHubApiService {
        return retrofit.create(GitHubApiService::class.java)
    }
}
