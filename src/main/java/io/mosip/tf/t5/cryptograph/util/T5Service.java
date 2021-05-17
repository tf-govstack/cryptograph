package io.mosip.tf.t5.cryptograph.util;

import java.util.List;

import io.mosip.tf.t5.cryptograph.model.BarCodeResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface T5Service {

	  @Multipart
	  @POST()
	  Call<BarCodeResponse> createBarCode(@Url String url, @Part List<MultipartBody.Part> parts);
}
