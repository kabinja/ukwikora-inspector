<#include "lib/base.ftl">

<#macro page_content>
    <!-- Collapsable Card Example -->
    <div class="card border-0 shadow mb-4">
        <a href="#collapseCardExample" class="d-block card-header border-0 py-3" data-toggle="collapse" role="button" aria-expanded="true" aria-controls="collapseCardExample">
            <h6 class="m-0 font-weight-bold text-primary">Duplicated Code</h6>
        </a>
        <div class="collapse show" id="collapseCardExample">
            <div class="card-body">
                Duplicated code can be referred to as "clones". There are 4 types of clones that can be distinguished,
                based on the degree of similarity of the code fragment. In this tool, we only consider clones at the
                keyword level, which means we will compare pairs of keywords in order to detect code duplication.

                <ul style="list-style-type:square;">
                    <li>
                        <b>Type 1</b>: Keywords containing strictly the same sequence of steps and same variables. The
                        documentation as well as the name of the keyword is ignore while comparing the keywords.
                    </li>
                    <li>
                        <b>Type 2</b>: Similar to Type 1 clones, but ignore the difference in variable names.
                    </li>
                    <li>
                        <b>Type 3</b>: Similar to Typ 2 clones, but do not take into account "Logging" steps. Therefore,
                        these clones perform the same actions but might vary in the logging messages.
                    </li>
                    <li>
                        <b>Type 4</b>: Keywords performing an identical sequence of actions (calling the same library
                        keywords in the same order), regardless of the User Keywords used to do so.
                    </li>
                </ul>
            </div>
        </div>
    </div>
</#macro>

<@display_page sidebar=sidebar title="Documentation" generated_date=generated_date/>