// this block is necessary to make enforcedPlatform work for Quarkus plugin available
// only locally (snapshot) that is also importing the Quarkus BOM
buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath "io.quarkus:quarkus-gradle-plugin:${quarkusVersion}"
    }
}

plugins {
    id 'java'
}

apply plugin: 'io.quarkus'

repositories {
     mavenLocal()
     mavenCentral()
}

dependencies {
    implementation enforcedPlatform("io.quarkus:quarkus-bom:${quarkusVersion}")
    implementation 'io.quarkus:quarkus-resteasy'

    testImplementation 'io.quarkus:quarkus-junit5'
    testImplementation 'io.rest-assured:rest-assured'
}

group '${project_groupId}'
version '${project_version}'

compileJava {
    options.compilerArgs << '-parameters'
}



