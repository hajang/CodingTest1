package com.company;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Main {

    private static String inputs[] = {"11", "22", "12", "1?2", "??1", "1??1", "1???1", "1???2"};
//    private static String inputs[] = {"11", "22", "12", "1?2", "111","1??1", "1???1", "1???2"};
//    private static String inputs[] = {"??1"};
    private final static String result[] = {"right word", "wrong word", "unknown word"};

    public static void main(String[] args) {
        right_wrong_wrapper(inputs);
    }

    // 여러 입력을 처리하는 wrapper
    public static void right_wrong_wrapper(String[] inputs){
        for(String input : inputs)
            System.out.println(input + " : " + result[right_wrong(input, false)]);
    }

    // 단일 입력 결과를 반환하는 실제 함수
    public static int right_wrong(String input, boolean question_at_first){
        char before_char = ' ';
        int current_index = -1;
        int end_index = input.length() - 1;
        int first_question_mark_index = -1;
        int status = 2; // 문자열의 초기 상태튼 unknown
        boolean before_was_question_mark = false;
        boolean did_one = false;

        // 입력 문자열에 데이터 만큼 반복
        while(current_index++ < end_index){
            char c = input.charAt(current_index); // 현재 위치의 문자을 가져옴

            // 문자열의 시작이 아니고 이전 문자가 ? 가 아니었다면 입력문자열에서 이전 문자를 가져옴
            if(current_index != 0 && before_was_question_mark == false)
                before_char = input.charAt(current_index - 1);

            // 현재 문자에 대하여
            if(c == '1'){
                if(current_index == 0) continue; // 문자열의 시작이라면
                if(before_char == '1'){
                    status = 1; // 11 은 wrong
                    break;
                }
                else if(before_char == '2')
                    status = 0; // 12 는 right

                before_was_question_mark = false; // 현재 문자가 숫자 였음을 표시
            }
            else if(c == '2'){
                if(current_index == 0) continue; // 문자열의 시작이라면

                if(before_char == '1')
                    status = 0; // 21 은 right
                else if(before_char == '2'){
                    status = 1; // 22 는 wrong
                    break;
                }

                before_was_question_mark = false; // 현재 문자가 숫자 였음을 표시
            }
            else if(c == '?'){
                // 문자열의 시작 이라면
                // TODO : 문자열 시작부터 ?가 나오는 경우 1, 2를 둘다 집어넣어 시도해 봐야함
                if(current_index == 0){
                    int one_result = right_wrong(input.replaceFirst("\\?", "1"), true);
                    int two_result = right_wrong(input.replaceFirst("\\?", "2"), true);

                    // 결과가 같은 경우 둘중 하나를 반환
                    // 근데 같은 결과가 나올 수 있나??
                    if(one_result == two_result)
                        return one_result;

                    // 둘 중 하나가 unknown 이라면 unknown 반환
                    else if(one_result == 2 || two_result ==2)
                        return 2;

                }

                if(before_char == '1')          before_char = '2'; // 이전 문자가 1이라면 ?를 2로 설정
                else if(before_char == '2')     before_char = '1'; // 이전 문자가 2라면  ?를 1로 설정

                status = 0; // ?를 right이 되도록 위에서 설정했으므로 아직까지는 무조건 right 이 되야함
                before_was_question_mark = true; // 이전 문자가 ? 였다는걸 표시
            }
        }

        // right 인 경우에도 ?가 연속해서 나오는 경우 unknown 이 될 수 있음
        // 또한 문자열 시작부터 ?가 연속해서 나오는 경우는 flag로 따로 처리
        // 개선 여지 있는듯.. 지저분해..
        if(status == 0)
            if(input.contains("??") || (input.charAt(1) == '?' && question_at_first))
                status = 2;

        return status; // 결과 반환 - 0, 1, 2 중 하나가 반환 되어야 함
    }

}
