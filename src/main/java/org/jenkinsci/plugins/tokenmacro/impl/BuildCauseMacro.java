package org.jenkinsci.plugins.tokenmacro.impl;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Cause;
import hudson.model.CauseAction;
import hudson.model.TaskListener;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.jenkinsci.plugins.tokenmacro.DataBoundTokenMacro;
import org.jenkinsci.plugins.tokenmacro.MacroEvaluationException;

@Extension
public class BuildCauseMacro extends DataBoundTokenMacro {

    public static final String MACRO_NAME = "BUILD_CAUSE";
    public static final String ALTERNATE_MACRO_NAME = "CAUSE";

    @Override
    public boolean acceptsMacroName(String macroName) {
        return macroName.equals(MACRO_NAME) || macroName.equals(ALTERNATE_MACRO_NAME);
    }

    @Override
    public List<String> getAcceptedMacroNames() {
        List<String> macroNames = new ArrayList<>();
        Collections.addAll(macroNames, MACRO_NAME, ALTERNATE_MACRO_NAME);
        return macroNames;
    }

    @Override
    public String evaluate(AbstractBuild<?, ?> build, TaskListener listener, String macroName)
            throws MacroEvaluationException, IOException, InterruptedException {
        List<Cause> causes = new LinkedList<Cause>();
        CauseAction causeAction = build.getAction(CauseAction.class);
        if (causeAction != null) {
            causes = causeAction.getCauses();
        }

        return formatCauses(causes);
    }

    private String formatCauses(List<Cause> causes) {
        if (causes.isEmpty()) {
            return "N/A";
        }

        List<String> causeNames = new LinkedList<String>();
        for (Cause cause : causes) {
            causeNames.add(cause.getShortDescription());
        }

        return StringUtils.join(causeNames, ", ");
    }
}

