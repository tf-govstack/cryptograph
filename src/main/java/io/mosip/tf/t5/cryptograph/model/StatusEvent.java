package io.mosip.tf.t5.cryptograph.model;

import lombok.Data;

@Data
public class StatusEvent {

		private String id;
	
		private String requestId;
		
		private String timestamp;
		
		private String status;
		
		private String url;
}
