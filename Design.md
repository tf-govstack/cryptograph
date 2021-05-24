# Design 

## Architecturel Diagram

  ![alt text](https://github.com/NagarjunaKuchi/cryptograph/blob/1.1.4/Cryptograph_Integrator.png?raw=true)
 
 
### Registration Processor:
  In MOSIP, registration processor is the main module. This module is responsible for validation of all resident details and generation of UIN. 
  After successfull generation of UIN, Registration processor will publish the resident data to websub component in the following format.
  
  ```
    {
	"event": {
		"data": {
			"credentialType": "euin",
			"credential": "xkOcWZsWlQRMdcjm3CBAy9Gzgpi_e9cGGMgBCwQRZPYN_9wxLfLK1UZNIRC_LLQrFg7aCo8vqxHs8kVXjCjHWZ7p4ICgWP3qO7NuanHBVk98-  6O1BsoBppr7NMYRsrs2fAtBIySBXFXlIzGyLBWesIMDMnmIRpCdOFtwQP4SW0hVa5cY4363oldkgeRwgm9Ki9w"
		},
		"dataShareUri": "null",
		"id": "a4bf01b1-a209-4bba-b58b-280743be5edf",
		"timestamp": "2021-03-29T10:55:01.586Z",
		"transactionId": "73127c68-e91b-4fe4-9d8d-12a11bd231e7",
		"type": {
			"name": "mosip",
			"namespace": "mosip"
		}
	},
	"publishedOn": "2021-03-29T10:55:01.586Z",
	"publisher": "CREDENTIAL_SERVICE",
	"topic": "mpartner-default-print/CREDENTIAL_ISSUED"
}

```

### Websub component:
 This is the centralized component. In MOSIP, most of the modules will share the data through websub. In this projcet, registartion processor is the publisher and
 cryptograph interface is the subscriber.
 
### Cryptograph Interface
  This module will subscribe the notifications for "mpartner-default-print/CREDENTIAL_ISSUED" topic. Whenever new UIN generation happens, registration processor will 
  publish data to websub and at the same time websub will notify to cryptograph interface. This module will extract the data and calls the idencode service for the 
  cryptograph generation.
  
  
### Cryptograph Service
  This service is responsible for cryptograph generation. More details of the service can be found on this link https://idencode.tech5-sa.com





