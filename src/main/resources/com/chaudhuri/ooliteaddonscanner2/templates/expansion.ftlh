<!--
  Shows one expansion in all it's beauty.
-->
<html>
    <head>
        <title><#if identifier != "oolite.core" >Expansion </#if>${ title }</title>
        <link rel="stylesheet" href="../style.css">
    </head>
    <body>
        <table border="0" width="100%">
            <tr>
                <td>Back to <a href='../index.html'>Index</a></td>
                <td align="right"><small>Page generated: ${.now}</small></td>
            </tr>
        </table>

        <h1><img style="vertical-align: middle;" src="https://ooliteproject.github.io/oolite/oolite-icon.png"/> <#if identifier != "oolite.core" >Expansion </#if>${ title }</h1>

        <h2>Content</h2>
        <ul>
          <#if warnings?has_content><li><a href="#warnings">Warnings</a></li></#if>
          <li><a href="#manifest">Manifest</a></li>
          <li><a href="#documentation">Documentation</a></li>
          <li><a href="#equipment">${ equipment?size } Equipment</a></li>
          <li><a href="#ships">${ ships?size } Ships</a></li>
          <li><a href="#models">${ models?size } Models</a></li>
          <#if scripts?has_content><li><a href="#scripts">${ scripts?size } Scripts</a></li></#if>
        </ul>

        <#if warnings?has_content>
          <a name="warnings">
            <h2>Warnings</h2>
            <ol>
            <#list warnings as w>
                <li>${ w }</li>
            </#list>
            </ol>
          </a>
        </#if>

        <a name="manifest">
        <h2>Manifest</h2>
        <#if identifier != "oolite.core" >
            <table border="1">
                <tr>
                    <th></th>
                    <th>from Expansion Manager's OXP list</th>
                    <th>from Expansion Manifest</th>
                </tr>
                <tr>
                    <td>Description</td>
                    <td>${ description }</td>
                    <td>${ manifest.description!"" }</td>
                </tr>
                <tr>
                    <td>Identifier</td>
                    <td>${ identifier }</td>
                    <td>${ manifest.identifier!"" }</td>
                </tr>
                <tr>
                    <td>Title</td>
                    <td>${ title }</td>
                    <td>${ manifest.title!"" }</td>
                </tr>
                <tr>
                    <td>Category</td>
                    <td>${ category }</td>
                    <td>${ manifest.category!"" }</td>
                </tr>
                <tr>
                    <td>Author</td>
                    <td>${ author }</td>
                    <td>${ manifest.author!"" }</td>
                </tr>
                <tr>
                    <td>Version</td>
                    <td>${ version }</td>
                    <td>${ manifest.version!"" }</td>
                </tr>
                <tr>
                    <td>Tags</td>
                    <td>${ tags?join(", ") }</td>
                    <td>${ manifest.tags?join(", ") }</td>
                </tr>
                <tr>
                    <td>Required Oolite Version</td>
                    <td>${ required_oolite_version!"" }</td>
                    <td>${ manifest.required_oolite_version!"" }</td>
                </tr>
                <tr>
                    <td>Maximum Oolite Version</td>
                    <td>${ maximum_oolite_version!"" }</td>
                    <td>${ manifest.maximum_oolite_version!"" }</td>
                </tr>
                <tr>
                    <td>Required Expansions</td>
                    <td>
                        <#list requiresOxps as dep>
                            <li>${ dep.identifier!"" }:${ dep.version!"" }</li>
                        </#list>
                    </td>
                    <td>
                        <#list manifest.requiresOxps as dep>
                            <li>${ dep.identifier!"" }:${ dep.version!"" }</li>
                        </#list>
                    </td>
                </tr>
                <tr>
                    <td>Optional Expansions</td>
                    <td>
                        <#list optionalOxps as dep>
                            <li>${ dep.identifier!"" }:${ dep.version!"" }</li>
                        </#list>
                    </td>
                    <td>
                        <#list manifest.optionalOxps as dep>
                            <li>${ dep.identifier!"" }:${ dep.version!"" }</li>
                        </#list>
                    </td>
                </tr>
                <tr>
                    <td>Conflict Expansions</td>
                    <td>
                        <#list conflictOxps as dep>
                            <li>${ dep.identifier!"" }:${ dep.version!"" }</li>
                        </#list>
                    </td>
                    <td>
                        <#list manifest.conflictOxps as dep>
                            <li>${ dep.identifier!"" }:${ dep.version!"" }</li>
                        </#list>
                    </td>
                </tr>
                <tr>
                    <td>Information URL</td>
                    <td><a target="_blank" href="${ informationUrl!"" }">${ informationUrl!"n/a"}</a></td>
                    <td><a target="_blank" href="${ manifest.information_url!"" }">${ manifest.information_url!"n/a"}</a></td>
                </tr>
                <tr>
                    <td>Download URL</td>
                    <td><a target="_blank" href="${ downloadUrl!"" }">${ downloadUrl!"n/a" }</a></td>
                    <td><a target="_blank" href="${ manifest.downloadUrl!"" }">${ manifest.downloadUrl!"n/a"}</a></td>
                </tr>
                <tr>
                    <td>License</td>
                    <td>${ license!"n/a"}</td>
                    <td>${ manifest.license!"" }</td>
                </tr>
                <tr>
                    <td>File Size</td>
                    <td>${ file_size!"n/a" }</td>
                </tr>
                <tr>
                    <td>Upload date</td>
                    <td>${ uploadDate!"n/a" }</td>
                </tr>
            </table>
        <#else><!-- oolite.core -->
            <table border='1'>
                <tr><td>Identifier</td><td>${ identifier }</td></tr>
                <tr><td>Title</td><td>${ title }</td></tr>
                <tr><td>Category</td><td>${ category }</td></tr>
                <tr><td>Author</td><td>${ author }</td></tr>
                <tr><td>Version</td><td>${ version }</td></tr>
                <tr><td>Tags</td><td>${ tags?join(", ") }</td></tr>
                <tr><td>Required Oolite Version</td><td>${ requiredOoliteVersion!"" }</td></tr>
                <tr><td>Maximum Oolite Version</td><td>${ maximumOoliteVersion!"" }</td></tr>
                <tr>
                    <td>Required Expansions</td>
                    <td>
                        <#list requiresOxps as dep>
                            <li>${ dep.identifier!"" }:${ dep.version!"" }</li>
                        </#list>
                    </td>
                </tr>
                <tr>
                    <td>Optional Expansions</td>
                    <td>
                        <#list optionalOxps as dep>
                            <li>${ dep.identifier!"" }:${ dep.version!"" }</li>
                        </#list>
                    </td>
                </tr>
                <tr>
                    <td>Conflict Expansions</td>
                    <td>
                        <#list conflictOxps as dep>
                            <li>${ dep.identifier!"" }:${ dep.version!"" }</li>
                        </#list>
                    </td>
                </tr>
                <tr><td>Information URL</td><td><a target="_blank" href="${ informationUrl!"" }">${ informationUrl!"n/a"}</a></td></tr>
                <tr><td>Download URL</td><td><a href="${ downloadUrl!"" }">${ downloadUrl!"n/a"}</a></td></tr>
                <tr><td>License</td><td>${ license!"n/a"}</td></tr>
                <tr><td>File Size</td><td>${ fileSize!"n/a" }</td></tr>
                <tr><td>Upload date</td><td>${ uploadDate!"n/a" } or { uploadDateAsInstant!"n/a" }</td></tr>
            </table>
        </#if>
        </a>

        <a name="documentation">
        <h2>Documentation</h2>
        <#if asWikipage??><p>Also read <a href='${asWikipage}'>${asWikipage}</a></p></#if>
        <#list readmes?keys as key>
        <h3>${ key }</h3>
        <pre>${ readmes[key] }</pre>
        </#list>
        </a>

        <a name="equipment">          
        <h2>Equipment</h2>
        <#if equipment?has_content>
            <table>
                <tr>
                    <th>Name</th>
                    <th>Visible</th>
                    <th>Cost [deci-credits]</th>
                    <th>Tech-Level</th>
                </tr>
            <#list equipment as e>
                <tr>
                    <td><a href="../equipment/${ e.identifier }.html">${ e.name }</a></td>
                    <td>${ e.visible?string('yes', 'no') }</td>
                    <td align="right">${ e.cost }</td>
                    <td align="center">${ e.techlevel?number + 1 }+</td>
                </tr>
            </#list>
            </table>
        <#else>
            This expansion declares no equipment. <#if warnings?has_content>This may be related to warnings.</#if>
        </#if>
        </a>

        <a name="ships">    
        <h2>Ships</h2>
        <#if ships?has_content>
            <table>
                <tr>
                    <th>Name</th>
                </tr>
            <#list ships as ship>
                <tr>
                    <td><a href="../ships/${ ship.identifier }.html">${ ship.name }</a></td>
                </tr>
            </#list>
            </table>
        <#else>
            This expansion declares no ships. <#if warnings?has_content>This may be related to warnings.</#if>
        </#if>
        </a>

        <a name="models">    
        <h2>Models</h2>
        <#if models?has_content>
            <table>
                <tr>
                    <th>Name</th>
                    <th>Vertexes</th>
                    <th>Faces</th>
                </tr>
            <#list models as modelname, model>
                <tr>
                    <td>${ modelname }</td>
                    <td>${ model.nvertexes }</td>
                    <td>${ model.nfaces }</td>
                </tr>
            </#list>
            </table>
        <#else>
            This expansion declares no models. <#if warnings?has_content>This may be related to warnings.</#if>
        </#if>
        </a>

        <a name="scripts">    
        <h2>Scripts</h2>
        <#if scripts?has_content>
            <table>
                <tr>
                    <th>Path</th>
                </tr>
            <#list scripts as script, content>
                <tr>
                    <td valign="top">${ script }</td>
                    <td><pre>${ content }</pre></td>
                </tr>
            </#list>
            </table>
        <#else>
            This expansion declares no scripts.
        </#if>
        </a>
    </body>
</html>
