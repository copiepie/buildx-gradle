package co.pie.pie;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;

public abstract class BuildxGradleExtension {
  abstract Property<String> getImageTag();

  abstract RegularFileProperty getDockerfilePath();

  abstract Property<Boolean> getPushImageToRemote();

  abstract ListProperty<String> getTargetPlatforms();

  abstract MapProperty<String, String> getBuildArgs();
}
