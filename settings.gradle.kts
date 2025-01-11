pluginManagement {

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }

        }
        mavenCentral()
        gradlePluginPortal()

        maven {
            url = uri("https://repo1.maven.org/maven2/")
        }
        maven { url = uri("https://repo1.maven.org/maven2/") }
        maven { url = uri("https://jitpack.io") }
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        maven {
            url = uri("https://repos.xiaomi.com/maven")
            credentials {
                username = "mimo-developer"
                password =  "AKCp8ih1PFG9tV8qaLyws67dLGZi8udFM39SfsHgihN15cgsiRvHuxj8JzFmuZjaViVeNawaA"
            }
        }

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://api.xposed.info")
        maven("https://jitpack.io")
        maven {
            url = uri("https://repo1.maven.org/maven2/")

        }
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo1.maven.org/maven2/") }
        maven {
            url = uri("https://maven.aliyun.com/repository/public")
        }
        maven {
            url = uri("https://repos.xiaomi.com/maven")
            credentials {
                username = "mimo-developer"
                password =  "AKCp8ih1PFG9tV8qaLyws67dLGZi8udFM39SfsHgihN15cgsiRvHuxj8JzFmuZjaViVeNawaA"
            }
        }
    }

}


rootProject.name = "Dynamic Notification"
include(":app")
 