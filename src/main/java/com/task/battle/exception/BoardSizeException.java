package com.task.battle.exception;

public class BoardSizeException extends Exception{

    public BoardSizeException(){
        super();
    }

    public BoardSizeException(String exception){
        super(exception);
    }
}
