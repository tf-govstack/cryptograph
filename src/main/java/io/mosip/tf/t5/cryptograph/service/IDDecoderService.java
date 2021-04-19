package io.mosip.tf.t5.cryptograph.service;

public interface IDDecoderService {
    
	/**
	 * Gets the documents.
	 *
	 * @param type
	 *            the type
	 * @param idValue
	 *            the id value
	 * @param cardType
	 *            the card type
	 * @param isPasswordProtected
	 *            the is password protected
	 * @return the documents
	 */
	public void getDocuments(String credentialSubject, String credentialType, String encryptionPin, String requestId,
			String sign,
			String cardType, boolean isPasswordProtected);	
}