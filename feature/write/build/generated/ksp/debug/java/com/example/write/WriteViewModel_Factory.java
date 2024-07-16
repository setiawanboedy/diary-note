package com.example.write;

import androidx.lifecycle.SavedStateHandle;
import com.example.mongo.database.ImageToDeleteDao;
import com.example.mongo.database.ImageToUploadDao;
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
public final class WriteViewModel_Factory implements Factory<WriteViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<ImageToUploadDao> imageToUploadDaoProvider;

  private final Provider<ImageToDeleteDao> imageToDeleteDaoProvider;

  public WriteViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ImageToUploadDao> imageToUploadDaoProvider,
      Provider<ImageToDeleteDao> imageToDeleteDaoProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.imageToUploadDaoProvider = imageToUploadDaoProvider;
    this.imageToDeleteDaoProvider = imageToDeleteDaoProvider;
  }

  @Override
  public WriteViewModel get() {
    return newInstance(savedStateHandleProvider.get(), imageToUploadDaoProvider.get(), imageToDeleteDaoProvider.get());
  }

  public static WriteViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<ImageToUploadDao> imageToUploadDaoProvider,
      Provider<ImageToDeleteDao> imageToDeleteDaoProvider) {
    return new WriteViewModel_Factory(savedStateHandleProvider, imageToUploadDaoProvider, imageToDeleteDaoProvider);
  }

  public static WriteViewModel newInstance(SavedStateHandle savedStateHandle,
      ImageToUploadDao imageToUploadDao, ImageToDeleteDao imageToDeleteDao) {
    return new WriteViewModel(savedStateHandle, imageToUploadDao, imageToDeleteDao);
  }
}
