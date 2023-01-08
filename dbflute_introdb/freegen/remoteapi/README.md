# RemoteApiGen
Class generator plugin for [DBFlute](http://dbflute.seasar.org/) to access to Remote API.

This plugin generates classes for accessing remote api from the swagger specification file.

[about remote api](http://dbflute.seasar.org/ja/lastaflute/howto/architecture/remoteapicall.html)

## Setup for RemoteApiGen
- Add dependency on [lasta-remoteapi](https://github.com/lastaflute/lasta-remoteapi).

  e.g. pom.xml (In the case of maven)

  ```
  <dependencies>
      <dependency>
          <groupId>org.lastaflute.remoteapi</groupId>
          <artifactId>lasta-remoteapi</artifactId>
          <version>${lastaRemoteApiVersion}</version>
      </dependency>
  </dependencies>
  ```

  e.g. build.gradle (In the case of gradle)

  ```
  dependencies {
      compile "org.lastaflute.remoteapi:lasta-remoteapi:${lastaRemoteApiVersion}"
  }
  ```

- Copy & paste directories under `freegen/remoteapi` to `freegen/remoteapi` in your project's dbflute-client.

- Create `ControlFreeGen.vm` under `freegen` and add following.

  ```
  #parse("./remoteapi/ControlFreeGenRemoteApiJava.vm")
  ```

- Put the swagger definition file of the application to be connected with the remote API.

  e.g. src/main/resources/remoteapi/schema/remoteapi_schema_petstore.json

  If the swagger definition file is not provided, create it with swagger editor.  
  https://editor.swagger.io/

- Create a rule js file that defines the generation rule.

  e.g. src/main/resources/remoteapi/schema/remoteapi_schema_petstore_rule.js

  For details of generation rules, please refer to the following.  
  freegen/remoteapi/RemoteApiRule.js

- Add Remote API information to `dfprop/freeGenMap.dfprop`.

  ```
  map:{
      # ==========================================================================
      #                                                       Remote Api(Petstore)
      #                                                       ====================
      ; RemoteApiPetstore = map:{
          ; resourceMap = map:{
              ; baseDir = ../src/main
              ; resourceType = SWAGGER
              ; resourceFile = $$baseDir$$/resources/remoteapi/schema/remoteapi_schema_petstore.json
          }
          ; outputMap = map:{
              ; outputDirectory = ../src/main/java
              ; package = org.docksidestage.remote
          }
          ; tableMap = map:{
              ; ruleJsPath = ../src/main/resources/remoteapi/schema/remoteapi_schema_petstore_rule.js
          }
      }
  }
  ```

- Execute 'freegen' task of DBFlute.

  ```
  sh manage.sh 12
  ```

- Set default rules and base URL.

  e.g. org.docksidestage.remote.petstore.AbstractRemotePetstoreBhv

  ```
  // ===================================================================================
  //                                                                          Initialize
  //                                                                          ==========
  @Override
  protected void yourDefaultRule(FlutyRemoteApiRule rule) {
      // TODO you #change_it set your common default rule here
      // e.g. When you want to convert the field naming from CAMEL to LOWER_SNAKE.
      // FlSelectedMappingPolicy selectedMappingPolicy = new LaSelectedMappingPolicy().fieldNaming(FormFieldNaming.CAMEL_TO_LOWER_SNAKE);
      // JsonMappingOption jsonMappingOption = new JsonMappingOption().asFieldNaming(JsonFieldNaming.CAMEL_TO_LOWER_SNAKE);
      // rule.sendQueryBy(new LaQuerySender(selectedMappingPolicy));
      // rule.sendBodyBy(new LaJsonSender(requestManager, jsonMappingOption)); or rule.sendBodyBy(new LaFormSender(selectedMappingPolicy));
      // rule.receiveBodyBy(new LaJsonReceiver(requestManager, jsonMappingOption));
      throw new IllegalStateException("set your common default rule here.");
  }

  @Override
  protected String getUrlBase() {
      // TODO you #change_it set base URL for the remote api here
      throw new IllegalStateException("set url base here.");
  }
  ```

## Usage example

```java
RemotePetGetReturn ret = remotePetstorePetBhv.requestGet(petId);

remotePetstorePetBhv.requestPost(param -> {
    param.id = 0L;
    param.name = "name";
});
```

## Supported remote api product
- [lasta-remoteapi](https://github.com/lastaflute/lasta-remoteapi)

## License
Apache 2.0
