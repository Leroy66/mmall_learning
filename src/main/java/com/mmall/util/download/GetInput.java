package com.mmall.util.download;

/**
 * Created by IntelliJ IDEA
 * User: leroy
 * Time: 2018/4/10 18:27
 */
public class GetInput {
    public static int getInputClassNo() {
        int classNo = 0;
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        for (;;)
        {
            System.out.println("请输入需要下载的课程编号（如：http://www.imooc.com/learn/601，则输入601）：");
            try {
                classNo = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("课程编号填写错误，只能输入数字!\n");
                scanner.nextLine();
            }
        }
        return classNo;
    }

    public static int getInputVideoDef()
    {
        int videoDef = 0;
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        for (;;) {
            System.out.println("请输入要下载的清晰度，【0】超清，【1】高清，【2】普清：");
            try {
                videoDef = scanner.nextInt();
                if ((videoDef <= 2) && (videoDef >= 0)) {
                    break;
                }
                throw new Exception();
            }
            catch (Exception e) {
                System.out.println("只能输入【0】【1】【2】中的一个数！\n");
                scanner.nextLine();
            }
        }
        return videoDef;
    }
}
