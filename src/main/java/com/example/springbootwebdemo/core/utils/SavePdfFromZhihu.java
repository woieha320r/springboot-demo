package com.example.springbootwebdemo.core.utils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Objects;

/**
 * 使用Java robot从知乎电子书网页保存PDF至本地
 * 所需环境：Windows10系统；关闭"历史剪贴板"功能；edge浏览器；
 * 解决方案：使用java.awt.Robot模拟鼠标键盘，以从网页以指定名称保存PDF至本地指定目录。各操作之间需要留给系统反应的时间间隔
 *
  * @date 2021-11-03
 */
public class SavePdfFromZhihu {

    private static final int DELAY_TIME_READY = 5000;
    private static final int DELAY_TIME_WAIT = 5000;
    private static final int DELAY_TIME_SHORT = DELAY_TIME_WAIT / 5;

    public static void main(String[] args) throws AWTException, IOException, UnsupportedFlavorException {
        System.out.println(">>> 准备运行 >>>");
        Robot robot = new Robot();
        System.out.println(">>> 倒计时" + DELAY_TIME_READY / 1000 + "秒开始，开始前请保证待操作窗口处于激活状态，并已将浏览器下载目录定正确定位 >>>");
        robot.delay(DELAY_TIME_READY);
        System.out.println(">>> 开始运行 >>>");

        int count = 0;
        String prevName = null;
        String currName;
        while (true) {
            System.out.println(">>> 打印快捷键 >>> Ctrl + P >>>");
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_P);
            robot.keyRelease(KeyEvent.VK_P);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.delay(DELAY_TIME_WAIT);

            System.out.println(">>> 确认页面 >>> Enter >>>");
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(DELAY_TIME_SHORT);

            System.out.println(">>> 将默认保存文件名称剪切至剪贴板 >>> Ctrl + X >>>");
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_X);
            robot.keyRelease(KeyEvent.VK_X);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.delay(DELAY_TIME_SHORT);

            //若此次名称与上次相同，则说明->键无用，已到达末尾

            System.out.println(">>> 从剪贴板读取内容 >>>");
            currName = (String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .getContents(null)
                    .getTransferData(DataFlavor.stringFlavor);
            if (Objects.equals(currName, prevName)) {
                System.out.println(">>> 已到达末尾，结束运行 >>>");
                break;
            }
            prevName = currName;
            System.out.println(">>> 修改读取的内容并写入剪贴板 >>>");
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(count++ + "-" + prevName), null);
            System.out.println(">>> 将剪贴板内容粘贴至文件名输入框 >>> Ctrl + V");
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.delay(DELAY_TIME_SHORT);

            System.out.println(">>> 保存 >>> Enter >>>");
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(DELAY_TIME_SHORT);

            System.out.println(">>> 切换至下一页 >>> ->");
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.keyRelease(KeyEvent.VK_RIGHT);
            robot.delay(DELAY_TIME_SHORT);
        }
    }
}
