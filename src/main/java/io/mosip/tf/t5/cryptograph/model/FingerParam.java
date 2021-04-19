package io.mosip.tf.t5.cryptograph.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FingerParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1935803927739445849L;

	private String fieldId;
	
	private String fingerType;
	
	private String impressionType;
	
	private String templateType;
	
	private int maxTemplateSize;
	
	private boolean performSegmentation;
	
	private String segmentationType;
	
	private String segmentationHand;
	
	private int age;
	
	private int gender;
	
}
