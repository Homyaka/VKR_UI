/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mysystem;

public class CodedValue {
    private final String value;
    private final int code;
    
    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }
    
    public CodedValue(String val, int cd){
        value=val;
        code=cd;
    }
    
 
    
}
