package com.divinelink.core.network.media.service;

import com.divinelink.core.network.client.RestClient;
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
public final class ProdMediaService_Factory implements Factory<ProdMediaService> {
  private final Provider<RestClient> restClientProvider;

  public ProdMediaService_Factory(Provider<RestClient> restClientProvider) {
    this.restClientProvider = restClientProvider;
  }

  @Override
  public ProdMediaService get() {
    return newInstance(restClientProvider.get());
  }

  public static ProdMediaService_Factory create(Provider<RestClient> restClientProvider) {
    return new ProdMediaService_Factory(restClientProvider);
  }

  public static ProdMediaService newInstance(RestClient restClient) {
    return new ProdMediaService(restClient);
  }
}
