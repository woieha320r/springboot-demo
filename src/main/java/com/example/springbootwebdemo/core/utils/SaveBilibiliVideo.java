package com.example.springbootwebdemo.core.utils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * 使用Java robot操控键盘鼠标从某网站解析下载bilibili视频
 *
  * @date 2021-12-29
 */
public class SaveBilibiliVideo {

    private static final int DELAY_TIME_READY = 5000;
    private static final int DELAY_TIME_WAIT = 5000;
    private static final int DELAY_TIME_SHORT = DELAY_TIME_WAIT / 5;
    private static final String BASE_URL = "https://www.bilibili.com/video/BV1Mg411g7C8?p=";

    public static void main(String[] args) throws AWTException {
        System.out.println(">>> 准备运行 >>>");
        Robot robot = new Robot();
        System.out.println(">>> 倒计时" + DELAY_TIME_READY / 1000 + "秒开始，开始前请保证待操作窗口处于激活状态，并已将浏览器下载目录定正确定位 >>>");
        robot.delay(DELAY_TIME_READY);
        System.out.println(">>> 开始运行 >>>");

        int count = 40;
        while (++count <= 49) {
            System.out.println(">>> 将URL并写入剪贴板 >>>");
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(BASE_URL + count), null);
            robot.delay(DELAY_TIME_SHORT);

            System.out.println(">>> 粘贴 >>> Ctrl + V >>>");
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.delay(DELAY_TIME_SHORT);

            System.out.println(">>> 确认解析 >>> Enter >>>");
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(DELAY_TIME_WAIT * 2);

            System.out.println(">>> 定位下载按钮 >>> Tab * 2 >>>");
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.delay(DELAY_TIME_SHORT / 2);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.delay(DELAY_TIME_SHORT);

            System.out.println(">>> 确认下载 >>> Enter >>>");
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(DELAY_TIME_WAIT * 3);

            System.out.println(">>> 鼠标右键 >>>");
            robot.mousePress(InputEvent.BUTTON3_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_MASK);
            robot.delay(DELAY_TIME_SHORT);

            System.out.println(">>> 定位另存按钮 >>> DOWN * 3 >>>");
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            robot.delay(DELAY_TIME_SHORT / 2);
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            robot.delay(DELAY_TIME_SHORT / 2);
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            robot.delay(DELAY_TIME_SHORT);

            System.out.println(">>> 确认 >>> Enter >>>");
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(DELAY_TIME_SHORT);

            System.out.println(">>> 修改读取的内容并写入剪贴板 >>>");
            Toolkit.getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(new StringSelection(count + ".mp4"), null);
            robot.delay(DELAY_TIME_SHORT);

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

            System.out.println(">>> 关闭当前标签页 >>> Ctrl + W >>>");
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_W);
            robot.keyRelease(KeyEvent.VK_W);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.delay(DELAY_TIME_SHORT);

            System.out.println(">>> 定位输入框 >>> 鼠标左键 >>>");
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            robot.delay(DELAY_TIME_SHORT);

            System.out.println(">>> 全选输入框内容 >>> Ctrl + A >>>");
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.delay(DELAY_TIME_SHORT);
        }

        System.out.println(">>> 执行结束 >>>");
    }

}
