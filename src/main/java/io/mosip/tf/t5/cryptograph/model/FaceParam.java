package io.mosip.tf.t5.cryptograph.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaceParam implements Serializable{

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean performTemplateExtraction;
    
    public float faceDetectorConfidence = 0.6f;
    
    public int faceSelectorAlg = 1;
    
    public boolean performCompression;
    
    public int compressionLevel;
}
