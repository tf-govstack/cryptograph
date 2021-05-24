# Prerequisites
  The following data should be present in mosip_pms database as a default.
  
  ```
  partner : mpartner-default-print
  auth_policy: mpolicy-default-reprint
  
  ```
  If above data is not present, follow the below steps to create the above required data.
    1. Create policy group 
    2. Create data share policy
    3. Create partner
    4. Upload partner certs
    5. Request for apiKey
    6. Approve apikey
    
   To execute the above steps authentication is required. 
   URL: POST https://{domain}/v1/authmanager/authenticate/useridPwd
   Request : 
      ```
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
   URL : POST https://{domain}/partnermanagement/v1/policies/policies/policyGroup
   Request :
      ```
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
   URL : POST https://{domain}/partnermanagement/v1/policies/policies
   Request : 
      ```
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
  ####2.1 Publish Policy
  URL: POST https://{domain}/partnermanagement/v1/policies/policies/publishPolicy/policyGroupId/{policyGroupId}/policyId/{policyId}
    polciyGroupId and policyId are getting from above responses.
  
 ### 3. Create Partner
 URL : POST https://{domain}/partnermanagement/v1/partners/partners
 Request :
    ```
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
  URL : 
  Request :
  
  ### 5. Request for apiKey
  URL : PATCH https://{domain}/partnermanagement/v1/partners/partners/mpartner-default-print/partnerAPIKeyRequests
  Request :
    ```
      {
        "id": "string",
        "metadata": {},
        "request": {
          "policyName": "mpolicy-default-reprint2",
          "useCaseDescription": "mpolicy-default-reprint"
        },
        "requesttime": "",
        "version": "string"
      }
     ```
### 6. Approve apiKey
URL : PATCH https://{domain}/partnermanagement/v1/pmpartners/pmpartners/PartnerAPIKeyRequests/{apiKey}
    apiKey have to capture from step 5 response.
Request :
  ```
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
  
   
   
