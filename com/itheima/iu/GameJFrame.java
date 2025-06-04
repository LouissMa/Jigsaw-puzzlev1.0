package com.itheima.iu;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File; // 建议使用 File.separator
import java.util.Random;

//代表游戏的主界面
public class GameJFrame extends JFrame implements KeyListener, ActionListener {

    //创建二维数组（打乱图片的作用）
    int[][] data = new int[4][4];
    //记录空白方块在二维数组中的位置
    int x = 0, y = 0;

    //定义变量，用来记录当前图片的路径
    // 修改：path 现在会动态改变，初始化为一个默认的有效图片集路径
    // 假设动物图片集在 animal1, animal2 ... animal8 子文件夹中
    // 假设美女图片集在 girl1, girl2 ... girl13 子文件夹中
    // 假设运动图片集在 sport1, sport2 ... sport10 子文件夹中
    String path = "PuzzleGame" + File.separator + "image" + File.separator + "animal" + File.separator + "animal1" + File.separator;

    // 定义图片集包含的组数
    private static final int NUM_GIRL_SETS = 13; // 假设有13组美女图片
    private static final int NUM_ANIMAL_SETS = 8; // 假设有8组动物图片
    private static final int NUM_SPORT_SETS = 10; // 假设有10组运动图片

    //定义二维数组，定义胜利
    int[][] win = new int[][]{
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}
    };

    //定义计步器
    int step = 0;

    //创建选项下面的条目
    JMenuItem girl = new JMenuItem("美女");
    JMenuItem animal = new JMenuItem("动物");
    JMenuItem sport = new JMenuItem("运动");

    JMenuItem replaceItem = new JMenuItem("重新游戏");
    JMenuItem reLoginItem = new JMenuItem("重新登录");
    JMenuItem closeItem = new JMenuItem("关闭游戏");

    JMenuItem accountItem = new JMenuItem("公众号");

    // 公共的Random对象，避免重复创建
    Random r = new Random();


    public GameJFrame() {
        //初始化界面
        initJFrame();

        //初始化菜单
        initJMenuBar();

        //初始化数据（打乱）
        initData();

        //初始化图片（根据打乱结果加载图片）
        initImage();

        //让界面显示出来
        this.setVisible(true);
    }


    //初始化数据（打乱）
    private void initData() {
        //1.定义1维数组
        int[] tempArr = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

        //2.打乱,用每一个元素和随即索引上的数据进行交换。
        // Random r = new Random(); // 移动到类成员变量
        for (int i = 0; i < tempArr.length; i++) {
            int index = r.nextInt(tempArr.length);
            int temp = tempArr[i];
            tempArr[i] = tempArr[index];
            tempArr[index] = temp;
        }

        //向二维数组赋值
        for (int i = 0; i < tempArr.length; i++) {
            if (tempArr[i] == 0) {
                x = i / 4;
                y = i % 4;
            }
            data[i / 4][i % 4] = tempArr[i];
        }
    }

    //初始化图片
    //添加图片需要按照打乱的二位数组去进行添加
    //先加载的图片在上方
    private void initImage() {
        //路径分两种：绝对路径：从盘符开始；相对路径：不从盘符开始，相对当前项目而言。

        //清空移动之前的图片
        this.getContentPane().removeAll();

        if (victory()) {
            // 注意：胜利图片的路径建议也使用相对路径或使其更灵活
            JLabel winJLabel = new JLabel(new ImageIcon("PuzzleGame" + File.separator + "image" + File.separator + "win.png"));
            winJLabel.setBounds(203, 283, 197, 73);
            this.getContentPane().add(winJLabel);
        }

        JLabel stepCount = new JLabel("步数：" + step);
        stepCount.setBounds(50, 30, 100, 20);
        this.getContentPane().add(stepCount);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //获取当前加载图片的序号
                int num = data[i][j];
                //创建一个JLabel的对象（管理容器）
                // 修改：确保path变量指向的是包含分割图片的文件夹路径，且图片名为 "数字.jpg"
                // 例如 path = "PuzzleGame/image/girl/girl1/"
                // 则图片为 "PuzzleGame/image/girl/girl1/1.jpg", "PuzzleGame/image/girl/girl1/2.jpg" ...
                ImageIcon icon = new ImageIcon(path + num + ".jpg");
                JLabel jLabel = new JLabel(icon);

                //指定图片位置
                jLabel.setBounds(105 * j + 83, 105 * i + 134, 105, 105);
                //给图片增加边框(Lowered 凹)（Raised 凸）
                jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                //管理容器添加到界面中
                this.getContentPane().add(jLabel);
            }
        }

        //添加背景
        // 注意：背景图片路径
        JLabel background = new JLabel(new ImageIcon("PuzzleGame" + File.separator + "image" + File.separator + "background.png"));
        background.setBounds(40, 40, 508, 560);
        this.getContentPane().add(background);

        //刷新界面
        this.getContentPane().repaint();
    }


    private void initJFrame() {
        this.setSize(603, 680);
        //设置标题
        this.setTitle("拼图游戏单机游戏 v1.0");
        //设置置顶
        this.setAlwaysOnTop(true);
        //设置界面居中
        this.setLocationRelativeTo(null);
        //设置关闭方式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //取消居中放置
        this.setLayout(null);
        //给界面添加键盘监听
        this.addKeyListener(this);
    }


    private void initJMenuBar() {
        //初始化菜单
        //创建整个菜单对象
        JMenuBar jMenuBar = new JMenuBar();

        //创建菜单上的两个选项对象
        JMenu functionJMenu = new JMenu("功能");
        JMenu aboutJMenu = new JMenu("关于我们");

        //创建更换图片 子菜单
        JMenu changeImage = new JMenu("更换图片"); // 新增：将更换图片作为一个子菜单

        //把美女，动物，运动添加到更换图片当中
        changeImage.add(girl);    // JMenuItem
        changeImage.add(animal);  // JMenuItem
        changeImage.add(sport);   // JMenuItem

        //将更换图片子菜单添加到功能菜单中
        functionJMenu.add(changeImage); // 添加子菜单
        functionJMenu.addSeparator(); // 添加分割线，美观一些
        //将条目放到选型中
        functionJMenu.add(replaceItem);
        functionJMenu.add(reLoginItem);
        functionJMenu.add(closeItem);

        aboutJMenu.add(accountItem);

        //给条目绑定事件
        girl.addActionListener(this);
        animal.addActionListener(this);
        sport.addActionListener(this);

        replaceItem.addActionListener(this);
        reLoginItem.addActionListener(this);
        closeItem.addActionListener(this);
        accountItem.addActionListener(this);

        //将选项添加到菜单中
        jMenuBar.add(functionJMenu);
        jMenuBar.add(aboutJMenu);

        //给整个界面设置菜单
        this.setJMenuBar(jMenuBar);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == 65) { // 'A'键查看完整图片
            //把界面图片删除
            this.getContentPane().removeAll();
            //加载完整图片和背景图片
            // 修改：确保path变量指向的是包含 "all.jpg" 的文件夹路径
            // 例如 path = "PuzzleGame/image/girl/girl1/"
            // 则完整图片为 "PuzzleGame/image/girl/girl1/all.jpg"
            JLabel all = new JLabel(new ImageIcon(path + "all.jpg"));
            all.setBounds(83, 134, 420, 420);
            this.getContentPane().add(all);

            JLabel background = new JLabel(new ImageIcon("PuzzleGame" + File.separator + "image" + File.separator + "background.png"));
            background.setBounds(40, 40, 508, 560);
            this.getContentPane().add(background);

            //刷新
            this.getContentPane().repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        //判断游戏是否胜利，若胜利则不让键盘继续移动图片
        if (victory()) {
            return;
        }

        //对上下左右进行判断
        //左：37 上：38 右：39 下：40
        int code = e.getKeyCode();
        // 记录移动前的空白格位置，用于判断是否真的发生了移动
        int oldX = x;
        int oldY = y;

        if (code == KeyEvent.VK_LEFT || code == 37) { // 左
            System.out.println("向左移动");
            if (y < 3) { // 空白块不在最右边，可以将右侧图片移入空白块
                data[x][y] = data[x][y + 1];
                data[x][y + 1] = 0;
                y++;
                step++;
            }
        } else if (code == KeyEvent.VK_UP || code == 38) { // 上
            System.out.println("向上移动");
            if (x < 3) { // 空白块不在最下边，可以将下方图片移入空白块
                data[x][y] = data[x + 1][y];
                data[x + 1][y] = 0;
                x++;
                step++;
            }
        } else if (code == KeyEvent.VK_RIGHT || code == 39) { // 右
            System.out.println("向右移动");
            if (y > 0) { // 空白块不在最左边，可以将左侧图片移入空白块
                data[x][y] = data[x][y - 1];
                data[x][y - 1] = 0;
                y--;
                step++;
            }
        } else if (code == KeyEvent.VK_DOWN || code == 40) { // 下
            System.out.println("向下移动");
            if (x > 0) { // 空白块不在最上边，可以将上方图片移入空白块
                data[x][y] = data[x - 1][y];
                data[x - 1][y] = 0;
                x--;
                step++;
            }
        } else if (code == KeyEvent.VK_A || code == 65) { // 松开A键时恢复游戏图片
            initImage();
        } else if (code == KeyEvent.VK_M || code == 77) { // 'M'键直接胜利
            // data数组会被设置为胜利状态
            // initImage会根据当前的path加载对应的胜利图片（实际上是加载0-15的图块）
            // 确保path是正确的当前选择的图片集的路径
            data = new int[][]{
                    {1, 2, 3, 4},
                    {5, 6, 7, 8},
                    {9, 10, 11, 12},
                    {13, 14, 15, 0}
            };
            // 找到0号元素（空白格）的位置
            for(int i=0; i<4; i++){
                for(int j=0; j<4; j++){
                    if(data[i][j] == 0){
                        x = i;
                        y = j;
                        break;
                    }
                }
            }
            initImage();
        }

        // 只有当空白格实际移动了才重新加载图片
        if (x != oldX || y != oldY) {
            initImage();
        }
    }

    //判断data是否与win相同
    public boolean victory() {
        for (int i = 0; i < data.length; i++) {
            // 修改：内层循环条件应该是 data[i].length 或者固定的4，而不是 data.length
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != win[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // 提取一个重新开始游戏的方法，用于更换图片和点击“重新游戏”
    private void restartGame() {
        //再次打乱二维数组
        initData();
        //记步器更新
        step = 0;
        //重新加载图片
        initImage();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == replaceItem) {
            System.out.println("点击了重新游戏");
            restartGame(); // 调用封装好的方法
        } else if (obj == reLoginItem) {
            System.out.println("点击了重新登录");
            //关闭当前游戏界面
            this.setVisible(false);
            //打开登陆界面 (假设LoginJFrame存在)
            new LoginJFrame();
        } else if (obj == closeItem) {
            System.out.println("点击了关闭游戏");
            //直接关闭虚拟机
            System.exit(0);
        } else if (obj == accountItem) {
            System.out.println("点击了公众号");
            //创建一个弹框对象
            JDialog jDialog = new JDialog();
            //创建容器 (假设about.png存在)
            JLabel jLabel = new JLabel(new ImageIcon("PuzzleGame" + File.separator + "image" + File.separator + "about.png"));
            jLabel.setBounds(0, 0, 258, 258); // 根据实际图片大小调整
            //把图片放到弹框中
            jDialog.getContentPane().add(jLabel);
            //弹框设置大小
            jDialog.setSize(344, 344); // 根据实际图片大小和需求调整
            jDialog.setAlwaysOnTop(true);
            jDialog.setLocationRelativeTo(null);
            //弹框不关闭无法执行下面的界面
            jDialog.setModal(true);
            jDialog.setVisible(true);
        } else if (obj == girl) {
            System.out.println("点击了美女图片");
            // 从1到NUM_GIRL_SETS中随机选择一个数
            int girlSetNumber = r.nextInt(NUM_GIRL_SETS) + 1;
            path = "PuzzleGame" + File.separator + "image" + File.separator + "girl" + File.separator + "girl" + girlSetNumber + File.separator;
            System.out.println("选择了美女图片集: " + path);
            restartGame();
        } else if (obj == animal) {
            System.out.println("点击了动物图片");
            // 从1到NUM_ANIMAL_SETS中随机选择一个数
            int animalSetNumber = r.nextInt(NUM_ANIMAL_SETS) + 1;
            path = "PuzzleGame" + File.separator + "image" + File.separator + "animal" + File.separator + "animal" + animalSetNumber + File.separator;
            System.out.println("选择了动物图片集: " + path);
            restartGame();
        } else if (obj == sport) {
            System.out.println("点击了运动图片");
            // 从1到NUM_SPORT_SETS中随机选择一个数
            int sportSetNumber = r.nextInt(NUM_SPORT_SETS) + 1;
            path = "PuzzleGame" + File.separator + "image" + File.separator + "sport" + File.separator + "sport" + sportSetNumber + File.separator;
            System.out.println("选择了运动图片集: " + path);
            restartGame();
        }
    }
}