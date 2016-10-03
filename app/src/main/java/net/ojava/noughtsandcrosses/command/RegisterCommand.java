package net.ojava.noughtsandcrosses.command;

import java.io.Serializable;

/**
 * Created by chenbao on 2016/9/20.
 */
public class RegisterCommand implements Serializable {
    private static final long serialVersionUID = -4564112291506336695L;

    public String name;
    public String password;
    public String result;
}
