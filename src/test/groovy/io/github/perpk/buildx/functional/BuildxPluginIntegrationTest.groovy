package io.github.perpk.buildx.functional


import io.github.perpk.buildx.util.DockerCommandUtil
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import spock.lang.Specification
import spock.lang.TempDir
import spock.lang.Unroll


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

    @Unroll
    def "Image created #label"() {
        given:
        File dockerFile = "${dockerFileCreator}"(testProjectDir)
        String tag = "test/${UUID.randomUUID().toString()}"
        buildFile << """
            buildxImage {
                imageTag = '$tag'
                dockerfilePath = file('${dockerFile.name}')
                pushImageToRemote = false
                targetPlatforms = ['linux/arm64']
                buildArgs = [testArg1: 'val1', testArg2: 'val2']
            }
        """

        when:
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments('buildxImage')
                .withPluginClasspath()
                ."${buildOrFail}"()
        then:
        println result.output
        result.task(':buildxImage').outcome == outcome
        result.output.contains(expectedMessageContent)
        if (checkImageSizeNotZero) {
            assert DockerCommandUtil.getImageSize(tag) != '0B'
        }

        cleanup:
        DockerCommandUtil.removeImage(tag)

        where:
        dockerFileCreator        | outcome             | buildOrFail    | label        | expectedMessageContent | checkImageSizeNotZero
        'createDockerFile'       | TaskOutcome.SUCCESS | 'build'        | 'successful' | 'sha256'               | true
        'createFaultyDockerFile' | TaskOutcome.FAILED  | 'buildAndFail' | 'failed'     | 'pull access denied'   | false
    }

    File createDockerFile(testDir) {
        File dockerFile = new File(testDir, 'Dockerfile')
        dockerFile << """
            FROM --platform=\$BUILDPLATFORM node:12.13.0-alpine as build
            ARG testArg1
            ARG testArg2
            
            FROM nginx
            EXPOSE 3000
        """
        dockerFile
    }

    File createFaultyDockerFile(testDir) {
        File dockerFile = new File(testDir, 'Dockerfile')
        dockerFile << """
            FROM --platform=\$BUILDPLATFORM random:random as build
        """
    }
}
