package de.triplet.gradle.play

import com.android.build.gradle.api.ApplicationVariant
import com.android.builder.model.SigningConfig
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.androidpublisher.AndroidPublisher
import com.google.api.services.androidpublisher.model.AppEdit
import org.gradle.api.DefaultTask
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.TaskExecutionException

class LatestVersionCodeTask extends PlayPublishTask {

    @TaskAction
    updateVersion(){
        try {
            publish()
            // Get a list of apks.
            def apksResponse = edits.apks()
                    .list(variant.applicationId, editId)
                    .execute()
            //Get latest apk (at least one apk should be there, otherwise it will throw an exception earlier)
            def apk = apksResponse.apks.last()

            //delete latest edit
            edits.delete(variant.applicationId, editId).execute()

            variant.mergedFlavor.versionCode =  apk.versionCode + 1;
        } catch (Exception ex){
            throw new TaskExecutionException(this, ex)
        }
        logger.warn("Version set to " + variant.mergedFlavor.versionCode)
    }
}
