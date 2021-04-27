package io.mosip.tf.t5.cryptograph.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import io.mosip.kernel.core.util.DateUtils;
import io.mosip.tf.t5.cryptograph.constant.IdType;
import io.mosip.tf.t5.cryptograph.model.BarCodeResponse;
import io.mosip.tf.t5.cryptograph.model.BarcodeParams;
import io.mosip.tf.t5.cryptograph.model.BarcodeTitle;
import io.mosip.tf.t5.cryptograph.model.EmailParam;
import io.mosip.tf.t5.cryptograph.model.FaceParam;
import io.mosip.tf.t5.cryptograph.model.Pipeline;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zipkin2.internal.DateUtil;

@Service
public class IDEncodeCaller {
	
	@Value("${mosip.primary-language}")
	private String primaryLang;
	
	public void uploadData(Map<String, Object> demoAttributes, Map<String, byte[]> bioAttributes) {
		ArrayList<MultipartBody.Part> parts = new ArrayList<>();
		parts.add(Utilities.byteArrayToMultipartFile("demog", getDemoAttributesAsString(demoAttributes).getBytes(),
				"demo.json", "application/octet-stream"));
		Pipeline pipeline = new Pipeline();
	    EmailParam emailSender = new EmailParam();
	    emailSender.setEmailto("nagarjunabtechece@gmail.com");
	    emailSender.subject = "Your Tech5 IDencode";	        
	    pipeline.setEmailSender(emailSender);
	    FaceParam faceParams = new FaceParam();
	    faceParams.setCompressionLevel(1);
	    faceParams.setFaceDetectorConfidence(0.6f);
	    faceParams.setFaceSelectorAlg(1);
	    faceParams.setPerformCompression(true);
	    faceParams.setPerformTemplateExtraction(true);
	    pipeline.setFacePipeline(faceParams);
	    
	    BarcodeParams par = new BarcodeParams();
	    par.setBlockCols(0);
	    par.setBlockRows(0);
	    par.setErrorCorrection(8);
	    par.setGridSize(8);
	    par.setThickness(2);
	    BarcodeTitle title = new BarcodeTitle();
	    title.setAlignment("center");
	    title.setLocation("bottom");
	    title.setOffset(10);
	    title.setText(demoAttributes.get(IdType.UIN.toString()).toString());	    
	    par.setTitle(title);
	    
	    pipeline.setBarcodeGenerationParameters(par);
	    for (Map.Entry<String,byte[]> e : bioAttributes.entrySet()) {
			parts.add(Utilities.byteArrayToMultipartFile(e.getKey().toString(), e.getValue(), e.getKey().toString() +".png", "image/png"));			
	    }

		String pipelineJson = new Gson().toJson(pipeline);
		parts.add(Utilities.byteArrayToMultipartFile("pipeline", pipelineJson.getBytes(), "pipeline.json", "application/json"));
		writeToFile("D://pileline.json",pipelineJson.getBytes());
		
        if(parts.size()> 0) {
        	T5Service service = RetrofitClientInstance.getRetrofitInstance().create(T5Service.class);
        	Call<BarCodeResponse> call = service.createBarCode("https://idencode.tech5-sa.com/v1/enroll", parts);
            call.enqueue(new Callback<BarCodeResponse>() {
                @Override
                public void onResponse(Call<BarCodeResponse> call, Response<BarCodeResponse> response) {
                    if (response != null) {
                        if (response.code() == 400) {
                            try { 
                                System.out.println(response.errorBody().string());                                
                            } catch (IOException e) {
                            	System.out.println(e.getStackTrace());
                            }

                        } else if (response.code() == 200 && response.body() != null){
                        	System.out.println(response.body().toString());
                        	getResponse(response);
                        } else {
                            
                        }

                    } else {
                    }
                }

                public void onFailure(Call<BarCodeResponse> call, Throwable t) {
                	System.out.println(t.getLocalizedMessage());
                }
            });
        }        
	}

	private String getDemoAttributesAsString(Map<String, Object> demoAttributes) {		
		String fullName = (String) demoAttributes.get("fullName");
		String dob = (String) demoAttributes.get("dateOfBirth");
		String email = (String) demoAttributes.get("email");
		String gender = (String) demoAttributes.get("gender_" + primaryLang);
		String uin = demoAttributes.get(IdType.UIN.toString()).toString();
		String requiredString = fullName + "," + dob + "," + "," + email + "," + gender + "," + "," + ","
				+ DateUtils.getUTCCurrentDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE) + "," + "validity = "
				+ DateUtils.getUTCCurrentDateTime().plusYears(1).format(DateTimeFormatter.ISO_LOCAL_DATE) + "," + uin;
		writeToFile("D://requiredString.json",requiredString.getBytes());
		return requiredString;
		
	}
	
	private void writeToFile(String fineName, byte[] data) {				
				try {
					  File pdfFile = new File(
							 fineName
							  ); 
							  OutputStream os = new FileOutputStream(pdfFile); 
							  os.write(data);
							  os.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	}
	
	private void getResponse(Response<BarCodeResponse> response) {	
		BarCodeResponse responseFromServer =response.body();		
		writeToFile("D://" + responseFromServer.getUuid(), Base64.getDecoder().decode(responseFromServer.getImage()));
		writeToFile("D://response" + responseFromServer.getUuid(), response.body().toString().getBytes());
	}


}
