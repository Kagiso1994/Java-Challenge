/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ikhokha.techcheck;

import java.util.regex.Pattern;

/**
 *
 * @author kagisom
 */
public class Matric {

    private String name;
    
    public Matric(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    boolean trueAnnalysis(String line) {
        return false;
    }
}

class MoverMentions extends Matric {
    public MoverMentions(String name) {
        super(name);
    }

    @Override
    boolean trueAnnalysis(String line) {
        if (Pattern.compile("Mover", Pattern.CASE_INSENSITIVE).matcher(line).find()) {
            return true;
        }
        return false;
    }
}

class ShakerMentions extends Matric {
    public ShakerMentions(String name) {
        super(name);
    }

    @Override
    boolean trueAnnalysis(String line) {
        if (Pattern.compile("Shaker", Pattern.CASE_INSENSITIVE).matcher(line).find()) {
            return true;
        }
        return false;
    }

}

class Question extends Matric {
    public Question(String name) {
        super(name);
    }
    @Override
    boolean trueAnnalysis(String line) {
        if (line.indexOf("?") != -1) {
            return true;
        }
        return false;
    }
}

class Spam extends Matric {
    public Spam(String name) {
        super(name);
    }
    @Override
    boolean trueAnnalysis(String line) {
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        if (Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE).matcher(line).find()) {
            //System.out.println("Url = " + line);
            return true;
        }
        return false;
    }
}

class ShorterThan15 extends Matric{
    public ShorterThan15(String name) {
        super(name);
    }
    
    @Override
    boolean trueAnnalysis(String line) {
        if(line.length() < 15){
            return true;
        }
        return false;
    }
}
