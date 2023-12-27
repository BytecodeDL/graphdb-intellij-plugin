package com.albertoventurini.graphdbplugin.database.neo4j.bytecodedl;

/**
 * @author daozhe@alibaba-inc.com
 * @date 2023/12/10 09:53
 */
public class InvocationSignature {
    private String callerSignature;
    private String callee;
    private Integer index;

    public InvocationSignature(String insn){
        String[] parts = insn.split("/");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid instruction format");
        }

        this.callerSignature = parts[0];
        this.callee = parts[1].replace("$", ".");

        try {
            this.index = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid index value", e);
        }
    }

    public MethodSignature getCallerMethodSignature(){
        MethodSignature methodSignature = new MethodSignature(this.callerSignature);
        return methodSignature;
    }

    public String getCallerSignature(){
        return this.callerSignature;
    }

    public String getCallee(){
        return this.callee;
    }

    public String getCalleeMethodName(){
        return this.callee.substring(callee.lastIndexOf(".")+1);
    }

    public Integer getIndex(){
        return this.index;
    }


}
