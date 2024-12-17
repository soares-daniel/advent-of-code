package org.sedam.aoc;

import java.util.*;

public class Day17 extends Day {

    public String part1(List<String> input) {
        int registerA = 0;
        int registerB = 0;
        int registerC = 0;

        for (String line : input) {
            if (line.startsWith("Register A")) {
                registerA = Integer.parseInt(line.split(": ")[1]);
            } else if (line.startsWith("Register B")) {
                registerB = Integer.parseInt(line.split(": ")[1]);
            } else if (line.startsWith("Register C")) {
                registerC = Integer.parseInt(line.split(": ")[1]);
            } else if (line.startsWith("Program")) {
                String[] programTokens = line.split(": ")[1].split(",");
                return runProgram(registerA, registerB, registerC, programTokens);
            }
        }

        return "";
    }

    private String runProgram(int registerA, int registerB, int registerC, String[] program) {
        int[] registers = {registerA, registerB, registerC};
        StringBuilder output = new StringBuilder();
        int instructionPointer = 0;

        while (instructionPointer < program.length) {
            int opcode = Integer.parseInt(program[instructionPointer]);
            int operand = Integer.parseInt(program[instructionPointer + 1]);

            switch (opcode) {
                case 0: // adv: A = A / 2^operand
                    registers[0] /= (int) Math.pow(2, getComboValue(operand, registers));
                    break;
                case 1: // bxl: B = B XOR literal
                    registers[1] ^= operand;
                    break;
                case 2: // bst: B = combo operand % 8
                    registers[1] = getComboValue(operand, registers) % 8;
                    break;
                case 3: // jnz: if A != 0, jump to literal operand
                    if (registers[0] != 0) {
                        instructionPointer = operand;
                        continue;
                    }
                    break;
                case 4: // bxc: B = B XOR C
                    registers[1] ^= registers[2];
                    break;
                case 5: // out: output combo operand % 8
                    output.append(getComboValue(operand, registers) % 8).append(",");
                    break;
                case 6: // bdv: B = A / 2^operand
                    registers[1] = registers[0] / (int) Math.pow(2, getComboValue(operand, registers));
                    break;
                case 7: // cdv: C = A / 2^operand
                    registers[2] = registers[0] / (int) Math.pow(2, getComboValue(operand, registers));
                    break;
                default:
                    throw new IllegalArgumentException("Invalid opcode: " + opcode);
            }
            instructionPointer += 2; // next

        }

        if (!output.isEmpty()) {
            output.setLength(output.length() - 1);
        }

        return output + "";
    }

    private int getComboValue(int operand, int[] registers) {
        return switch (operand) {
            case 0, 1, 2, 3 -> operand;
            case 4 -> registers[0]; // A
            case 5 -> registers[1]; // B
            case 6 -> registers[2]; // C
            default -> throw new IllegalArgumentException("Invalid combo operand: " + operand);
        };
    }

    public String part2(List<String> input) {
        return "";
    }
}