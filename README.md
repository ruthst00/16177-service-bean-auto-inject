<!-- SPDX-License-Identifier: Apache-2.0 -->
# Grails issue #16177: service bean auto-injection

Minimal standalone application demonstrating the symptom described in [apache/grails-core#16177](https://github.com/apache/grails-core/issues/16177), pinned to **Grails 8.0.0-M5**.

## Scope

The issue shows `@Service class LicenseService` with three unannotated service properties, but does not identify the annotation's import. This sample explicitly uses **`org.springframework.stereotype.Service`**. It does not assume the report involves GORM's `grails.gorm.services.Service`; there is no database or data-service dependency.

**Important limitation:** the report does not supply an application, source locations, or the `@Service` import. Its exact M3-to-M5 regression cannot be established from that snippet. Locally, placing all services in `grails-app/services` on M5 injected every dependency successfully, with or without Spring `@Service` and component scanning.

This sample instead demonstrates the same null-injection symptom for **Spring-managed services in `src/main/groovy`**, discovered by the explicit `@ComponentScan('example')` on `Application`. Those are not conventional Grails service artefacts. This is a candidate configuration to compare with the reporter, **not proof of the reported framework regression**. The source location and scanning are deliberate, visible differences; do not attribute the failure to M5 alone.

Three variants use the same real dependencies (the dependencies themselves live in `grails-app/services/example`):

| Bean | Source location | Class annotation | Property annotations |
| --- | --- | --- | --- |
| `licenseService` | `src/main/groovy/example` | Spring `@Service` | None (symptom reproduction) |
| `autowiredLicenseService` | `src/main/groovy/example` | Spring `@Service` | `@Autowired` (reported workaround) |
| `conventionalLicenseService` | `grails-app/services/example` | None | None (Grails convention control) |

There are no manual bean definitions, mocked services, or injection-disabling settings. The controller and integration spec explicitly autowire their entry-point services so the behavior under test is **service-to-service** injection only.

## Run

Use JDK 21 or newer (JDK 25 matches the issue's Java major version). The included Gradle 9.5.1 wrapper matches the issue's Gradle version and supports JDK 25. No Grails CLI, database, Docker, or local framework checkout is needed. The first build needs internet access to Maven Central and the Gradle Plugin Portal.

```bash
./gradlew bootRun
```

In another terminal:

```bash
# Injection status for all three variants
curl -sS http://localhost:8080/

# Reproduction: invokes methods on the unannotated properties
curl -i http://localhost:8080/broken

# Workaround and convention controls
curl -i http://localhost:8080/workaround
curl -i http://localhost:8080/conventional
```

When the issue reproduces, `/` reports all three `annotated` values as `false`, but the `autowired` and `conventional` values as `true`. `/broken` returns HTTP 500 because a dependency is null; both controls return HTTP 200 with `invoice:user:project`.

## Regression tests

```bash
./gradlew test integrationTest
```

`ServiceInjectionSpec` boots the real application context and asserts the **expected correct behavior**, rather than treating null injection as success. On an affected version the annotated-service test fails; the workaround and convention tests should pass. Each test checks all three dependencies and calls their public methods through the enclosing service.

Reports: `build/reports/tests/integrationTest/index.html` and `build/test-results/integrationTest/`.

To compare another published Grails version without editing files:

```bash
./gradlew clean integrationTest -PgrailsVersion=8.0.0-M3
```

The report says M3 worked, but that claim refers to the reporter's unknown configuration, not necessarily this Spring-managed variant. A passing run on a different version means this particular scenario no longer reproduces there.

## Local validation

Using JDK 25.0.3 and Gradle 9.5.1, `./gradlew clean test integrationTest` on M5 compiles and runs all three integration tests: **one intentionally failing regression assertion** (all three properties in the Spring-managed service are null), **two passing controls**. `test` has no unit tests because injection is exercised in a real application context rather than mocked.

Live HTTP checks with `bootRun` also confirmed `/` returns the injection statuses above, `/broken` returns 500, and `/workaround` and `/conventional` both return 200 with `invoice:user:project`.

To investigate the exact issue further, obtain the reporter's `@Service` import, service source locations, component-scan configuration, and ideally their example application.