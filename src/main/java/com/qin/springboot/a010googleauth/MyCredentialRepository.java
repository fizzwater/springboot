package com.qin.springboot.a010googleauth;

import com.warrenstrange.googleauth.ICredentialRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyCredentialRepository implements ICredentialRepository {
    private final Map<String,String> map = new HashMap<>();
    @Override
    public String getSecretKey(String s) {
        String kye= map.get(s);
        return kye;
    }

    //GoogleAuthenticator.java
    // emergency scratch codes  紧急情况的备用code ，可不用存储
    // repository.saveUserCredentials(userName, key.getKey(), key.getVerificationCode(), key.getScratchCodes());
    @Override
    public void saveUserCredentials(String s, String s1, int i, List<Integer> list) {
       map.put(s,s1);
    }
}
