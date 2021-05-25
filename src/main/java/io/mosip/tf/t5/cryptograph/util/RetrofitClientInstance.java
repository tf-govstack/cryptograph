package io.mosip.tf.t5.cryptograph.util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

	private static Retrofit retrofit;
	
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)
                    .writeTimeout(300, TimeUnit.SECONDS).build();
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl("https://idencode.tech5-sa.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
        }
        return retrofit;
    }
}
