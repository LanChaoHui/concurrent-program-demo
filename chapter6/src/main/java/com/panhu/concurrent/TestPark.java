package com.panhu.concurrent;

import java.util.concurrent.locks.LockSupport;

public class TestPark {
    public void testPark(){
        LockSupport.park(this);
    }
}
