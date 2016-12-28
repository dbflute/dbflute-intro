package org.dbflute.intro.app.web.client;

import org.dbflute.intro.app.logic.client.ClientInfoLogic;
import org.dbflute.intro.app.logic.client.ClientUpdateLogic;
import org.dbflute.intro.app.logic.dfprop.TestConnectionLogic;
import org.dbflute.intro.app.logic.document.DocumentPhysicalLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ProjectMeta;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.database.various.AdditionalSchemaMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.bizfw.tellfailure.ClientNotFoundException;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.core.time.TimeManager;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 * @author hakiba
 */
public class ClientSettingsAction extends IntroBaseAction {


    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClientUpdateLogic clientUpdateLogic;
    @Resource
    private ClientInfoLogic clientInfoLogic;
    @Resource
    private TestConnectionLogic testConnectionLogic;
    @Resource
    private DocumentPhysicalLogic documentLogic;
    @Resource
    private TimeManager timeManager;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    // -----------------------------------------------------
    //                                              Settings
    //                                              --------
    @Execute
    public JsonResponse<ClientSettingsResult> index(String clientProject) {
        ClientModel clientModel = clientInfoLogic.findClient(clientProject).orElseThrow(() -> {
            return new ClientNotFoundException("Not found the project: " + clientProject, clientProject);
        });
        ClientSettingsResult result = mappingToSettingsResult(clientModel);
        return asJson(result);
    }

    private ClientSettingsResult mappingToSettingsResult(ClientModel clientModel) {
        ClientSettingsResult result = new ClientSettingsResult();
        ProjectMeta projectMeta = clientModel.getProjectMeta();
        BasicInfoMap basicInfoMap = clientModel.getBasicInfoMap();
        result.projectName = projectMeta.getClientProject();
        result.databaseCode = basicInfoMap.getDatabase();
        result.languageCode = basicInfoMap.getTargetLanguage();
        result.containerCode = basicInfoMap.getTargetContainer();
        result.packageBase = basicInfoMap.getPackageBase();
        result.jdbcDriverFqcn = clientModel.getDatabaseInfoMap().getDriver();
        result.dbfluteVersion = projectMeta.getDbfluteVersion();
        result.jdbcDriverJarPath = projectMeta.getJdbcDriverJarPath().orElse(null);
        DbConnectionBox dbConnectionBox = clientModel.getDatabaseInfoMap().getDbConnectionBox();
        result.mainSchemaSettings = new ClientSettingsResult.DatabaseSettingsPart();
        result.mainSchemaSettings.url = dbConnectionBox.getUrl();
        result.mainSchemaSettings.schema = dbConnectionBox.getSchema();
        result.mainSchemaSettings.user = dbConnectionBox.getUser();
        result.mainSchemaSettings.password = dbConnectionBox.getPassword();

        return result;
    }

    // -----------------------------------------------------
    //                                                  Edit
    //                                                  ----
    @Execute
    public JsonResponse<Void> edit(String projectName, ClientUpdateBody clientBody) {
        validate(clientBody, messages -> {});
        ClientModel clientModel = mappingToClientModel(projectName, clientBody.client);
        clientUpdateLogic.updateClient(clientModel);
        return JsonResponse.asEmptyBody();
    }

    private ClientModel mappingToClientModel(String projectName, ClientUpdateBody.ClientPart clientBody) {
        ClientModel clientModel = newClientModel(projectName, clientBody);
        // TODO jflute intro: re-making (2016/08/12)
        return clientModel;
    }

    private ClientModel newClientModel(String projectName, ClientUpdateBody.ClientPart clientBody) {
        ProjectMeta projectMeta = prepareProjectMeta(projectName, clientBody);
        BasicInfoMap basicInfoMap = prepareBasicInfoMap(clientBody);
        DatabaseInfoMap databaseInfoMap = prepareDatabaseInfoMap(clientBody);
        ClientModel clientModel = new ClientModel(projectMeta, basicInfoMap, databaseInfoMap);
        return clientModel;
    }

    private ProjectMeta prepareProjectMeta(String projectName, ClientUpdateBody.ClientPart clientBody) {
        return new ProjectMeta(projectName, clientBody.dbfluteVersion, clientBody.jdbcDriverJarPath);
    }

    private BasicInfoMap prepareBasicInfoMap(ClientUpdateBody.ClientPart clientBody) {
        return new BasicInfoMap(clientBody.databaseCode, clientBody.languageCode, clientBody.containerCode, clientBody.packageBase);
    }

    private DatabaseInfoMap prepareDatabaseInfoMap(ClientUpdateBody.ClientPart clientBody) {
        return OptionalThing.ofNullable(clientBody.mainSchemaSettings, () -> {}).map(databaseBody -> {
            DbConnectionBox connectionBox =
                new DbConnectionBox(databaseBody.url, databaseBody.schema, databaseBody.user, databaseBody.password);
            AdditionalSchemaMap additionalSchemaMap = new AdditionalSchemaMap(new LinkedHashMap<>()); // #pending see the class code
            return new DatabaseInfoMap(clientBody.jdbcDriverFqcn, connectionBox, additionalSchemaMap);
        }).orElseThrow(() -> {
            return new IllegalStateException("Not found the database body: " + clientBody);
        });
    }
}
