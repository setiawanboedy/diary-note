package com.example.home;

import com.example.mongo.database.ImageToDeleteDao;
import com.example.util.connectivity.NetworkConnectivityObserver;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<NetworkConnectivityObserver> connectivityProvider;

  private final Provider<ImageToDeleteDao> imageToDeleteDaoProvider;

  public HomeViewModel_Factory(Provider<NetworkConnectivityObserver> connectivityProvider,
      Provider<ImageToDeleteDao> imageToDeleteDaoProvider) {
    this.connectivityProvider = connectivityProvider;
    this.imageToDeleteDaoProvider = imageToDeleteDaoProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(connectivityProvider.get(), imageToDeleteDaoProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<NetworkConnectivityObserver> connectivityProvider,
      Provider<ImageToDeleteDao> imageToDeleteDaoProvider) {
    return new HomeViewModel_Factory(connectivityProvider, imageToDeleteDaoProvider);
  }

  public static HomeViewModel newInstance(NetworkConnectivityObserver connectivity,
      ImageToDeleteDao imageToDeleteDao) {
    return new HomeViewModel(connectivity, imageToDeleteDao);
  }
}
