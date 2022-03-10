package co.pie.pie;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class BuildxGradlePlugin implements Plugin<Project> {

  @Override
  public void apply(Project project) {
    project.getExtensions().create("buildxImage", BuildxGradleExtension.class);

    project
        .getTasks()
        .register(
            "buildxImage",
            BuildxImageTask.class,
            t -> {
              BuildxGradleExtension extension =
                  project.getExtensions().findByType(BuildxGradleExtension.class);
              t.imageTag = extension.getImageTag().get();
              t.dockerfilePath = extension.getDockerfilePath().get().getAsFile();
              t.targetPlatforms = extension.getTargetPlatforms().get();
              t.pushImageToRemote = extension.getPushImageToRemote().get();
              t.buildArgs = extension.getBuildArgs().get();
            });
  }
}
