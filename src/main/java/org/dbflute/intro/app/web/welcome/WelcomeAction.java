package org.dbflute.intro.app.web.welcome;

import org.dbflute.intro.app.logic.client.ClientInfoLogic;
import org.dbflute.intro.app.logic.client.ClientUpdateLogic;
import org.dbflute.intro.app.logic.core.PublicPropertiesLogic;
import org.dbflute.intro.app.logic.dfprop.TestConnectionLogic;
import org.dbflute.intro.app.logic.engine.EngineInstallLogic;
import org.dbflute.intro.app.model.client.ClientModel;
import org.dbflute.intro.app.model.client.ProjectMeta;
import org.dbflute.intro.app.model.client.basic.BasicInfoMap;
import org.dbflute.intro.app.model.client.database.DatabaseInfoMap;
import org.dbflute.intro.app.model.client.database.DbConnectionBox;
import org.dbflute.intro.app.model.client.database.various.AdditionalSchemaMap;
import org.dbflute.intro.app.web.base.IntroBaseAction;
import org.dbflute.intro.app.web.client.ClientCreateBody;
import org.dbflute.optional.OptionalThing;
import org.lastaflute.web.Execute;
import org.lastaflute.web.response.JsonResponse;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 * @author hakiba
 */
public class WelcomeAction extends IntroBaseAction {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Resource
    private ClientInfoLogic clientInfoLogic;
    @Resource
    private ClientUpdateLogic clientUpdateLogic;
    @Resource
    private TestConnectionLogic testConnectionLogic;
    @Resource
    private EngineInstallLogic engineInstallLogic;
    @Resource
    private PublicPropertiesLogic publicPropertiesLogic;

    // ===================================================================================
    //                                                                             Execute
    //                                                                             =======
    @Execute
    public JsonResponse<Void> create(ClientCreateBody clientCreateBody) {
        validate(clientCreateBody, messages -> {
            String projectName = clientCreateBody.client.projectName;
            if (clientInfoLogic.getProjectList().contains(projectName)) {
                messages.addErrorsWelcomeClientAlreadyExists("projectName", projectName); // TODO: hakiba refactor type-safe (2016/10/10)
            }
        });
        String latestVersion = publicPropertiesLogic.findProperties().getDBFluteLatestReleaseVersion();
        engineInstallLogic.downloadUnzipping(latestVersion);
        ClientModel clientModel = mappingToClientModel(clientCreateBody.client);
        if (clientCreateBody.testConnection) {
            testConnectionIfPossible(clientModel);
        }
        clientUpdateLogic.createClient(clientModel);
        return JsonResponse.asEmptyBody();
    }

    protected ClientModel mappingToClientModel(ClientCreateBody.ClientPart clientBody) {
        ClientModel clientModel = newClientModel(clientBody);
        // TODO jflute intro: re-making (2016/08/12)
        return clientModel;
    }

    private ClientModel newClientModel(ClientCreateBody.ClientPart clientBody) {
        ProjectMeta projectMeta = prepareProjectMeta(clientBody);
        BasicInfoMap basicInfoMap = prepareBasicInfoMap(clientBody);
        DatabaseInfoMap databaseInfoMap = prepareDatabaseInfoMap(clientBody);
        ClientModel clientModel = new ClientModel(projectMeta, basicInfoMap, databaseInfoMap);
        return clientModel;
    }

    private ProjectMeta prepareProjectMeta(ClientCreateBody.ClientPart clientBody) {
        return new ProjectMeta(clientBody.projectName, clientBody.dbfluteVersion, clientBody.jdbcDriverJarPath);
    }

    private BasicInfoMap prepareBasicInfoMap(ClientCreateBody.ClientPart clientBody) {
        return new BasicInfoMap(clientBody.databaseCode, clientBody.languageCode, clientBody.containerCode, clientBody.packageBase);
    }

    private DatabaseInfoMap prepareDatabaseInfoMap(ClientCreateBody.ClientPart clientBody) {
        return OptionalThing.ofNullable(clientBody.mainSchemaSettings, () -> {}).map(databaseBody -> {
            DbConnectionBox connectionBox =
                new DbConnectionBox(databaseBody.url, databaseBody.schema, databaseBody.user, databaseBody.password);
            AdditionalSchemaMap additionalSchemaMap = new AdditionalSchemaMap(new LinkedHashMap<>()); // #pending see the class code
            return new DatabaseInfoMap(clientBody.jdbcDriverFqcn, connectionBox, additionalSchemaMap);
        }).orElseThrow(() -> {
            return new IllegalStateException("Not found the database body: " + clientBody);
        });
    }

    private void testConnectionIfPossible(ClientModel clientModel) {
        String dbfluteVersion = clientModel.getProjectMeta().getDbfluteVersion();
        OptionalThing<String> jdbcDriverJarPath = clientModel.getProjectMeta().getJdbcDriverJarPath();
        DatabaseInfoMap databaseInfoMap = clientModel.getDatabaseInfoMap();
        testConnectionLogic.testConnection(dbfluteVersion, jdbcDriverJarPath, databaseInfoMap);
    }

}
