package co.pie.pie.unit

import co.pie.pie.build.ImageBuilder
import spock.lang.Specification
import spock.lang.Unroll

class ImageBuilderSpec extends Specification {


    @Unroll
    def "Created Docker command is correct"() {
        given: 'An image build object'
        ImageBuilder imageBuilder = new ImageBuilder(
                ['linux/amd64', 'linux/arm64'],
                new File('~/mock/docker/dir/Dockerfile'),
                'mock/dockertag:4',
                pushImage,
                [buildArg1: 'valueOne', buildArg2: 'valueTwo']
        )
        when: 'The docker command is created'
        String dockerCommand = imageBuilder.createDockerBuildCommand()

        then: 'The created docker command has the expected form'
        dockerCommand == command

        where:
        pushImage | command
        true      | 'docker buildx build --platform=linux/amd64,linux/arm64 --build-arg buildArg1=valueOne --build-arg buildArg2=valueTwo -t mock/dockertag:4 --push .'
        false     | "docker buildx build --platform=linux/amd64,linux/arm64 --build-arg buildArg1=valueOne --build-arg buildArg2=valueTwo -t mock/dockertag:4 -o type=tar,dest=${System.getProperty("java.io.tmpdir")}out.tar ."
    }
}
