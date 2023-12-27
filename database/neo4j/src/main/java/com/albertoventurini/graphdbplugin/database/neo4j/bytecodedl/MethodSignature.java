package com.albertoventurini.graphdbplugin.database.neo4j.bytecodedl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author daozhe@alibaba-inc.com
 * @date 2023/12/10 09:45
 */
public class MethodSignature {
    private String signature;
    private String className;
    private String methodName;
    private final List<String> paramTypes;

    private static final Pattern SIGNATURE_PATTERN = Pattern.compile(
            "^<([^:]+):\\s*(\\S+)\\s+([^\\(]+)\\(([^\\)]*)\\)>$"
    );

    public MethodSignature(String signature) {
        this.signature = signature;
        // 假设输入的格式为 <org.apache.logging.log4j.core.filter.Filterable: boolean isFiltered(org.apache.logging.log4j.core.LogEvent)>
        Matcher matcher = SIGNATURE_PATTERN.matcher(signature);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid method signature format");
        }

        this.className = matcher.group(1).trim().replace("$", ".");
        // Group 2 is the return type which is not used in this implementation
        this.methodName = matcher.group(3).trim();

        if (this.methodName.equals("<init>")) {
            this.methodName = className.substring(className.lastIndexOf(".") + 1);
        }

        String params = matcher.group(4).trim();
        this.paramTypes = params.isEmpty() ? Collections.emptyList() : Arrays.stream(params.split(",")).map(param ->param.replace("$", ".")).collect(Collectors.toList());
    }

    public String getClassName() {
        return this.className;
    }

    public String getShortClassName(){
        return this.className.substring(this.className.lastIndexOf(".") + 1, this.className.length());
    }

    public String getMethodName() {
        return this.methodName;
    }

    public List<String> getParamTypes() {
        return this.paramTypes;
    }
}
