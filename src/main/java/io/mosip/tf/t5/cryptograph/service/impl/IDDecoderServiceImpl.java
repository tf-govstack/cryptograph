package io.mosip.tf.t5.cryptograph.service.impl;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.jaiimageio.jpeg2000.impl.J2KImageReader;

import io.mosip.kernel.core.cbeffutil.spi.CbeffUtil;
import io.mosip.kernel.core.exception.ExceptionUtils;
import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.core.pdfgenerator.exception.PDFGeneratorException;
import io.mosip.kernel.core.util.CryptoUtil;
import io.mosip.kernel.logger.logback.factory.Logfactory;
import io.mosip.kernel.pdfgenerator.itext.constant.PDFGeneratorExceptionCodeConstant;
import io.mosip.tf.t5.cryptograph.constant.IdType;
import io.mosip.tf.t5.cryptograph.constant.LoggerFileConstant;
import io.mosip.tf.t5.cryptograph.constant.ServiceConstant;
import io.mosip.tf.t5.cryptograph.dto.DecryptRequestDto;
import io.mosip.tf.t5.cryptograph.dto.DecryptResponseDto;
import io.mosip.tf.t5.cryptograph.dto.JsonValue;
import io.mosip.tf.t5.cryptograph.exception.ApisResourceAccessException;
import io.mosip.tf.t5.cryptograph.exception.IdentityNotFoundException;
import io.mosip.tf.t5.cryptograph.exception.ParsingException;
import io.mosip.tf.t5.cryptograph.exception.PlatformErrorMessages;
import io.mosip.tf.t5.cryptograph.logger.CryptographLogger;
import io.mosip.tf.t5.cryptograph.logger.LogDescription;
import io.mosip.tf.t5.cryptograph.service.IDDecoderService;
import io.mosip.tf.t5.cryptograph.service.RestClientService;
import io.mosip.tf.t5.cryptograph.util.IDEncodeCaller;
import io.mosip.tf.t5.cryptograph.util.JsonUtil;
import io.mosip.tf.t5.cryptograph.util.Utilities;
import io.mosip.tf.t5.http.RequestWrapper;
import io.mosip.tf.t5.http.ResponseWrapper;

@Service
public class IDDecoderServiceImpl implements IDDecoderService {

	@Autowired
	private IDEncodeCaller enrollDataUpload;

	/** The Constant FILE_SEPARATOR. */
	public static final String FILE_SEPARATOR = File.separator;

	/** The Constant VALUE. */
	private static final String VALUE = "value";

	/** The primary lang. */
	@Value("${mosip.primary-language}")
	private String primaryLang;

	/** The secondary lang. */
	@Value("${mosip.secondary-language}")
	private String secondaryLang;

	/** The un masked length. */
	@Value("${registration.processor.unMaskedUin.length}")
	private int unMaskedLength;

	/** The uin length. */
	@Value("${mosip.kernel.uin.length}")
	private int uinLength;

	@Value("${mosip.print.uin.header.length}")
	private int headerLength;
	/** The Constant FACE. */
	private static final String FACE = "Face";

	/** The reg proc logger. */
	private static Logger printLogger = CryptographLogger.getLogger(IDDecoderServiceImpl.class);

	private Logger log = Logfactory.getSlf4jLogger(IDDecoderServiceImpl.class);

	/** The utilities. */
	@Autowired
	private Utilities utilities;

	/** The rest client service. */
	@Autowired
	private RestClientService<Object> restClientService;

	@Autowired
	private CbeffUtil cbeffutil;

	@Override
	public void getDocuments(String credential, String credentialType, String encryptionPin, String requestId,
			String sign, String cardType, boolean isPasswordProtected) {
		printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "",
				"IDDecoderServiceImpl::getDocuments()::entry");

