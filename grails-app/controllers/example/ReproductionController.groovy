// SPDX-License-Identifier: Apache-2.0
package example

import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class ReproductionController {
    @Autowired
    LicenseService licenseService

    @Autowired
    AutowiredLicenseService autowiredLicenseService

    @Autowired
    ConventionalLicenseService conventionalLicenseService

    def index() {
        render(contentType: 'application/json', text: JsonOutput.toJson([
            issue: 'https://github.com/apache/grails-core/issues/16177',
            annotated: licenseService.injectionStatus(),
            autowired: autowiredLicenseService.injectionStatus(),
            conventional: conventionalLicenseService.injectionStatus()
        ]))
    }

    def broken() {
        render(text: licenseService.issueLicense(), contentType: 'text/plain')
    }

    def workaround() {
        render(text: autowiredLicenseService.issueLicense(), contentType: 'text/plain')
    }

    def conventional() {
        render(text: conventionalLicenseService.issueLicense(), contentType: 'text/plain')
    }
}