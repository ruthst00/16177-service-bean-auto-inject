// SPDX-License-Identifier: Apache-2.0
package example

import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

import grails.testing.mixin.integration.Integration

@Integration
class ServiceInjectionSpec extends Specification {
    @Autowired
    LicenseService licenseService

    @Autowired
    AutowiredLicenseService autowiredLicenseService

    @Autowired
    ConventionalLicenseService conventionalLicenseService

    void 'annotated service dependencies should be injected without Autowired - issue 16177'() {
        expect:
        licenseService.injectionStatus() == [invoiceService: true, projectUserService: true, projectService: true]
        licenseService.issueLicense() == 'invoice:user:project'
    }

    void 'explicit Autowired workaround injects every dependency'() {
        expect:
        autowiredLicenseService.injectionStatus() == [invoiceService: true, projectUserService: true, projectService: true]
        autowiredLicenseService.issueLicense() == 'invoice:user:project'
    }

    void 'conventional Grails service injects every dependency without annotations'() {
        expect:
        conventionalLicenseService.injectionStatus() == [invoiceService: true, projectUserService: true, projectService: true]
        conventionalLicenseService.issueLicense() == 'invoice:user:project'
    }
}