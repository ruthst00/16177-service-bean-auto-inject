// SPDX-License-Identifier: Apache-2.0
package example

import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
class ProjectService {
    String project() {
        'project'
    }
}