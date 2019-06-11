<#include "lib/base.ftl">
<#include "lib/chart-area.ftl">

<#macro js_imports>
    <#list project.scripts as url>
        <script src="${url}"></script>
    </#list>
</#macro>

<#macro page_content>
    <div class="row mb-4">
        <div class="col-sm">
            <div class="card border-0 bg-primary text-white shadow">
                <div class="card-body">
                    ${project.linesOfCode} Lines of Code
                </div>
            </div>
        </div>
        <div class="col-sm">
            <div class="card border-0 bg-primary text-white shadow">
                <div class="card-body">
                    ${project.numberKeywords} Keywords
                </div>
            </div>
        </div>
        <div class="col-sm">
            <div class="card border-0 bg-primary text-white shadow">
                <div class="card-body">
                    ${project.numberTestCases} Test Cases
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <@chartArea chart=project.connectivityChart/>
        </div>
        <div class="col-lg-6">
            <@chartArea chart=project.depthChart/>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <@chartArea chart=project.sizeChart/>
        </div>
        <div class="col-lg-6">
            <@chartArea chart=project.sequenceChart/>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-6">
            <!-- Area Chart -->
            <div class="card border-0 shadow mb-4">
                <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
                    <h6 class="m-0 font-weight-bold text-primary">${project.dependencyGraph.name}</h6>
                </div>
                <div class="card-body">
                    <div class="chart-area" id="${project.dependencyGraph.id}">
                    </div>
                </div>
            </div>
        </div>
        <div class="col-lg-6">
            <@chartArea chart=project.cloneChart/>
        </div>
    </div>
</#macro>

<@display_page sidebar=sidebar title=project.link.text generated_date=generated_date/>