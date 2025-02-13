package umc.study.umc_7th.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import umc.study.umc_7th.data.AuthRepository
import umc.study.umc_7th.data.network.KakaoOAuthServer
import umc.study.umc_7th.data.network.Server
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        server: Server,
        kakaoOAuthServer: KakaoOAuthServer,
    ): AuthRepository {
        return AuthRepository(server, kakaoOAuthServer)
    }
}