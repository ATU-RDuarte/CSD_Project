This is a Kotlin Multiplatform project targeting Android, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…

## Traceability

- a9c4c0a Add car app to gradle build [SMS-6](https://atu-team-dktaegt8.atlassian.net/browse/SMS-6)
- 3336437 Fix jwt signing and returning public key as pem format [SMS-7](https://atu-team-dktaegt8.atlassian.net/browse/SMS-7)
- 25b9db2 Clean up client register
- 8a796c3 Completed Design section in report pre peer review [SMS-15](https://atu-team-dktaegt8.atlassian.net/browse/SMS-15)
- 2c820e7 Enhanced server car register and session request [SMS-4](https://atu-team-dktaegt8.atlassian.net/browse/SMS-4)
- 9abb866 Added initial server routes and support for car [SMS-4](https://atu-team-dktaegt8.atlassian.net/browse/MS-4)registration
- d63556e Added HLD segment in report [SMS-15](https://atu-team-dktaegt8.atlassian.net/browse/SMS-15)
- 6760b26 Added HLD sequence and use case diagram [SMS-15](https://atu-team-dktaegt8.atlassian.net/browse/SMS-15)
- 9ba4cce Added design section (requirements) [SMS-15](https://atu-team-dktaegt8.atlassian.net/browse/SMS-15)
- a238cbb Fixed introduction chapter and initialized design chapter [SMS-15](https://atu-team-dktaegt8.atlassian.net/browse/SMS-15)
- 1e0e312 Implemented Car Card - Added dynamic fuel battery - Display carId - Display availability [SMS-8](https://atu-team-dktaegt8.atlassian.net/browse/SMS-8)
- b349c69 Added code skeleton for app and server
- bab5557 Added Project Proposal and Report [SMS-15](https://atu-team-dktaegt8.atlassian.net/browse/SMS-15)