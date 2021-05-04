# cryptograph
This is the interface between mosip and Tech5 idencode service.

### Build
The following commands should be used to build the project.

`mvn clean install`

### Deploy
The following command should be executed to run any service locally in specific profile and local configurations - 
`java -Dspring.profiles.active=<profile> -jar <jar-name>.jar

The following command should be executed to run service locally in specific profile and `remote` configurations - 
`java -Dspring.profiles.active=<profile> -Dspring.cloud.config.uri=<config-url> -Dspring.cloud.config.label=<config-label> -jar <jar-name>.jar`
  
 The following command should be executed to run a docker image - 
`docker run -it -p <host-port>:<container-port> -e active_profile_env={profile} -e spring_config_label_env= {branch} -e spring_config_url_env={config_server_url} <docker-registry-IP:docker-registry-port/<dcker-image>`


