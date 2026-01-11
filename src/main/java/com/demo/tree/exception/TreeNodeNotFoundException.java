package com.demo.tree.exception;

public class TreeNodeNotFoundException extends RuntimeException{

    public TreeNodeNotFoundException(String message){
        super(message);
    }
}
