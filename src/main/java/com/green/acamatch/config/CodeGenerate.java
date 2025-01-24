package com.green.acamatch.config;

import org.apache.commons.text.RandomStringGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeGenerate {
    public static String generateCode(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("Length must be at least 4 to include all character types.");
        }

        // 각각의 범위에서 최소 하나씩 가져오기
        char upperCase = getRandomChar('A', 'Z'); // 대문자
        char lowerCase = getRandomChar('a', 'z'); // 소문자
        char digit = getRandomChar('0', '9');     // 숫자
        char special = getRandomSpecialChar();    // 특수문자

        // 나머지 길이만큼 무작위로 생성
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('!', '~') // 전체 범위 ('!' ~ '~')
                .filteredBy(ch -> Character.isLetterOrDigit(ch) || "!@#$%^&*()_+[]{}|;:,.<>?".indexOf(ch) >= 0)
                .get();

        String remaining = generator.generate(length - 4);

        // 모든 문자 합치기
        List<Character> allCharacters = new ArrayList<>();
        allCharacters.add(upperCase);
        allCharacters.add(lowerCase);
        allCharacters.add(digit);
        allCharacters.add(special);

        for (char ch : remaining.toCharArray()) {
            allCharacters.add(ch);
        }

        // 문자 섞기
        Collections.shuffle(allCharacters);

        // 최종 문자열로 변환
        StringBuilder result = new StringBuilder();
        for (char ch : allCharacters) {
            result.append(ch);
        }

        return result.toString();
    }

    // 범위 내에서 랜덤 문자 생성
    private static char getRandomChar(char start, char end) {
        return (char) (start + (int) (Math.random() * (end - start + 1)));
    }

    // 허용된 특수문자 중 랜덤 선택
    private static char getRandomSpecialChar() {
        String specialChars = "!@#$%^&*()_+[]{}|;:,.<>?";
        return specialChars.charAt((int) (Math.random() * specialChars.length()));
    }
}
