package io.mosip.tf.t5.cryptograph.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarcodeTitle  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5945694158208198553L;

	public String text;
	
	public String location;
	
	public String alignment;
	
	public int offset;

}