		String credentialSubject;
		new HashMap<>();
		String uin = null;
		LogDescription description = new LogDescription();
		String individualBio = null;
		Map<String, Object> demoAttributes = new LinkedHashMap<>();
		Map<String, byte[]> bioAttributes = new LinkedHashMap<>();
		try {
			credentialSubject = getCrdentialSubject(credential);
			printLogger.error(LoggerFileConstant.SESSIONID.toString(), "credentialSubject", "", credentialSubject);
			org.json.JSONObject credentialSubjectJson = new org.json.JSONObject(credentialSubject);
			printLogger.error(LoggerFileConstant.SESSIONID.toString(), "credentialSubjectJson", "",
					credentialSubjectJson.toString());
			org.json.JSONObject decryptedJson = decryptAttribute(credentialSubjectJson, encryptionPin, credential);
			printLogger.error(LoggerFileConstant.SESSIONID.toString(), "decryptedJson", "", decryptedJson.toString());
			individualBio = decryptedJson.getString("biometrics");
			printLogger.error(LoggerFileConstant.SESSIONID.toString(), "individualBio", "", individualBio);
			String individualBiometric = new String(individualBio);
			uin = decryptedJson.getString("UIN");
			boolean isPhotoSet = extractBiometrics(individualBiometric, bioAttributes);
			if (!isPhotoSet) {
				printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
						uin, PlatformErrorMessages.PRT_PRT_APPLICANT_PHOTO_NOT_SET.name());
			}
			setTemplateAttributes(decryptedJson.toString(), demoAttributes);
			demoAttributes.put(IdType.UIN.toString(), uin);
			enrollDataUpload.uploadData(demoAttributes, bioAttributes);
		} catch (Exception ex) {
			ex.printStackTrace();
			description.setMessage(PlatformErrorMessages.PRT_PRT_PDF_GENERATION_FAILED.getMessage());
			description.setCode(PlatformErrorMessages.PRT_PRT_PDF_GENERATION_FAILED.getCode());
			printLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
					"UIN", description + ex.getMessage() + ExceptionUtils.getStackTrace(ex));
			throw new PDFGeneratorException(PDFGeneratorExceptionCodeConstant.PDF_EXCEPTION.getErrorCode(),
					ex.getMessage() + ExceptionUtils.getStackTrace(ex));

		}
		printLogger.debug(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(), "",
				"IDDecoderServiceImpl::getDocuments()::exit");
	}

	/**
	 * Sets the applicant photo.
	 *
	 * @param response   the response
	 * @param attributes the attributes
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	private boolean extractBiometrics(String individualBio, Map<String, byte[]> attributes) throws Exception {
		String value = individualBio;

		if (value != null) {
			Map<String, String> bdbBasedOnFace = cbeffutil.getBDBBasedOnType(CryptoUtil.decodeBase64(value), FACE,
					null);
			for (Entry<String, String> iterable_element : bdbBasedOnFace.entrySet()) {
				printLogger.error(LoggerFileConstant.SESSIONID.toString(), "cbeff", "", iterable_element.getValue());
				attributes.put("face_image", convertToJPG(iterable_element.getValue()));
				printLogger.error(LoggerFileConstant.SESSIONID.toString(), "After Converting to png", "",
						iterable_element.getValue());

			}

			Map<String, String> bdbBasedOnFinger = cbeffutil.getBDBBasedOnType(CryptoUtil.decodeBase64(value), "Finger",
					null);
			for (Entry<String, String> iterable_element : bdbBasedOnFinger.entrySet()) {
				String[] str = iterable_element.getKey().split("_");
//				if (str[1].equalsIgnoreCase("Right Thumb")) {
//					attributes.put("finger_image_r1", convertToJPG(iterable_element.getValue()));
//				}
//				if (str[1].equalsIgnoreCase("Right IndexFinger")) {
//					attributes.put("finger_image_r2", convertToJPG(iterable_element.getValue()));
//				}
//				if (str[1].equalsIgnoreCase("Right MiddleFinger")) {
//					attributes.put("finger_image_r3", convertToJPG(iterable_element.getValue()));
//				}
//				if (str[1].equalsIgnoreCase("Right RingFinger")) {
//					attributes.put("finger_image_r4", convertToJPG(iterable_element.getValue()));
//				}
//				if (str[1].equalsIgnoreCase("Right LittleFinger")) {
//					attributes.put("finger_image_r5", convertToJPG(iterable_element.getValue()));
//				}
//				if (str[1].equalsIgnoreCase("Left Thumb")) {
//					attributes.put("finger_image_l1", convertToJPG(iterable_element.getValue()));
//				}
//				if (str[1].equalsIgnoreCase("Left IndexFinger")) {
//					attributes.put("finger_image_l2", convertToJPG(iterable_element.getValue()));
//				}
//				if (str[1].equalsIgnoreCase("Left MiddleFinger")) {
//					attributes.put("finger_image_l3", convertToJPG(iterable_element.getValue()));
//				}
//				if (str[1].equalsIgnoreCase("Left RingFinger")) {
//					attributes.put("finger_image_l4", convertToJPG(iterable_element.getValue()));
//				}
//				if (str[1].equalsIgnoreCase("Left LittleFinger")) {
//					attributes.put("finger_image_l5", convertToJPG(iterable_element.getValue()));
//				}
			}
		}

		if (attributes.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the artifacts.
	 *
	 * @param idJsonString the id json string
	 * @param attribute    the attribute
	 * @return the artifacts
	 * @throws IOException    Signals that an I/O exception has occurred.
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	private void setTemplateAttributes(String jsonString, Map<String, Object> attribute)
			throws IOException, ParseException {
		try {
			JSONObject demographicIdentity = JsonUtil.objectMapperReadValue(jsonString, JSONObject.class);
			if (demographicIdentity == null)
				throw new IdentityNotFoundException(PlatformErrorMessages.PRT_PIS_IDENTITY_NOT_FOUND.getMessage());

			String mapperJsonString = Utilities.getJson(utilities.getConfigServerFileStorageURL(),
					utilities.getGetRegProcessorIdentityJson());
			JSONObject mapperJson = JsonUtil.objectMapperReadValue(mapperJsonString, JSONObject.class);
			JSONObject mapperIdentity = JsonUtil.getJSONObject(mapperJson,
					utilities.getGetRegProcessorDemographicIdentity());

			List<String> mapperJsonKeys = new ArrayList<>(mapperIdentity.keySet());
			for (String key : mapperJsonKeys) {
				LinkedHashMap<String, String> jsonObject = JsonUtil.getJSONValue(mapperIdentity, key);
				Object obj = null;
				String values = jsonObject.get(VALUE);
				for (String value : values.split(",")) {
					// Object object = demographicIdentity.get(value);
					Object object = demographicIdentity.get(value);
					if (object != null) {
						try {
							obj = new JSONParser().parse(object.toString());
						} catch (Exception e) {
							obj = object;
						}

						if (obj instanceof JSONArray) {
							// JSONArray node = JsonUtil.getJSONArray(demographicIdentity, value);
							JsonValue[] jsonValues = JsonUtil.mapJsonNodeToJavaObject(JsonValue.class, (JSONArray) obj);
							for (JsonValue jsonValue : jsonValues) {
								if (jsonValue.getLanguage().equals(primaryLang))
									attribute.put(value + "_" + primaryLang, jsonValue.getValue());
								if (jsonValue.getLanguage().equals(secondaryLang))
									attribute.put(value + "_" + secondaryLang, jsonValue.getValue());
							}

						} else if (object instanceof JSONObject) {
							JSONObject json = (JSONObject) object;
							attribute.put(value, (String) json.get(VALUE));
						} else {
							attribute.put(value, String.valueOf(object));
						}
					}

				}
			}

		} catch (JsonParseException | JsonMappingException e) {
			printLogger.error(LoggerFileConstant.SESSIONID.toString(), LoggerFileConstant.REGISTRATIONID.toString(),
					null, "Error while parsing Json file" + ExceptionUtils.getStackTrace(e));
			throw new ParsingException(PlatformErrorMessages.PRT_RGS_JSON_PARSING_EXCEPTION.getMessage(), e);
		}
	}

	private String getCrdentialSubject(String crdential) {
		org.json.JSONObject jsonObject = new org.json.JSONObject(crdential);
		String credentialSubject = jsonObject.get("credentialSubject").toString();
		return credentialSubject;
	}

	public org.json.JSONObject decryptAttribute(org.json.JSONObject data, String encryptionPin, String credential) {

		org.json.JSONObject jsonObj = new org.json.JSONObject(credential);

		RequestWrapper<DecryptRequestDto> request = new RequestWrapper<>();
		ResponseWrapper<DecryptResponseDto> response = new ResponseWrapper<DecryptResponseDto>();
		LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
		request.setRequesttime(now);
		org.json.JSONArray jsonArray = (org.json.JSONArray) jsonObj.get("protectedAttributes");
		if (!jsonArray.isEmpty()) {
			for (Object str : jsonArray) {
				try {
					DecryptRequestDto decryptRequestDto = new DecryptRequestDto();
					DecryptResponseDto decryptResponseDto = new DecryptResponseDto();
					decryptRequestDto.setUserPin(encryptionPin);
					decryptRequestDto.setData(data.getString(str.toString()));
					request.setRequest(decryptRequestDto);
					// response=(DecryptResponseDto)restApiClient.postApi(env.getProperty(ApiName.DECRYPTPINBASSED.name()),
					// "", de, DecryptResponseDto.class)
					response = (ResponseWrapper) restClientService.postApi(ServiceConstant.DECRYPTPINBASSED, "", "",
							request, ResponseWrapper.class);

					decryptResponseDto = JsonUtil.readValue(JsonUtil.writeValueAsString(response.getResponse()),
							DecryptResponseDto.class);
					data.put((String) str, decryptResponseDto.getData());
				} catch (ApisResourceAccessException e) {
					printLogger.error(LoggerFileConstant.SESSIONID.toString(),
							LoggerFileConstant.REGISTRATIONID.toString(), null,
							"Error while parsing Json file" + ExceptionUtils.getStackTrace(e));
					throw new ParsingException(PlatformErrorMessages.PRT_RGS_JSON_PARSING_EXCEPTION.getMessage(), e);
				} catch (IOException e) {
					printLogger.error(LoggerFileConstant.SESSIONID.toString(),
							LoggerFileConstant.REGISTRATIONID.toString(), null,
							"Error while parsing Json file" + ExceptionUtils.getStackTrace(e));
					throw new ParsingException(PlatformErrorMessages.PRT_RGS_JSON_PARSING_EXCEPTION.getMessage(), e);
				}
			}
		}

		return data;
	}

	/**
	 * 
	 * @param isoTemplate
	 * @return
	 */
	private byte[] convertToJPG(String isoTemplate) {
		byte[] inputFileBytes = CryptoUtil.decodeBase64(isoTemplate);
		int index;
		for (index = 0; index < inputFileBytes.length; index++) {
			if ((char) inputFileBytes[index] == 'j' && (char) inputFileBytes[index + 1] == 'P') {
				break;
			}
		}
		try {
			return convertToJPG(Arrays.copyOfRange(inputFileBytes, index - 4, inputFileBytes.length), "image");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param jp2Data
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private byte[] convertToJPG(byte[] jp2Data, String fileName) throws IOException {
		ByteArrayOutputStream beforeUpScale = new ByteArrayOutputStream();
		ByteArrayOutputStream afterUpScale = new ByteArrayOutputStream();
		J2KImageReader j2kImageReader = new J2KImageReader(null);
		j2kImageReader.setInput(ImageIO.createImageInputStream(new ByteArrayInputStream(jp2Data)));
		ImageReadParam imageReadParam = j2kImageReader.getDefaultReadParam();
		BufferedImage image = j2kImageReader.read(0, imageReadParam);
		ImageIO.write(image, "PNG", beforeUpScale);
		int height = image.getHeight();
		int width = image.getWidth();
		printLogger.error(LoggerFileConstant.SESSIONID.toString(), "OriginalFaceImage", "", image.toString());
		BufferedImage outputImage = createResizedCopy(image, 2 * width, 2 * height, true);
		ImageIO.write(outputImage, "PNG", afterUpScale);
		printLogger.error(LoggerFileConstant.SESSIONID.toString(), "UpscaledImage", "", outputImage.toString());
		byte[] jpgImg = afterUpScale.toByteArray();
		return jpgImg;
	}

	/**
	 * 
	 * @param originalImage
	 * @param scaledWidth
	 * @param scaledHeight
	 * @param preserveAlpha
	 * @return
	 */
	private BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight,
			boolean preserveAlpha) {
		System.out.println("resizing...");
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
		Graphics2D g = scaledBI.createGraphics();
		if (preserveAlpha) {
			g.setComposite(AlphaComposite.Src);
		}
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}
}
