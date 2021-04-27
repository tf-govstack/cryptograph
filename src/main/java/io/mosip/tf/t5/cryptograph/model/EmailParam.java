package io.mosip.tf.t5.cryptograph.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String emailto;
	
	public String subject;
}
