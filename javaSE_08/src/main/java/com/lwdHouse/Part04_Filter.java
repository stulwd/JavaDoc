package com.lwdHouse;

import java.io.*;

/**
 * FilterInputStream
 *  是装饰器模式
 */
public class Part04_Filter {

    public static void main(String[] args) throws IOException {

        try(CountInputStream cis = new CountInputStream(new FileInputStream("./javaCE_08/pom.xml"))){
            int n;
            while ((n = cis.read()) != -1){
                System.out.print((char) n);
            }
            System.out.println("\n总共读取"+cis.getBytesRead()+"字节");
        }

        try(CountInputStream cis = new CountInputStream(new FileInputStream("./javaCE_08/pom.xml"))){
            int n;
            while ( (n = cis.read(new byte[30], 0,10)) != -1){

            }
            System.out.println("\n总共读取"+cis.getBytesRead()+"字节");
        }
    }
}

/* 装饰器的思想就是，把原来的类自己持有，然后把自己掩饰成那个类的样子，表现为实现那个类的接口
* 然后，把原来类的实体为参数，构造一个包装类，在包装类内部自己持有原来的实体类。然后重写函数，
* 在函数内部继续调用原来实体类的相同的方法去处理，此外还可以增加一些额外的装饰代码执行 */

class CountInputStream extends FilterInputStream{

    private int count = 0;

    protected CountInputStream(InputStream in) {
        super(in);
    }

    public int getBytesRead(){
        return count;
    }

    @Override
    public int read() throws IOException {
        int n = in.read();
        if (n != -1){
            count++;
        }
        System.out.println("\n读了a");
        return n;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int n = in.read(b, off, len);
        if (n != -1){
            count += n;
        }
        return n;
    }
}