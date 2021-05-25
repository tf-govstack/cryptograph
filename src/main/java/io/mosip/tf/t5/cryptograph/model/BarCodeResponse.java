package io.mosip.tf.t5.cryptograph.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarCodeResponse {

    private String uuid;
    private String image;
}
