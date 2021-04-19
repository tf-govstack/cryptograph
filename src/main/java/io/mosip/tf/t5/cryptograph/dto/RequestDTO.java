package io.mosip.tf.t5.cryptograph.dto;

import java.io.Serializable;

import io.mosip.tf.t5.cryptograph.constant.IdType;
import lombok.Data;

/**
 * Instantiates a new request DTO.
 *
 */

/**
 * Instantiates a new request DTO.
 */
@Data
public class RequestDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The idtype. */
	private IdType idtype;

	/** The id value. */
	private String idValue;

	/** The card type. */
	private String cardType;
}
