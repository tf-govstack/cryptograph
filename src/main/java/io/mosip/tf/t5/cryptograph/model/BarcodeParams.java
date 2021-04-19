package io.mosip.tf.t5.cryptograph.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BarcodeParams  implements Serializable{
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -9214197644488947793L;
	

	public int blockCols = 4;
    
    public int blockRows = 4;
    
    public int errorCorrection = 10;
    
    public int gridSize = 6;
    
    public int thickness = 2;
    
    public BarcodeTitle title;
    
    public String expirationDate;
}
