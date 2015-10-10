package com.careerhub.go.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class GoEnvironment {
    private Pattern envPat = Pattern.compile("\\$\\{(\\w+)\\}");
    private Map<String, String> environment = new HashMap<String, String>();

    public GoEnvironment() {
        this.environment.putAll(System.getenv());
    }

    public GoEnvironment putAll(Map<String, String> existing) {
        environment.putAll(existing);
        return this;
    }

    public String get(String name) {
        return environment.get(name);
    }

    public boolean has(String name) {
        return environment.containsKey(name) && isNotEmpty(get(name));
    }

    public boolean isAbsent(String name) {
        return !has(name);
    }

    public String replaceVariables(String str) {
        Matcher m = envPat.matcher(str);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String replacement = get(m.group(1));
            if(replacement != null) {
                m.appendReplacement(sb, replacement);
            }
        }

        m.appendTail(sb);

        return sb.toString();
    }
}
