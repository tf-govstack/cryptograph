package io.mosip.tf.t5.cryptograph.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.Data;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * The Class Utilities. 
 */
@Component

/**
 * Instantiates a new utilities.
 */
@Data
public class Utilities {	
	
	/** The config server file storage URL. */
	@Value("${config.server.file.storage.uri}")
	private String configServerFileStorageURL;

	/** The get reg processor identity json. */
	@Value("${registration.processor.identityjson}")
	private String getRegProcessorIdentityJson;

	/** The get reg processor demographic identity. */
	@Value("${registration.processor.demographic.identity}")
	private String getRegProcessorDemographicIdentity;



	private String mappingJsonString = null;

	/**
	 * Gets the json.
	 *
	 * @param configServerFileStorageURL the config server file storage URL
	 * @param uri                        the uri
	 * @return the json
	 */
	public static String getJson(String configServerFileStorageURL, String uri) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(configServerFileStorageURL + uri, String.class);
	}
		
	
	/**
	 * 
	 * @param key
	 * @param content
	 * @param filename
	 * @param contentType
	 * @return
	 */
	public static MultipartBody.Part byteArrayToMultipartFile(String key, byte[] content, String filename, String contentType) {
        @SuppressWarnings("deprecation")
		RequestBody prtFile =
                RequestBody.create(
                        MediaType.parse(contentType),
                        content);    
        
        return MultipartBody.Part.createFormData(key, filename, prtFile);
    }

}
