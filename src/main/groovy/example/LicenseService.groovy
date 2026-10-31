// SPDX-License-Identifier: Apache-2.0
package example

import org.springframework.stereotype.Service

import grails.compiler.GrailsCompileStatic

@Service
@GrailsCompileStatic
class LicenseService {
    InvoiceService invoiceService
    ProjectUserService projectUserService
    ProjectService projectService

    Map<String, Boolean> injectionStatus() {
        [invoiceService: invoiceService != null,
         projectUserService: projectUserService != null,
         projectService: projectService != null]
    }

    String issueLicense() {
        "${invoiceService.invoice()}:${projectUserService.user()}:${projectService.project()}"
    }
}