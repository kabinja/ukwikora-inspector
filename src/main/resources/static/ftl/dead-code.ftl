<#include "lib/base.ftl">
<#include "lib/table.ftl">

<#macro js_imports>
    <script src="${deadCodePage.table.url}"></script>
</#macro>

<#macro page_content>
    <@table deadCodePage.table/>
</#macro>

<@display_page sidebar=sidebar title=deadCodePage.name generated_date=generated_date/>