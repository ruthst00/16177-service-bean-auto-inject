// SPDX-License-Identifier: Apache-2.0
package example

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

import grails.compiler.GrailsCompileStatic

@Service
@GrailsCompileStatic
class AutowiredLicenseService {
    @Autowired
    InvoiceService invoiceService

    @Autowired
    ProjectUserService projectUserService

    @Autowired
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