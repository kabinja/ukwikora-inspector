<#include "lib/base.ftl">
<#include "lib/chart-area.ftl">

<#macro js_imports>
  <#list summaryPage.scripts as url>
    <script src="${url}"></script>
  </#list>
</#macro>

<#macro page_content>
  <div class="row mb-4">
    <div class="col-sm">
      <div class="card border-0 bg-primary text-white shadow">
        <div class="card-body">
          ${summaryPage.linesOfCode} Lines of Code
        </div>
      </div>
    </div>
    <div class="col-sm">
      <div class="card border-0 bg-primary text-white shadow">
        <div class="card-body">
          ${summaryPage.numberKeywords} Keywords
        </div>
      </div>
    </div>
    <div class="col-sm">
      <div class="card border-0 bg-primary text-white shadow">
        <div class="card-body">
          ${summaryPage.numberTestCases} Test Cases
        </div>
      </div>
    </div>
  </div>

  <div class="row">
    <div class="col-sm-12">
      <@chartArea chart=summaryPage.deadCodeChart/>
    </div>
  </div>

  <div class="row">
    <div class="col-sm-12">
      <@chartArea chart=summaryPage.duplicatedChart/>
    </div>
  </div>

  <div class="row">
    <div class="col-sm-12">
      <@chartArea chart=summaryPage.testCasesChart/>
    </div>
  </div>

  <div class="row">
    <div class="col-sm-12">
      <@chartArea chart=summaryPage.userKeywordsChart/>
    </div>
  </div>

  <div class="row">
    <div class="col-sm-6">
      <@chartArea chart=summaryPage.cloneChart/>
    </div>
  </div>
</#macro>

<@display_page sidebar=sidebar title=summaryPage.name generated_date=generated_date/>