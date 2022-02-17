package co.pie.pie

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification
import spock.lang.TempDir


class BuildxPluginIntegrationTest extends Specification {
    @TempDir
    File testProjectDir
    File buildFile

    def setup() {
        buildFile = new File(testProjectDir, 'build.gradle')
        buildFile << """
            plugins {
                id 'co.pie.pie.buildxImagePlugin'
            }
        """
    }

    def "Image is created successfully"() {
        given:
        String imageId = UUID.randomUUID().toString()
        File dockerFile = createDockerFile()
        buildFile << """
            buildxImage {
                imageTag = 'test/$imageId'
                dockerfilePath = file('${dockerFile.name}')
                pushImageToRemote = false
                targetPlatforms = ['linux/arm64']
            }
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments('buildxImage')
                .withPluginClasspath()
                .build()
        then:
        println result.output
        result.task(':buildxImage').outcome == TaskOutcome.SUCCESS
    }

    File createDockerFile() {
        File dockerFile = new File(testProjectDir, 'Dockerfile')
        dockerFile << """
        FROM --platform=\$BUILDPLATFORM node:12.13.0-alpine as build
        
        FROM nginx
        EXPOSE 3000
"""
        dockerFile
    }
}
