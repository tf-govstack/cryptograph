package io.mosip.tf.t5.cryptograph.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Errors {
	String errorCode;
	String message;
}