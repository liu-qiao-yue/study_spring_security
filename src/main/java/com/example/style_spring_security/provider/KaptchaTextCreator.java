package com.example.style_spring_security.provider;

import com.google.code.kaptcha.text.impl.DefaultTextCreator;

import java.util.Random;

/**
 * @Author: ellie
 * @CreateTime: 2024-12-07
 * @Description:
 * @Version: 1.0
 */
public class KaptchaTextCreator extends DefaultTextCreator {

    private static final String[] NUMBER= {"0","1","2","3","4","5","6","7","8","9","10"};
    private static final String[] OPERATORS = {"+", "-", "*", "÷"};
    private static final Random RANDOM = new Random();

    @Override
    public String getText() {
        // 生成两个随机整数，用于构建数学表达式
        int x = RANDOM.nextInt(10);
        int y = RANDOM.nextInt(10);

        // 随机选择一个运算符下标
        int operatorIndex = RANDOM.nextInt(OPERATORS.length);
        // 获取运算符
        String operator = OPERATORS[operatorIndex];

        StringBuilder expression = new StringBuilder();
        Integer result = null; // 用于保存计算结果

        // 根据选定的运算符生成对应的数学表达式
        switch (operator) {
            case "+":
                // 加法操作
                result = x + y;
                break;

            case "-":
                // 减法操作
                result = x - y;
                break;

            case "*":
                // 乘法操作
                result = x * y;
                break;

            case "÷":
                // 除法操作，需要确保分母不为零且结果是整数
                if (x == 0 || y % x != 0) {
                    // 如果条件不满足，则递归调用自身重新生成表达式
                    return getText();
                } else {
                    // 确保除法显示为 y / x 形式，并且结果为整数
                    result = y / x;
                    int temp = x;
                    x = y; // 对于显示目的，交换x和y
                    y = temp;
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + operator);
        }

        // 将数学表达式与计算结果拼接成字符串
        expression.append(NUMBER[x]).append(operator).append(NUMBER[y]).append("=?@").append(result);

        // 返回最终生成的验证码文本
        return expression.toString();
    }
}
