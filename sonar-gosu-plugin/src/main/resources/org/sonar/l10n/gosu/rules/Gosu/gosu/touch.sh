#!/usr/bin/env bash
arr=(GosuNestedIfRule GosuClassSizeRule GosuCognitiveComplexityRule GosuCyclomaticComplexityRule GosuFunctionSizeRule
       GosuFunctionParameterLengthRule GosuCommentedOutCodeRule GosuFindStatementRule GosuGetCountUsageRule GosuIllegalImportsRule
       GosuInternalImportsRule GosuJavaStyleLineEndingRule GosuObjectEqualityRule)
count=0
for i in "${arr[@]}"
do
template="{
  \"title\": \"<script>...<\/script> elements should not be nested\",
  \"type\": \"BUG\",
  \"status\": \"ready\",
  \"remediation\": {
    \"func\": \"Constant\/Issue\",
    \"constantCost\": \"2mn\"
  },
  \"tags\": [
    
  ], 
 \"defaultSeverity\": \"Major\",
  \"ruleSpecification\": \"RSPEC-46450$count\",
  \"sqKey\": \"$i\",
  \"scope\": \"All\"
}
"
	touch org.codenarc.rule.gosu.$i.html
	echo "<h1>Placeholder Content</h1>" > org.codenarc.rule.gosu.$i.html
	touch org.codenarc.rule.gosu.$i.json
	echo $template > org.codenarc.rule.gosu.$i.json	
	count=$count+1
done
