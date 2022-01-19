package geekbrains.catch_the_drop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame { // унаследовали от класа JFrame
//обьявим переменную в которой будет храниться обьект нашего окна
// и с этой переменной смогут работать любые методы находящиеся в дайнном классе
    private static GameWindow game_window; // тип переменной GameWindow, назывется game_window
    //для подсчета времени между кадрами вводим доп переменну long которая хранит большие целые значения
    private static long last_frame_time;
    //для отрисовки картинок в Java есть класс Image. Обьявим 3 переменных
    private static Image background;
    private static Image game_over;
    private static Image drop;
    //создадим 2 переменные для движения капли
    private static float drop_left = 200; //эта переменная хранит координату Х левого верхнего угла
    private static float drop_top = -100; // хранит координату Y левого верхнего угла капли
    private static float drop_v = 200;//скорость капли
    private static int score; //изначально равно 0



    public static void main(String[] args) throws IOException {
        //для загрузки картинок в переменные Image используем метод Read
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        //среда разработки подчеркивает строчку, тк  она может генерировать исключения - вынесем его в метод throw
// создадим сам обьект на который будет ссылаться game_window
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
        game_window = new GameWindow(); // создали обьект класса GameWindow и ссылку на него поместили в game_window
        //настроим обьект
        //данной командой при закрытии окна прога завершается
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // настроим где будет стоять точка - сверху вниз = Y справа налево X
        game_window.setLocation(200,100);
        //настроим размер окна
        game_window.setSize(906,478);
        //запретим изменять размер окна с пом мышки и разворачивать
        game_window.setResizable(false);
        last_frame_time = System.nanoTime(); //данная команда возвращает текущее время в наносекундах
        //создадим обьект класса GameField
        GameField game_field = new GameField();
        // задача - отлавливать события нажатия на кнопку мыши
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                //напишем условие которое ответит на вопрос точка XY попадает в нашу каплю или нет
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                if(is_drop){
                    //откинем нашу каплю по вертикали
                    drop_top = -100;
                    drop_left = (int) (Math.random() * (game_field.getWidth() - drop.getWidth(null)));
                    //увеличим скорость
                    drop_v = drop_v + 20;
                    //посчитаем очки при каждом нажатии на каплю
                    score++;
                    game_window.setTitle("Score  " + score);
                }
            }
        });
        //добавим в окно
        game_window.add(game_field);
        //сдедаем окно видимым потому что по умолчанию оно невидимое
        game_window.setVisible(true);
    }
    //подготовим окно для того чтобы в нем рисовать
    //обьявим класс onRepaint, в качестве параметра будем передавать обьект класса Graphics, назовем параметр g
    private  static void onRepaint (Graphics g) {
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;
        drop_top = drop_top + drop_v * delta_time;
       // drop_left = drop_left + drop_v * delta_time; // капля будет двигаться по диагонали
        // нарисуем овал в точке 10 10 - шириной и длиной  200 на 100
       // g.fillOval(10,10,200,100);
        //g.drawLine(500,100,300,100);
        //нарисуем фон, каплю и конец игры
        g.drawImage(background,0,0,null);
        g.drawImage(drop,(int) drop_left,(int) drop_top,null); //обрежем int
        //когда капля достигает конца экрана - гейм оувер
       if (drop_top > game_window.getHeight()) g.drawImage(game_over, 280,120,null);

    }
    // рисовать можем на панелях которые представлены в классе JPanel
    //в классе GameWindow опишем еще один класс GameField
    private static class GameField extends JPanel {
        // когда какой то графический компонет отрисовывается н-р JPanel
        //то у него внутри вызывается метод paintComponent которому как параметр передается обьект класса Graphics
        // c помощью которого он и рисуется
        //ЗАДАЧА : изменить поведение метода Paint Component который находитмся в  JPanel на свое поведение
        //это наз-ся ДИНАМИЧЕСКОЕ ЗАМЕЩЕНИЕ МЕТОДОВ
        @Override
        protected void paintComponent (Graphics g) {
            super.paintComponent(g); // метод отрисовывает панель
            onRepaint(g);
            repaint();
        }
    }

}
