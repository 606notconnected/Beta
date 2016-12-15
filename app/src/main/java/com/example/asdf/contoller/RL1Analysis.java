package com.example.asdf.contoller;

import java.util.ArrayList;

import java.util.Scanner;



public class RL1Analysis {

    String[][] sentence = new String[][]{//文法，也是内定了，当然要用到其他文法也可以，就只要这些参数改一下就可以了，但为了简单起见，字符不能使用带一撇的字符

            {"E","E+T"},//E->E+T

            {"E","T"},//E->T

            {"T","T*F"},//T->T*F

            {"T","F"},//T->F

            {"F","(E)"},//F->(E)

            {"F","i"}//F->i

    };

    String[][] table = new String[][]{//为了简单，LR分析表就手工输入了，那样输入也挺麻烦的，其实老师还忘了一个个东西给我们，给了我们分析表，就必须得给对应的文法才可以，还好，看出来那文法了

            {"","i","+","*","(",")","#","E","T","F"},

            {"S0","S5","","","S4","","","1","2","3"},

            {"S1","","S6","","","","acc","","",""},

            {"S2","","r2","S7","","r2","r2","","",""},

            {"S3","","r4","r4","","r4","r4","","",""},

            {"S4","S5","","","S4","","","8","2","3"},

            {"S5","","r6","r6","","r6","r6","","",""},

            {"S6","S5","","","S4 ","","r1","","9","3"},

            {"S7","S5","","","S4","","","","","10"},

            {"S8","","S6","","","S11","","","",""},

            {"S9","","r1","S7","","r1","r1","","",""},

            {"S10","","r3","r3","","r3","r3","","",""},

            {"S11","","r5","r5","","r5","r5","","",""}

    };

    String in=null;

    public RL1Analysis(){

        this.printSentence();//打印文法

        Scanner sc = new Scanner(System.in);

        System.out.println("\n请输入一个句子：");

        in = sc.nextLine().toString();

        in = removeBlank(in);

        int index=0;//记录输入串的索引位置

        StringBuffer stateTrack = new StringBuffer("S0");//状态栈，初始时如S0

        StringBuffer analysisTrack = new StringBuffer("#");//分析栈，初始放#

        ArrayList<String> list = new ArrayList<String>();

        String operator = "";//记录分析表中对应的值

        String staTrack="";

        int x,y;//记录在分析表中的位置

        boolean isOver=false;

        System.out.println("\n语句分析过程如下：");

        System.out.println("状态栈"+getBlank(26)+"符号栈"+getBlank(20)+"输入串\tACTION\tGOTO");

        while(!isOver){

            x=0;y=0;

            String inS = in.charAt(index)+"";//输入串如今读到的位置

            for(int i= 1;i<table[0].length;i++)//查表得到Y坐标

                if(table[0][i].equals(inS))

                {

                    y = i;

                    break;

                }

            staTrack = stateTrack.substring(stateTrack.lastIndexOf("S"),stateTrack.length());//读取状态栈最上面一个状态

            for(int i=1;i<table.length;i++)//得到X坐标

                if(staTrack.equals(table[i][0])){

                    x = i;

                    break;





                }

            operator = table[x][y];//分析表中对应位置的动作

            if(operator.length()>0){

                System.out.print(stateTrack+getBlank(16-stateTrack.length())+analysisTrack+getBlank(18-analysisTrack.length()-in.length()+index)+in.substring(index,in.length())+"\t"+operator);

                if(operator.startsWith("S")){//移进

                    stateTrack.append(operator);

                    analysisTrack.append(inS);

                    index++;//输入串指针下移一位

                }else if(operator.startsWith("r")){//规约

                    int m = Integer.parseInt(operator.substring(1,operator.length()))-1;//截取后面的数字，用于查产生式，还有一点，这是从1开始的

                    String liutemp = sentence[m][1];

                    list.add(sentence[m][1]+"=>"+sentence[m][0]);//规约步骤

                    if(analysisTrack.toString().endsWith(liutemp))

                        analysisTrack.delete(analysisTrack.length()-liutemp.length(),analysisTrack.length());//在分析栈除去产生式右部的字符串

                    else{

                        System.out.println("\n规约时出错！");

                        System.exit(0);

                    }

                    analysisTrack.append(sentence[m][0]);//加上产生式左部，就相当于规约了

                    int stindex=stateTrack.lastIndexOf("S");

                    for(int i=0;i<liutemp.length();i++){//为了程序不节外生枝，就简单化了，这个地方的求liutemp的长度才确定有效字符个数还是不准确的，假如产生式中字母后跟了一撇的，就不对了，如E->T'+F

                        stateTrack.delete(stindex,stateTrack.length());//一次去掉去掉一个状态

                        stindex=stateTrack.lastIndexOf("S");

                    }

                    x = 0;y = 0;

                    staTrack = stateTrack.substring(stateTrack.lastIndexOf("S"),stateTrack.length());

                    for(int i=1;i<table.length;i++){//得到待规约X坐标

                        if(staTrack.equals(table[i][0])){

                            x = i;

                            break;

                        }

                    }

                    for(int i=1;i<table[0].length;i++){

                        if(table[0][i].equals(sentence[m][0])){

                            y = i;

                            break;

                        }

                    }

                    operator = table[x][y];//得到GOTO值

                    System.out.print("\t"+operator);

                    if(operator.matches("\\d+"))

                        stateTrack.append("S"+operator);

                    else{

                        System.out.println("\n待规约项出错！");

                        System.exit(0);

                    }

                }else if(operator.equals("acc")){

                    System.out.println("\n该语句符合该合法！");

                    isOver=true;

                }

                System.out.println();

            }else {

                System.out.println("输入的语句不符合给定文法！");

                System.exit(0);

            }

        }

        System.out.println("该句子语法树自底向上生成过程：");

        for(int i=0;i<list.size();i++)

            System.out.println(list.get(i));

    }

    public void printSentence(){

        System.out.println("假定文法如下：");

        for(int i=0;i<sentence.length;i++)

            System.out.println(sentence[i][0]+"->"+sentence[i][1]);

    }

    public String getBlank(int n){//打印n个连续空格

        String blank="";

        for(int i=0;i<n;i++)

            blank+=" ";

        return blank;

    }

    public String removeBlank(String s){//消除多余的空格，最后追加#

        StringBuffer res = new StringBuffer(s);

        int kong=res.indexOf(" ");

        while(kong>=0){

            res.delete(kong,kong+1);

            kong=res.indexOf(" ");

        }

        if(s.indexOf("#")==-1)//如果没有#号

            res.append("#");

        return res.toString();

    }

    public static void main(String[] args){

        new RL1Analysis();

    }

} 
