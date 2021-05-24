# Prerequisites
  The following data should be present in mosip_pms database as a default.
  
  ```
  partner : mpartner-default-print
  auth_policy: mpolicy-default-reprint
  
  ```
  If above data is not present, follow the below steps to create the above required data.<br/>
    1. Create policy group <br/>
    2. Create data share policy <br/>
    3. Create partner <br/>
    4. Upload partner certs <br/>
    5. Request for apiKey <br/>
    6. Approve apikey <br/>
    
   To execute the above steps authentication is required. <br/>
   Resource URL: POST  `https://{domain}/v1/authmanager/authenticate/useridPwd` <br/>
   Request :   <br/>
   ```json
    {
            "id": "string",
            "metadata": {},
            "request": {
              "appId": "partner",
              "password": "Techno@123",
              "userName": "110124"
            },
            "requesttime": "2018-12-10T06:12:52.994Z",
            "version": "string"
       }
   ```
   ### 1. Create policy group 
   Resource URL : POST `https://{domain}/partnermanagement/v1/policies/policies/policyGroup` <br/>
   Request : <br/>
   ```json
    {
              "id": "string",
              "metadata": {},
              "request": {
                "desc": "mpolicygroup-deafult-print",
                "name": "mpolicygroup-deafult-print"
              },
              "requesttime": "2018-12-10T06:12:52.994Z",
              "version": "string"
            }
   ```
   ### 2. Create policy 
   Resource URL : POST `https://{domain}/partnermanagement/v1/policies/policies` <br/>
   Request : <br/>
   ```json
    {
            "id": "string",
            "metadata": {},
            "request": {
              "desc": "mpolicy-default-reprint",
              "name": "mpolicy-default-reprint",
              "policies": {
                "dataSharePolicies": {
                  "typeOfShare": "direct",
                  "validForInMinutes": "30",
                  "transactionsAllowed": "2",
                  "encryptionType": "Partner Based",
                  "shareDomain": "datashare-service",
                  "source": "ID Repository"
                },
                "shareableAttributes": [{
                  "attributeName": "fullName",
                  "source": [{
                    "attribute": "fullName",
                    "filter": [{
                      "language": "eng"
                    }]
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "dateOfBirth",
                  "source": [{
                    "attribute": "dateOfBirth"
                  }],
                  "encrypted": false,
                  "format": "YYYY"
                }, {
                  "attributeName": "gender",
                  "source": [{
                    "attribute": "gender"
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "phone",
                  "source": [{
                    "attribute": "phone"
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "email",
                  "source": [{
                    "attribute": "email"
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "addressLine1",
                  "source": [{
                    "attribute": "addressLine1"
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "addressLine2",
                  "source": [{
                    "attribute": "addressLine2"
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "addressLine3",
                  "source": [{
                    "attribute": "addressLine3"
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "region",
                  "source": [{
                    "attribute": "region"
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "province",
                  "source": [{
                    "attribute": "province"
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "city",
                  "source": [{
                    "attribute": "city"
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "UIN",
                  "source": [{
                    "attribute": "UIN"
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "postalCode",
                  "source": [{
                    "attribute": "postalCode"
                  }],
                  "encrypted": false
                }, {
                  "attributeName": "biometrics",
                  "group": "CBEFF",
                  "source": [{
                    "attribute": "individualBiometrics",
                    "filter": [{
                      "type": "Face"
                    }, {
                      "type": "Finger"
                    }]
                  }],
                  "encrypted": true,
                  "format": "extraction"
                }]
              },
              "policyGroupName": "mpolicygroup-deafult-print",
              "policyId": "mpolicy-default-reprint",
              "policyType": "DataShare",
              "version": "v1"
            },
            "requesttime": "2018-12-10T06:12:52.994Z",
            "version": "string"
          }
   ```
  ####2.1 Publish Policy <br/>
  Resource URL: POST `https://{domain}/partnermanagement/v1/policies/policies/publishPolicy/policyGroupId/{policyGroupId}/policyId/{policyId}` <br/>
    polciyGroupId and policyId are getting from above responses.
  
 ### 3. Create Partner 
 Resource URL : POST `https://{domain}/partnermanagement/v1/partners/partners` <br/>
 Request : <br/>
 ```json
      {
        "id": "string",
        "metadata": {},
        "request": {
          "address": "Bangalore",
          "contactNumber": "9908766554",
          "emailId": "info@gmail.com",
          "organizationName": "Tech5",
          "partnerId": "mpartner-default-print",
          "partnerType": "Auth_Partner",
          "policyGroup": "mpolicygroup-deafult-print"
        },
        "requesttime": "",
        "version": "string"
      }
  ```
  ### 4. Upload Partner certs
   **4.1 Upload CA Cert:**  <br/>  
    Resource URL : `https://{domain}/partnermanagement/v1/partners/partners/uploadCACertificate` <br/>
    Request :  <br/>
    ```json
      {
        "id": "string",
        "metadata": {},
        "request": {
                "certificateData": "-----BEGIN CERTIFICATE-----\nMIIDFTCCAf2g     AwIBAgIESXfzlDANBgkqhkiG9w0BAQsFADA7MQswCQYDVQQGEwJJ\nTjELMAkGA1UECBMCS0ExDTALBgNVBAoTBFRlc3QxEDAOBgNVBAMTB1JPT1QtQ0Ew\nHhcNMjAxMjE4MTM1NDM0WhcNMjMxMjE4MTM1NDM0WjA7MQswCQYDVQQGEwJJTjEL\nMAkGA1UECBMCS0ExDTALBgNVBAoTBFRlc3QxEDAOBgNVBAMTB1JPT1QtQ0EwggEi\nMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC8gwrXv+9PXyY4P4+6Kr2i5EYE\nycrqoBz0UrZYT+mIUl1JF7CFddujpy8ejF78RbgjZoCqcxmi31Abj6Sj6MPRlMXI\nXVWzE6DQPK2xHn+3it8es+QAHt2Z4zDJSICm2DI9qCQa95iTl/WbrQK4nHYeiSx3\nBOQuf05pNSiPcrBqP+xwxcPkNVIl0eRbs0Lnd37qWh9ZBIy3N4tJduneJGRwW1qs\nYvTTUxPBIlCBraqLq7o9uxMr9+kOv1wxesV8ZGOS3KFa4CQxOHywJ1secnMnca0e\ntQQcmSzckG+8QqKQQNbfFSRYkRkfOM4aHRDozmMMTdI0TA9eELZxFMXiTkjlAgMB\nAAGjITAfMB0GA1UdDgQWBBSKXY7NPI6uL5BhbICYl4wWQUdEOTANBgkqhkiG9w0B\nAQsFAAOCAQEAbk+qM7X1G0SRH/bHEu0Yntx2Lae+iYyrCLEHOgJ+ygQ4u/FzGjQQ\nJWGWZtqpehE0sN/CBB/xfz3FDAJdj7iNYD3H0fNRNywpQWQt1EzKkwJFxgJ16P7l\nq9DzZpz1EuFZC/eqbkFhQSqbRrBnLVzYgAc9RZnaqRS5TSF/J+Qj+uUYbcGeWBRN\nPVcPwM7Nx0sQLHosPYZt7YTytr2P9NVZgBGLre+aNECWeRh3hBIBZ52qSVDq/1Df\nW9sqNLTel9i0l5eNC9AuIf8TwSqGUUTkHqLI4lk5KoUfei15DDOPLOESqplYYezb\nY6Ujm9OwNYFSDuS2IMTAIxzheHt/XJnxMw==\n-----END CERTIFICATE-----",
              "partnerDomain": "Auth"
            },
        "requesttime": "2021-02-04T10:03:17.406Z",
        "version": "string"
      } 
    ```
   
   **4.2 Upload partner cert**
   Resource URL : `https://{domain}/partnermanagement/v1/partners/partners/uploadPartnerCertificate` <br/>
   Request : <br/>
   ```json
    {
  "id": "string",
  "metadata": {},
  "request": {
    "certificateData": "-----BEGIN CERTIFICATE-----\nMIIDPTCCAiWgAwIBAgIEBiaW/DANBgkqhkiG9w0BAQsFADA7MQswCQYDVQQGEwJJ\nTjELMAkGA1UECBMCS0ExDTALBgNVBAoTBFRlc3QxEDAOBgNVBAMTB1JPT1QtQ0Ew\nHhcNMjAxMjE4MTM1NjQwWhcNMjExMjE4MTM1NjQwWjBCMQswCQYDVQQGEwJJTjEL\nMAkGA1UECBMCS0ExDTALBgNVBAoTBFRlc3QxFzAVBgNVBAMTDlNpZ25lZC1QYXJ0\nbmVyMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAstCVDizWIlcXj4EQ\nhFfoE3jd3MaDNjHXA5MC4PpwHuHnDl9EGrAKEXL2j7tdOQBBMu6VBLSDTnG2jInT\ne33Ou/yNLQX6/47FXZnRmPQ7ZqUL4ETp+bSBah9spQVQ9Uh7jxfWBeq9pA1xHrLh\nkDm0JCIYKQxqupnZZFdl4M7QmhKtpMct5KkwU5a4J+6an0uSX+ZdGr0lB/mtKEyh\n2wJlJG7THKMCbrODLH3Fhw+1Gz0e9qYF+tvM7T8tQGAUzeXc7Mr/+vE1RsC3IzQf\nwbjO19b2GLYEHLXNKbYYr3c9H+/GaaWJArMDmp+MNG8M0J/JPlxhsUi5mD/y4fFP\n4VWEsQIDAQABo0IwQDAdBgNVHQ4EFgQUQuT6c0WImkDDnQHTK0gx3DRjvBswHwYD\nVR0jBBgwFoAUil2OzTyOri+QYWyAmJeMFkFHRDkwDQYJKoZIhvcNAQELBQADggEB\nAAyLGiqdH6gTXgjDbngmdNbNuusTeflVaqdWtteIBgXvn01aigK/9eQqAGsQH6SO\nICae+GCmTTAWF5wkZbecPSravndzMNNtCVbhq32+liQHkNIzZCsc0Ous+Ifr+zOE\nJ1nc/3/Wrx5q2OH7A0IvE/wIBi1KHLxcDdkXL6E27TUiXvT3tgITtpVKq5UPcpag\nHnf80AVZ/6CA+kbTxgsXg+0ScVCy1lCuw25HdMhEr4MS3vGEPfv5yttLz7ImNY1L\nMIAyRu4DzDctAebecNOVIL6Jp3o4MCl458p/kVqKluGMW3A9c0+iim1wlmwhWTin\nptNsenIRS+hBPLYHJZ6sz4I=\n-----END CERTIFICATE-----",
    "organizationName": "Test",
    "partnerDomain": "Auth",
    "partnerId": "mpartner-default-print",
    "partnerType": "Credential_Partner"
  },
  "requesttime": "2020-12-31T04:51:15.390Z",
  "version": "string"
}
   ```
 
  ### 5. Request for apiKey
  Resource URL : PATCH `https://{domain}/partnermanagement/v1/partners/partners/mpartner-default-print/partnerAPIKeyRequests` <br/>
  Request : <br/>
  ```json
    {
        "id": "string",
        "metadata": {},
        "request": {
          "policyName": "mpolicy-default-reprint",
          "useCaseDescription": "mpolicy-default-reprint"
        },
        "requesttime": "",
        "version": "string"
      }    
 ```
### 6. Approve apiKey
Resource URL : PATCH `https://{domain}/partnermanagement/v1/pmpartners/pmpartners/PartnerAPIKeyRequests/{apiKey}` <br/>
    apiKey have to capture from step 5 response. <br/>
Request : <br/>
  ```JSON
      {
        "id": "string",
        "metadata": {},
        "request": {
          "status": "approve"
        },
        "requesttime": "",
        "version": "string"
      }
   ```
   
   
