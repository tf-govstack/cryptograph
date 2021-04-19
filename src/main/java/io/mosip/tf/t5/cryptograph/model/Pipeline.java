package io.mosip.tf.t5.cryptograph.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pipeline implements Serializable{	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FaceParam facePipeline;
    
    private BarcodeParams barcodeGenerationParameters;
    
    private EmailParam emailParams;
    
    private FingerParam fingerParams;

}
