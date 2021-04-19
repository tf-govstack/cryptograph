package io.mosip.tf.t5.cryptograph.controller;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.mosip.kernel.websub.api.annotation.PreAuthenticateContentAndVerifyIntent;
import io.mosip.tf.t5.cryptograph.exception.RegPrintAppException;
import io.mosip.tf.t5.cryptograph.model.EventModel;
import io.mosip.tf.t5.cryptograph.service.IDDecoderService;
import io.mosip.tf.t5.cryptograph.util.CryptoCoreUtil;

@RestController
@RequestMapping(value = "/print")
public class CryptographController {

	/** The printservice. */
	@Autowired
	private IDDecoderService iDDecoderService;
	
	@Value("${mosip.event.topic}")
	private String topic;

	@Autowired
	CryptoCoreUtil cryptoCoreUtil;



	/**
	 * Gets the file.
	 *
	 * @param printRequest the print request DTO
	 * @param token        the token
	 * @param errors       the errors
	 * @param printRequest the print request DTO
	 * @return the file
	 * @throws Exception
	 * @throws RegPrintAppException the reg print app exception
	 */
	@PostMapping(path = "/callback/notifyPrint", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	//@PreAuthenticateContentAndVerifyIntent(secret = "${mosip.event.secret}", callback = "/v1/print/print/callback/notifyPrint", topic = "${mosip.event.topic}")
	public ResponseEntity<String> handleSubscribeEvent(@RequestBody EventModel eventModel) throws Exception {
		String credential = eventModel.getEvent().getData().get("credential").toString();
		String ecryptionPin = eventModel.getEvent().getData().get("protectionKey").toString();
		String decodedCrdential = cryptoCoreUtil.decrypt(credential);
		Map<String, String> proofMap = new HashMap<String, String>();
		proofMap = (Map<String, String>) eventModel.getEvent().getData().get("proof");
		String sign = proofMap.get("signature").toString();
		iDDecoderService.getDocuments(decodedCrdential,
				eventModel.getEvent().getData().get("credentialType").toString(), ecryptionPin,
				eventModel.getEvent().getTransactionId(),
				getSignature(sign, credential), "UIN", false);				
		return new ResponseEntity<>("successfully sent to genereate cryptograph", HttpStatus.OK);

	}

	private String getSignature(String sign, String crdential) {
		String signHeader = sign.split("\\.")[0];
		String signData = sign.split("\\.")[2];
		String signature = signHeader + "." + crdential + "." + signData;
		return signature;
	}

}
