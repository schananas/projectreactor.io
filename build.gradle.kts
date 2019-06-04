/*
 * Copyright (c) 2011-Present Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import com.github.jengelman.gradle.plugins.shadow.ShadowExtension
import com.github.robfletcher.compass.CompassExtension
import java.util.concurrent.TimeUnit

configurations.all {
    resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.SECONDS)

    //force JRuby version, bumping Ruby to 2.5
    resolutionStrategy.force("org.jruby:jruby-complete:9.2.5.0")
}

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "4.0.4"
    id("com.github.anbuck.compass") version "2.0.7"
}

group = "io.projectreactor"
version = "1.0.0.BUILD-SNAPSHOT"

configure<ApplicationPluginConvention> {
	mainClassName = "io.projectreactor.Application"
}

configure<ShadowExtension> {
	version = ""
}

configure<CompassExtension> {
    sassDir = file("$projectDir/src/main/sass")
    cssDir = file("$buildDir/resources/main/static/assets/css")
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { setUrl("https://repo.spring.io/milestone") }
    maven { setUrl("https://repo.spring.io/snapshot") }
}

dependencies {
    compile("org.springframework:spring-core:5.1.3.RELEASE")
    compile("io.projectreactor.netty:reactor-netty:0.8.6.RELEASE") //TODO downgrade until wildcard routing regression fixed
    compile("io.projectreactor:reactor-core:3.2.9.RELEASE")
    compile("org.thymeleaf:thymeleaf:3.0.9.RELEASE")
    compile("org.yaml:snakeyaml:1.17")
    runtime("commons-logging:commons-logging:1.2")
    runtime("org.slf4j:slf4j-api:1.7.21")
    runtime("ch.qos.logback:logback-classic:1.1.7")

    testCompile("junit:junit:4.12")
    testCompile("org.assertj:assertj-core:3.6.1")
}

val processResources = tasks.getByName("processResources")
val compassCompile = tasks.getByName("compassCompile")
processResources.dependsOn(compassCompile)
