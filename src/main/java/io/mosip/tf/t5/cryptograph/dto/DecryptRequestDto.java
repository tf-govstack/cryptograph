package io.mosip.tf.t5.cryptograph.dto;

import lombok.Data;

@Data
public class DecryptRequestDto {

	private String userPin;

	private String data;

}
