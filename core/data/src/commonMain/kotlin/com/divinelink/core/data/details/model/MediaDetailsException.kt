package com.divinelink.core.data.details.model

class InvalidMediaTypeException : Exception()
class MediaDetailsException : Exception()
class ReviewsException : Exception()
class RecommendedException(val order: Int) : Exception()
class VideosException : Exception()
