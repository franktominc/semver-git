package com.cinnober.gradle.java

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.tasks.bundling.Jar

class CinnoberJavaPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.apply plugin: 'maven'
        project.apply plugin: 'java'
        project.apply plugin: 'eclipse'
        
        project.repositories {
            mavenLocal()
            maven {
                url "http://nexus.cinnober.com/nexus/content/groups/public/"
            }
            maven {
                url "http://nexus.cinnober.com/nexus/content/repositories/buildtest_snapshots/"
            }
            maven {
                url "http://nexus.cinnober.com/nexus/content/repositories/buildtest_releases/"
            }
        }
        project.uploadArchives {
            repositories {
                mavenDeployer {
                    repository(url: "http://nexus.cinnober.com/nexus/content/repositories/buildtest_releases/")
                    snapshotRepository(url: "http://nexus.cinnober.com/nexus/content/repositories/buildtest_snapshots/")
                }
            }
        }
    
        project.task('sourcesJar', type: Jar) {
            dependsOn 'classes'
            classifier = 'sources' 
            group = 'Build'
            description = 'Assembles a jar archive containing the main source code.'
            from project.sourceSets.main.allSource
        } 
        
        project.task('javadocJar', type: Jar) {
            dependsOn 'javadoc'
            classifier = 'javadoc' 
            group = 'Documentation'
            description = 'Assembles a jar archive containing the javadoc.'
            from project.tasks.javadoc.destinationDir
        } 
        
        project.artifacts { 
            archives project.tasks.sourcesJar
            archives project.tasks.javadocJar
        }
    }
}