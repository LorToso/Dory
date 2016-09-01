package com.doryapp.backend;

/**
 * Created by Lorenzo Toso on 01.09.2016.
 */
public class BoxedBool {
    boolean valid;

    public BoxedBool(boolean valid)
    {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
