package io.mosip.tf.t5.cryptograph.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.tf.t5.cryptograph.constant.LoggerFileConstant;
import io.mosip.tf.t5.cryptograph.constant.MappingJsonConstants;
import io.mosip.tf.t5.cryptograph.constant.ServiceConstant;
import io.mosip.tf.t5.cryptograph.dto.ErrorDTO;
import io.mosip.tf.t5.cryptograph.exception.ApisResourceAccessException;
import io.mosip.tf.t5.cryptograph.exception.IdRepoAppException;
import io.mosip.tf.t5.cryptograph.exception.PlatformErrorMessages;
import io.mosip.tf.t5.cryptograph.idrepo.dto.IdResponseDTO1;
import io.mosip.tf.t5.cryptograph.logger.CryptographLogger;
import io.mosip.tf.t5.cryptograph.service.RestClientService;
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
	/** The reg proc logger. */
	private static Logger printLogger = CryptographLogger.getLogger(Utilities.class);
	/** The Constant FILE_SEPARATOR. */
	public static final String FILE_SEPARATOR = "\\";

	@Autowired
	private ObjectMapper objMapper;

	/** The rest client service. */
	@Autowired
	private RestClientService<Object> restClientService;

	/** The config server file storage URL. */
	@Value("${config.server.file.storage.uri}")
	private String configServerFileStorageURL;

	/** The get reg processor identity json. */
	@Value("${registration.processor.identityjson}")
	private String getRegProcessorIdentityJson;

	/** The get reg processor demographic identity. */
	@Value("${registration.processor.demographic.identity}")
	private String getRegProcessorDemographicIdentity;

	/** The registration processor abis json. */
	@Value("${registration.processor.print.textfile}")
	private String registrationProcessorPrintTextFile;

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
	 * retrieving identity json ffrom id repo by UIN.
	 *
	 * @param uin the uin
	 * @return the JSON object
	 * @throws ApisResourceAccessException the apis resource access exception
	 * @throws IdRepoAppException          the id repo app exception
	 * @throws IOException                 Signals that an I/O exception has
	 *                                     occurred.
	 */
	public JSONObject retrieveIdrepoJson(String uin)
			throws ApisResourceAccessException, IdRepoAppException, IOException {

		if (uin != null) {
			printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.UIN.toString(), "",
					"Utilities::retrieveIdrepoJson()::entry");
			List<String> pathSegments = new ArrayList<>();
			pathSegments.add(uin);
			IdResponseDTO1 idResponseDto;

			idResponseDto = (IdResponseDTO1) restClientService.getApi(ServiceConstant.IDREPOGETIDBYUIN, pathSegments,
					"", "", IdResponseDTO1.class);
			if (idResponseDto == null) {
				printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.UIN.toString(), "",
						"Utilities::retrieveIdrepoJson()::exit idResponseDto is null");
				return null;
			}
			if (!idResponseDto.getErrors().isEmpty()) {
				List<ErrorDTO> error = idResponseDto.getErrors();
				printLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.UIN.toString(), "",
						"Utilities::retrieveIdrepoJson():: error with error message " + error.get(0).getMessage());
				throw new IdRepoAppException(error.get(0).getMessage());
			}
			String response = objMapper.writeValueAsString(idResponseDto.getResponse().getIdentity());
			printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.UIN.toString(), "",
					"Utilities::retrieveIdrepoJson():: IDREPOGETIDBYUIN GET service call ended Successfully");
			try {
				return (JSONObject) new JSONParser().parse(response);
			} catch (org.json.simple.parser.ParseException e) {
				printLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.UIN.toString(), "",
						ExceptionUtils.getStackTrace(e));
				throw new IdRepoAppException("Error while parsing string to JSONObject", e);
			}

		}
		printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.UIN.toString(), "",
				"Utilities::retrieveIdrepoJson()::exit UIN is null");
		return null;
	}

	/**
	 * Gets registration processor mapping json from config and maps to
	 * RegistrationProcessorIdentity java class.
	 *
	 * @return the registration processor identity json
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public JSONObject getRegistrationProcessorMappingJson() throws IOException {
		printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.USERID.toString(), "",
				"Utilities::getRegistrationProcessorMappingJson()::entry");

		mappingJsonString = (mappingJsonString != null && !mappingJsonString.isEmpty()) ? mappingJsonString
				: Utilities.getJson(configServerFileStorageURL, getRegProcessorIdentityJson);
		ObjectMapper mapIdentityJsonStringToObject = new ObjectMapper();
		printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.USERID.toString(), "",
				"Utilities::getRegistrationProcessorMappingJson()::exit");
		return JsonUtil.getJSONObject(mapIdentityJsonStringToObject.readValue(mappingJsonString, JSONObject.class),
				MappingJsonConstants.IDENTITY);

	}

	public String getMappingJsonValue(String key) throws IOException {
		JSONObject jsonObject = getRegistrationProcessorMappingJson();
		Object obj = jsonObject.get(key);
		if (obj instanceof LinkedHashMap) {
			LinkedHashMap hm = (LinkedHashMap) obj;
			return hm.get("value") != null ? hm.get("value").toString() : null;
		}
		return jsonObject.get(key) != null ? jsonObject.get(key).toString() : null;
	}

	/**
	 * retrieve UIN from IDRepo by registration id.
	 *
	 * @param regId the reg id
	 * @return the JSON object
	 * @throws ApisResourceAccessException the apis resource access exception
	 * @throws IdRepoAppException          the id repo app exception
	 * @throws IOException                 Signals that an I/O exception has
	 *                                     occurred.
	 */
	public JSONObject retrieveUIN(String regId) throws ApisResourceAccessException, IdRepoAppException, IOException {
		printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), regId,
				"Utilities::retrieveUIN()::entry");

		if (regId != null) {
			List<String> pathSegments = new ArrayList<>();
			pathSegments.add(regId);
			IdResponseDTO1 idResponseDto;
			printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
					regId, "Utilities::retrieveUIN():: RETRIEVEIDENTITYFROMRID GET service call Started");

			idResponseDto = (IdResponseDTO1) restClientService.getApi(ServiceConstant.RETRIEVEIDENTITYFROMRID,
					pathSegments, "", "", IdResponseDTO1.class);
			printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.UIN.toString(), "",
					"Utilities::retrieveUIN():: RETRIEVEIDENTITYFROMRID GET service call ended successfully");

			if (!idResponseDto.getErrors().isEmpty()) {
				printLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
						regId,
						"Utilities::retrieveUIN():: error with error message "
								+ PlatformErrorMessages.PRT_PVM_INVALID_UIN.getMessage() + " "
								+ idResponseDto.getErrors().toString());
				throw new IdRepoAppException(
						PlatformErrorMessages.PRT_PVM_INVALID_UIN.getMessage() + idResponseDto.getErrors().toString());
			}
			String response = objMapper.writeValueAsString(idResponseDto.getResponse().getIdentity());
			try {
				return (JSONObject) new JSONParser().parse(response);
			} catch (org.json.simple.parser.ParseException e) {
				printLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.UIN.toString(), "",
						ExceptionUtils.getStackTrace(e));
				throw new IdRepoAppException("Error while parsing string to JSONObject", e);
			}

		}
		printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "",
				"Utilities::retrieveUIN()::exit regId is null");

		return null;
	}
	
	public static MultipartBody.Part byteArrayToMultipartFile(String key, byte[] content, String filename, String contentType) {
        @SuppressWarnings("deprecation")
		RequestBody prtFile =
                RequestBody.create(
                        MediaType.parse(contentType),
                        content);    
        
        return MultipartBody.Part.createFormData(key, filename, prtFile);
    }

}
