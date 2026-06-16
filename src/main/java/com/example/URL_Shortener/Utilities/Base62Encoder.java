package com.example.URL_Shortener.Utilities;

public final class Base62Encoder {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALPHABET.length();
    private static final int TARGET_LENGTH = 7;

    private Base62Encoder(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }


    public static String encode(long num){
        if(num<=0){
            throw new IllegalArgumentException("Id must be greater than 0");
        }
        StringBuilder sb = new StringBuilder();
        while(num>0){
            sb.append(ALPHABET.charAt((int)(num % BASE)));
            num/=BASE;
        }
        String shortCode= sb.reverse().toString();
        while(shortCode.length()<TARGET_LENGTH){
            shortCode = ALPHABET.charAt(0) + shortCode;

        }

        return shortCode;
    }
}
