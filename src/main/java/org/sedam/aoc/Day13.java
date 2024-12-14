package org.sedam.aoc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 extends Day {

    static class ClawMachine {
        long buttonAX, buttonAY;
        long buttonBX, buttonBY;
        long prizeX, prizeY;
        ClawMachine(long ax, long ay, long bx, long by, long px, long py) {
            buttonAX = ax; buttonAY = ay; buttonBX = bx; buttonBY = by; prizeX = px; prizeY = py;
        }
    }

    static class Solution {
        long a, b;
        Solution(long a, long b) {
            this.a = a; this.b = b;
        }
        long cost() {
            return 3*a+b;
        }
    }

    @Override
    public String part1(List<String> input) {
        List<ClawMachine> machines = parseInput(input);
        long totalTokens = 0;
        for (ClawMachine m : machines) {
            int[] sol = solveMachine(m);
            if (sol!=null) totalTokens += sol[0]*3+sol[1];
        }
        return String.valueOf(totalTokens);
    }

    @Override
    public String part2(List<String> input) {
        List<ClawMachine> machines = parseInput2(input);
        AtomicLong total =new AtomicLong(0);
        machines
                .parallelStream()
                .forEach(m->{
                    //System.out.println("Machine:" + m);
                    Solution zz = solveMachine2(m);
                    if (zz!=null) {
                        //System.out.println("Got sol:" + zz.a + "," + zz.b);
                        total.addAndGet(zz.cost());
                    } else {
                        //System.out.println("No solution???");
                    }
                });
        return total.get() + "";
    }

    private static List<ClawMachine> parseInput(List<String> input) {
        List<ClawMachine> machines=new ArrayList<>();
        Pattern p=Pattern.compile("-?\\d+");
        for (int i=0; i<input.size(); i+=4) {
            if (i>=input.size()) break;
            if (input.get(i).trim().isEmpty()) continue;
            List<Long> nums=extractNumbers(input,p,i,3);
            if (nums.size()>=6) {
                machines.add(new ClawMachine(nums.get(0),nums.get(1),nums.get(2),nums.get(3),nums.get(4),nums.get(5)));
            }
        }
        return machines;
    }

    private static List<ClawMachine> parseInput2(List<String> input) {
        List<ClawMachine> machines=new ArrayList<>();
        Pattern p=Pattern.compile("-?\\d+");
        for (int i=0; i<input.size(); i+=4) {
            if (i>=input.size()) break;
            if (input.get(i).trim().isEmpty()) continue;
            List<Long> nums=extractNumbers(input,p,i,3);
            if (nums.size()>=6) {
                machines.add(new ClawMachine(nums.get(0),nums.get(1),nums.get(2),nums.get(3),
                        10000000000000L+nums.get(4),10000000000000L+nums.get(5)));
            }
        }
        return machines;
    }

    private static List<Long> extractNumbers(List<String> input, Pattern pattern, int startIndex, int linesToRead) {
        List<Long> numbers=new ArrayList<>();
        for (int j=0; j<linesToRead && startIndex+j<input.size(); j++) {
            Matcher m=pattern.matcher(input.get(startIndex+j));
            while (m.find()) numbers.add(Long.parseLong(m.group()));
        }
        return numbers;
    }

    private static int[] solveMachine(ClawMachine machine) {
        for (int a=0;a<=100;a++) {
            for (int b=0;b<=100;b++) {
                long x=a*machine.buttonAX+b*machine.buttonBX;
                long y=a*machine.buttonAY+b*machine.buttonBY;
                if (x==machine.prizeX && y==machine.prizeY) return new int[]{a,b};
            }
        }
        return null;
    }

    private static Solution solveMachine2(ClawMachine machine) {
        long gcdX=gcd(machine.buttonAX,machine.buttonBX);
        long gcdY=gcd(machine.buttonAY,machine.buttonBY);
        if (machine.prizeX%gcdX!=0||machine.prizeY%gcdY!=0) return null;
        ExtendedResult extX=extendedEuclidean(machine.buttonAX,machine.buttonBX);
        long factorX=machine.prizeX/extX.g;
        long aX0=extX.x*factorX;
        long bX0=extX.y*factorX;
        long d=gcdX;
        long A_x_div=machine.buttonAX/d;
        long B_x_div=machine.buttonBX/d;
        long R=machine.prizeY-(machine.buttonAY*aX0+machine.buttonBY*bX0);
        long C=machine.buttonAY*B_x_div - machine.buttonBY*A_x_div;
        if (C==0) {
            if (R!=0) return null;
            return infiniteSolutions(aX0,bX0,B_x_div, A_x_div);
        }
        if (R%C!=0) return null;
        long k0=R/C;
        return singleSolution(aX0,bX0,B_x_div, A_x_div,k0);
    }

    private static Solution infiniteSolutions(long a0,long b0,long BxDiv,long AxDiv) {
        Range range=findKRange(a0,b0,BxDiv,AxDiv);
        if (range==null) return null;
        Solution c1=evaluateSolutionAtK(a0,b0,BxDiv,AxDiv,range.min);
        Solution c2=evaluateSolutionAtK(a0,b0,BxDiv,AxDiv,range.max);
        if (c1==null&&c2==null) return null;
        if (c1==null) return c2;
        if (c2==null) return c1;
        return c1.cost()<c2.cost()?c1:c2;
    }

    private static Solution singleSolution(long a0,long b0,long BxDiv,long AxDiv,long k0) {
        long aK0=a0+k0*BxDiv;
        long bK0=b0-k0*AxDiv;
        if (aK0>=0&&bK0>=0) return new Solution(aK0,bK0);
        return null;
    }

    private static class Range {
        long min,max;
        Range(long min,long max){this.min=min;this.max=max;}
    }

    private static Range findKRange(long a0,long b0,long BxDiv,long AxDiv) {
        long kMin=Long.MIN_VALUE;
        long kMax=Long.MAX_VALUE;
        if (BxDiv>0) {
            long val = divCeil(-a0,BxDiv);
            if (val>kMin) kMin=val;
        } else if (BxDiv<0) {
            long val = divFloor(-a0,BxDiv);
            if (val<kMax) kMax=val;
        } else {
            if (a0<0) return null;
        }
        if (AxDiv>0) {
            long val=b0/AxDiv;
            if (val<kMax) kMax=val;
        } else if (AxDiv<0) {
            long val=divCeil(b0,AxDiv);
            if (val>kMin) kMin=val;
        } else {
            if (b0<0) return null;
        }
        if (kMin>kMax) return null;
        return new Range(kMin,kMax);
    }

    private static long divCeil(long a,long b) {
        if (b==0) throw new ArithmeticException("div0");
        if ((a>0 && b>0)||(a<0 && b<0)) {
            long q=a/b; long r=a%b;
            if (r!=0) q++;
            return q;
        } else {
            long q=a/b;
            return q;
        }
    }

    private static long divFloor(long a,long b) {
        if (b==0) throw new ArithmeticException("div0");
        long q=a/b; long r=a%b;
        if (r==0) return q;
        if ((a>0 && b<0)||(a<0 && b>0)) q--;
        return q;
    }

    private static Solution evaluateSolutionAtK(long a0,long b0,long BxDiv,long AxDiv,long K) {
        long a=a0+K*BxDiv;
        long b=b0-K*AxDiv;
        if (a<0||b<0) return null;
        return new Solution(a,b);
    }

    private static class ExtendedResult {
        long x,y,g;
        ExtendedResult(long x,long y,long g){this.x=x;this.y=y;this.g=g;}
    }

    private static ExtendedResult extendedEuclidean(long a,long b) {
        if (b==0) return new ExtendedResult(1,0,a);
        ExtendedResult r=extendedEuclidean(b,a%b);
        long x1=r.y;
        long y1=r.x-(a/b)*r.y;
        return new ExtendedResult(x1,y1,r.g);
    }

    private static long gcd(long a,long b) {
        while(b!=0){long t=b;b=a%b;a=t;}
        return Math.abs(a);
    }
}
