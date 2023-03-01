package com.kemsky.pokedex.core.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MustBeAuthenticated {
}