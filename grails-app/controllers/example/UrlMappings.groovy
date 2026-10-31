// SPDX-License-Identifier: Apache-2.0
package example

class UrlMappings {
    static mappings = {
        '/'(controller: 'reproduction', action: 'index')
        '/broken'(controller: 'reproduction', action: 'broken')
        '/workaround'(controller: 'reproduction', action: 'workaround')
        '/conventional'(controller: 'reproduction', action: 'conventional')
    }
}