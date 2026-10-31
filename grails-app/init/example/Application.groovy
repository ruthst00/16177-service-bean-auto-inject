// SPDX-License-Identifier: Apache-2.0
package example

import org.springframework.context.annotation.ComponentScan

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ComponentScan('example')
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}